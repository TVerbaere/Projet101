package org.eclipse.papyrus.diagramdrawer.factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.papyrus.diagramdrawer.othersources.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.ModelsReader;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;

/**
 * Lets to create new papyrus model inside a project programatically. The main
 * use is to call the method build to create a new model
 * 
 * @author Allan Rakotoarivony
 * @author Cedric Dumoulin
 *
 */
public class ModelSetFactory {

	public static final ModelSetFactory instance = new ModelSetFactory();
	
	private ModelSetFactory(){}
	
	/**
	 * 
	 * @param project
	 * @return the uml model corresponding to the created model
	 * @throws ExecutionException 
	 */
	@SuppressWarnings("deprecation")
	public ModelSet createIn(EclipseProject eclipseProject,
			String modelName, boolean override) throws ExecutionException {
		try {
			eclipseProject.createFolders(modelName);

			// delete existing files, and then create a new model
			IFile file = eclipseProject.getProject().getFile(modelName + ".di");
			if (override && file.exists()) {
				file.delete(true, new NullProgressMonitor());
			}

			if (!file.exists()) {
				return createModel(file);
			}
			else{
				ModelSet modelSet= new ModelSet();
				modelSet.loadModels(file);
				return modelSet;
			}
		} catch (Exception e) {
			throw new ExecutionException("Can't init Project and Resources", e);
		}
	}

	/**
	 * Loads a model from an eclipse project
	 * 
	 * @param project
	 * @return
	 * @throws ExecutionException 
	 */
	public ModelSet load(EclipseProject eclipseProject, String modelName) throws ExecutionException {
		return createIn(eclipseProject, modelName, false);
	}

	@SuppressWarnings("deprecation")
	protected ModelSet createModel(IFile file) throws CoreException, IOException {

		// Create ModelSet and initialize it with models declared in eclipse extensions
		ModelSet modelSet = new ModelSet();
		ModelsReader reader = new ModelsReader();
		reader.readModel(modelSet);

		file.create(new ByteArrayInputStream(new byte[0]), true, new NullProgressMonitor());
		modelSet.createsModels(file);
		// populate the model
		UmlModel umlModel = UmlUtils.getUmlModel(modelSet);
		umlModel.initializeEmptyModel();

		// ICreationCommand command = getDiagramCommandCreation();
		// command.createDiagram(modelSet, null, "DiagramToTest");
		modelSet.save(new NullProgressMonitor());
		
		return modelSet;
		
	}

}
