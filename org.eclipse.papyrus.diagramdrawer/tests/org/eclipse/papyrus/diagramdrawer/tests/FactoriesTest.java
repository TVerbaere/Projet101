package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.DiagramNotFoundException;
import org.eclipse.papyrus.diagramdrawer.factories.DiagramFactory;
import org.eclipse.papyrus.diagramdrawer.factories.PapyrusEditorFactory;
import org.eclipse.papyrus.diagramdrawer.factories.ProjectFactory;
import org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.utils.DiagramType;
import org.eclipse.papyrus.diagramdrawer.utils.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.utils.ExecutionException;
import org.eclipse.papyrus.diagramdrawer.utils.PapyrusEditor;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.junit.Test;

/**
 * Tests for the factories
 * 
 * @author Allan Rakotoarivony
 *
 */
public class FactoriesTest {
	
	@Test
	public void testBuildProject() throws ExecutionException{
		EclipseProject project = ProjectFactory.instance.build("test new project");
		assertNotNull(project);
		EclipseProject project2 = ProjectFactory.instance.build("test new project");
		assertNotNull(project2);
		assertEquals(project.getProject(), project2.getProject());
	}
	
	@Test
	public void testCreatePapyrusEditor() throws ExecutionException{
		EclipseProject project = ProjectFactory.instance.build("test papyrus editor");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(project, "test papyrus model");
		assertNotNull(papyrusEditor);
	}
	
	@Test
	public void testCreateDiagram() throws ExecutionException, CreationCommandNotFoundException, ServiceException{
		EclipseProject project = ProjectFactory.instance.build("test diagram creation");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(project, "test diagram creation model");
		
		IDiagramHandler classDiagram = DiagramFactory.instance.create("class diagram", DiagramType.Class, papyrusEditor);
		assertNotNull(classDiagram);
		
		IDiagramHandler activityDiagram = DiagramFactory.instance.create("activity diagram", DiagramType.Activity, papyrusEditor);
		assertNotNull(activityDiagram);
		
		IDiagramHandler communicationDiagram = DiagramFactory.instance.create("communication diagram", DiagramType.Communication, papyrusEditor);
		assertNotNull(communicationDiagram);
		
		IDiagramHandler componentDiagram = DiagramFactory.instance.create("component diagram", DiagramType.Component, papyrusEditor);
		assertNotNull(componentDiagram);
		
		IDiagramHandler compositeDiagram = DiagramFactory.instance.create("composite diagram", DiagramType.Composite, papyrusEditor);
		assertNotNull(compositeDiagram);
		
		IDiagramHandler deploymentDiagram = DiagramFactory.instance.create("deployment diagram", DiagramType.Deployment, papyrusEditor);
		assertNotNull(deploymentDiagram);
		
		IDiagramHandler sequenceDiagram = DiagramFactory.instance.create("sequence diagram", DiagramType.Sequence, papyrusEditor);
		assertNotNull(sequenceDiagram);
		
		IDiagramHandler stateMachineDiagram = DiagramFactory.instance.create("state machine diagram", DiagramType.StateMachine, papyrusEditor);
		assertNotNull(stateMachineDiagram);
		
		IDiagramHandler timingDiagram = DiagramFactory.instance.create("timing diagram", DiagramType.Timing, papyrusEditor);
		assertNotNull(timingDiagram);
		
		IDiagramHandler useCaseDiagram = DiagramFactory.instance.create("useCase diagram", DiagramType.UseCase, papyrusEditor);
		assertNotNull(useCaseDiagram);
		
	}
	
	@Test
	public void testLoadDiagram() throws ExecutionException, CreationCommandNotFoundException, ServiceException, DiagramNotFoundException{
		EclipseProject project = ProjectFactory.instance.build("test diagram creation");
		PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(project, "test diagram load model");
		
		IDiagramHandler classDiagram = DiagramFactory.instance.create("class diagram", DiagramType.Class, papyrusEditor);
		IDiagramHandler activityDiagram = DiagramFactory.instance.create("activity diagram", DiagramType.Activity, papyrusEditor);
		
		IDiagramHandler loadedClassDiagram = DiagramFactory.instance.load("class diagram", papyrusEditor);
		IDiagramHandler loadedActivityDiagram =  DiagramFactory.instance.load("activity diagram", papyrusEditor);
		
		assertEquals("The loaded diagram is not the one excpected",classDiagram.getDiagramEditPart(),loadedClassDiagram.getDiagramEditPart());
		assertEquals("The loaded diagram is not the one excpected",activityDiagram.getDiagramEditPart(),loadedActivityDiagram.getDiagramEditPart());
		
		IDiagramHandler classDiagram2 = DiagramFactory.instance.load("class diagram", papyrusEditor);
		loadedClassDiagram = DiagramFactory.instance.load("class diagram", papyrusEditor);
		
		assertEquals("The loaded diagram is not the one excpected",classDiagram2.getDiagramEditPart(),loadedClassDiagram.getDiagramEditPart());

	}

}
