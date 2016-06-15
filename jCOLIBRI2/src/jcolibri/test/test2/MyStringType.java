/**
 * MyStringType.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 01/06/2007
 */
package jcolibri.test.test2;

/**
 * This class shows how to define your own data types for an attribute of the case.
 * You only need to implement TypeAdaptor.
 * <p>
 * IMPORTANT: You must define the equals() method to avoid problems with the data base connector.
 * If you continue having problems try returning always "true".
 * 
 * @author Juan A. Recio-Garcia
 *
 */
public class MyStringType implements jcolibri.connector.TypeAdaptor{
	
	private String _internalString; 
	
	public MyStringType()
	{
	}
	
	public void fromString(String content) throws Exception {
		_internalString = content;
	}
	
	public String toString(){
		return _internalString;
	}
	
	public boolean equals(Object o)
	{
		MyStringType mst = (MyStringType) o;
		return mst._internalString.equals(this._internalString);
	}
}
