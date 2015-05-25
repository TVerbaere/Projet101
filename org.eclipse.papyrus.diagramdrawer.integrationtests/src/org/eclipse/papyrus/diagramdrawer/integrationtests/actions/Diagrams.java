package org.eclipse.papyrus.diagramdrawer.integrationtests.actions;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
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
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UseCase;

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
					Enumeration status = model.createOwnedEnumeration("Status");
					PrimitiveType string = model.createOwnedPrimitiveType("String");
					
					status.createOwnedLiteral("alive");
					status.createOwnedLiteral("dead");
					character.createOwnedAttribute("status", status);
					character.createOwnedAttribute("name", string);
					
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
					battleclass.createOwnedAttribute("place", string);
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
					
					
					// Create representation for class Character and Status :
					handler.draw(character, new Point(370,150), true);
					handler.draw(status, new Point(370,300), true);
										
					
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
					// Recover the main interaction :
					Interaction interaction = (Interaction) handler.getElementByName("Interaction1").get(0);
					View interaction_view =  handler.getViewByElement(interaction).get(0);
					
					//You can change the default name
					interaction.setName("Restaurant");
					
					// Create elements :
					Lifeline client = interaction.createLifeline("Wout :Client");
					Lifeline waiter = interaction.createLifeline("Jan :Waiter");
					Lifeline cook = interaction.createLifeline("Niklas :Cook");
					
					Message message = interaction.createMessage("order meal");
					MessageOccurrenceSpecification mosSrc = (MessageOccurrenceSpecification)
					interaction.createFragment("src",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					MessageOccurrenceSpecification mosDst = (MessageOccurrenceSpecification)
					interaction.createFragment("dst",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					mosSrc.setMessage(message);
					mosSrc.setCovered(client);
					mosDst.setMessage(message);
					mosDst.setCovered(waiter);
					message.setSendEvent(mosSrc);
					message.setReceiveEvent(mosDst);
					
					Message message1 = interaction.createMessage("order meal");
					MessageOccurrenceSpecification mosSrc1 = (MessageOccurrenceSpecification)
					interaction.createFragment("src1",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					MessageOccurrenceSpecification mosDst1 = (MessageOccurrenceSpecification)
					interaction.createFragment("dst1",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					mosSrc1.setMessage(message1);
					mosSrc1.setCovered(waiter);
					mosDst1.setMessage(message1);
					mosDst1.setCovered(cook);
					message1.setSendEvent(mosSrc1);
					message1.setReceiveEvent(mosDst1);
					
					Message message11 = interaction.createMessage("pickup");
					MessageOccurrenceSpecification mosSrc11 = (MessageOccurrenceSpecification)
					interaction.createFragment("src11",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					MessageOccurrenceSpecification mosDst11 = (MessageOccurrenceSpecification)
					interaction.createFragment("dst11",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					mosSrc11.setMessage(message11);
					mosSrc11.setCovered(cook);
					mosDst11.setMessage(message11);
					mosDst11.setCovered(waiter);
					message11.setSendEvent(mosSrc11);
					message11.setReceiveEvent(mosDst11);
					
					Message message111 = interaction.createMessage("serve meal");
					MessageOccurrenceSpecification mosSrc111 = (MessageOccurrenceSpecification)
					interaction.createFragment("src111",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					MessageOccurrenceSpecification mosDst111 = (MessageOccurrenceSpecification)
					interaction.createFragment("dst111",UMLPackage.eINSTANCE.getMessageOccurrenceSpecification());
					mosSrc111.setMessage(message111);
					mosSrc111.setCovered(waiter);
					mosDst111.setMessage(message111);
					mosDst111.setCovered(client);
					message111.setSendEvent(mosSrc111);
					message111.setReceiveEvent(mosDst111);
					
					BehaviorExecutionSpecification bes = (BehaviorExecutionSpecification) interaction.createFragment(null, UMLPackage.eINSTANCE.getBehaviorExecutionSpecification());
					bes.setStart(mosDst);
					bes.setFinish(mosSrc1);
					bes.getCovereds().add(waiter);
					bes.setName("go");
					
					BehaviorExecutionSpecification bes1 = (BehaviorExecutionSpecification) interaction.createFragment(null, UMLPackage.eINSTANCE.getBehaviorExecutionSpecification());
					bes1.setStart(mosDst1);
					bes1.setFinish(mosSrc11);
					bes1.getCovereds().add(cook);
					bes1.setName("cook");
					
					BehaviorExecutionSpecification bes11 = (BehaviorExecutionSpecification) interaction.createFragment(null, UMLPackage.eINSTANCE.getBehaviorExecutionSpecification());
					bes11.setStart(mosDst11);
					bes11.setFinish(mosSrc111);
					bes11.getCovereds().add(waiter);
					bes11.setName("return");
					
					// Draw lifelines :
					View client_view = handler.drawElementInsideAtLocation(interaction_view, client, new Point(100,50), true);
					View waiter_view = handler.drawElementInsideAtLocation(interaction_view, waiter, new Point(300,50), true);
					View cook_view = handler.drawElementInsideAtLocation(interaction_view, cook, new Point(500,50), true);
					
					// Change height of lifelines :
					handler.setHeight(client_view, 600);
					handler.setHeight(waiter_view, 600);
					handler.setHeight(cook_view, 600);
					handler.setHeight(waiter_view, 600);
					
					// Draw behaviors :
					View behavior = handler.drawElementInside(waiter_view, bes, true);
					View behavior1 = handler.drawElementInside(cook_view, bes1, true);
					View behavior11 = handler.drawElementInside(waiter_view, bes11, true);			
					
					// Change height and ordonnee of behaviors :
					handler.setHeight(behavior, 70);
					handler.setHeight(behavior1, 150);
					handler.setHeight(behavior11, 100);
				
					handler.setYLocation(behavior,100);
					handler.setYLocation(behavior1,170);
					handler.setYLocation(behavior11,320);
					
					// Draw messages :
					View message_view = handler.draw(message, true);
					View message1_view = handler.draw(message1, true);
					View message11_view = handler.draw(message11, true);
					View message111_view = handler.draw(message111, true);
					
					// Connect edges with good views :
					handler.reconnectEdge(message_view, null, behavior);
					handler.reconnectEdge(message1_view, behavior, behavior1);
					handler.reconnectEdge(message11_view, behavior1, behavior11);
					handler.reconnectEdge(message111_view, behavior11, null);
					
					// Move elements :
					handler.setEdgeLocation(message1_view, new PrecisionPoint(1.0,1.0), null);
					handler.setEdgeLocation(message11_view, new PrecisionPoint(1.0,1.0), null);
					
					handler.setEdgeLocation(message111_view, new PrecisionPoint(0.0,1.0), new PrecisionPoint(0.0,0.7363796133567663));
					handler.setEdgeLocation(message_view, new PrecisionPoint(1,0.17), null);
					
					
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
					Package model = (Package) handler.getModel().lookupRoot();
					
					// Create elements :		
					Actor requester = (Actor) model.createPackagedElement("Requester", UMLPackage.eINSTANCE.getActor());
					Actor staff = (Actor) model.createPackagedElement("Staff", UMLPackage.eINSTANCE.getActor());
					Actor technician = (Actor) model.createPackagedElement("Technician", UMLPackage.eINSTANCE.getActor());
					
					Package pack = model.createNestedPackage("Machine");
					
					
					UseCase realize = (UseCase) pack.createPackagedElement("Realize request", UMLPackage.eINSTANCE.getUseCase());
					UseCase validate = (UseCase) pack.createPackagedElement("Validate request", UMLPackage.eINSTANCE.getUseCase());
					UseCase recover = (UseCase) pack.createPackagedElement("Revover request", UMLPackage.eINSTANCE.getUseCase());
					UseCase evaluate = (UseCase) pack.createPackagedElement("Evaluate request", UMLPackage.eINSTANCE.getUseCase());
					UseCase generate = (UseCase) pack.createPackagedElement("Generate reports", UMLPackage.eINSTANCE.getUseCase());
					
					validate.createInclude(null, recover);
					evaluate.createInclude(null, recover);
								
					realize.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,requester, false, AggregationKind.NONE_LITERAL, null,0, 1);
					realize.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,staff, false, AggregationKind.NONE_LITERAL, null,0, 1);
					validate.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,staff, false, AggregationKind.NONE_LITERAL, null,0, 1);
					recover.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,staff, false, AggregationKind.NONE_LITERAL, null,0, 1);
					generate.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,technician, false, AggregationKind.NONE_LITERAL, null,0, 1);
					validate.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,technician, false, AggregationKind.NONE_LITERAL, null,0, 1);
					evaluate.createAssociation(false,AggregationKind.NONE_LITERAL, null, 0, 1,technician, false, AggregationKind.NONE_LITERAL, null,0, 1);

					// Draw elements :
					View machine_view = handler.draw(pack, new Point(200,50), false);
					handler.draw(technician, new Point(750,150), true);
					handler.draw(requester, new Point(100,100), true);
					handler.draw(staff, new Point(100,200), true);
					
					handler.setHeight(machine_view, 300);
					handler.setWidth(machine_view, 500);
					
					handler.drawElementInsideAtLocation(machine_view, realize, new Point(50,20), true);
					handler.drawElementInsideAtLocation(machine_view, validate, new Point(50,100), true);
					handler.drawElementInsideAtLocation(machine_view, recover, new Point(50,200), true);
				
					handler.drawElementInsideAtLocation(machine_view, generate, new Point(300,20), true);
					handler.drawElementInsideAtLocation(machine_view, evaluate, new Point(300,200), true);

					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});		
		
	}
}
