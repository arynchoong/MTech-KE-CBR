package jcolibri.test.test14;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Bean storing the description for the Glass data base.
 * @author Lisa Cummins
 * @version 1.0
 */
public class GlassDescription implements CaseComponent {

	Double ri;
    Double na;
    Double mg;
    Double al;
	Double si;
	Double k;
	Double ca;
	Double ba;
	Double fe;
	String id;

	/**
	 * Returns a String representation of this object.
	 * @return a String representation of this object.
	 */
	public String toString()
	{	return id+", "+ri+", "+na+", "+mg+", "+al+", "+si+", "+k+", "+ca+", "+ba+", "+fe;
	}
	
	/**
	 * Returns the id attribute of this object.
	 * @return the id attribute of this object.
	 */
	public Attribute getIdAttribute() 
	{	return new Attribute("id", this.getClass());
	}

	/**
	 * Returns the id.
	 * @return the id.
	 */
	public String getId() 
	{	return id;
	}

	/**
	 * Sets the id to be the given id.
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the Ri value.
	 * @return the Ri value.
	 */
	public Double getRi() 
	{	return ri;
	}

	/**
	 * Sets the Ri value to be the given value.
	 * @param ri The Ri value to set.
	 */
	public void setRi(Double ri) 
	{	this.ri = ri;
	}

	/**
	 * Returns the Na value.
	 * @return the Na value.
	 */
	public Double getNa() 
	{	return ri;
	}

	/**
	 * Sets the Na value to be the given value.
	 * @param na The Na value to set.
	 */
	public void setNa(Double na) 
	{	this.na = na;
	}

	/**
	 * Returns the Mg value.
	 * @return the Mg value.
	 */
	public Double getMg() 
	{	return mg;
	}

	/**
	 * Sets the Mg value to be the given value.
	 * @param mg The Mg value to set.
	 */
	public void setMg(Double mg) 
	{	this.mg = mg;
	}
	
	/**
	 * Returns the Al value.
	 * @return the Al value.
	 */
	public Double getAl() 
	{	return al;
	}

	/**
	 * Sets the Al value to be the given value.
	 * @param al The Al value to set.
	 */
	public void setAl(Double al) 
	{	this.al = al;
	}

	/**
	 * Returns the Si value.
	 * @return the Si value.
	 */
	public Double getSi() 
	{	return si;
	}

	/**
	 * Sets the Si value to be the given value.
	 * @param si The Si value to set.
	 */
	public void setSi(Double si) 
	{	this.si = si;
	}
	
	/**
	 * Returns the K value.
	 * @return the K value.
	 */
	public Double getK() 
	{	return k;
	}

	/**
	 * Sets the K value to be the given value.
	 * @param k The k value to set.
	 */
	public void setK(Double k) 
	{	this.k = k;
	}

	/**
	 * Returns the Ca value.
	 * @return the Ca value.
	 */
	public Double getCa() 
	{	return ca;
	}

	/**
	 * Sets the Ca value to be the given value.
	 * @param ca The Ca value to set.
	 */
	public void setCa(Double ca) 
	{	this.ca = ca;
	}

	/**
	 * Returns the ba value.
	 * @return the Ba value.
	 */
	public Double getBa() 
	{	return ba;
	}

	/**
	 * Sets the Ba value to be the given value.
	 * @param ba The Ba value to set.
	 */
	public void setBa(Double ba) 
	{	this.ba = ba;
	}

	/**
	 * Returns the Fe value.
	 * @return the Fe value.
	 */
	public Double getFe() 
	{	return fe;
	}

	/**
	 * Sets the Fe value to be the given value.
	 * @param fe The Fe value to set.
	 */
	public void setFe(Double fe) 
	{	this.fe = fe;
	}
}