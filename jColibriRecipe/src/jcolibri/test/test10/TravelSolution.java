/**
 * TravelSolution.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/06/2007
 */
package jcolibri.test.test10;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;

/**
 * Bean that stores the solution of the case.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */ 
public class TravelSolution implements jcolibri.cbrcore.CaseComponent{

	Instance mainConcept;
	Instance price;
	
	public String toString()
	{
		return "("+mainConcept+";"+price+")";
	}
	
	public Attribute getIdAttribute() {
		
		return new Attribute("mainConcept", this.getClass());
	}
	/**
	 * @return Returns the id.
	 */
	public Instance getMainConcept() {
		return mainConcept;
	}

	/**
	 * @param mc The main concept to set.
	 */
	public void setMainConcept(Instance mc) {
		this.mainConcept = mc;
	}

	/**
	 * @return Returns the price.
	 */
	public Instance getPrice() {
		return price;
	}

	/**
	 * @param price The price to set.
	 */
	public void setPrice(Instance price) {
		this.price = price;
	}
	
	

}
