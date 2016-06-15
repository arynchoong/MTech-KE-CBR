/**
 * HouseDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 26/10/2007
 */
package jcolibri.test.recommenders.rec8.housesData;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Implements the house description.
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class HouseDescription implements CaseComponent
{
    public enum Beds  {StudioFlat,one,two,three,four,five,six,seven};
    public enum Type  {Flat,House};
    public enum Area  {Acton,Addlestone,Alperton,Balham,Barnes,Battersea,Bayswater,Belsize_Park,Bermondsey,Bloomsbury,Brentford,Brixton,Brondesbury,Byfleet,Camden,Canary_Wharf,Chelsea,Chiswick,Clapham,Clerkenwell,Cricklewood,Croyden,Earls_Court,Egham,Finchley,Finsbury,Fulham,Golders_Green,Greenwich,Hammersmith,Hampstead,Highgate,Holland_Park,Holloway,Hornsey,Hounslow,Hyde_Park,Islington,Kensington,Kilburn,Kings_Cross,Kingston,Knightsbridge,Lambeth,Little_Venice,Maida_Vale,Marylebone,Mayfair,Mitcham,Notting_Hill,Paddington,Parsons_Green,Pimlico,Primrose_Hill,Putney,Regents_Park,Richmond,Shepherds_Bush,Shoreditch,Soho,South_Kensington,Southwark,St_Johns_Wood,Stepney,Streatham,Swiss_Cottage,The_City,Tooting,Twickenham,Walton_on_Thames,Wandsworth,Wapping,West_Ham,West_Horsley,West_Kensington,Westminster,Weybridge,Whitechapel,Willesden,Wimbledon};
    public enum PriceRange { from_0_to_99, from_100_to_199, from_200_to_299, from_300_to_399, from_400_to_499, from_500_to_599, 
	from_600_to_699, from_700_to_799, from_800_to_899, from_900_to_999,
	from_1000_to_1099, from_1100_to_1199, from_1200_to_1299, from_1300_to_1399, from_1400_to_1499, from_1500_to_1599, 
	from_1600_to_1699, from_1700_to_1799, from_1800_to_1899, from_1900_to_1999, from_2000_to_2099,
	form_2100_to_2199, from_2200_to_2299, from_2300_to_2399, from_2400_to_2499, more_than_2500};
    
    Integer id;
    Area area;
    Beds beds;
    PriceRange priceRange;
    Boolean furnished;
    Type type;
    Integer baths;
    
    public String toString() 
    {
		return "("+id+";"+area+";"+beds+";"+priceRange+";"+furnished+";"+type+";"+baths+")";
    }

    

    public PriceRange getPriceRange()
    {
        return priceRange;
    }



    public void setPriceRange(PriceRange priceRange)
    {
        this.priceRange = priceRange;
    }



    public Area getArea()
    {
        return area;
    }


    public void setArea(Area area)
    {
        this.area = area;
    }


    public Integer getBaths()
    {
        return baths;
    }


    public void setBaths(Integer baths)
    {
        this.baths = baths;
    }


    public Beds getBeds()
    {
        return beds;
    }


    public void setBeds(Beds beds)
    {
        this.beds = beds;
    }


    public Boolean getFurnished()
    {
        return furnished;
    }


    public void setFurnished(Boolean furnished)
    {
        this.furnished = furnished;
    }


    public Integer getId()
    {
        return id;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }




    
    public Type getType()
    {
        return type;
    }


    public void setType(Type type)
    {
        this.type = type;
    }


    /* (non-Javadoc)
     * @see jcolibri.cbrcore.CaseComponent#getIdAttribute()
     */
    public Attribute getIdAttribute()
    {
	return new Attribute("id",this.getClass());
    }

}
