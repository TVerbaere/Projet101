package org.eclipse.papyrus.diagramdrawer.handlers;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.Element;

/**
 * 
 * @author Thibaud VERBAERE
 *
 */
public abstract class AbstractDiagramHandler implements IDiagramHandler {

	@Override
	public void setDefaultSize(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public View draw(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View draw(Element element, Point location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View drawElementInside(View container, Element element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<View> drawAll(List<Element> elements, List<Point> locations) {
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

}
