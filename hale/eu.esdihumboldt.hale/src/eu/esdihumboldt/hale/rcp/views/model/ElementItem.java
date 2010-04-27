/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.hale.rcp.views.model;

import eu.esdihumboldt.hale.schemaprovider.model.Definition;
import eu.esdihumboldt.hale.schemaprovider.model.SchemaElement;
import eu.esdihumboldt.hale.schemaprovider.model.TypeDefinition;

/**
 * Schema item representing an element
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class ElementItem extends TreeParent {
	
	private final SchemaElement element;

	/**
	 * Creates a element item
	 * 
	 * @param element the type definition
	 */
	public ElementItem(SchemaElement element) {
		super(
				element.getElementName().getLocalPart(), 
				element.getElementName(), 
				determineType(element.getType()), 
				element.getAttributeType(null));
		
		this.element = element;
	}

	/**
	 * Determine the {@link TreeObject.TreeObjectType} for a feature type
	 * 
	 * @param type the type definition
	 * 
	 * @return the tree object type
	 */
	private static TreeObjectType determineType(TypeDefinition type) {
		if (type.isFeatureType()) {
			if (type.isAbstract()) {
				return TreeObjectType.ABSTRACT_FT;
			}
			else {
				return TreeObjectType.CONCRETE_FT;
			}
		}
		else {
			return TreeObjectType.PROPERTY_TYPE;
		}
	}

	/**
	 * @return the typeDefinition
	 */
	public SchemaElement getElement() {
		return element;
	}

	/**
	 * @see SchemaItem#getDefinition()
	 */
	@Override
	public Definition getDefinition() {
		return element;
	}

}
