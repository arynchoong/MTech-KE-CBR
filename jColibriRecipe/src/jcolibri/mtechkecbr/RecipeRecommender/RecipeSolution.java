/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.mtechkecbr.RecipeRecommender;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;

/**
 * Bean that stores the solution of the case (trip)
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RecipeSolution  implements jcolibri.cbrcore.CaseComponent {

	String id;
	String szDetailedIngredients;
	String szMethod;
	Instance  Cuisine;

	public String toString()
	{
		return "("+id+";"+")";
	}
	
	public Attribute getIdAttribute() {
		
		return new Attribute("id", this.getClass());
	}

	/**
	 * @return the cuisine
	 */
	public Instance getCuisine() {
		return Cuisine;
	}
	/**
	 * @param cuisine the cuisine to set
	 */
	public void setCuisine(Instance cuisine) {
		Cuisine = cuisine;
	}

	// @return the DetailedIngredients
	public String getDetailedIngredients() {
		return szDetailedIngredients;
	}
	// @sets the DetailedIngredients
	public void setDetailedIngredients(String DetailedIngredients) {
		this.szDetailedIngredients = DetailedIngredients;
	}
	
	// @return the Method
	public String getMethod() {
		return szMethod;
	}
	// @sets the Method
	public void setMethod(String Method) {
		this.szMethod = Method;
	}

	// returns the Id
	public String getId() {
		return id;
	}

	// sets the Id
	public void setId(String id) {
		this.id = id;
	}
}
