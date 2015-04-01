package org.eclipse.papyrus.diagramdrawer.helpers;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.papyrus.diagramdrawer.exceptions.ElementDeleteException;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

/**
 * Provides methods useful to modify a model.
 * 
 * @author Thibaud VERBAERE
 *
 */
public class UMLModelHelper {
	
	/**
	 * The UML model.
	 */
	private UmlModel model;

	
	/**
	 * 
	 * Constructor: Create a new UMLModelHelper.
	 *
	 * @param model the model where modifications take place.
	 */
	public UMLModelHelper(UmlModel model){
		this.model = model;
	}
	
	
	/**
	 * Deletes an element on the model referenced of this helper.
	 * @param element the element to delete
	 * 
	 * @throws ElementDeleteException if the element cannot be deleted
	 */
	public void delete(Element element) throws ElementDeleteException {
		
		try {
			
			EObject model = this.model.lookupRoot();
			
			// Find all relationships associated from the element.
			EList<Relationship> relationships = element.getRelationships();

			// Create the command.
			org.eclipse.emf.common.command.Command command = DeleteCommand.create(TransactionUtil.getEditingDomain(model),element);
			
			if (command.canExecute()) {
				// Execute the command.
				command.execute();
				
				// Delete all relationships.
				for (Relationship relation : relationships)
					relation.destroy();
			}
			
		}
		catch (NotFoundException e) {
			throw new ElementDeleteException();
		}

	}
	
}
