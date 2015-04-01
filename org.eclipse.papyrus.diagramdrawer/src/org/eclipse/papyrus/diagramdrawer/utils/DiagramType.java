package org.eclipse.papyrus.diagramdrawer.utils;

import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.diagramdrawer.exceptions.CreationCommandNotFoundException;

/**
 * Enumeration of types for Papyrus diagrams.
 * 
 * @author Thibaud VERBAERE
 *
 */
public enum DiagramType {
	
	Activity,
	Class,
	Communication,
	Component,
	Composite,
	Deployment,
	Sequence,
	StateMachine,
	Timing,
	UseCase;
	
	
	/**
	 * Return the command to create the specific diagram.
	 * 
	 * @return the command
	 * 
	 * @throws CreationCommandNotFoundException if the command cannot be found
	 */
	public ICreationCommand getCreationCommand() throws CreationCommandNotFoundException {
		
		String thepackage = this.toString().toLowerCase();
		
		// The path is different for class and timing diagram.
		if (thepackage.equals("class"))
			thepackage =  "clazz";
		
		if (thepackage.equals("timing"))
			thepackage =  "timing.custom";
		
		String _package = "org.eclipse.papyrus.uml.diagram."+thepackage+".Create"+this.toString()+"DiagramCommand";
	
		// Try to create the creation command.
		try {
			Class<?> c = java.lang.Class.forName(_package);
			return (ICreationCommand) c.newInstance();
		}
		catch (Exception e) {
			throw new CreationCommandNotFoundException();
		}
	
	
	}
	
	
}
