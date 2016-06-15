/**
 * TravelDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/06/2007
 */

package jcolibri.test.test10;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;

/**
 * Bean that stores the description of the case.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class TravelDescription implements jcolibri.cbrcore.CaseComponent {
	
	Instance  mainConcept;
	Instance  HolidayType;
	Instance  NumberOfPersons;
	Instance  Destination;
	Instance  Transportation;
	Instance  Duration;
	Instance  Season;
	Instance  Accommodation;
	
	
	public String toString()
	{
		return "("+mainConcept+";"+HolidayType+";"+NumberOfPersons+";"+Destination+";"+Transportation+";"+Duration+";"+Season+";"+Accommodation+")";
	}
	
	/**
	 * @return the accomodation
	 */
	public Instance getAccommodation() {
		return Accommodation;
	}
	/**
	 * @param accomodation the accomodation to set
	 */
	public void setAccommodation(Instance accomodation) {
		Accommodation = accomodation;
	}
	/**
	 * @return the caseId
	 */
	public Instance getCaseId() {
		return mainConcept;
	}
	/**
	 * @param mc the main concept to set
	 */
	public void setMainConcept(Instance mc) {
		this.mainConcept = mc;
	}
	/**
	 * @return Returns the mainConcept.
	 */
	public Instance getMainConcept() {
		return mainConcept;
	}
	/**
	 * @return the duration
	 */
	public Instance getDuration() {
		return Duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Instance duration) {
		Duration = duration;
	}
	/**
	 * @return the holidayType
	 */
	public Instance getHolidayType() {
		return HolidayType;
	}
	/**
	 * @param holidayType the holidayType to set
	 */
	public void setHolidayType(Instance holidayType) {
		HolidayType = holidayType;
	}
	/**
	 * @return the numberOfPersons
	 */
	public Instance getNumberOfPersons() {
		return NumberOfPersons;
	}
	/**
	 * @param numberOfPersons the numberOfPersons to set
	 */
	public void setNumberOfPersons(Instance numberOfPersons) {
		NumberOfPersons = numberOfPersons;
	}

	/**
	 * @return the destination
	 */
	public Instance getDestination() {
		return Destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Instance destination) {
		Destination = destination;
	}
	/**
	 * @return the season
	 */
	public Instance getSeason() {
		return Season;
	}
	/**
	 * @param season the season to set
	 */
	public void setSeason(Instance season) {
		Season = season;
	}
	/**
	 * @return the transportation
	 */
	public Instance getTransportation() {
		return Transportation;
	}
	/**
	 * @param transportation the transportation to set
	 */
	public void setTransportation(Instance transportation) {
		Transportation = transportation;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("mainConcept", this.getClass());
	}


}
