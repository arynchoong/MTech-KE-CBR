/**
 * TravelDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/01/2007
 */
package jcolibri.test.test3;

import jcolibri.cbrcore.Attribute;


/**
 * Bean that stores the description of the case.
 * This version includes a compound attribute to store the region.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.test.test3.Region
 */
public class TravelDescription implements jcolibri.cbrcore.CaseComponent {
	
	enum AccommodationTypes  { OneStar, TwoStars, ThreeStars, HolidayFlat, FourStars, FiveStars};
	
	String  caseId;
	String  HolidayType;
	Integer Price;
	Integer NumberOfPersons;
	Region  Region;
	String  Transportation;
	Integer Duration;
	String  Season;
	AccommodationTypes  Accommodation;
	String  Hotel;
	
	
	public String toString()
	{
		return "("+caseId+";"+HolidayType+";"+Price+";"+NumberOfPersons+";"+Region+";"+Transportation+";"+Duration+";"+Season+";"+Accommodation+";"+Hotel+")";
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
	 * @return the hotel
	 */
	public String getHotel() {
		return Hotel;
	}
	/**
	 * @param hotel the hotel to set
	 */
	public void setHotel(String hotel) {
		Hotel = hotel;
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
	 * @return the price
	 */
	public Integer getPrice() {
		return Price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		Price = price;
	}
	/**
	 * @return the region
	 */
	public Region getRegion() {
		return Region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		Region = region;
	}
	/**
	 * @return the season
	 */
	public String getSeason() {
		return Season;
	}
	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
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
	
	public static void main(String[] args) {
		TravelDescription t = new TravelDescription();
		t.setAccommodation(AccommodationTypes.ThreeStars);
		Attribute at = new Attribute("Accommodation", TravelDescription.class);
		try {
			System.out.println(at.getValue(t));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}
}
