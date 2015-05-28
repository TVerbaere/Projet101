package org.eclipse.papyrus.diagramdrawer.exceptions;

/**
 * Exception thrown during the plugin execution.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class DrawerException extends Exception {

	public DrawerException(){
		super();
	}
	
	public DrawerException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -5042326691008299680L;

}
