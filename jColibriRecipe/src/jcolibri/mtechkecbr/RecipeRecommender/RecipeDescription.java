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
import jcolibri.exception.OntologyAccessException;


/**
 * Bean that stores the description of the case.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RecipeDescription implements jcolibri.cbrcore.CaseComponent {

	public enum DifficultyLevels  { Easy, Medium, Hard };
	
	String caseId;
	String DifficultyLevel;
	DifficultyLevels eDifficulty;
	Integer NumberOfPersons;
	Integer PrepDuration;
	Integer CookingDuration;
	Instance  Cuisine;
	String TypeOfMeal;
	String Equipment;
	String MainIngredient;
	String CookingMethod;
	String Ingredients;
	Boolean bHealthyOption;
	Boolean bVeganOption;
	Boolean bHalalOption;
	Boolean bNutsFreeOption;
	Boolean bNonSpicyOption;
	Integer nDifficultyPriority;
	Integer nNumberOfPersonsPriority;
	Integer nCookingDurationPriority;
	Integer nTypeOfCuisinePriority;
	Integer nTypeOfMealPriority;
	Integer nMainIngredientPriority;
	Integer nCookingMethodPriority;
	Integer nHealthyOptionPriority;
	Integer nVeganOptionPriority;
	Integer nHalalOptionPriority;
	Integer nNutsFreeOptionPriority;
	Integer nNonSpicyOptionPriority;
	
	
	
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	////////////////////////// For DifficultyLevel //////////////////////////////////
	public String getDifficultyLevel() {
		return DifficultyLevel;
	}
	public void setDifficultyLevel(String Difficulty) {
		this.DifficultyLevel = Difficulty;
	}
	public DifficultyLevels getDifficulty() {
		return eDifficulty;
	}
	public void setDifficulty(DifficultyLevels Difficulty) {
		this.eDifficulty = Difficulty;
	}
	public Integer getDifficultyLevelPriority() {
		return nDifficultyPriority;
	}
	public void setDifficultyLevelPriority(Integer pDifficulty) {
		this.nDifficultyPriority = pDifficulty;
	}

	////////////////////////// For NumberOfPersons //////////////////////////////////	
	public Integer getNumberOfPersons() {
		return NumberOfPersons;
	}
	public void setNumberOfPersons(Integer NoOfPersons) {
		this.NumberOfPersons = NoOfPersons;
	}
	public Integer getNumberOfPersonsPriority() {
		return nNumberOfPersonsPriority;
	}
	public void setNumberOfPersonsPriority(Integer pNoOfPersons) {
		nNumberOfPersonsPriority = pNoOfPersons;
	}
	
	
	////////////////////////// For PrepDuration //////////////////////////////////	
	public Integer getPrepDuration() {
		return PrepDuration;
	}
	public void setPrepDuration(Integer duration) {
		this.PrepDuration = duration;
	}
	
	////////////////////////// For CookingDuration ////////////////////////////////		
	public Integer getCookingDuration() {
		return CookingDuration;
	}
	public void setCookingDuration(Integer duration) {
		this.CookingDuration = duration;
	}
	public Integer getCookingDurationPriority() {
		return nCookingDurationPriority;
	}
	public void setCookingDurationPriority(Integer pDuration) {
		nCookingDurationPriority = pDuration;
	}
	////////////////////////// For TypeOfCuisine //////////////////////////////////	
	public Instance getCuisine() {
		return Cuisine;
	}
	public void setCuisine(Instance cuisine) {
		Cuisine = cuisine;
	}
	public String getTypeOfCuisine() {
		return Cuisine.toString();
	}
	public void setCuisine(String cuisine) throws OntologyAccessException {
		if (cuisine.contentEquals("null"))
			return;
		Cuisine = new Instance(cuisine);
	}
	public Integer getTypeOfCuisinePriority() {
		return nTypeOfCuisinePriority;
	}
	public void setTypeOfCuisinePriority(Integer pCuisineType) {
		this.nTypeOfCuisinePriority = pCuisineType;
	}

	////////////////////////// For TypeOfMeal //////////////////////////////////	
	public String getTypeOfMeal() {
		return TypeOfMeal;
	}
	public void setTypeOfMeal(String MealType) {
		this.TypeOfMeal = MealType;
	}
	public Integer getTypeOfMealPriority() {
		return nTypeOfMealPriority;
	}
	public void setTypeOfMealPriority(Integer pMealType) {
		this.nTypeOfMealPriority = pMealType;
	}
	
	////////////////////////// For Equipment //////////////////////////////////		
	public String getEquipment() {
		return Equipment;
	}
	public void setEquipment(String Equipment) {
		this.Equipment = Equipment;
	}

	////////////////////////// For MainIngredient//////////////////////////////		
	public String getMainIngredient() {
		return MainIngredient;
	}
	public void setMainIngredient(String Ingredient) {
		this.MainIngredient = Ingredient;
	}
	public Integer getMainIngredientPriority() {
		return nMainIngredientPriority;
	}
	public void setMainIngredientPriority(Integer pIngredient) {
		this.nMainIngredientPriority = pIngredient;
	}

	////////////////////////// For CookingMethod //////////////////////////////////		
	public String getCookingMethod () {
		return this.CookingMethod;
	}
	public void setCookingMethod(String cookingMethod) {
		this.CookingMethod = cookingMethod;
	}
	public Integer getCookingMethodPriority() {
		return this.nCookingMethodPriority;
	}	
	public void setCookingMethodPriority(Integer pCookingMethod) {
		this.nCookingMethodPriority = pCookingMethod;
	}
	
	////////////////////////// For Ingredients //////////////////////////////////		
	public String getIngredients() {
		return Ingredients;
	}
	public void setIngredients(String Ingredients) {
		this.Ingredients = Ingredients;
	}


	// @return the HealthyOption
	public Boolean getHealthyOption () {
		return this.bHealthyOption;
	}
	public void setHealthyOption(Boolean HealthyOption) {
		this.bHealthyOption = HealthyOption;
	}
	// @return the HealthyOption
	public Integer getHealthyOptionPriority() {
		return this.nHealthyOptionPriority;
	}	
	public void setHealthyOptionPriority(Integer HealthyOption) {
		this.nHealthyOptionPriority = HealthyOption;
	}
	
	// @return the HalalOption
	public Boolean getHalalOption () {
		return this.bHalalOption;
	}
	public void setHalalOption(Boolean HalalOption) {
		this.bHalalOption = HalalOption;
	}
	// @return the HalalOption
	public Integer getHalalOptionPriority() {
		return this.nHalalOptionPriority;
	}	
	public void setHalalOptionPriority(Integer HalalOption) {
		this.nHalalOptionPriority = HalalOption;
	}
	
	// @return the VeganOption
	public Boolean getVeganOption () {
		return this.bVeganOption;
	}
	public void setVeganOption(Boolean VeganOption) {
		this.bVeganOption = VeganOption;
	}
	// @return the VeganOption
	public Integer getVeganOptionPriority() {
		return this.nVeganOptionPriority;
	}	
	public void setVeganOptionPriority(Integer VeganOption) {
		this.nVeganOptionPriority = VeganOption;
	}
	
	// @return the NutsFreeOption
	public Boolean getNutsFreeOption () {
		return this.bNutsFreeOption;
	}
	public void setNutsFreeOption(Boolean NutsFreeOption) {
		this.bNutsFreeOption = NutsFreeOption;
	}
	// @return the NutsFreeOption
	public Integer getNutsFreeOptionPriority() {
		return this.nNutsFreeOptionPriority;
	}	
	public void setNutsFreeOptionPriority(Integer NutsFreeOption) {
		this.nNutsFreeOptionPriority = NutsFreeOption;
	}

	// @return the NonSpicyOption
	public Boolean getNonSpicyOption () {
		return this.bNonSpicyOption;
	}
	public void setNonSpicyOption(Boolean NonSpicyOption) {
		this.bNonSpicyOption = NonSpicyOption;
	}
	// @return the NonSpicyOption
	public Integer getNonSpicyOptionPriority() {
		return this.nNonSpicyOptionPriority;
	}	
	public void setNonSpicyOptionPriority(Integer NonSpicyOption) {
		this.nNonSpicyOptionPriority = NonSpicyOption;
	}
	
	
	public String toString() {
		return "("+caseId+";"+DifficultyLevel+";"+NumberOfPersons+
				";"+CookingDuration+";"+Cuisine+
				";"+TypeOfMeal+";"+MainIngredient+";"+CookingMethod+")";
	}
	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return new Attribute("caseId", this.getClass());
	}
}
