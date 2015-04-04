package org.eclipse.papyrus.diagramdrawer.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.BoundsImpl;
import org.eclipse.papyrus.diagramdrawer.exceptions.LocationNotFoundException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotDimensionedViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.NotResizableViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.UnmovableViewException;
import org.eclipse.papyrus.diagramdrawer.utils.Position;
import org.eclipse.papyrus.editor.PapyrusMultiDiagramEditor;
import org.eclipse.papyrus.infra.gmfdiag.menu.utils.DeleteActionUtil;
import org.eclipse.papyrus.uml.diagram.common.util.DiagramEditPartsUtil;
import org.eclipse.papyrus.uml.diagram.menu.actions.SizeAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;


/**
 * Abstract drawer used to handle elements on a diagram.
 * 
 * @author Thibaud VERBAERE
 *
 */
public abstract class AbstractDiagramHandler implements IDiagramHandler {
	
	
	/**
	 * the Papyrus editor.
	 */
	protected PapyrusMultiDiagramEditor papyrusEditor;
	
	/**
	 * The diagram model.
	 */
	protected EObject model;
	
	/**
	 * The DropObjectsRequest.
	 */
	protected DropObjectsRequest drop;
	
	/**
	 * the Diagram Edit Part.
	 */
	protected DiagramEditPart clazzdiagrameditPart;
	
	
	/**
	 * 
	 * Constructor.
	 *
	 * @param model
	 * @param papyrusEditor
	 */
	public AbstractDiagramHandler(EObject model,PapyrusMultiDiagramEditor papyrusEditor) {
		this.model = model;
		this.papyrusEditor = papyrusEditor;
		this.drop = new DropObjectsRequest();
		DiagramEditor editor  = ((DiagramEditor)this.papyrusEditor.getActiveEditor());
		this.clazzdiagrameditPart = (DiagramEditPart) editor.getDiagramGraphicalViewer().getEditPartRegistry().get(editor.getDiagram());
	}

	
	@Override
	public View draw(Element element, boolean cascade) {
		return this.draw(element,null,cascade);
	}

	
	@Override
	public View draw(Element element, Point location, boolean cascade) {
		this.abstractDraw(element,location,cascade,this.clazzdiagrameditPart);
		//TODO : voir si elle se trouve en dernier ou premier dans la liste.
		return this.getViewByElement(element).get(0);
	}

	
	@Override
	public View drawElementInside(View container, Element element,
			boolean cascade) throws LocationNotFoundException {
		List<EditPart> editparts = this.viewToEditParts(container);
		for (EditPart editpart : editparts) {
			if (editpart instanceof GraphicalEditPart) {
				this.abstractDraw(element,null,cascade,editpart);
				//TODO : voir si elle se trouve en dernier ou premier dans la liste.
				return this.getViewByElement(element).get(0);
			}
		}
		
		throw new LocationNotFoundException();
	}

	
	private void abstractDraw(Element element,Point location, boolean cascade,
			EditPart editpart) {

		// Create the list for the DropObjectsRequest.
		ArrayList<Element> list = new ArrayList<Element>();
		list.add(element);
			
		// Parameterization of the request.
		this.drop.setObjects(list);
		if (location != null)
			this.drop.setLocation(location);
					
		// Execution.
		this.executeDropOn(editpart);
		
		if (cascade) {
			List<Relationship> relations = element.getRelationships();
			for (Relationship relation : relations) {
				this.draw(relation.getOwner(),cascade);
			}
			
			//TODO : voir si elle se trouve en dernier ou premier dans la liste.
			View view = this.getViewByElement(element).get(0);
			
			for (Element elem : element.allOwnedElements()) {
				try {
					this.drawElementInside(view, elem, cascade);
				} catch (LocationNotFoundException e) {
					// Ignore
				}
			}
			
		}
		
	}


	@Override
	public List<View> drawAll(List<Element> elements, List<Point> locations,
			boolean cascade) throws IllegalArgumentException {
		List<View> views = new ArrayList<View>();
		
		// It's not possible with differents sizes.
		if (elements.size() != locations.size())
			throw new IllegalArgumentException();
		
		// Draw elements.
		for (int i=0; i< elements.size(); i++)
			views.add(this.draw(elements.get(i), locations.get(i), cascade));
		
		return views;
	}

	
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

	
	/* ------------------------------------------------------------------------ */
	
	
	@Override
	public void AutoSize(View view) {
			
		List<EditPart> parts = this.viewToEditParts(view);
			
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
	
	@Override
	public void delete(View view) {
		
		String DELETE = "Delete From Diagram";
		// Create the command.
		CompoundCommand command = new CompoundCommand(DELETE);
		
		List<EditPart> parts = this.viewToEditParts(view);
		
		for (EditPart part : parts) {
			// Send the request for each instance of IGraphicalEditPart.
			if (part instanceof IGraphicalEditPart)
				command.add(DeleteActionUtil.getDeleteFromDiagramCommand((IGraphicalEditPart) part));
		}
		
		// Execute the command.
		if (command.canExecute())
			command.execute();

	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<View> getViewByElement(Element element) {
		return DiagramEditPartsUtil.getEObjectViews(element);
	}
	
	
	@Override
	public List<View> getElementViewByName(String name) {
		List<Element> list = new ArrayList<Element>();
		NamedElement element;
		
		// Crossing the model.
		for(int i = 0; i < ((Element)this.model).getOwnedElements().size(); ++i) {
			// The element must be a NamedElement.
			element = (NamedElement) ((Element)this.model).getOwnedElements().get(i);

			// If the element is found, it's added into the list
			if (element.getName() != null && element.getName().equals(name))
				list.add(element);

		}
	
		List<View> views = new ArrayList<View>();
		// For each element find associted views.
		for (Element elem : list)
			views.addAll(this.getViewByElement(elem));
			
		return views;

	}

	
	@Override
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

	
	@Override
	public void setLocation(View view, Point location) throws UnmovableViewException {
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

	
	@Override
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

	
	@Override
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

	
	@Override
	public void setHeight(View view, int newheight) throws NotResizableViewException {
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

	
	@Override
	public void setWidth(View view, int newwidth) throws NotResizableViewException {
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
	 * 
	 * @param view
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<EditPart> viewToEditParts(View view) {

		IEditorPart activeEditor = this.papyrusEditor.getActiveEditor();
		
		if (activeEditor instanceof DiagramEditor) {
			// If the EditorPart is an instance of DiagramEditor we can found editparts of the view.
			IDiagramGraphicalViewer viewer = ((DiagramEditor)activeEditor).getDiagramGraphicalViewer();
			String elementID = EMFCoreUtil.getProxyID(view.getElement());
			
			return viewer.findEditPartsForElement(elementID, EditPart.class);
		}
		return null;

	}	
	
	/**
	 * Execute the DropObjectsRequest on a specific edit part.
	 */
	public abstract void executeDropOn(EditPart editpart);

}
