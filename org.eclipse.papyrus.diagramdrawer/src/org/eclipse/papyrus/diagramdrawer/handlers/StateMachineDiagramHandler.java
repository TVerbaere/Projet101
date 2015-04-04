package org.eclipse.papyrus.diagramdrawer.handlers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.papyrus.editor.PapyrusMultiDiagramEditor;
import org.eclipse.papyrus.uml.diagram.statemachine.part.UMLDiagramEditor;


/**
 * used to handle elements on a state-machine diagram.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class StateMachineDiagramHandler extends AbstractDiagramHandler {
	
	
	private UMLDiagramEditor diagramEditor;
	

	public StateMachineDiagramHandler(EObject model,
			PapyrusMultiDiagramEditor papyrusEditor) {
		super(model, papyrusEditor);
	}


	@Override
	public void executeDropOn(EditPart editpart) {
		// Create the command with the request.
		Command commandDrop = editpart.getCommand(this.drop);
		
		this.diagramEditor.getDiagram();
		// Execute the command.
		if (commandDrop.canExecute())
			this.clazzdiagrameditPart.getDiagramEditDomain().getDiagramCommandStack().execute(commandDrop);
		
	}

}