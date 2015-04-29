package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NonExistantViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidLocationException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidSizeException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotDimensionedViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotResizableViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.TargetOrSourceNotDrawnException;
import org.eclipse.papyrus.diagramdrawer.exceptions.UnmovableViewException;
import org.eclipse.papyrus.diagramdrawer.factories.DiagramFactory;
import org.eclipse.papyrus.diagramdrawer.factories.PapyrusEditorFactory;
import org.eclipse.papyrus.diagramdrawer.factories.ProjectFactory;
import org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.utils.DiagramType;
import org.eclipse.papyrus.diagramdrawer.utils.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.utils.ExecutionException;
import org.eclipse.papyrus.diagramdrawer.utils.PapyrusEditor;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the class DefaultDiagramHandler.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class DefaultDiagramHandlerTestBasicsMethods {
	
	private static IDiagramHandler handler;
	
	private static View drawn_element;
	
	private static View drawn_element2;
	
	private static View drawn_association;
	
	private static Class element;
	
	private static Class element2;
	
	private static Association association;
	
	private static TransactionalEditingDomain ted;
	
	@BeforeClass
	public static void setUp() throws ExecutionException, NotFoundException, ServiceException, CreationCommandNotFoundException {
	
		EclipseProject eclipseProject = ProjectFactory.instance.build("test project");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(eclipseProject, "test model");
		ted = papyrusEditor.getTransactionalEditingDomain();
		handler = DiagramFactory.instance.create("test diagram", DiagramType.Class, papyrusEditor);	
		
		
		final Package model = (Package)handler.getModel().lookupRoot();
		
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {	
				// Dans la transaction :
				element = model.createOwnedClass("Class1", false);
				element2 = model.createOwnedClass("Class2", false);
				association = element.createAssociation(true, AggregationKind.COMPOSITE_LITERAL, "name1", 1, 1, element2, true, AggregationKind.SHARED_LITERAL, "name2", 0, 1);
				association.setName("asso");
				try {
					drawn_element = handler.draw(element, false);
					drawn_element2 = handler.draw(element2, false);
					drawn_association = handler.draw(association, false);
				} catch (TargetOrSourceNotDrawnException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
			}});
		
	}
	
	
	@Test
	public void changeDefaultLocationTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				
				Point illegal_location = new Point(-5,-20);
				Point legal_location = new Point(5,20);
				Point default_location = new Point(0,0);
				
				// Start : defaultLocation is (0,0)
				assertEquals(default_location,handler.getDefaultLocation());
				
				boolean result = false;
				
				// Try : change defaultLocation with an illegal location.
				try {
					handler.setDefaultLocation(illegal_location);
				} catch (NotAValidLocationException e) {
					result = true;
				}
				assertTrue(result);
				
				// Try : change defaultLocation with a legal location.
				try {
					handler.setDefaultLocation(legal_location);
				} catch (NotAValidLocationException e) {
					result = false;
				}
				assertTrue(result);
				assertEquals(legal_location,handler.getDefaultLocation());
			}
		});	
		
	}
	
	
	@Test
	public void changeWidthTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				int illegal_width = -200;
				int legal_width = 80;
				
				boolean result = false;
				
				// Try : change width with an illegal value.
				try {
					handler.setWidth(drawn_element, illegal_width);
				} catch (NotResizableViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				} catch (NotAValidSizeException e) {
					result = true;
				}
				assertTrue(result);
				
				// Try : change width with a legal value.
				try {
					handler.setWidth(drawn_element, legal_width);
				} catch (NotResizableViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				} catch (NotAValidSizeException e) {
					result = false;
				}
				assertTrue(result);
				
				int width = 0;
				try {
					width = handler.getWidth(drawn_element);
				} catch (NotDimensionedViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				assertEquals(legal_width,width);
			}
		});
		
	}
	
	
	@Test
	public void changeWidthOnAssociationTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				int legal_width = 80;
				
				boolean result = false;
				
				// Try : change width with a legal value on association.
				try {
					handler.setWidth(drawn_association, legal_width);
				} catch (NotResizableViewException e) {
					result = true;
				} catch (NotAValidSizeException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				assertTrue(result);
			}
		});
		
	}
	
	
	@Test
	public void changeHeightTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				int illegal_height = -200;
				int legal_height = 80;
				
				boolean result = false;
				
				// Try : change height with an illegal value.
				try {
					handler.setHeight(drawn_element, illegal_height);
				} catch (NotResizableViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				} catch (NotAValidSizeException e) {
					result = true;
				}
				assertTrue(result);
				
				// Try : change height with a legal value.
				try {
					handler.setHeight(drawn_element, legal_height);
				} catch (NotResizableViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				} catch (NotAValidSizeException e) {
					result = false;
				}
				assertTrue(result);
				
				int height = 0;
				try {
					height = handler.getHeight(drawn_element);
				} catch (NotDimensionedViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				assertEquals(legal_height,height);
			}
		});
		
	}
	
	@Test
	public void changeHeightOnAssociationTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				int legal_height = 80;
				
				boolean result = false;
				
				// Try : change height with a legal value on association.
				try {
					handler.setHeight(drawn_association, legal_height);
				} catch (NotResizableViewException e) {
					result = true;
				} catch (NotAValidSizeException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				assertTrue(result);
			}
		});
		
	}
	
	
	@Test
	public void changeLocationTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				Point illegal_location = new Point(-500,-200);
				final Point legal_location = new Point(350,200);
				
				boolean result = false;
				
				// Try : change Location with an illegal location.
				try {
					handler.setLocation(drawn_element, illegal_location);
				} catch (NotAValidLocationException e) {
					result = true;
				} catch (UnmovableViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				assertTrue(result);
				
				// Try : change Location with a legal location.
				try {
					handler.setLocation(drawn_element, legal_location);
				} catch (UnmovableViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				} catch (NotAValidLocationException e) {
					result = false;
				}
				assertTrue(result);
				
				Point location = null;
				try {
					location = handler.getLocation(drawn_element);
				} catch (LocationNotFoundException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				assertEquals(legal_location,location);
			}
		});
		
	}
	
	@Test
	public void changeLocationOnAssociationTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				Point legal_location = new Point(350,200);
				
				boolean result = false;
				
				// Try : change Location with a legal location.
				try {
					handler.setLocation(drawn_association, legal_location);
				} catch (NotAValidLocationException e) {
					// Normally it's impossible !
					assertTrue(false);
				} catch (UnmovableViewException e) {
					result = true;
				}
				assertTrue(result);
			}
		});
		
	}
	
	
	@Test
	public void getElementByNameTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				List<Element> noclass = handler.getElementByName("NoClass");
				
				assertEquals(0,noclass.size());
				
				List<Element> class1 = handler.getElementByName("Class1");
				
				assertEquals(1,class1.size());
				assertEquals(element,class1.get(0));
				
				List<Element> class2 = handler.getElementByName("Class2");
				
				assertEquals(1,class2.size());
				assertEquals(element2,class2.get(0));
				
				List<Element> asso = handler.getElementByName("asso");
				
				assertEquals(1,asso.size());
				assertEquals(association,asso.get(0));
			}
		});
				
	}
	
	
	@Test
	public void getElementViewByNameTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				List<View> noclass = handler.getElementViewByName("NoClass");
				
				assertEquals(0,noclass.size());
				
				List<View> class1 = handler.getElementViewByName("Class1");
				
				assertEquals(1,class1.size());
				assertEquals(drawn_element,class1.get(0));
				
				List<View> class2 = handler.getElementViewByName("Class2");
				
				assertEquals(1,class2.size());
				assertEquals(drawn_element2,class2.get(0));
				
				List<View> asso = handler.getElementViewByName("asso");
				
				assertEquals(1,asso.size());
				assertEquals(drawn_association,asso.get(0));
			}
		});
				
	}
	
	
	@AfterClass
	public static void deleteTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				// Delete :
				try {
					handler.delete(drawn_element);
				} catch (NonExistantViewException e) {
					// Normally it's impossible !
					assertTrue(false);
				}
				
				/// element and association not drawn.
				assertFalse(handler.isDrawn(element));
				assertFalse(handler.isDrawn(association));
				assertTrue(handler.isDrawn(element2));
				
				boolean already_delete = false;
				// It's impossible to delete an element already delected.
				try {
					handler.delete(drawn_element);
				} catch (NonExistantViewException e) {
					already_delete = true;
				}
				
				assertTrue(already_delete);
			}
		});
		
	}
	
	
	@Test
	public void autoSizeTest() {
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				try {
					// Change height and width :
					handler.setHeight(drawn_element2, 10);
					handler.setWidth(drawn_element2, 10);
					// Autosize :
					handler.autoSize(drawn_element2);
					
					assertNotEquals(10,handler.getHeight(drawn_element2));
					assertNotEquals(10,handler.getWidth(drawn_element2));
				
				}
				catch (Exception e) {
					// Normally it's impossible !
					assertTrue(false);
				}
			}
		});
	
	}
	
}
