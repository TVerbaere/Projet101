package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.InvalidContainerException;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidLocationException;
import org.eclipse.papyrus.diagramdrawer.exceptions.TargetOrSourceNotDrawnException;
import org.eclipse.papyrus.diagramdrawer.factories.DiagramFactory;
import org.eclipse.papyrus.diagramdrawer.factories.PapyrusEditorFactory;
import org.eclipse.papyrus.diagramdrawer.factories.ProjectFactory;
import org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.othersources.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.papyrus.diagramdrawer.othersources.PapyrusEditor;
import org.eclipse.papyrus.diagramdrawer.utils.DiagramType;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

public class IntegrationTest {

	public static final String PROJECT_NAME = "org.eclipse.papyrus.diagramdrawer.integration.test.project";
	public static final String MODEL_NAME = "integration test model";

	@Test
	public void testApi() throws ExecutionException,
			CreationCommandNotFoundException, NotFoundException,
			TargetOrSourceNotDrawnException, InvalidContainerException,
			LocationNotFoundException, NotAValidLocationException, ServiceException {
		final EclipseProject eclipseProject = ProjectFactory.instance
				.build(PROJECT_NAME);
		final PapyrusEditor papyrusEditor = PapyrusEditorFactory.instance.create(eclipseProject, MODEL_NAME);
		final ModelSet modelSet = papyrusEditor.getModelSet();
		final UmlModel umlModel = UmlUtils.getUmlModel(modelSet);
		// UMLModelHelper umlModelHelper = new UMLModelHelper(umlModel);
		TransactionalEditingDomain ted = papyrusEditor.getTransactionalEditingDomain();
//		ted.getCommandStack().execute(new RecordingCommand(ted) {
//			protected void doExecute() {
//				try {
//					Model model = (Model) umlModel.lookupRoot();
//					IDiagramHandler diagramHandler = DiagramFactory.instance.create("TEST DIAGRAM1", DiagramType.Class, papyrusEditor);
//					Class class1 = model.createOwnedClass("class 1", false);
//					Class class2 = model.createOwnedClass("class 2", false);
//					class1.createOwnedAttribute("c1_attr1", null);
//					class1.createOwnedAttribute("c1_attr2", null);
//					Property c2_attr = class2.createOwnedAttribute("c2_attr",
//							null);
//					View viewClass1 = diagramHandler.draw(class1, true);
//					// s'assurer que bien dessiné
//					assertEquals(new Point(0, 0),
//							diagramHandler.getLocation(viewClass1));
//					View viewClass2 = diagramHandler.draw(class2, new Point(
//							200, 200), false);
//					// s'assurer que bien dessiné
//					assertEquals(new Point(200, 200),
//							diagramHandler.getLocation(viewClass2));
//					diagramHandler
//							.drawElementInside(viewClass2, c2_attr, false);
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//			}
//		});
	}
}