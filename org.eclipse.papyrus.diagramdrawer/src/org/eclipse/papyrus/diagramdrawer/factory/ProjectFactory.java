package org.eclipse.papyrus.diagramdrawer.factory;

import org.eclipse.papyrus.diagramdrawer.othersources.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;

/**
 * Lets to create new papyrus project programatically.
 * The main use is to call the method build to create a new project
 * @author Allan Rakotoarivony
 *
 */
public class ProjectFactory {
	
	public final static ProjectFactory instance= new ProjectFactory();
	
	private ProjectFactory(){};
	
	/**
	 * Creates a new eclipse project with the given project name or loads it if it exists in the workspace
	 * @param projectName
	 * @return
	 * @throws ExecutionException
	 */
	public EclipseProject build(String projectName) throws ExecutionException{
		return new EclipseProject(projectName);
	}
}
