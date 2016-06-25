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
 * Bean that stores the description of the case.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RecipeDescription implements jcolibri.cbrcore.CaseComponent {
	
	public enum TypeOfCuisines { AnyCuisine, Chinese, Indian, Malay_Indonesian, Nyonya, Western}
	public enum MainIngredients { AnyIngredient, Chicken, Duck, Port, Dough, Vegetables, Fruits}
	public enum TypeOfMeals {AnyMeal, Appetizer, MainCourse, Dessert, Drinks}
	public enum DifficultyLevels { AnyLevel, Easy, Medium, Hard}
	public enum DietaryRequirements { None, Vegan, Halal, NutsFree, NonSpicy }

	
	String  szCaseId;
	MainIngredients enumMainIngredient;
	TypeOfMeals enumTypeOfMeal;
	TypeOfCuisines TypeOfCuisine;
	Integer nCookingDuration;
	DifficultyLevels enumDifficultyLevel;
	Integer nNumberOfPersons;
	DietaryRequirements enumDietaryRequirement;
	Boolean bHealthyOption; //Healthy Options, Less Oil
	
	
	
	public String toString()
	{
		return "("+szCaseId+";"+enumMainIngredient+";"+enumTypeOfMeal+";"+TypeOfCuisine+";"+nCookingDuration+";"+enumDifficultyLevel+";"+nNumberOfPersons+";"+enumDietaryRequirement+";"+bHealthyOption+")";
	}
	
	// @return the TypeOfCuisine
	public TypeOfCuisines getTypeOfCuisine() {
		return TypeOfCuisine;
	}
	// @set the TypeOfCuisine
	public void setTypeOfCuisine(TypeOfCuisines CuisineType) {
		TypeOfCuisine = CuisineType;
	}

	// @return the caseId
	public String getCaseId() {
		return szCaseId;
	}
	// @set the caseId
	public void setCaseId(String caseid) {
		this.szCaseId = caseid;
	}
	// @return the duration
	public Integer getCookingDuration() {
		return nCookingDuration;
	}
	// @set the Cooking Duration
	public void setCookingDuration(Integer duration) {
		nCookingDuration = duration;
	}
	// @set the Cooking Duration
	public Integer getNumberOfPersons() {
		return nNumberOfPersons;
	}
	// @set the Cooking Duration
	public void setNumberOfPersons(Integer NoOfPersons) {
		nNumberOfPersons = NoOfPersons;
	}


	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}	
}
