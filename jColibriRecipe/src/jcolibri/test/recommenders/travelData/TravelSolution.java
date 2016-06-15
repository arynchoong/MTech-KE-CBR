/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.test.recommenders.travelData;

import jcolibri.cbrcore.Attribute;

/**
 * Bean that stores the solution of the case (trip)
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class TravelSolution  implements jcolibri.cbrcore.CaseComponent {

	String id;
	String hotel;
	
	public String toString()
	{
		return "("+id+";"+hotel+")";
	}
	
	public Attribute getIdAttribute() {
		
		return new Attribute("id", this.getClass());
	}

	/**
	 * @return Returns the hotel.
	 */
	public String getHotel() {
		return hotel;
	}

	/**
	 * @param hotel The hotel to set.
	 */
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
