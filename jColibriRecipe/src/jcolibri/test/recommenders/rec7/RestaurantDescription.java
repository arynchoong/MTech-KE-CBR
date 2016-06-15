/**
 * RestaurantDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/06/2007
 */
package jcolibri.test.recommenders.rec7;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.datatypes.Text;
import jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP;

/**
 * Stores the description of a restaurant. <br>
 * Some attributes are loaded by the connector (name, address, location, phone and description). 
 * But the other ones are obtained (filled) by the Textual CBR methods applied to the description attribute.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 * @see jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP
 */
public class RestaurantDescription implements CaseComponent
{
    String name;
    String address;
    String location;
    String phone;
    IETextOpenNLP description;
    
    // Extracted values from the description
    String price;
    String foodType;
    String breakfastDays;
    String lunchDays;
    String dinnerDays;
    String food;
    Boolean alcohol;
    Boolean takeout;
    Boolean delivery;
    Boolean parking;
    Boolean catering;
    
    /* (non-Javadoc)
     * @see jcolibri.cbrcore.CaseComponent#getIdAttribute()
     */
    public Attribute getIdAttribute()
    {
	return new Attribute("name",this.getClass());
    }

    /**
     * @return Returns the address.
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address The address to set.
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return Returns the alcohol.
     */
    public Boolean getAlcohol()
    {
        return alcohol;
    }

    /**
     * @param alcohol The alcohol to set.
     */
    public void setAlcohol(Boolean alcohol)
    {
        this.alcohol = alcohol;
    }

    /**
     * @return Returns the breakfastDays.
     */
    public String getBreakfastDays()
    {
        return breakfastDays;
    }

    /**
     * @param breakfastDays The breakfastDays to set.
     */
    public void setBreakfastDays(String breakfastDays)
    {
        this.breakfastDays = breakfastDays;
    }

    /**
     * @return Returns the catering.
     */
    public Boolean getCatering()
    {
        return catering;
    }

    /**
     * @param catering The catering to set.
     */
    public void setCatering(Boolean catering)
    {
        this.catering = catering;
    }

    /**
     * @return Returns the delivery.
     */
    public Boolean getDelivery()
    {
        return delivery;
    }

    /**
     * @param delivery The delivery to set.
     */
    public void setDelivery(Boolean delivery)
    {
        this.delivery = delivery;
    }

    /**
     * @return Returns the description.
     */
    public Text getDescription()
    {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(Text description)
    {
	if(description == null)
	    this.description = null;
	else
	    this.description = new IETextOpenNLP(description.toString());
    }
    


    /**
     * @return Returns the dinnerDays.
     */
    public String getDinnerDays()
    {
        return dinnerDays;
    }

    /**
     * @param dinnerDays The dinnerDays to set.
     */
    public void setDinnerDays(String dinnerDays)
    {
        this.dinnerDays = dinnerDays;
    }

    /**
     * @return Returns the food.
     */
    public String getFood()
    {
        return food;
    }

    /**
     * @param food The food to set.
     */
    public void setFood(String food)
    {
        this.food = food;
    }

    /**
     * @return Returns the foodType.
     */
    public String getFoodType()
    {
        return foodType;
    }

    /**
     * @param foodType The foodType to set.
     */
    public void setFoodType(String foodType)
    {
        this.foodType = foodType;
    }

    /**
     * @return Returns the location.
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * @param location The location to set.
     */
    public void setLocation(String location)
    {
        this.location = location;
    }

    /**
     * @return Returns the lunchDays.
     */
    public String getLunchDays()
    {
        return lunchDays;
    }

    /**
     * @param lunchDays The lunchDays to set.
     */
    public void setLunchDays(String lunchDays)
    {
        this.lunchDays = lunchDays;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the parking.
     */
    public Boolean getParking()
    {
        return parking;
    }

    /**
     * @param parking The parking to set.
     */
    public void setParking(Boolean parking)
    {
        this.parking = parking;
    }

    /**
     * @return Returns the phone.
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * @param phone The phone to set.
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * @return Returns the price.
     */
    public String getPrice()
    {
        return price;
    }

    /**
     * @param price The price to set.
     */
    public void setPrice(String price)
    {
        this.price = price;
    }

    /**
     * @return Returns the takeout.
     */
    public Boolean getTakeout()
    {
        return takeout;
    }

    /**
     * @param takeout The takeout to set.
     */
    public void setTakeout(Boolean takeout)
    {
        this.takeout = takeout;
    }

    public String toString()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(this.name);
	sb.append(", ");
	sb.append(this.address);
	sb.append(", ");
	sb.append(this.location);
	sb.append(", ");
	sb.append(this.phone);
	sb.append(",");
	sb.append(this.description);
	
	return sb.toString();
    }
    
}
