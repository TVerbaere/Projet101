package org.eclipse.papyrus.diagramdrawer.factories;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;
import org.eclipse.papyrus.diagramdrawer.handlers.DefaultDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.othersources.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.papyrus.diagramdrawer.othersources.PapyrusEditor;
import org.eclipse.papyrus.diagramdrawer.utils.DiagramType;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;
import org.eclipse.ui.IEditorPart;

/**
 * Lets to create new papyrus diagram inside a project programatically. The main use is to call
 * the method build to create a new diagramHandler
 * 
 * @author Allan Rakotoarivony
 *
 */
public class DiagramFactory {
	
	public static final DiagramFactory instance = new DiagramFactory();
	
	private DiagramFactory(){}
	
	/**
	 * Creates a new diagram inside the model modelname inside the project.
	 * @param diagramName
	 * @param diagramType
	 * @param papyrusEditor the editor in which the diagram will be created
	 * @return
	 * @throws CreationCommandNotFoundException
	 * @throws ServiceException
	 * @throws ExecutionException
	 */
	public IDiagramHandler create(String diagramName,DiagramType diagramType,PapyrusEditor papyrusEditor) throws CreationCommandNotFoundException, ServiceException, ExecutionException{
		ModelSet modelSet = papyrusEditor.getModelSet();
		ICreationCommand creationCommand = diagramType.getCreationCommand();
		creationCommand.createDiagram(modelSet, null, diagramName);
		IEditorPart activeEditor = papyrusEditor.getEditor().getActiveEditor();		
		DiagramEditor diagramEditor = (DiagramEditor) activeEditor; //if there is a cast exception, there is a grave error?
		return new DefaultDiagramHandler(UmlUtils.getUmlModel(modelSet),diagramEditor.getDiagramEditPart());			
	}
	
	/**
	 * Loads a diagram inside a papyrus editor
	 * @param diagramName the name of the diagram
	 * @return the diagram handler of the asked diagram
	 * @throws CreationCommandNotFoundException
	 * @throws ServiceException
	 * @throws ExecutionException
	 */
//	public IDiagramHandler load(String diagramName,PapyrusEditor papyrusEditor) throws CreationCommandNotFoundException, ServiceException, ExecutionException{
//		ModelSet modelSet = papyrusEditor.getModelSet();
//		IEditorPart activeEditor = papyrusEditor.getEditor().getActiveEditor();		
//		DiagramEditor diagramEditor = (DiagramEditor) activeEditor; //if there is a cast exception, there is a grave error?
//		return new DefaultDiagramHandler(UmlUtils.getUmlModel(modelSet),diagramEditor.getDiagramEditPart());			
//	}
	



}
