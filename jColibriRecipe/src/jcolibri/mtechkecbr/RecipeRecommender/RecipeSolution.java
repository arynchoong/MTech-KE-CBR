/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.mtechkecbr.RecipeRecommender;

import jcolibri.cbrcore.Attribute;

/**
 * Bean that stores the solution of the case (trip)
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RecipeSolution  implements jcolibri.cbrcore.CaseComponent {

	String id;
	String szCookingMethod;
	int nCookingDuration;
	int nPrepDuration;
	String szTypeOfCuisine;
	String szDetailedIngredients;
	String szMethod;

	public String toString()
	{
		return "("+id+";"+")";
	}
	
	public Attribute getIdAttribute() {
		
		return new Attribute("id", this.getClass());
	}

	// @return the TypeOfCuisine
	public String getTypeOfCuisine() {
		return szTypeOfCuisine;
	}
	// @set the TypeOfCuisine
	public void setTypeOfCuisine(String CuisineType) {
	//	TypeOfCuisine = CuisineType;
		this.szTypeOfCuisine = CuisineType;
	}
	
	// @return the CookingDuration
	public Integer getCookingDuration() {
		return nCookingDuration;
	}
	// @set the CookingDuration
	public void setCookingDuration(Integer duration) {
		nCookingDuration = duration;
	}
	// @return the PrepDuration
	public Integer getPrepDuration() {
		return nPrepDuration;
	}
	// @set the PrepDuration
	public void setPrepDuration(Integer duration) {
		nPrepDuration = duration;
	}
	
	// @return the CookingMethod
	public String getCookingMethod () {
		return this.szCookingMethod;
	}
	
	// @sets the CookingMethod
	public void setCookingMethod(String cookingMethod) {
		this.szCookingMethod = cookingMethod;
	}
	
	public String getDetailedIngredients() {
		return szDetailedIngredients;
	}
	public void setDetailedIngredients(String DetailedIngredients) {
		this.szDetailedIngredients = DetailedIngredients;
	}
	
	public String getMethod() {
		return szMethod;
	}
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
