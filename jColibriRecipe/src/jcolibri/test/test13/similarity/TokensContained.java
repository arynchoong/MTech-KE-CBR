package jcolibri.test.test13.similarity;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * This function returns a similarity value depending on the tokens (words) that
 * appear in the query attribute and also are in the case attribute.
 */
public class TokensContained implements LocalSimilarityFunction {

	/** Creates a new instance of TokensContained */
	public TokensContained() {
	}

	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException
	{
		if ((caseObject == null) || (queryObject == null))
			return 0;
		if (!(caseObject instanceof java.lang.String))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
		if (!(queryObject instanceof java.lang.String))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());


		String caseS  = (String) caseObject;
		String queryS = (String) queryObject;
		
		Set<String> caseSet = new HashSet<String>();
		Set<String> querySet = new HashSet<String>();
		
		for(StringTokenizer ct = new StringTokenizer(caseS); ct.hasMoreTokens(); )
		    caseSet.add(ct.nextToken());
		for(StringTokenizer ct = new StringTokenizer(queryS); ct.hasMoreTokens(); )
		    querySet.add(ct.nextToken());		    
		
		double totalsize = caseSet.size();
		caseSet.retainAll(querySet);
		double foundsize = caseSet.size();
		
		return foundsize / totalsize;
	}

	/** Applicable to String */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof String;
		else if(o2==null)
			return o1 instanceof String;
		else
			return (o1 instanceof String)&&(o2 instanceof String);
	}

}