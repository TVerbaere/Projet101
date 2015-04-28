package org.eclipse.papyrus.diagramdrawer.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.papyrus.diagramdrawer.factories.ModelSetFactory;
import org.eclipse.papyrus.diagramdrawer.factories.ProjectFactory;
import org.eclipse.papyrus.diagramdrawer.helpers.UmlModelHelper;
import org.eclipse.papyrus.diagramdrawer.othersources.EclipseProject;
import org.eclipse.papyrus.diagramdrawer.othersources.ExecutionException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Class;
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
	
	private Class tested_element;
	
	private Association tested_association;
	
	private Class tested_element2;
	
	@BeforeClass
	public void setUp() throws ExecutionException, NotFoundException {
		// -> Impossible d'executer les tests avec les versions actuelles des factories.
		EclipseProject eclipseProject = ProjectFactory.instance.build("test");
		ModelSet modelSet = ModelSetFactory.instance.createIn(eclipseProject, "test", true);
		UmlModel umlModel = UmlUtils.getUmlModel(modelSet);
		
		TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(umlModel.lookupRoot());
		final Package model = (Package)umlModel.lookupRoot();
		helper = new UmlModelHelper(umlModel);

		ted.getCommandStack().execute(new RecordingCommand(ted) {
			protected void doExecute() {	
				tested_element = model.createOwnedClass("ClassTest", false);
				tested_element2 = model.createOwnedClass("ClassTest2", false);
				tested_association = tested_element.createAssociation(true, AggregationKind.COMPOSITE_LITERAL, "name1", 1, 1, tested_element2, true, AggregationKind.SHARED_LITERAL, "name2", 0, 1);
				tested_association.setName("asso1");
			}
		});
	}
	
	
	@Test
	public void elementsInModel() {		
		// Normally, ClassTest, ClassTest2 and the association are in the model.
		List<Element> elements = getElementByName("ClassTest");
		assertTrue(elements.size() == 1 && elements.get(0) == tested_element);
		elements = getElementByName("ClassTest2");
		assertTrue(elements.size() == 1 && elements.get(0) == tested_element2);
		elements = getElementByName("asso1");
		assertTrue(elements.size() == 1 && elements.get(0) == tested_association);
	}
	
	
	@Test
	public void deleteTest() throws NotFoundException {
		// Delete element ClassTest1 (-> and the association in cascade).
		helper.delete(tested_element);
				
		// Normally, just ClassTes2 in the model.
		List<Element> elements = getElementByName("ClassTest");
		assertTrue(elements.size() == 0);
		elements = getElementByName("ClassTest2");
		assertTrue(elements.size() == 1);
		elements = getElementByName("asso1");
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
