package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.InvalidContainerException;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NonExistantViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidLocationException;
import org.eclipse.papyrus.diagramdrawer.exceptions.TargetOrSourceNotDrawnException;
import org.eclipse.papyrus.diagramdrawer.handlers.DefaultDiagramHandler;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Operation;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the class DefaultDiagramHandler.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class DefaultDiagramHandlerTestDraws {
	
	private DefaultDiagramHandler handler;
	
	private Class element;
	
	private Class element2;
	
	private Class element3;
	
	private Association association;
	
	private Association association2;
	
	private Property attribute;
	
	private Property attribute2;
	
	private Property attribute3;
	
	private Operation method;
	
	private Operation method2;
	
	private Operation method3;
	
	
	@BeforeClass
	public void setUp() throws ExecutionException {
	
		//TODO : Creer un model avec les factories + creer des classes/associations/propriétés.
		// -> Quand les factories seront créées !
		
		Package model = null;
		handler = null;
		
		// Dans la transaction :
		element = model.createOwnedClass("Class1", false);
		element2 = model.createOwnedClass("Class2", false);
		element3 = model.createOwnedClass("Class3", false);
		attribute = element.createOwnedAttribute("att1", null);
		attribute2 = element2.createOwnedAttribute("att2", null);
		attribute3 = element3.createOwnedAttribute("att3", null);
		method = element.createOwnedOperation("meth1", null, null);
		method2 = element.createOwnedOperation("meth2", null, null);
		method3 = element.createOwnedOperation("meth3", null, null);
		association = element.createAssociation(true, AggregationKind.COMPOSITE_LITERAL, "name1", 1, 1, element2, true, AggregationKind.SHARED_LITERAL, "name2", 0, 1);
		association.setName("asso1");
		association2 = element.createAssociation(true, AggregationKind.COMPOSITE_LITERAL, "name3", 1, 1, element3, true, AggregationKind.SHARED_LITERAL, "name4", 0, 1);
		association2.setName("asso2");

	}
	
	
	@Test
	public void drawClassAtDefaultLocationTest() {
		View view = null;
		// Draw Class1 without location : draw at default location.
		try {
			view = handler.draw(element, false);
		} catch (TargetOrSourceNotDrawnException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Only Class1 is drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(method));
		assertFalse(handler.isDrawn(attribute));
		
		// Check the location.
		try {
			assertEquals(handler.getDefaultLocation(),handler.getLocation(view));
		} catch (LocationNotFoundException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Reset :
		try {
			handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
	}
	
	@Test
	public void drawClassAtDefaultLocationOnCascadeTest() {
		View view = null;
		// Draw Class1 without location : draw at default location.
		try {
			view = handler.draw(element, true);
		} catch (TargetOrSourceNotDrawnException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Class1, meth1 and att1 are drawn.
		assertTrue(handler.isDrawn(element));
		assertTrue(handler.isDrawn(method));
		assertTrue(handler.isDrawn(attribute));
		
		// Check the location.
		try {
			assertEquals(handler.getDefaultLocation(),handler.getLocation(view));
		} catch (LocationNotFoundException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Reset :
		try {
			handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
	}
	
	@Test
	public void drawAssociationSuccessfulTest() {
		View view = null;
		View view2 = null;
		// Draw Class1 and Class2.
		try {
			view = handler.draw(element, false);
			view2 = handler.draw(element2, false);
			// it's possible to draw asso1 :
			handler.draw(association, false);
		} catch (TargetOrSourceNotDrawnException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Check Class1, Class2 and Asso1 are drawn :
		assertTrue(handler.isDrawn(element));
		assertTrue(handler.isDrawn(element2));
		assertTrue(handler.isDrawn(association));
				
		// Reset :
		try {
			handler.delete(view);
			handler.delete(view2);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
	}
	
	@Test
	public void drawAssociationUnSuccessfulTest() {

		boolean exception_ok = false;
		
		try {
			// it's not possible to draw asso1 because the target and the source are not drawn.
			handler.draw(association, false);
		} catch (TargetOrSourceNotDrawnException e) {
			exception_ok = true;
		}
		
		assertTrue(exception_ok);
		
		// Check Asso1 is not drawn :
		assertFalse(handler.isDrawn(association));
		
	}
	
	@Test
	public void drawAssociationOnCascadeTest() {

		try {
			// it's possible to draw asso1 with cascade active :
			handler.draw(association, true);
		} catch (TargetOrSourceNotDrawnException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Check Class1, Class2 and Asso1 are drawn :
		assertTrue(handler.isDrawn(element));
		assertTrue(handler.isDrawn(element2));
		assertTrue(handler.isDrawn(association));
				
		// Reset :
		try {
			View view = handler.getViewByElement(element).get(0);
			View view2 = handler.getViewByElement(element2).get(0);
			handler.delete(view);
			handler.delete(view2);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
	}
	
	@Test
	public void drawAtInvalidLocationTest() {
		boolean exception_ok = false;
		
		try {
			handler.draw(element, new Point(-5,100), false);
		} catch (NotAValidLocationException e) {
			exception_ok = true;
		}
		
		assertTrue(exception_ok);
		
		// Check Class1 is not drawn :
		assertFalse(handler.isDrawn(element));
	}
	
	@Test
	public void drawAllWithInvalidLocationTest() {
		boolean exception_ok = false;
		List<Element> elem_list = new ArrayList<Element>();
		List<Point> pt_list = new ArrayList<Point>();
		List<View> result = null;
		
		//Create lists :
		elem_list.add(element);
		elem_list.add(element2);
		pt_list.add(new Point(50,50));
		pt_list.add(new Point(-50,50)); // Not valid !
		
		try {
			result = handler.drawAll(elem_list, pt_list, false);
		} catch (NotAValidLocationException e) {
			exception_ok = true;
		} catch (IllegalArgumentException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		assertTrue(exception_ok);
		assertTrue(result.size() == 1);
		
		// Check Class1 is drawn and Class2 not drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(element2));
		
		// Reset :
		try {
			handler.delete(result.get(0));
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
	}
	
	@Test
	public void drawAllWithButBadSizeTest() {
		boolean exception_ok = false;
		List<Element> elem_list = new ArrayList<Element>();
		List<Point> pt_list = new ArrayList<Point>();
		List<View> result = null;
		
		//Create lists :
		elem_list.add(element);
		elem_list.add(element2);
		pt_list.add(new Point(50,50));
		
		try {
			result = handler.drawAll(elem_list, pt_list, false);
		} catch (IllegalArgumentException e) {
			exception_ok = true;
		} catch (NotAValidLocationException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		assertTrue(exception_ok);
		assertTrue(result.size() == 0);
		
		// Check Class1 and Class2 are not drawn.
		assertFalse(handler.isDrawn(element));
		assertFalse(handler.isDrawn(element2));
	}
	
	@Test
	public void drawSuccessful() {
		View view = null;
		Point LOCATION = new Point(50,50);
		// Draw Class1.
		try {
			view = handler.draw(element,LOCATION,false);
		} catch (NotAValidLocationException e1) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Only Class1 is drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(method));
		assertFalse(handler.isDrawn(attribute));
		
		// Check the location.
		try {
			assertEquals(LOCATION,handler.getLocation(view));
		} catch (LocationNotFoundException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Reset :
		try {
			handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
	}

	@Test
	public void drawAllSuccessful() {
		List<Element> elem_list = new ArrayList<Element>();
		List<Point> pt_list = new ArrayList<Point>();
		List<View> result = null;
		
		Point LOCATION = new Point(50,50);
		Point LOCATION2 = new Point(100,100);
		Point LOCATION3 = new Point(100,50);
		
		// Create lists :
		elem_list.add(element);
		elem_list.add(element2);
		elem_list.add(element3);
		pt_list.add(LOCATION);
		pt_list.add(LOCATION2);
		pt_list.add(LOCATION3);
		
		
		// DrawAll :
		try {
			result = handler.drawAll(elem_list,pt_list,false);
		} catch (Exception e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Class1,Class2 and Class3 are drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(method));
		assertFalse(handler.isDrawn(attribute));
		assertTrue(handler.isDrawn(element2));
		assertFalse(handler.isDrawn(method2));
		assertFalse(handler.isDrawn(attribute2));
		assertTrue(handler.isDrawn(element3));
		assertFalse(handler.isDrawn(method3));
		assertFalse(handler.isDrawn(attribute3));
		
		assertTrue(result.size() == 3);
		
		// Check locations.
		try {
			assertEquals(LOCATION,handler.getLocation(result.get(0)));
			assertEquals(LOCATION2,handler.getLocation(result.get(1)));
			assertEquals(LOCATION3,handler.getLocation(result.get(2)));
		} catch (LocationNotFoundException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Reset :
		try {
			for (View view : result)
				handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
	}
	
	@Test
	public void drawAllSuccessfulOnCascade() {
		List<Element> elem_list = new ArrayList<Element>();
		List<Point> pt_list = new ArrayList<Point>();
		List<View> result = null;
		
		Point LOCATION = new Point(50,50);
		Point LOCATION2 = new Point(100,100);
		Point LOCATION3 = new Point(100,50);
		
		// Create lists :
		elem_list.add(element);
		elem_list.add(element2);
		elem_list.add(element3);
		pt_list.add(LOCATION);
		pt_list.add(LOCATION2);
		pt_list.add(LOCATION3);
		
		
		// DrawAll :
		try {
			result = handler.drawAll(elem_list,pt_list,true);
		} catch (Exception e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Class1,Class2 and Class3 are drawn.
		assertTrue(handler.isDrawn(element));
		assertTrue(handler.isDrawn(method));
		assertTrue(handler.isDrawn(attribute));
		assertTrue(handler.isDrawn(element2));
		assertTrue(handler.isDrawn(method2));
		assertTrue(handler.isDrawn(attribute2));
		assertTrue(handler.isDrawn(element3));
		assertTrue(handler.isDrawn(method3));
		assertTrue(handler.isDrawn(attribute3));
		
		assertTrue(result.size() == 3);
		
		// Normally, asso1 et asso2 are drawn.
		List<View> view_asso1 = handler.getViewByElement(association);
		List<View> view_asso2 = handler.getViewByElement(association2);
		
		assertTrue(view_asso1.size() == 1);
		assertTrue(view_asso2.size() == 1);
		
		assertTrue(handler.isDrawn(association));
		assertTrue(handler.isDrawn(association2));

		
		// Check locations.
		try {
			assertEquals(LOCATION,handler.getLocation(result.get(0)));
			assertEquals(LOCATION2,handler.getLocation(result.get(1)));
			assertEquals(LOCATION3,handler.getLocation(result.get(2)));
		} catch (LocationNotFoundException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Reset :
		try {
			for (View view : result)
				handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
	}
	
	@Test
	public void drawSuccessfulOnCascade() {
		View view = null;
		View view2 = null;
		Point LOCATION = new Point(50,50);
		// Draw Class3 with cascade.
		try {
			// Draw Class1 without cascade before.
			view = handler.draw(element,false);
			view2 = handler.draw(element3,LOCATION,true);
		} catch (Exception e1) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Only Class1 is drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(method));
		assertFalse(handler.isDrawn(attribute));
		
		// Class3, att3 and method3 are drawn.
		assertTrue(handler.isDrawn(element3));
		assertTrue(handler.isDrawn(method3));
		assertTrue(handler.isDrawn(attribute3));
		
		// Normally, asso1 et asso2 are drawn.
		List<View> view_asso2 = handler.getViewByElement(association2);
				
		assertTrue(view_asso2.size() == 1);
				
		assertTrue(handler.isDrawn(association2));
		
		// Check the location.
		try {
			assertEquals(LOCATION,handler.getLocation(view2));
		} catch (LocationNotFoundException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Reset :
		try {
			handler.delete(view);
			handler.delete(view2);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
	}
	
	@Test
	public void drawInsideUnSuccessful() {
		View view = null;
		try {
			// Draw Class1 without cascade before.
			view = handler.draw(element,false);
		} catch (Exception e1) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Only Class1 is drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(method));
		assertFalse(handler.isDrawn(attribute));
		
		try {
			handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		boolean exception_ok = false;
		
		//Try to place att1 inside a delected element.
		try {
			handler.drawElementInside(view, attribute, false);
		} catch (InvalidContainerException e) {
			exception_ok = true;
		}
		
		assertTrue(exception_ok);
	}
	
	@Test
	public void drawInsideSuccessful() {
		View view = null;
		try {
			// Draw Class1 without cascade before.
			view = handler.draw(element,false);
		} catch (Exception e1) {
			// Normally it's impossible !
			assertTrue(false);
		}
		
		// Only Class1 is drawn.
		assertTrue(handler.isDrawn(element));
		assertFalse(handler.isDrawn(method));
		assertFalse(handler.isDrawn(attribute));
		
		//Try to place att1 inside an element.
		try {
			handler.drawElementInside(view, attribute, false);
		} catch (InvalidContainerException e) {
			// Normally it's impossible !
			assertTrue(false);
		}

		assertTrue(handler.isDrawn(attribute));
		
		// Reset :
		try {
			handler.delete(view);
		} catch (NonExistantViewException e) {
			// Normally it's impossible !
			assertTrue(false);
		}
	}

}
