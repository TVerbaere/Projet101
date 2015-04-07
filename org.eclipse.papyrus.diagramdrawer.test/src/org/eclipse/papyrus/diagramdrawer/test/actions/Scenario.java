package org.eclipse.papyrus.diagramdrawer.test.actions;


import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.UnmovableViewException;
import org.eclipse.papyrus.diagramdrawer.handlers.AbstractDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler;
import org.eclipse.papyrus.editor.PapyrusMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Element;

public class Scenario {

	public static void execute() throws NotFoundException, ServiceException, LocationNotFoundException, UnmovableViewException {
		
		// Triche pour tester, normalement on doit utiliser la factory :
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		PapyrusMultiDiagramEditor papyrusEditor = ((PapyrusMultiDiagramEditor)editorPart);
		ModelSet modelSet = papyrusEditor.getServicesRegistry().getService(ModelSet.class);
		UmlModel umlModel = UmlUtils.getUmlModel(modelSet);
		EObject model = umlModel.lookupRoot();

		// Création du handler pour un diagramme de classe :
		final IDiagramHandler handler = new AbstractDiagramHandler(model,papyrusEditor);
		TransactionalEditingDomain ted = handler.getTED();
		
		// On doit faire les actions dans la transaction pour les undo/redo
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {
				
		
				List<View> views = handler.getElementViewByName("Sith");

				View sith_view = views.get(0);
				try {
					Element elem = handler.getElementByName("ennemy").get(0);
					System.out.println(elem);
					handler.draw(elem, false);
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			
			
			}
		});
		
		
	}

}
