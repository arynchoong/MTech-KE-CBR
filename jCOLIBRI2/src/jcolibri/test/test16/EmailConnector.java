/**
 * EmailConnector.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 01/08/2007
 */
package jcolibri.test.test16;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;
import jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP;

/**
 * Connector that read cases from a zip file with several textual files (one per email).
 * The filename must start by ham or spam depending on the class.
 * <br>
 * The corpus is packed into the lib\textual\spamcorpus\spamcorpus.jar file and was extracted
 * from the Apache Spamassassin project (http://spamassassin.apache.org/publiccorpus/).
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class EmailConnector implements Connector
{
    String zipfile;
    
    /**
     * Creates a connector for a given zip file.
     */
    public EmailConnector(String zipfile)
    {
	this.zipfile = zipfile;
    }
    
    /* (non-Javadoc)
     * @see jcolibri.cbrcore.Connector#close()
     */
    public void close()
    {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.Connector#deleteCases(java.util.Collection)
     */
    public void deleteCases(Collection<CBRCase> cases)
    {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.Connector#initFromXMLfile(java.net.URL)
     */
    public void initFromXMLfile(URL file) throws InitializingException
    {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.Connector#retrieveAllCases()
     */
    public Collection<CBRCase> retrieveAllCases()
    {
	int ham_easy = 0;
	int ham_hard = 0;
	int spam = 0;
	ArrayList<CBRCase> cases = new ArrayList<CBRCase>();
	try
	{
    
	    BufferedInputStream source = new BufferedInputStream (jcolibri.util.FileIO.openFile(zipfile));
	    ZipInputStream zip_in_stream = new ZipInputStream (source);

	    BufferedReader br = new BufferedReader(new InputStreamReader(zip_in_stream));
	    
	    ZipEntry entry;
	    while((entry=zip_in_stream.getNextEntry())!=null)
	    {
		String id = entry.getName();
		
		StringBuffer buffer = new StringBuffer();
	        while(br.ready())
	        {
	            buffer.append(br.readLine());
	            buffer.append("\n");
	        }	
		
	        String _class;
	        if(id.startsWith("spam"))
	        {
	            _class = EmailSolution.SPAM;
	            spam++;
	        }
	        else
	        {
	            _class = EmailSolution.HAM;
	            if(id.startsWith("hard"))
	        	ham_hard++;
	            else
	        	ham_easy++;
	        }
	        
	        EmailDescription desc = new EmailDescription(id, new IETextOpenNLP(new String(buffer)));
	        EmailSolution    sol  = new EmailSolution(_class);
	        
	        CBRCase _case = new CBRCase();
	        _case.setDescription(desc);
	        _case.setSolution(sol);
	        
	        cases.add(_case);
	    }
	    org.apache.commons.logging.LogFactory.getLog(this.getClass()).info("Loaded "+ham_easy+" easy ham - "+ham_hard+" hard ham - "+spam+" spam");
	    br.close();
	} catch (IOException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
	    
	}
	return cases;
    }

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.Connector#retrieveSomeCases(jcolibri.cbrcore.CaseBaseFilter)
     */
    public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter filter)
    {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see jcolibri.cbrcore.Connector#storeCases(java.util.Collection)
     */
    public void storeCases(Collection<CBRCase> cases)
    {
	// TODO Auto-generated method stub

    }

}
