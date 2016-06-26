package jcolibri.mtechkecbr.RecipeRecommender;

public class SimilarityAlgoInputs {
	private java.lang.String AttributeName;
	private java.lang.Double AttributeWeight;
	
	public SimilarityAlgoInputs(java.lang.String sAttribName, java.lang.Double sAttribWeight){
		this.AttributeName = sAttribName;
		this.AttributeWeight = sAttribWeight;
	}
	
	public String getAttributeName(){
		return AttributeName;
	}
	public Double getAttributeWeight(){
		return AttributeWeight;
	}
    public String toString() {
        return "AttributeName: " + getAttributeName() + 
        		", AttributeWeight: " + getAttributeWeight();
    }
}
