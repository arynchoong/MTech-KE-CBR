/**
 * EmailDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 01/08/2007
 */
package jcolibri.test.test16;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.extensions.textual.IE.representation.IEText;

/**
 * Email description.
 * Contains the file name and its contents.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class EmailDescription implements CaseComponent
{
    String id;
    IEText content;
    
    /**
     * Constructor
     * @param id
     * @param content
     */
    public EmailDescription(String id, IEText content)
    {
	super();
	this.id = id;
	this.content = content;
    }
    
    public String toString()
    {
	return id+","+content;
    }

    /**
     * @return Returns the content.
     */
    public IEText getContent()
    {
        return content;
    }


    /**
     * @param content The content to set.
     */
    public void setContent(IEText content)
    {
        this.content = content;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }


    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.CaseComponent#getIdAttribute()
     */
    public Attribute getIdAttribute()
    {
	return new Attribute("id", this.getClass());
    }

    
}
