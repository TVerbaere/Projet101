package org.eclipse.papyrus.diagramdrawer.handlers;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.diagramdrawer.exceptions.InvalidContainerException;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NonExistantViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotDimensionedViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotResizableViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.UnmovableViewException;
import org.eclipse.uml2.uml.Element;

/**
 * Provides methods to draw elements inside a diagram.
 * A diagram handler is meant to handle one diagram.
 * Every view in the diagram can be also manipulated by resizing or relocating them.
 * @author Allan RAKOTOARIVONY
 * @author Thibaud VERBAERE
 * 
 * @version 1.4
 *
 */
public interface IDiagramHandler {
	
		/**
		 * Sets the size of the view v to the maximum size of its contents.
		 * @param v the view
		 */
		public void autoSize(View v);
		
		/**
		 * Draws the requested element into the handled diagram.
		 * If the element is an inner element (for example properties inside a class),
		 * it will be automatically placed inside the view of its parent if it exists in the diagram.
		 * If it does not exist, it will be created.
		 * If the element is not an inner element, it will be automatically placed next to the last not inner element drawn.
		 * @param element The element to be drawn on the handled diagram
		 * @param cascade True if all contents in the element must be drawn in the same time, False in the other case
		 * @return A View representing the element in the diagram
		 */
		public View draw(Element element, boolean cascade);
		
		/**
		 * Draws the requested element into the handled diagram at the given position.
		 * If the element is an inner element (for example properties inside a class),the location will be ignored and
		 * it will be automatically placed inside the view of its parent if it exists in the diagram.
		 * If it does not exist, it will be created.
		 * If the element is not an inner element, it will be placed to the requested position if it is free.
		 * If not an exception is thrown.
		 * @param element The element to be drawn on the handled diagram
		 * @param location The location of the element
		 * @param cascade True if all contents in the element must be drawn in the same time, False in the other case
		 * @return A view representing the drawn element
		 */
		
		public View draw(Element element, Point location, boolean cascade);
		
		/**
		 * Draws the view of the element inside a view.
		 * The view must be a valid location for the element which means that the view must be a parent representation of the element.
		 * Otherwise, an exception is thrown.
		 * @param container The view n which the element will be drawn
		 * @param element The element to be drawn in the view
		 * @param cascade True if all contents in the element must be drawn in the same time, False in the other case
		 * @throws InvalidContainerException if the element cannot be placed inside the container or the container does not exists
		 * @return A view representing the drawn element
		 */
		public View drawElementInside(View container, Element element, boolean cascade) throws InvalidContainerException;
		
		/**
		 * Draws all elements at their locations.
		 * If an view of an element overlaps the location of another view, it will not be drawn.
		 * @param elements A list of elements to be drawn
		 * @param locations A list of location corresponding to the elements
		 * @param cascade True if all contents in the element must be drawn in the same time, False in the other case
		 * @throws IllegalArgumentException If the number of locations is smaller than the  number of elements
		 * @return A list of the drawn views
		 */
		public List<View> drawAll(List<Element> elements,List<Point>locations, boolean cascade) throws IllegalArgumentException;
		
//		/**
//		 * 
//		 * @param element
//		 * @param position
//		 * @param base
//		 * @param interval
//		 * @param cascade
//		 * @return
//		 * @throws LocationNotFoundException 
//		 */
//		public View drawAtPosition(Element element, Position position, View base, int interval, boolean cascade) throws LocationNotFoundException;
		
		/**
		 * Deletes the view in the handled diagram.
		 * An exception is thrown if the view does not exists or cannot be deleted.
		 * @param view The view to delete from the handled diagram
		 * @throws NonExistantViewException If the view does not exists
		 */
		public void delete(View view) throws NonExistantViewException;
		
		/**
		 * Returns a list of all views representing the element given as parameter.
		 * @param element The element which we want to get the views
		 * @return A list of views.
		 */
		public List<View> getViewByElement(Element element);
		
		/**
		 * Returns the location of a view inside the handled diagram.
		 * @param view the view which we want to get the location
		 * @return A point representing the coordinate of the upper left-most point of the view
		 * @throws LocationNotFoundException If the view has no location or doesn't exist.
		 */
		public Point getLocation(View view) throws LocationNotFoundException;
		
		/**
		 * Moves the view to the given location. The upper left-most point of the view is placed to the location given in parameter.
		 * @param view The view to move
		 * @param location The location where the view will be moved
		 * @throws UnmovableViewException If the view cannot be moved
		 */
		public void setLocation(View view,Point location) throws UnmovableViewException;
		
		/**
		 * Returns a list of all views representing the element which name is given as parameter.
		 * @param name The name of the element which we want to get the views
		 * @return A list of views.
		 */
		public List<View> getElementViewByName(String name);
		
		/**
		 * Returns a list of all elements representing in UML model the element which name is given as parameter.
		 * @param name The name of the element which we want to get the UML representation.
		 * @return A list of element.
		 */
		public List<Element> getElementByName(String name);
		
		/**
		 * Return the width of a view which name is given as parameter.
		 * @param view the view which we want to get his width
		 * @return the width of the view
		 * @throws NotDimensionedViewException if the view hasn't dimensions
		 */
		public int getWidth(View view) throws NotDimensionedViewException;
		
		/**
		 * Return the height of a view which name is given as parameter.
		 * @param view the view which we want to get his height
		 * @return the height of the view
		 * @throws NotDimensionedViewException if the view hasn't dimensions
		 */
		public int getHeight(View view) throws NotDimensionedViewException;

		/**
		 * Change the height of a view which name is given as parameter.
		 * @param view the view which we want to change his height
		 * @param newheight the newest value of the height
		 * @throws NotResizableViewException if the view hasn't dimensions
		 */
		public void setHeight(View view, int newheight) throws NotResizableViewException;
		
		/**
		 * Change the width of a view which name is given as parameter.
		 * @param view the view which we want to change his width
		 * @param newheight the newest value of the width
		 * @throws NotResizableViewException if the view hasn't dimensions
		 */
		public void setWidth(View view, int newwidth) throws NotResizableViewException;
		
		/**
		 * Return the model of the diagram.
		 * @return the model.
		 */
		public EObject getModel();

		/**
		 * Return the TransactionalEditingDomain of the diagram.
		 * @return the TransactionalEditingDomain
		 */
		public TransactionalEditingDomain getTED();

}