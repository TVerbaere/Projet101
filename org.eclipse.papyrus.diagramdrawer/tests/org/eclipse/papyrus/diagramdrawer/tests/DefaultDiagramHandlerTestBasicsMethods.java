package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidLocationException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidSizeException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotDimensionedViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotResizableViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.TargetOrSourceNotDrawnException;
import org.eclipse.papyrus.diagramdrawer.exceptions.UnmovableViewException;
import org.eclipse.papyrus.diagramdrawer.handlers.DefaultDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.uml2.uml.Element;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the class DefaultDiagramHandler.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class DefaultDiagramHandlerTestBasicsMethods {
	
	private DefaultDiagramHandler handler;
	
	private View drawn_element;
	
	private View drawn_association;
	
	@BeforeClass
	public void setUp() throws ExecutionException {
	
		//TODO : Creer un model avec les factories + creer une classe : Class1.
		// + creer une association.
		// -> Quand les factories seront créées !
		
		
		// Draw a class.
		Element element = null;
		Element element2 = null;
		Element association = null;
		try {
			drawn_element = handler.draw(element, false);
			handler.draw(element2, false);
			drawn_association = handler.draw(association, false);
		} catch (TargetOrSourceNotDrawnException e) {
			// Ignore.
		}

	}
	
	
	@Test
	public void changeDefaultLocationTest() {
		
		Point illegal_location = new Point(-5,-20);
		Point legal_location = new Point(5,20);
		Point default_location = new Point(0,0);
		
		// Start : defaultLocation is (0,0)
		assertEquals(default_location,handler.getDefaultLocation());
		
		boolean result = false;
		
		// Try : change defaultLocation with an illegal location.
		try {
			handler.setDefaultLocation(illegal_location);
		} catch (NotAValidLocationException e) {
			result = true;
		}
		assertTrue(result);
		
		// Try : change defaultLocation with a legal location.
		try {
			handler.setDefaultLocation(legal_location);
		} catch (NotAValidLocationException e) {
			result = false;
		}
		assertTrue(result);
		assertEquals(legal_location,handler.getDefaultLocation());
		
	}
	
	
	@Test
	public void changeWidthTest() {
		
		int illegal_width = -200;
		int legal_width = 80;
		
		boolean result = false;
		
		// Try : change width with an illegal value.
		try {
			handler.setWidth(drawn_element, illegal_width);
		} catch (NotResizableViewException e) {
			// Ignore : it's a class.
		} catch (NotAValidSizeException e) {
			result = true;
		}
		assertTrue(result);
		
		// Try : change width with a legal value.
		try {
			handler.setWidth(drawn_element, legal_width);
		} catch (NotResizableViewException e) {
			// Ignore : it's a class.
		} catch (NotAValidSizeException e) {
			result = false;
		}
		assertTrue(result);
		
		int width = 0;
		try {
			width = handler.getWidth(drawn_element);
		} catch (NotDimensionedViewException e) {
			//Ignore.
		}
		assertEquals(legal_width,width);
		
	}
	
	
	@Test
	public void changeWidthOnAssociationTest() {
		
		int legal_width = 80;
		
		boolean result = false;
		
		// Try : change width with a legal value on association.
		try {
			handler.setWidth(drawn_association, legal_width);
		} catch (NotResizableViewException e) {
			result = true;
		} catch (NotAValidSizeException e) {
			// Ignore, good value.
		}
		assertTrue(result);
		
	}
	
	
	@Test
	public void changeHeightTest() {
		
		int illegal_height = -200;
		int legal_height = 80;
		
		boolean result = false;
		
		// Try : change height with an illegal value.
		try {
			handler.setHeight(drawn_element, illegal_height);
		} catch (NotResizableViewException e) {
			// Ignore : it's a class.
		} catch (NotAValidSizeException e) {
			result = true;
		}
		assertTrue(result);
		
		// Try : change height with a legal value.
		try {
			handler.setHeight(drawn_element, legal_height);
		} catch (NotResizableViewException e) {
			// Ignore : it's a class.
		} catch (NotAValidSizeException e) {
			result = false;
		}
		assertTrue(result);
		
		int height = 0;
		try {
			height = handler.getHeight(drawn_element);
		} catch (NotDimensionedViewException e) {
			//Ignore.
		}
		assertEquals(legal_height,height);
		
	}
	
	@Test
	public void changeHeightOnAssociationTest() {
		
		int legal_height = 80;
		
		boolean result = false;
		
		// Try : change height with a legal value on association.
		try {
			handler.setHeight(drawn_association, legal_height);
		} catch (NotResizableViewException e) {
			result = true;
		} catch (NotAValidSizeException e) {
			// Ignore, good value.
		}
		assertTrue(result);
		
	}
	
	
	@Test
	public void changeLocationTest() {
		
		Point illegal_location = new Point(-500,-200);
		Point legal_location = new Point(350,200);
		
		boolean result = false;
		
		// Try : change Location with an illegal location.
		try {
			handler.setLocation(drawn_element, illegal_location);
		} catch (NotAValidLocationException e) {
			result = true;
		} catch (UnmovableViewException e) {
			// Ignore.
		}
		assertTrue(result);
		
		// Try : change Location with a legal location.
		try {
			handler.setLocation(drawn_element, legal_location);
		} catch (NotAValidLocationException e) {
			result = false;
		} catch (UnmovableViewException e) {
			// Ignore.
		}
		assertTrue(result);
		
		Point location = null;
		try {
			location = handler.getLocation(drawn_element);
		} catch (LocationNotFoundException e) {
			// Ignore.
		}
		assertEquals(legal_location,location);
		
	}
	
	@Test
	public void changeLocationOnAssociationTest() {
		
		Point legal_location = new Point(350,200);
		
		boolean result = false;
		
		// Try : change Location with a legal location.
		try {
			handler.setLocation(drawn_association, legal_location);
		} catch (NotAValidLocationException e) {
			// Ignore.
		} catch (UnmovableViewException e) {
			result = true;
		}
		assertTrue(result);
		
	}
	
	
	@Test
	public void getElementByNameTest() {
		//TODO
	}
	
	
	@Test
	public void getElementViewByNameTest() {
		//TODO
	}
	
	
	@Test
	public void deleteTest() {
		//TODO
	}
	
	
	@Test
	public void autoSizeTest() {
		//TODO
	}

}
