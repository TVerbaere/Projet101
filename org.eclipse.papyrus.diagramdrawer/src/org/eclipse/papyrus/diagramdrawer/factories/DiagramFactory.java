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
import org.eclipse.papyrus.diagramdrawer.othersources.ProgramaticPapyrusEditor;
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
	 * creates a new diagram inside the model modelname inside the project
	 * @param diagramName
	 * @param diagramType
	 * @param project
	 * @param modelName
	 * @return
	 * @throws CreationCommandNotFoundException
	 * @throws ServiceException
	 * @throws ExecutionException
	 */
	public IDiagramHandler create(String diagramName,DiagramType diagramType,EclipseProject project,String modelName) throws CreationCommandNotFoundException, ServiceException, ExecutionException{
		ProgramaticPapyrusEditor papyrusEditor = new ProgramaticPapyrusEditor(project, modelName);
		ModelSet modelSet = papyrusEditor.getModelSet();
		ICreationCommand creationCommand = diagramType.getCreationCommand();
		creationCommand.createDiagram(modelSet, null, diagramName);
		IEditorPart activeEditor = papyrusEditor.getEditor().getActiveEditor();
		if(activeEditor instanceof DiagramEditor){
			DiagramEditor diagramEditor = (DiagramEditor) activeEditor;
			return new DefaultDiagramHandler(UmlUtils.getUmlModel(modelSet),diagramEditor.getDiagramEditPart());
		}
		return null;		
	}
	
	/**
	 * Creates a new project with the new projectname, the model name for the model
	 * and a new diagram inside the model
	 * @param diagramName
	 * @param diagramType
	 * @param projectname
	 * @param modelName
	 * @return
	 * @throws CreationCommandNotFoundException
	 * @throws ServiceException
	 * @throws ExecutionException
	 */
	public IDiagramHandler create(String diagramName,DiagramType diagramType,String projectname,String modelName) throws CreationCommandNotFoundException, ServiceException, ExecutionException{
		ProgramaticPapyrusEditor papyrusEditor = new ProgramaticPapyrusEditor(projectname, modelName);
		ModelSet modelSet = papyrusEditor.getModelSet();
		ICreationCommand creationCommand = diagramType.getCreationCommand();
		creationCommand.createDiagram(modelSet, null, diagramName);
		IEditorPart activeEditor = papyrusEditor.getEditor().getActiveEditor();
		if(activeEditor instanceof DiagramEditor){
			DiagramEditor diagramEditor = (DiagramEditor) activeEditor;
			return new DefaultDiagramHandler(UmlUtils.getUmlModel(modelSet),diagramEditor.getDiagramEditPart());
		}
		return null;		
	}


}
