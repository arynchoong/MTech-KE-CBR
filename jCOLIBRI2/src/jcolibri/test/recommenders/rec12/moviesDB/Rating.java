/**
 * Rating.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/11/2007
 */
package jcolibri.test.recommenders.rec12.moviesDB;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Bean that represents a rating
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class Rating implements CaseComponent
{
    Integer id;
    Integer rating;
    
    public String toString()
    {
	return id+","+rating;
    }
    
    /**
     * @return Returns the id.
     */
    public Integer getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }
    /**
     * @return Returns the rating.
     */
    public Integer getRating()
    {
        return rating;
    }
    /**
     * @param rating The rating to set.
     */
    public void setRating(Integer rating)
    {
        this.rating = rating;
    }
    
    public Attribute getIdAttribute()
    {
	return new Attribute("id",Rating.class);
    }

    
}
