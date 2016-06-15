/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.test.recommenders.travelData;


import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;


/**
 * Bean that stores the description of the case.
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class TravelDescription implements jcolibri.cbrcore.CaseComponent {
	
	public enum AccommodationTypes  { OneStar, TwoStars, ThreeStars, HolidayFlat, FourStars, FiveStars};
	public enum Seasons { January,February,March,April,May,June,July,August,September,October,November,December };
	
	String  caseId;
	String  HolidayType;
	Integer NumberOfPersons;
	Instance  Region;
	String  Transportation;
	Integer Duration;
	Seasons  Season;
	AccommodationTypes  Accommodation;
	Integer Price;
	
	
	public String toString()
	{
		return "("+caseId+";"+HolidayType+";"+NumberOfPersons+";"+Region+";"+Transportation+";"+Duration+";"+Season+";"+Accommodation+")";
	}
	
	/**
	 * @return the accomodation
	 */
	public AccommodationTypes getAccommodation() {
		return Accommodation;
	}
	/**
	 * @param accomodation the accomodation to set
	 */
	public void setAccommodation(AccommodationTypes accomodation) {
		Accommodation = accomodation;
	}
	/**
	 * @return the caseId
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return Duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		Duration = duration;
	}
	/**
	 * @return the holidayType
	 */
	public String getHolidayType() {
		return HolidayType;
	}
	/**
	 * @param holidayType the holidayType to set
	 */
	public void setHolidayType(String holidayType) {
		HolidayType = holidayType;
	}
	/**
	 * @return the numberOfPersons
	 */
	public Integer getNumberOfPersons() {
		return NumberOfPersons;
	}
	/**
	 * @param numberOfPersons the numberOfPersons to set
	 */
	public void setNumberOfPersons(Integer numberOfPersons) {
		NumberOfPersons = numberOfPersons;
	}
	/**
	 * @return the region
	 */
	public Instance getRegion() {
		return Region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(Instance region) {
		Region = region;
	}
	/**
	 * @return the season
	 */
	public Seasons getSeason() {
		return Season;
	}
	/**
	 * @param season the season to set
	 */
	public void setSeason(Seasons season) {
		Season = season;
	}
	/**
	 * @return the transportation
	 */
	public String getTransportation() {
		return Transportation;
	}
	/**
	 * @param transportation the transportation to set
	 */
	public void setTransportation(String transportation) {
		Transportation = transportation;
	}

	/**
	 * @return Returns the price.
	 */
	public Integer getPrice() {
		return Price;
	}

	/**
	 * @param price The price to set.
	 */
	public void setPrice(Integer price) {
		this.Price = price;
	}

	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}
	
}
