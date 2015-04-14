package org.eclipse.papyrus.diagramdrawer.exceptions;


/**
 * Exception thrown when draw is called with cascade mode on a relationship but
 * the target or/and the source of this relationship is not drawn.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class TargetOrSourceNotDrawnException extends DrawerException {

	private static final long serialVersionUID = 5983301756864560102L;

}
