/**
 * IrisSolution.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.test.test6;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Bean storing the solution for the Iris data base
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class IrisSolution implements CaseComponent {

	String type;
	
	public String toString()
	{
		return type;
	}
	
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
	
}
