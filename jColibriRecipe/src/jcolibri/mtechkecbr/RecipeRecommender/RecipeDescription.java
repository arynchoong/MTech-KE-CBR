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

	public enum DifficultyLevels  { Easy, Medium, Hard };
	
	String caseId;
	String DifficultyLevel;
	DifficultyLevels eDifficulty;
	Integer NumberOfPersons;
	Integer PrepDuration;
	Integer CookingDuration;
	String TypeOfCuisine;
	String TypeOfMeal;
	String Equipment;
	String MainIngredient;
	String CookingMethod;
	String Ingredients;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

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
	
	public Integer getNumberOfPersons() {
		return NumberOfPersons;
	}
	public void setNumberOfPersons(Integer NoOfPersons) {
		this.NumberOfPersons = NoOfPersons;
	}

	public Integer getPrepDuration() {
		return PrepDuration;
	}
	public void setPrepDuration(Integer duration) {
		this.PrepDuration = duration;
	}
	public Integer getCookingDuration() {
		return CookingDuration;
	}
	public void setCookingDuration(Integer duration) {
		this.CookingDuration = duration;
	}

	public String getTypeOfCuisine() {
		return TypeOfCuisine;
	}
	public void setTypeOfCuisine(String CuisineType) {
		this.TypeOfCuisine = CuisineType;
	}

	public String getTypeOfMeal() {
		return TypeOfMeal;
	}
	public void setTypeOfMeal(String MealType) {
		this.TypeOfMeal = MealType;
	}

	public String getEquipment() {
		return Equipment;
	}
	public void setEquipment(String Equipment) {
		this.Equipment = Equipment;
	}

	public String getMainIngredient() {
		return MainIngredient;
	}
	public void setMainIngredient(String Ingredient) {
		this.MainIngredient = Ingredient;
	}

	public String getCookingMethod () {
		return this.CookingMethod;
	}
	public void setCookingMethod(String cookingMethod) {
		this.CookingMethod = cookingMethod;
	}
	public String getIngredients() {
		return Ingredients;
	}
	public void setIngredients(String Ingredients) {
		this.Ingredients = Ingredients;
	}

	public String toString() {
		return "("+caseId+";"+DifficultyLevel+";"+NumberOfPersons+
				";"+CookingDuration+";"+TypeOfCuisine+
				";"+TypeOfMeal+";"+MainIngredient+";"+CookingMethod+")";
	}
	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return new Attribute("caseId", this.getClass());
	}


	
	private Integer nDifficultyPriority;
	private Integer nNumberOfPersonsPriority;
	private Integer nCookingDurationPriority;
	private Integer nTypeOfCuisinePriority;
	private Integer nTypeOfMealPriority;
	private Integer nMainIngredientPriority;
	private Integer nCookingMethodPriority;
	
	
	public Integer getDifficultyLevelPriority() {
		return nDifficultyPriority;
	}
	public void setDifficultyLevelPriority(Integer pDifficulty) {
		this.nDifficultyPriority = pDifficulty;
	}
	
	public Integer getNumberOfPersonsPriority() {
		return nNumberOfPersonsPriority;
	}
	public void setNumberOfPersonsPriority(Integer pNoOfPersons) {
		nNumberOfPersonsPriority = pNoOfPersons;
	}
	
	public Integer getCookingDurationPriority() {
		return nCookingDurationPriority;
	}
	public void setCookingDurationPriority(Integer pDuration) {
		nCookingDurationPriority = pDuration;
	}
	
	public Integer getTypeOfCuisinePriority() {
		return nTypeOfCuisinePriority;
	}
	public void setTypeOfCuisinePriority(Integer pCuisineType) {
		this.nTypeOfCuisinePriority = pCuisineType;
	}
	
	public Integer getTypeOfMealPriority() {
		return nTypeOfMealPriority;
	}
	public void setTypeOfMealPriority(Integer pMealType) {
		this.nTypeOfMealPriority = pMealType;
	}
	
	public Integer getMainIngredientPriority() {
		return nMainIngredientPriority;
	}
	public void setMainIngredientPriority(Integer pIngredient) {
		this.nMainIngredientPriority = pIngredient;
	}
	
	public Integer getCookingMethodPriority() {
		return this.nCookingMethodPriority;
	}	
	public void setCookingMethodPriority(Integer pCookingMethod) {
		this.nCookingMethodPriority = pCookingMethod;
	}
