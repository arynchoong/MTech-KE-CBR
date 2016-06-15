package jcolibri.test.test14;

import jcolibri.cbrcore.Attribute;
import jcolibri.extensions.classification.ClassificationSolution;

/**
 * Bean storing the solution for the Glass data base.
 * @author Lisa Cummins
 * @version 1.0
 */
public class GlassSolution implements ClassificationSolution {

	String type;
	
	/**
	 * Returns a String representation of this object.
	 * @return a String representation of this object.
	 */
	public String toString()
	{	return type;
	}
	
	/**
	 * Returns the id attribute of this object.
	 * @return the id attribute of this object.
	 */
	public Attribute getIdAttribute() 
	{	return new Attribute("type", this.getClass());
	}

	/**
	 * Returns the type of this object.
	 * @return the type of this object.
	 */
	public String getType() 
	{	return type;
	}

	/**
	 * Sets the type of this object to be the given type.
	 * @param type The type to set.
	 */
	public void setType(String type) 
	{	this.type = type;
	}

	public Object getClassification()
	{
	    return type;
	}	
}