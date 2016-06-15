/**
 * TravelDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/01/2007
 */
package jcolibri.test.test8;

import jcolibri.cbrcore.Attribute;

/**
 * Bean that stores the description of the case.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class TravelDescription implements jcolibri.cbrcore.CaseComponent {
	String  caseId;
	String  HolidayType;
	Integer Price;
	Integer NumberOfPersons;
	String  Region;
	String  Transportation;
	Integer Duration;
	String  Season;
	String  Accomodation;
	String  Hotel;
	
	
	public String toString()
	{
		return "("+caseId+";"+HolidayType+";"+Price+";"+NumberOfPersons+";"+Region+";"+Transportation+";"+Duration+";"+Season+";"+Accomodation+";"+Hotel+")";
	}
	
	/**
	 * @return the accomodation
	 */
	public String getAccomodation() {
		return Accomodation;
	}
	/**
	 * @param accomodation the accomodation to set
	 */
	public void setAccomodation(String accomodation) {
		Accomodation = accomodation;
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
	public String getRegion() {
		return Region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
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


	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}
	
	public static void main(String[] args) {
		TravelDescription t = new TravelDescription();
		t.setAccomodation("prueba");
		Attribute at = new Attribute("Accomodation", TravelDescription.class);
		try {
			System.out.println(at.getValue(t));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
