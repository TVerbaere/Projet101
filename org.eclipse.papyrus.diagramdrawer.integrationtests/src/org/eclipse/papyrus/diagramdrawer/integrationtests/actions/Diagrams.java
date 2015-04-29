package org.eclipse.papyrus.diagramdrawer.integrationtests.actions;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;
import org.eclipse.papyrus.diagramdrawer.factories.DiagramFactory;
import org.eclipse.papyrus.diagramdrawer.factories.PapyrusEditorFactory;
import org.eclipse.papyrus.diagramdrawer.factories.ProjectFactory;
import org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.utils.DiagramType;
import org.eclipse.papyrus.diagramdrawer.utils.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.utils.ExecutionException;
import org.eclipse.papyrus.diagramdrawer.utils.PapyrusEditor;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Class;

/**
 * 
 * @author Thibaud VERBAERE
 * Example of use for a Class Diagram, a Sequence Diagram and a UseCase Diagram.
 *
 */
public class Diagrams {

	
	public static void createClassDiagram() throws ExecutionException, ServiceException, CreationCommandNotFoundException {
		
		// Create an eclipse project and a papyrus editor :
		EclipseProject eclipseProject = ProjectFactory.instance.build("project1");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(eclipseProject, "model1");
		
		// Create the diagram :
		final IDiagramHandler handler = DiagramFactory.instance.create("classdiagram", DiagramType.Class, papyrusEditor);
		TransactionalEditingDomain ted = papyrusEditor.getTransactionalEditingDomain();
		
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				try {
					// Recover the model :
					Package model = (Package) handler.getModel().lookupRoot();
					
					// Add elements :
					Class character = model.createOwnedClass("Character", false);
					Package nation = model.createNestedPackage("Nation");
					Package battle = model.createNestedPackage("Battle");
					
					Class nationclass = nation.createOwnedClass("Nation", false);
					Class persian = nation.createOwnedClass("Persian", false);
					Class greek = nation.createOwnedClass("Greek", false);
					Class sparta = nation.createOwnedClass("Sparta", false);
					Class athens = nation.createOwnedClass("Athens", false);
					
					nationclass.createOwnedAttribute("leader", character);
					athens.createGeneralization(greek);
					sparta.createGeneralization(greek);
					persian.createGeneralization(nationclass);
					greek.createGeneralization(nationclass);
					
					Interface battleclass = battle.createOwnedInterface("Battle");
					Class naval = battle.createOwnedClass("NavalBattle", false);
					Class land = battle.createOwnedClass("LandBattle", false);
					
					battleclass.createOwnedAttribute("winner", nationclass);
					naval.createInterfaceRealization("i1", battleclass);
					land.createInterfaceRealization("i2", battleclass);
					
					battleclass.createAssociation(false, AggregationKind.COMPOSITE_LITERAL, "entrants", 1, 4, nationclass, false, AggregationKind.NONE_LITERAL, "battle", 0, 10);
					
					// Create representation for package "Battle" :
					View battle_view = handler.draw(battle, new Point(25,25), false);
					View battleclass_view = handler.drawElementInsideAtLocation(battle_view, battleclass, new Point(20,10), true);
					handler.drawElementInsideAtLocation(battle_view, naval, new Point(20,200), true);
					handler.drawElementInsideAtLocation(battle_view, land, new Point(150,200), true);
					
					handler.setWidth(battleclass_view, 200);
					handler.setWidth(battle_view, 300);
					handler.setHeight(battle_view, 350);
					
					// Create representation for package "Nation" :
					View nation_view = handler.draw(nation, new Point(500,25), false);
					View nationclass_view = handler.drawElementInsideAtLocation(nation_view, nationclass, new Point(100,10), true);
					handler.drawElementInsideAtLocation(nation_view, greek, new Point(400,10), true);
					handler.drawElementInsideAtLocation(nation_view, athens, new Point(300,200), true);
					handler.drawElementInsideAtLocation(nation_view, sparta, new Point(450,200), true);
					handler.drawElementInsideAtLocation(nation_view, persian, new Point(100,200), true);
					
					handler.setWidth(nationclass_view, 200);
					handler.setWidth(nation_view, 600);
					handler.setHeight(nation_view, 350);
					
					
					// Create representation for class Character :
					handler.draw(character, new Point(370,200), false);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});		
		
	}
	
	public static void createSequenceDiagram() throws ExecutionException, ServiceException, CreationCommandNotFoundException {
		
		// Create an eclipse project and a papyrus editor :
		EclipseProject eclipseProject = ProjectFactory.instance.build("project2");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(eclipseProject, "model2");
		
		// Create the diagram :
		final IDiagramHandler handler = DiagramFactory.instance.create("sequencediagram", DiagramType.Sequence, papyrusEditor);
		TransactionalEditingDomain ted = papyrusEditor.getTransactionalEditingDomain();
		
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				try {
					// Recover the model :
					//???????? cast in ?????
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});		
		
	}
	
	public static void createUseCaseDiagram() throws ExecutionException, ServiceException, CreationCommandNotFoundException {
		
		// Create an eclipse project and a papyrus editor :
		EclipseProject eclipseProject = ProjectFactory.instance.build("project3");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(eclipseProject, "model3");
		
		// Create the diagram :
		final IDiagramHandler handler = DiagramFactory.instance.create("usecasediagram", DiagramType.UseCase, papyrusEditor);
		TransactionalEditingDomain ted = papyrusEditor.getTransactionalEditingDomain();
		
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				try {
					// Recover the model :
					//???????? cast in ?????
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});		
		
	}
}
