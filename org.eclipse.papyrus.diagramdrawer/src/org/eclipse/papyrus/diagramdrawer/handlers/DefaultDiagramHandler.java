package org.eclipse.papyrus.diagramdrawer.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.BoundsImpl;
import org.eclipse.papyrus.diagramdrawer.exceptions.InvalidContainerException;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NonExistantViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidLocationException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotAValidSizeException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotDimensionedViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotResizableViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.TargetOrSourceNotDrawnException;
import org.eclipse.papyrus.diagramdrawer.exceptions.UnmovableViewException;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.gmfdiag.menu.utils.DeleteActionUtil;
import org.eclipse.papyrus.uml.diagram.common.util.DiagramEditPartsUtil;
import org.eclipse.papyrus.uml.diagram.menu.actions.SizeAction;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;

/**
 * Drawer used to handle elements on a diagram.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class DefaultDiagramHandler implements IDiagramHandler {
	
	/**
	 * Default location to display element without location.
	 */
	protected Point DEFAULT_LOCATION;
	
	/**
	 * The diagram model.
	 */
	protected UmlModel model;
	
	/**
	 * the Diagram Edit Part that is handled.
	 */
	protected DiagramEditPart diagrameditPart;
	
	/**
	 * Transactional editing domain.
	 */
	protected TransactionalEditingDomain ted;
	
	
	/**
	 * 
	 * Constructor.
	 *
	 * @param model the model of the diagram
	 * @param diagrameditPart the editpart of the diagram to handle
	 */
	public DefaultDiagramHandler(UmlModel model,DiagramEditPart diagrameditPart) {
		this.model = model;
		this.diagrameditPart = diagrameditPart;
		try {
			this.ted = TransactionUtil.getEditingDomain(this.model.lookupRoot());
		}
		catch (NotFoundException e) {
			// Ignore
		}
		this.DEFAULT_LOCATION = new Point(0,0);
	}

	
	/* ------------------------------------------------------------------------ */
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#draw(org.eclipse.uml2.uml.Element, boolean)
	 *
	 * @param element
	 * @param cascade
	 * @return
	 * @throws TargetOrSourceNotDrawnException
	 */
	public View draw(Element element, boolean cascade) throws TargetOrSourceNotDrawnException {
		
		if (element instanceof Relationship) {
			// Find the target and the source of the relationship (normally 2 elements).
			List<Element> related = ((Relationship)element).getRelatedElements();
			
			if (cascade) {
				// If the mode cascade is active then draw source and target element.
				for (Element e : related) {
					if (!this.isDrawn(e)) {
						try {
							this.draw(e,DEFAULT_LOCATION,cascade);
						}
						catch (NotAValidLocationException e1) {
							// Ignore : Impossible because it's the default location.
						}
					}
					
				}
				// Finally draw the relationship.
				try {
					return this.draw(element,DEFAULT_LOCATION,cascade);
				}
				catch (NotAValidLocationException e1) {
					// Ignore : Impossible because it's the default location.
				}
			}
			else {
				if (this.isDrawn(related.get(0)) && this.isDrawn(related.get(1))) {
					// Draw the relationship normally.
					try {
						return this.draw(element,DEFAULT_LOCATION,cascade);
					}
					catch (NotAValidLocationException e) {
						// Ignore : Impossible because it's the default location.
					}
				}
				else {
					// It's not possible to draw the relationship.
					throw new TargetOrSourceNotDrawnException();
				}
			}
			
		} else {
			try {
				return this.draw(element,DEFAULT_LOCATION,cascade);
			}
			catch (NotAValidLocationException e) {
				// Ignore : Impossible because it's the default location.
			}
		}
		
		return null;
	}

	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#draw(org.eclipse.uml2.uml.Element, org.eclipse.draw2d.geometry.Point, boolean)
	 *
	 * @param element
	 * @param location
	 * @param cascade
	 * @return
	 * @throws NotAValidLocationException
	 */
	public View draw(Element element, Point location, boolean cascade) throws NotAValidLocationException {
		// Find the list of views for the element before executing the request.
		List<View> views_before = this.getViewByElement(element);

		// Draw the element
		try {
			this.simpleDraw(element,location,null);
		}
		catch (NonExistantViewException e) {
			// Ignore : No parameter view
		}

		// Find the list of views for the element after.
		List<View> views_after = this.getViewByElement(element);
		// Remove before-elements to find the view created.
		views_after.removeAll(views_before);	
		
		
		if (cascade) {
			// Draw all relationships.
			List<Relationship> relations = element.getRelationships();
			for (Relationship relation : relations) {
				try {
					this.draw(relation,false);
				} 
				catch (TargetOrSourceNotDrawnException e) {
					// Ignore, draw just possible relationships.
				}
			}
			
			for (Element elem : element.allOwnedElements()) {

				try {
					this.drawElementInside(views_after.get(0), elem, cascade);
				}
				catch (InvalidContainerException e) {
					// ignore
				}
			}
			
		}

		return views_after.get(0);

	}


	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#drawElementInside(org.eclipse.gmf.runtime.notation.View, org.eclipse.uml2.uml.Element, boolean)
	 *
	 * @param container
	 * @param element
	 * @param cascade
	 * @return
	 * @throws InvalidContainerException
	 */
	public View drawElementInside(View container, Element element,
			boolean cascade) throws InvalidContainerException {
		// Find the list of views for the element before executing the request.
		List<View> views_before = this.getViewByElement(element);
		
		// Draw the element
		try {
			this.simpleDraw(element,DEFAULT_LOCATION,container);
		}
		catch (NonExistantViewException e1) {
			throw new InvalidContainerException();
		}
		catch (NotAValidLocationException e) {
			// Ignore : Impossible because it's the default location.
		}
		
		// Find the list of views for the element after.
		List<View> views_after = this.getViewByElement(element);
		// Remove before-elements to find the view created.
		views_after.removeAll(views_before);		
		
		
		if (cascade) {
			// Draw all relationships.
			List<Relationship> relations = element.getRelationships();
			for (Relationship relation : relations) {
				try {
					this.draw(relation,false);
				}
				catch (TargetOrSourceNotDrawnException e) {
					// Ignore, draw just possible relationships.
				}
			}
			
			
			
			for (Element elem : element.allOwnedElements()) {

				try {
					this.drawElementInside(views_after.get(0), elem, cascade);
				}
				catch (InvalidContainerException e) {
					// ignore
				}
			}
			
		}

		return views_after.get(0);
	}


	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#drawAll(java.util.List, java.util.List, boolean)
	 *
	 * @param elements
	 * @param locations
	 * @param cascade
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NotAValidLocationException
	 */
	public List<View> drawAll(List<Element> elements, List<Point> locations,
			boolean cascade) throws IllegalArgumentException, NotAValidLocationException {
		List<View> views = new ArrayList<View>();
		
		// It's not possible with differents sizes.
		if (elements.size() != locations.size())
			throw new IllegalArgumentException();
		
		// Draw elements.
		for (int i=0; i< elements.size(); i++)
			views.add(this.draw(elements.get(i), locations.get(i), cascade));
		
		return views;
	}

	
	/*
	@Override
	public View drawAtPosition(Element element, Position position, View base,
			int interval, boolean cascade) throws LocationNotFoundException {
		
		Point base_location = this.getLocation(base);
		
		switch (position) {
			case BOTTOM:
				return this.draw(element, new Point(base_location.x,base_location.y+interval), cascade);
			case TOP:
				return this.draw(element, new Point(base_location.x,base_location.y-interval), cascade);
			case LEFT:
				return this.draw(element, new Point(base_location.x-interval,base_location.y), cascade);
			default : // RIGHT
				return this.draw(element, new Point(base_location.x+interval,base_location.y), cascade);
		}
		
	}
	*/

	
	/* ------------------------------------------------------------------------ */
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#autoSize(org.eclipse.gmf.runtime.notation.View)
	 *
	 * @param view
	 */
	public void autoSize(View view) {
		List<EditPart> parts = null;
		
		try {
			// Find the editpart of the node.
			parts = this.viewToEditParts(view);
		}
		catch (NonExistantViewException e) {
			throw new IllegalArgumentException();
		}
		
		
		for (EditPart part : parts) {
			
			if (part instanceof IGraphicalEditPart) {
				List<IGraphicalEditPart> edit = new ArrayList<IGraphicalEditPart>();
				edit.add((IGraphicalEditPart)part);

				// Send the request for each instance of IGraphicalEditPart.
				SizeAction action = new SizeAction(SizeAction.PARAMETER_AUTOSIZE, edit);
				Command cmd = action.getCommand();
				
				// Execute the command.
				if (cmd.canExecute())
					cmd.execute();
			}
		}
		
	}
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#delete(org.eclipse.gmf.runtime.notation.View)
	 *
	 * @param view
	 * @throws NonExistantViewException
	 */
	public void delete(View view) throws NonExistantViewException {
		// The view is a node -> delete all the editpart.
			String DELETE = "Delete From Diagram";
			// Create the command.
			CompoundCommand command = new CompoundCommand(DELETE);
		
			List<EditPart> parts = null;
		
			parts = this.viewToEditParts(view);
		
			for (EditPart part : parts) {
				// Send the request for each instance of IGraphicalEditPart.
				if (part instanceof IGraphicalEditPart)
					command.add(DeleteActionUtil.getDeleteFromDiagramCommand((IGraphicalEditPart) part));
			}
		
			// Execute the command.
			if (command.canExecute())
				command.execute();
	}

	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getViewByElement(org.eclipse.uml2.uml.Element)
	 *
	 * @param element
	 * @return
	 */
	public List<View> getViewByElement(Element element) {
		return DiagramEditPartsUtil.getEObjectViews(element); // A changer par org.eclipse.papyrus.infra.gmfdiag.common.utils.DiagramEditPartsUtil
	}
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getElementViewByName(java.lang.String)
	 *
	 * @param name
	 * @return
	 */
	public List<View> getElementViewByName(String name) {
		try {
			return this.getElementViewByName(name, (Element)this.model.lookupRoot());
		}
		catch (NotFoundException e) {
			return null;
		}
	}
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getElementByName(java.lang.String)
	 *
	 * @param name
	 * @return
	 */
	public List<Element> getElementByName(String name) {
		try {
			return this.getElementByName(name, (Element)this.model.lookupRoot());
		}
		catch (NotFoundException e) {
			return null;
		}
		
	}
		
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getLocation(org.eclipse.gmf.runtime.notation.View)
	 *
	 * @param view
	 * @return
	 * @throws LocationNotFoundException
	 */
	public Point getLocation(View view) throws LocationNotFoundException {
		// X and Y can be return only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// return a point with x and y.
			return new Point(bounds.getX(),bounds.getY());

		}
		else
			throw new LocationNotFoundException();
	}

	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#setLocation(org.eclipse.gmf.runtime.notation.View, org.eclipse.draw2d.geometry.Point)
	 *
	 * @param view
	 * @param location
	 * @throws UnmovableViewException
	 * @throws NotAValidLocationException
	 */
	public void setLocation(View view, Point location) throws UnmovableViewException, NotAValidLocationException {
		if (!this.isValidLocation(location))
			throw new NotAValidLocationException();
		// X and Y can be set only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// Change x and y.
			bounds.setX(location.x());
			bounds.setY(location.y());

		}
		else
			throw new UnmovableViewException();
		
	}

	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getWidth(org.eclipse.gmf.runtime.notation.View)
	 *
	 * @param view
	 * @return
	 * @throws NotDimensionedViewException
	 */
	public int getWidth(View view) throws NotDimensionedViewException {
		// the view have a width only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// return the width.
			return bounds.getWidth();

		}
		else
			throw new NotDimensionedViewException();
	}

	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getHeight(org.eclipse.gmf.runtime.notation.View)
	 *
	 * @param view
	 * @return
	 * @throws NotDimensionedViewException
	 */
	public int getHeight(View view) throws NotDimensionedViewException {
		// the view have an height only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// return the height.
			return bounds.getHeight();

		}
		else
			throw new NotDimensionedViewException();
	}

	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#setHeight(org.eclipse.gmf.runtime.notation.View, int)
	 *
	 * @param view
	 * @param newheight
	 * @throws NotResizableViewException
	 * @throws NotAValidSizeException
	 */
	public void setHeight(View view, int newheight) throws NotResizableViewException, NotAValidSizeException {
		if (newheight < 0)
			throw new NotAValidSizeException();
		// the height can be resize only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// Change height.
			bounds.setHeight(newheight);

		}
		else
			throw new NotResizableViewException();
	}

	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#setWidth(org.eclipse.gmf.runtime.notation.View, int)
	 *
	 * @param view
	 * @param newwidth
	 * @throws NotResizableViewException
	 * @throws NotAValidSizeException
	 */
	public void setWidth(View view, int newwidth) throws NotResizableViewException, NotAValidSizeException {
		if (newwidth < 0)
			throw new NotAValidSizeException();
		// the width can be resize only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// Change width.
			bounds.setWidth(newwidth);

		}
		else
			throw new NotResizableViewException();
		
	}
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getModel()
	 *
	 * @return
	 */
	public UmlModel getModel() {
		return this.model;
	}
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#getDefaultLocation()
	 *
	 * @return
	 */
	public Point getDefaultLocation() {
		return DEFAULT_LOCATION;
	}
	
	
	/**
	 * 
	 * @see org.eclipse.papyrus.diagramdrawer.handlers.IDiagramHandler#setDefaultLocation(org.eclipse.draw2d.geometry.Point)
	 *
	 * @param location
	 * @throws NotAValidLocationException
	 */
	public void setDefaultLocation(Point location) throws NotAValidLocationException {
		if (this.isValidLocation(location))
			DEFAULT_LOCATION = location;
		else
			throw new NotAValidLocationException();
	}
	

	/* ------------------------------------------------------------------------ */
	
	
	/**
	 * Return all elements named by a specific name and contained in an element father.
	 * 
	 * @param name the name of elements searched
	 * @param elem the element father
	 * @return a list of element named "name"
	 */
	private List<Element> getElementByName(String name,Element elem) {
		NamedElement nelement;
		Set<Element> list = new HashSet<Element>();
		
		// Crossing the model.
		for(int i = 0; i < elem.getOwnedElements().size(); i++) {
			Element element = elem.getOwnedElements().get(i);
			// The element must be a NamedElement.
			if (element instanceof NamedElement) {
				nelement = (NamedElement) element;
				// If the element is found, it's added into the list
				if (nelement.getName() != null && nelement.getName().equals(name))
					list.add(element);
				// For each, search in sub-elements
				list.addAll(this.getElementByName(name, element));
			}
		}
			
		return new ArrayList<Element>(list);
	}
	
	
	/**
	 * Draw an element at a specific location in a view father.
	 * 
	 * @param element the element to draw
	 * @param location the location of the element
	 * @param father the view father (or null if the element hasn't father)
	 * @throws NonExistantViewException if the view doesn't exist
	 * @throws NotAValidLocationException if the location is not valid
	 */
	private void simpleDraw(Element element,Point location,View father) throws NonExistantViewException, NotAValidLocationException {
		
		if (!this.isValidLocation(location))
			throw new NotAValidLocationException();
			
		
		DropObjectsRequest drop = new DropObjectsRequest();
		// Create the list for the DropObjectsRequest.
		ArrayList<Element> list = new ArrayList<Element>();
		list.add(element);
			
		// Parameterization of the request.
		drop.setObjects(list);
		drop.setLocation(location);
		Command commandDrop = null;	
		
		if (father != null) {
			List<EditPart> edit = this.viewToEditParts(father);

			Iterator<EditPart> it = edit.iterator();
			
			// Search the good EditPart and create the command with the request.
			while (commandDrop == null && it.hasNext()) {
				commandDrop = it.next().getCommand(drop);
			}
		}
		else {
			// Create the command with the request.
			commandDrop = this.diagrameditPart.getCommand(drop);
		}
		
		// Execute the command.
		if (commandDrop != null && commandDrop.canExecute())
			this.diagrameditPart.getDiagramEditDomain().getDiagramCommandStack().execute(commandDrop);

		this.diagrameditPart.refresh();

	}
	
	
	/**
	 * Return all view which contains an element named by a specific name
	 * and contained in an element father.
	 * 
	 * @param name the name of elements searched
	 * @param elem the element father
	 * @return a list of element named "name"
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<View> getElementViewByName(String name,Element elem) {
		List<Element> list = new ArrayList<Element>();
		Set<View> views = new HashSet<View>();
		NamedElement nelement;
		
		// Crossing the model.
		for(int i = 0; i < elem.getOwnedElements().size(); i++) {
			Element element = elem.getOwnedElements().get(i);
			// The element must be a NamedElement.
			if (element instanceof NamedElement) {
				nelement = (NamedElement) element;
				// If the element is found, it's added into the list
				if (nelement.getName() != null && nelement.getName().equals(name))
					list.add(element);
	
				for (Element se : elem.getOwnedElements()) {
						views.addAll(this.getElementViewByName(name,se));
				}
			}
		}
	
		// For each element find associated views.
		for (Element e : list) {
			views.addAll(this.getViewByElement(e));
		}
			
		return new ArrayList(views);

	}
	
	
	/**
	 * Convert a view to their EditParts.
	 * 
	 * @param view the view to convert
	 * @return EditParts associated to the view
	 * @throws NonExistantViewException if the view doesn't exist
	 */
	@SuppressWarnings("unchecked")
	private List<EditPart> viewToEditParts(View view) throws NonExistantViewException {
		// Find the diagramGraphicalViewer of the diagram.
		IDiagramGraphicalViewer viewer=(IDiagramGraphicalViewer)this.diagrameditPart.getViewer();
		// Find the ID of the element.
		String elementID = EMFCoreUtil.getProxyID(view.getElement());
		// Now, we can found editparts of the element.
		List<EditPart> editparts = viewer.findEditPartsForElement(elementID, EditPart.class);

		if (editparts.isEmpty())
			throw new NonExistantViewException();
		else
			return editparts;


	}
	
	
	/**
	 * Check if an element is drawn on the diagram.
	 * @param element the element
	 * @return True or False if the element is drawn
	 */
	private boolean isDrawn(Element element) {
		List<View> views = getViewByElement(element);
		List<EditPart> editparts = new ArrayList<EditPart>();
		
		// No associated view, so not drawn !
		if (views.isEmpty())
			return false;
		else {
			for (View view : views) {
				try {
					editparts.addAll(this.viewToEditParts(view));
				}
				catch (NonExistantViewException e) {
					// Ignore 
				}
			}
		}
		// If there are editparts for the view, not drawn !
		return !editparts.isEmpty();
	}
	
	
	/**
	 * Check if the location is valid.
	 * A location is valid if x and y are positives.
	 * @param location the location to check.
	 * @return True | False
	 */
	private boolean isValidLocation(Point location) {
		if (location.x >= 0 && location.y >= 0)
			return true;
		
		return false;
	}

}