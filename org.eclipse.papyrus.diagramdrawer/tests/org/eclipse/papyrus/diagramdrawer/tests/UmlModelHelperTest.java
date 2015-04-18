package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.papyrus.diagramdrawer.helpers.UmlModelHelper;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the class UmlModelHelper.
 * Only one method : delete.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class UmlModelHelperTest {
	
	private UmlModel umlModel;
	
	private UmlModelHelper helper;
	
	private Element added_element;
	
	@BeforeClass
	public void setUp() throws ExecutionException {
	
		//TODO : Creer un model avec les factories et ajouter une classe dedans : ClassTest.
		// -> Quand les factories seront créées !
		umlModel = null;
		Package model = null;
		// Dans la transaction :
		added_element = model.createOwnedClass("ClassTest", false);
		
		helper = new UmlModelHelper(umlModel);
	}
	
	
	@Test
	public void elementInModel() {
		List<Element> elements = getElementByName("ClassTest");
		
		// Normally, one element in the model : ClassTest.
		assertTrue(elements.size() == 1 && elements.get(0) == added_element);
	}
	
	
	@Test
	public void deleteTest() throws NotFoundException {
		// Delete element.
		helper.delete(added_element);
		
		List<Element> elements = getElementByName("ClassTest");
		
		// Normally, no elements in the model.
		assertTrue(elements.size() == 0);
	}
	
	/* ----------------------------Useful methods----------------------------------------------- */
	
	
	private List<Element> getElementByName(String name) {
		try {
			return getElementByName(name, (Element)umlModel.lookupRoot());
		}
		catch (NotFoundException e) {
			return null;
		}
		
	}
	
	
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

}
