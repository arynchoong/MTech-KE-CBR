/**
 * HouseDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 26/10/2007
 */
package jcolibri.test.recommenders.housesData;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Implements the house description.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class HouseDescription implements CaseComponent
{
    public enum Beds  {StudioFlat,one,two,three,four,five,six,seven};
    public enum Type  {Flat,House};
    public enum Area  {Acton,Addlestone,Alperton,Balham,Barnes,Battersea,Bayswater,Belsize_Park,Bermondsey,Bloomsbury,Brentford,Brixton,Brondesbury,Byfleet,Camden,Canary_Wharf,Chelsea,Chiswick,Clapham,Clerkenwell,Cricklewood,Croyden,Earls_Court,Egham,Finchley,Finsbury,Fulham,Golders_Green,Greenwich,Hammersmith,Hampstead,Highgate,Holland_Park,Holloway,Hornsey,Hounslow,Hyde_Park,Islington,Kensington,Kilburn,Kings_Cross,Kingston,Knightsbridge,Lambeth,Little_Venice,Maida_Vale,Marylebone,Mayfair,Mitcham,Notting_Hill,Paddington,Parsons_Green,Pimlico,Primrose_Hill,Putney,Regents_Park,Richmond,Shepherds_Bush,Shoreditch,Soho,South_Kensington,Southwark,St_Johns_Wood,Stepney,Streatham,Swiss_Cottage,The_City,Tooting,Twickenham,Walton_on_Thames,Wandsworth,Wapping,West_Ham,West_Horsley,West_Kensington,Westminster,Weybridge,Whitechapel,Willesden,Wimbledon};
	
    Integer id;
    Area area;
    Beds beds;
    Integer price;
    Boolean furnished;
    Type type;
    Integer baths;
    
    public String toString() 
    {
		return "("+id+";"+area+";"+beds+";"+price+";"+furnished+";"+type+";"+baths+")";
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


    public Integer getPrice()
    {
        return price;
    }


    public void setPrice(Integer price)
    {
        this.price = price;
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
