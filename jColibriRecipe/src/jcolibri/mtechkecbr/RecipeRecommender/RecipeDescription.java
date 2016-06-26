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

	// this class is NOT working yet
	public class RecipeItem {
		   private String szName;
		   private Integer nPriority;
		   private GenericValue<Integer> nValue;
		   private GenericValue<String> szValue;
		   private GenericValue<Float>  dValue;
		   private GenericValue<Boolean>  bValue;
		   
		   class GenericValue<T> {
			   private T itemValue;
			   public GenericValue(T myValue) { this.itemValue = myValue; }
			   public T getVal() { return itemValue; }
		   }
		   // constructor
		   public RecipeItem(String name, Integer priority, Integer value) {
		      this.szName = name;
		      this.nPriority = priority;
		      this.nValue = new GenericValue<Integer>(value);
		   }
		   public RecipeItem(String name, Integer priority, String value) {
			      this.szName = name;
			      this.nPriority = priority;
			      this.szValue = new GenericValue<String>(value);
		   }   
		   public RecipeItem(String name, Integer priority, Float value) {
			      this.szName = name;
			      this.nPriority = priority;
			      this.dValue = new GenericValue<Float>(value);
		   }
		   public RecipeItem(String name, Integer priority, Boolean value) {
			      this.szName = name;
			      this.nPriority = priority;
			      this.bValue = new GenericValue<Boolean>(value);			      
		   }
		   
		   // getter
	       public String getName() { return szName; }
	       public Integer getPriority() { return nPriority; }
	       public GenericValue<Integer> getValueAsInteger() { return nValue; }
	       public GenericValue<String> getValueAsString() { return szValue; }
	       public GenericValue<Float> getValueAsFloat() { return dValue; }
	       public GenericValue<Boolean> getValueAsBoolean() { return bValue; }
	       // setter
	       public void setName(String name) { this.szName = name; }
	       public void setPriority(Integer priority) { this.nPriority = priority; }
	       public void setValueAsInteger(GenericValue<Integer> value) { this.nValue = value; }
	       public void setValueAsString(GenericValue<String> value) { this.szValue = value; }
	       public void setValueAsDouble(GenericValue<Float> value) { this.dValue = value; }
	       public void setValueAsBoolean(GenericValue<Boolean> value) { this.bValue = value; }
	}
	
	public RecipeItem Difficulty;
	public RecipeItem TypeOfCuisine;
	public RecipeItem TypeOfMeal;
	public RecipeItem MainIngredient;
	public RecipeItem CookingDuration;
	public RecipeItem NumberOfPersons;
	public RecipeItem HealthyOption;
	public RecipeItem HalalOption;
	public RecipeItem VeganOption;
	public RecipeItem NutsFreeOption;
	public RecipeItem NonSpicyOption;
	
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
		Difficulty = new RecipeItem("Difficulty", 99, "Anything");
		TypeOfCuisine = new RecipeItem("TypeOfCuisine", 99, "Anything");
		TypeOfMeal = new RecipeItem("TypeOfMeal", 99, "Anything");
		MainIngredient = new RecipeItem("MainIngredient", 99, "Anything");
		CookingDuration = new RecipeItem("CookingDuration", 99, (java.lang.Integer)120);
		NumberOfPersons = new RecipeItem("NumberOfPersons", 99, (java.lang.Integer)2);
		HealthyOption = new RecipeItem("HealthyOption", 99, (java.lang.Boolean)false);
		HalalOption = new RecipeItem("HalalOption", 99, (java.lang.Boolean)false);
		VeganOption = new RecipeItem("VeganOption", 99, (java.lang.Boolean)false);
		NutsFreeOption = new RecipeItem("NutsFreeOption", 99, (java.lang.Boolean)false);
		NonSpicyOption = new RecipeItem("NonSpicyOption", 99, (java.lang.Boolean)false);
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
}
