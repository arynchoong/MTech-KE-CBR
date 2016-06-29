package jcolibri.mtechkecbr.RecipeRecommender;

public class SimilAlgo {
	private java.lang.String AttributeName;
	private java.lang.String AttributeValue;
	private java.lang.Integer AttributePriority;
	
	public SimilAlgo(java.lang.String sAttribName, java.lang.String sAttribValue,java.lang.Integer sAttribPriority){
		this.AttributeName = sAttribName;
		this.AttributeValue = sAttribValue;
		this.AttributePriority = sAttribPriority;
	}
	
	public String getAttributeName(){
		return AttributeName;
	}
	public String getAttributeValue(){
		return AttributeValue;
	}
	public void setAttributeValue(String Value){
		this.AttributeValue = Value;
	}
	public Integer getAttributePriority(){
		return AttributePriority;
	}
    public String toString() {
        return "Name: " + getAttributeName() + 
        		", Value: " + getAttributeValue() +
        		", Priority: " + getAttributePriority();
    }
}
