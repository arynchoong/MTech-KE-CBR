/**
 * Recipe Recommender for MTech KE CBR project PT-02
 * adapted from:
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

	
	String  caseId;
	String  szDifficult;
	String  szTypeOfCuisine;
	String  szTypeOfMeal;
	String  szMainIngredient;
	String  szDietaryRequirement;
	Integer nCookingDuration;
	Integer nNumberOfPersons;
	Boolean bHealthyOption; //Healthy Options, Less Oil
	String szCookingMethod;
			
	public String toString()
	{
		return "("+caseId+";"+szMainIngredient+";"+szTypeOfMeal+";"+szTypeOfCuisine+";"+nCookingDuration+";"+szDifficult+";"+nNumberOfPersons+";"+szDietaryRequirement+";"+bHealthyOption+")";
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
	// @return the TypeOfMeal
	public String getTypeOfMeal() {
		return szTypeOfMeal;
	}
	// @set the TypeOfMeal
	public void setTypeOfMeal(String MealType) {
		this.szTypeOfMeal = MealType;
	}
	
	// @return the DifficultyLevel
	public String getDifficultyLevel() {
		return szDifficult;
	}
	// @set the DifficultyLevel
	public void setDifficultyLevel(String Difficulty) {
	//	DifficultyLevel = Difficulty;
		this.szDifficult = Difficulty;
	}
	// @return the caseId
	public String getCaseId() {
		return caseId;
	}
	// @set the caseId
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	// @return the CookingDuration
	public Integer getCookingDuration() {
		return nCookingDuration;
	}
	// @set the CookingDuration
	public void setCookingDuration(Integer duration) {
		nCookingDuration = duration;
	}
	// @set the NumberOfPersons
	public Integer getNumberOfPersons() {
		return nNumberOfPersons;
	}
	// @set the NumberOfPersons
	public void setNumberOfPersons(Integer NoOfPersons) {
		nNumberOfPersons = NoOfPersons;
	}
	// @return the MainIngredient
	public String getMainIngredient() {
		return szMainIngredient;
	}
	// @set the MainIngredient
	public void setMainIngredient(String Ingredient) {
		this.szMainIngredient = Ingredient;
	}
	// @return the CookingMethod
	public String getCookingMethod () {
		return this.szCookingMethod;
	}
	
	public void setCookingMethod(String cookingMethod) {
		this.szCookingMethod = cookingMethod;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}	
}
