/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                  01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.goml.align;

import eu.esdihumboldt.cst.align.ISchema;
import eu.esdihumboldt.cst.rdf.IAbout;
import eu.esdihumboldt.goml.rdf.About;

/**
 * This {@link Schema} type contains some metadata on a schema mapped in an 
 * {@link Alignment}.
 * 
 * @author Thorsten Reitz 
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class Schema 
	implements ISchema {
	
	/**
	 * TODO add comment
	 */
	private String location;
	
	/**
	 * The {@link Formalism} used to express this schema. 
	 */
	private Formalism formalism;
	
	/**
	 * Metadata on this {@link Schema} object
	 */
	private IAbout about;
	
	// constructors ............................................................
	
	/**
	 * @param location
	 * @param formalism
	 */
	public Schema(String location, Formalism formalism) {
		super();
		this.location = location;
		this.formalism = formalism;
	}

	// getters / setters .......................................................

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the formalism
	 */
	public Formalism getFormalism() {
		return formalism;
	}

	/**
	 * @param formalism the formalism to set
	 */
	public void setFormalism(Formalism formalism) {
		this.formalism = formalism;
	}

	/**
	 * @return the about
	 */
	public IAbout getAbout() {
		return about;
	}

	/**
	 * @param about the about to set
	 */
	public void setAbout(IAbout about) {
		this.about = about;
	}
	
}
