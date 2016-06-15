/**
 * IrisDescription.java
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
 * Bean storing the description for the Iris data base
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class IrisDescription implements CaseComponent {

	Double sepalLength;
	Double sepalWidth;
	Double petalLength;
	Double petalWidth;
	String id;
	
	public String toString()
	{
		return id+", "+sepalLength+", "+sepalWidth+", "+petalLength+", "+petalWidth;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the petalLength.
	 */
	public Double getPetalLength() {
		return petalLength;
	}

	/**
	 * @param petalLength The petalLength to set.
	 */
	public void setPetalLength(Double petalLength) {
		this.petalLength = petalLength;
	}

	/**
	 * @return Returns the petalWidth.
	 */
	public Double getPetalWidth() {
		return petalWidth;
	}

	/**
	 * @param petalWidth The petalWidth to set.
	 */
	public void setPetalWidth(Double petalWidth) {
		this.petalWidth = petalWidth;
	}

	/**
	 * @return Returns the sepalLength.
	 */
	public Double getSepalLength() {
		return sepalLength;
	}

	/**
	 * @param sepalLength The sepalLength to set.
	 */
	public void setSepalLength(Double sepalLength) {
		this.sepalLength = sepalLength;
	}

	/**
	 * @return Returns the sepalWidth.
	 */
	public Double getSepalWidth() {
		return sepalWidth;
	}

	/**
	 * @param sepalWidth The sepalWidth to set.
	 */
	public void setSepalWidth(Double sepalWidth) {
		this.sepalWidth = sepalWidth;
	}

	
	
}
