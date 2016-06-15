/**
 * EmailSolution.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 01/08/2007
 */
package jcolibri.test.test16;

import jcolibri.cbrcore.Attribute;
import jcolibri.extensions.classification.ClassificationSolution;

/**
 * Solution (class) of the case. As this solution is used by classification
 * methods it implements the ClassificationSolution interface
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.extensions.classification.ClassificationSolution
 */
public class EmailSolution implements ClassificationSolution
{
    public final static String HAM = "ham";
    public final static String SPAM = "spam";
    
    
    String emailClass;
    
    /**
     * @param emailClass
     */
    public EmailSolution( String emailClass)
    {
	super();
	this.emailClass = emailClass;
    }

    public String toString()
    {
	return emailClass;
    }
    
    /**
     * @return Returns the emailClass.
     */
    public String getEmailClass()
    {
        return emailClass;
    }



    /**
     * @param emailClass The emailClass to set.
     */
    public void setEmailClass(String emailClass)
    {
        this.emailClass = emailClass;
    }

/*
    public boolean equals(Object o)
    {
	if(!(o instanceof EmailSolution))
	    return false;
	EmailSolution other = (EmailSolution)o;
	return other.emailClass.equals(this.emailClass);
    }
    public int hashCode()
    {
	return emailClass.hashCode();
    }
*/

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.CaseComponent#getIdAttribute()
     */
    public Attribute getIdAttribute()
    {
	return new Attribute("emailClass", this.getClass());
    }

    public Object getClassification()
    {
	return emailClass;
    }

}
