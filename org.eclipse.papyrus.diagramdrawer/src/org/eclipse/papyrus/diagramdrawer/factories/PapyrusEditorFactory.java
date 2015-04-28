package org.eclipse.papyrus.diagramdrawer.factories;

import org.eclipse.papyrus.diagramdrawer.othersources.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.papyrus.diagramdrawer.othersources.PapyrusEditor;

/**
 * Creates a new papyrus editor which contains a reference to the model
 * and the transactional editing domain.
 * @author rakotoarivony
 *
 */
public class PapyrusEditorFactory {
	
	public static final PapyrusEditorFactory instance = new PapyrusEditorFactory();
	
	private PapyrusEditorFactory(){};
	
	/**
	 * Creates a new papyrus editor inside the model of the given project.
	 * If the model doesn't exist, it will be automatically created
	 * @param project the project in which we want to create an editor
	 * @param modelname the name of the model in which we want to create a new papyrus editor
	 * @return a new papyrus editor
	 * @throws ExecutionException 
	 */
	public PapyrusEditor create(EclipseProject project,String modelname) throws ExecutionException{
		return new PapyrusEditor(project,modelname);
	}
}
