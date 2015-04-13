package org.eclipse.papyrus.diagramdrawer.factory;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;
import org.eclipse.papyrus.diagramdrawer.utils.DiagramType;
import org.eclipse.papyrus.infra.core.resource.ModelSet;

/**
 * Lets to create new papyrus diagram inside a project programatically. The main use is to call
 * the method build to create a new diagramEditPart
 * 
 * @author Allan Rakotoarivony
 *
 */
public class DiagramFactory {
	
	public static final DiagramFactory instance = new DiagramFactory();
	
	private DiagramFactory(){}
	
	public DiagramEditPart create(ModelSet modelSet,String diagramName,DiagramType diagramType) throws CreationCommandNotFoundException{
		ICreationCommand command = diagramType.getCreationCommand();			
		Diagram diagram = command.createDiagram(modelSet, null, diagramName);		
		return new DiagramEditPart(diagram);
	}

}
