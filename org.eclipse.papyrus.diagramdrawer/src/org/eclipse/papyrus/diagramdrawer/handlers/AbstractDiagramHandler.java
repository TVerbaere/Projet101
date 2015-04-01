package org.eclipse.papyrus.diagramdrawer.handlers;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.BoundsImpl;
import org.eclipse.papyrus.diagram.programmaticcreation.exception.NotResizableViewException;
import org.eclipse.papyrus.diagramdrawer.exceptions.ViewNotDrawnException;
import org.eclipse.papyrus.diagramdrawer.utils.Position;
import org.eclipse.uml2.uml.Element;


/**
 * 
 * @author Thibaud VERBAERE
 *
 */
public abstract class AbstractDiagramHandler implements IDiagramHandler {

	@Override
	public void AutoSize(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View draw(Element element, boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View draw(Element element, Point location, boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View drawElementInside(View container, Element element,
			boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<View> drawAll(List<Element> elements, List<Point> locations,
			boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View drawAtPosition(Element element, Position position, View base,
			int interval, boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<View> getViewByElement(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getLocation(View view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(View view, Point location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<View> getElementViewByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidth(View view) throws ViewNotDrawnException {
		// the view have a width only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// return the width.
			return bounds.getWidth();

		}
		else
			throw new ViewNotDrawnException();
	}

	@Override
	public int getHeight(View view) throws ViewNotDrawnException {
		// the view have an height only if the view is an instance of Node. 
		if (view instanceof Node) {
			
			LayoutConstraint sh = ((Node) view).getLayoutConstraint();
			BoundsImpl bounds = ((BoundsImpl)sh);
			// return the height.
			return bounds.getHeight();

		}
		else
			throw new ViewNotDrawnException();
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
	
	
	public abstract void executeDrop();

}