/*	
	// Stick to standard calls for the time being
	public String  caseId;	

	private String szTypeOfCuisine;
	private String szTypeOfMeal;
	private String szDifficulty;
	private Integer nCookingDuration;
	private Integer nNumberOfPersons;
	private String szMainIngredient;
	private String szCookingMethod;
	private Boolean bHealthyOption;
	private Boolean bVeganOption;
	private Boolean bHalalOption;
	private Boolean bNutsFreeOption;
	private Boolean bNonSpicyOption;

	private Integer nTypeOfCuisinePriority;
	private Integer nTypeOfMealPriority;
	private Integer nDifficultyPriority;
	private Integer nCookingDurationPriority;
	private Integer nNumberOfPersonsPriority;
	private Integer nMainIngredientPriority;
	private Integer nCookingMethodPriority;
	private Integer nHealthyOptionPriority;
	private Integer nVeganOptionPriority;
	private Integer nHalalOptionPriority;
	private Integer nNutsFreeOptionPriority;
	private Integer nNonSpicyOptionPriority;

	public RecipeDescription()
	{
	}
	
			
	public String toString()
	{
		return "("+caseId+";"+szMainIngredient+";"+szTypeOfMeal+";"+szTypeOfCuisine+";"+nCookingDuration+";"+szDifficulty+";"+nNumberOfPersons+";"+bHealthyOption+")";
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
	// @return the TypeOfCuisine
	public Integer getTypeOfCuisinePriority() {
		return nTypeOfCuisinePriority;
	}
	// @set the TypeOfCuisine
	public void setTypeOfCuisinePriority(Integer CuisineType) {
		this.nTypeOfCuisinePriority = CuisineType;
	}
	
	// @return the TypeOfMeal
	public String getTypeOfMeal() {
		return szTypeOfMeal;
	}
	// @set the TypeOfMeal
	public void setTypeOfMeal(String MealType) {
		this.szTypeOfMeal = MealType;
	}
	// @return the TypeOfMeal
	public Integer getTypeOfMealPriority() {
		return nTypeOfMealPriority;
	}
	// @set the TypeOfMeal
	public void setTypeOfMealPriority(Integer MealType) {
		this.nTypeOfMealPriority = MealType;
	}
	
	// @return the DifficultyLevel
	public String getDifficultyLevel() {
		return szDifficulty;
	}
	// @set the DifficultyLevel
	public void setDifficultyLevel(String Difficulty) {
	//	DifficultyLevel = Difficulty;
		this.szDifficulty = Difficulty;
	}
	// @return the DifficultyLevel
	public Integer getDifficultyLevelPriority() {
		return nDifficultyPriority;
	}
	// @set the DifficultyLevel
	public void setDifficultyLevelPriority(Integer Difficulty) {
		this.nDifficultyPriority = Difficulty;
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
	// @return the CookingDuration
	public Integer getCookingDurationPriority() {
		return nCookingDurationPriority;
	}
	// @set the CookingDuration
	public void setCookingDurationPriority(Integer duration) {
		nCookingDurationPriority = duration;
	}

	// @set the NumberOfPersons
	public Integer getNumberOfPersons() {
		return nNumberOfPersons;
	}
	// @set the NumberOfPersons
	public void setNumberOfPersons(Integer NoOfPersons) {
		nNumberOfPersons = NoOfPersons;
	}
	public Integer getNumberOfPersonsPriority() {
		return nNumberOfPersonsPriority;
	}
	// @set the NumberOfPersons
	public void setNumberOfPersonsPriority(Integer NoOfPersons) {
		nNumberOfPersonsPriority = NoOfPersons;
	}
	
	// @return the MainIngredient
	public String getMainIngredient() {
		return szMainIngredient;
	}
	// @set the MainIngredient
	public void setMainIngredient(String Ingredient) {
		this.szMainIngredient = Ingredient;
	}
	// @return the MainIngredient
	public Integer getMainIngredientPriority() {
		return nMainIngredientPriority;
	}
	// @set the MainIngredient
	public void setMainIngredientPriority(Integer Ingredient) {
		this.nMainIngredientPriority = Ingredient;
	}

	// @return the CookingMethod
	public String getCookingMethod () {
		return this.szCookingMethod;
	}
	public void setCookingMethod(String cookingMethod) {
		this.szCookingMethod = cookingMethod;
	}
	// @return the CookingMethod
	public Integer getCookingMethodPriority() {
		return this.nCookingMethodPriority;
	}	
	public void setCookingMethodPriority(Integer cookingMethod) {
		this.nCookingMethodPriority = cookingMethod;
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
	
	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}	
	
	*/
}
