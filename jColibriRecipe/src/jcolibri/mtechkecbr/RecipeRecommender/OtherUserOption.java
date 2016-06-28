package jcolibri.mtechkecbr.RecipeRecommender;

public class OtherUserOption {
	private java.lang.String AttributeName;
	private java.lang.Boolean AttributeValue;
	private java.lang.Integer AttributePriority;
	
	public OtherUserOption(java.lang.String sAttribName, java.lang.Boolean bAttribValue,java.lang.Integer sAttribPriority){
		this.AttributeName = sAttribName;
		this.AttributeValue = bAttribValue;
		this.AttributePriority = sAttribPriority;
	}
	
	public String getAttributeName(){
		return AttributeName;
	}
	public Boolean getAttributeValue(){
		return AttributeValue;
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
