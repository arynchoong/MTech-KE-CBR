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

	
	String  szCaseId;
	MainIngredients MainIngredient;
	TypeOfMeals TypeOfMeal;
	TypeOfCuisines TypeOfCuisine;
	Integer nCookingDuration;
	DifficultyLevels DifficultyLevel;
	Integer nNumberOfPersons;
	DietaryRequirements enumDietaryRequirement;
	Boolean bHealthyOption; //Healthy Options, Less Oil
	
	
	
	public String toString()
	{
		return "("+szCaseId+";"+MainIngredient+";"+TypeOfMeal+";"+TypeOfCuisine+";"+nCookingDuration+";"+DifficultyLevel+";"+nNumberOfPersons+";"+enumDietaryRequirement+";"+bHealthyOption+")";
	}
	
	// @return the TypeOfCuisine
	public TypeOfCuisines getTypeOfCuisine() {
		return TypeOfCuisine;
	}
	// @set the TypeOfCuisine
	public void setTypeOfCuisine(TypeOfCuisines CuisineType) {
		TypeOfCuisine = CuisineType;
	}
	// @return the TypeOfMeal
	public TypeOfMeals getTypeOfMeal() {
		return TypeOfMeal;
	}
	// @set the TypeOfMeal
	public void setTypeOfMeal(TypeOfMeals MealType) {
		TypeOfMeal = MealType;
	}
	
	// @return the DifficultyLevel
	public DifficultyLevels getDifficultyLevel() {
		return DifficultyLevel;
	}
	// @set the DifficultyLevel
	public void setDifficultyLevel(DifficultyLevels Difficulty) {
		DifficultyLevel = Difficulty;
	}
	// @return the caseId
	public String getCaseId() {
		return szCaseId;
	}
	// @set the caseId
	public void setCaseId(String caseid) {
		this.szCaseId = caseid;
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
	public MainIngredients getMainIngredient() {
		return MainIngredient;
	}
	// @set the MainIngredient
	public void setMainIngredient(MainIngredients Ingredient) {
		MainIngredient = Ingredient;
	}

	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}	
}
