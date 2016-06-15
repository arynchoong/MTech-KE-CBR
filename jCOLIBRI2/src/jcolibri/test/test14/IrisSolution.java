/**
 * IrisSolution.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.test.test14;

import jcolibri.cbrcore.Attribute;
import jcolibri.extensions.classification.ClassificationSolution;

/**
 * Bean storing the solution for the Iris data base.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class IrisSolution implements ClassificationSolution {

	String type;
	
	/**
	 * Returns a String representation of this object.
	 * @return a String representation of this object.
	 */
	public String toString()
	{
		return type;
	}
	
	/**
	 * Returns the id attribute of this object.
	 * @return the id attribute of this object.
	 */
	public Attribute getIdAttribute() {
		return new Attribute("type", this.getClass());
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	public Object getClassification()
	{
	    return type;
	}
	
}
