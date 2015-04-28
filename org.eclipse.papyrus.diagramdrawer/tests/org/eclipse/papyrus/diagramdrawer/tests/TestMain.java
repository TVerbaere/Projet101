package org.eclipse.papyrus.diagramdrawer.tests;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.papyrus.diagramdrawer.factories.ProjectFactory;
import org.eclipse.papyrus.diagramdrawer.utils.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.utils.ExecutionException;

public class TestMain {

	public static void main(String[] args) throws ExecutionException, CoreException {
		ProjectFactory.instance.build("blabla");
	}

}
