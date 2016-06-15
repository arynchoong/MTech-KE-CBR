package jcolibri.test.test13.similarity;

import java.util.ArrayList;
import java.util.StringTokenizer;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * This function converts the text value of an attribute into several numeric
 * values and calculates its average. Then it computes the interval difference
 * between query and case.
 * <p>
 * Example:
 * <p>
 * <p>
 * Query attribute: 2.0, 4.0 -> Average = 3.0
 * <p>
 * Case attribute: 5.0, 9.0, 4.0 -> Average = 6.0
 * <p>
 * If interval param is 9.0 computed similarity equals: 0.33
 */
public class AverageMultipleTextValues implements LocalSimilarityFunction {

	double _interval;

	/** Creates a new instance of MaxString */
	public AverageMultipleTextValues(double interval) {
		_interval = interval;
	}

	private double extractStringAverage(String s) {
		ArrayList<Double> list = new ArrayList<Double>();
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			String num = st.nextToken();
			double dnum = 0;
			try {
				dnum = Double.parseDouble(num);
			} catch (Exception e) {
			}
			if (dnum == 0) {
				try {
					dnum = (double) Integer.parseInt(num);
				} catch (Exception e) {
				}
			}
			list.add(new Double(dnum));
		}
		
		double total = list.size();
		if (total == 0)
			return 0;
		
		double acum = 0;
		for (Double Dnum : list) 
		{
			acum += Dnum.doubleValue();
		}
		return acum / total;
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
		
		if (queryS.length() == 0)
			return 0;
		if (caseS.length() == 0)
			return 0;

		double qV = extractStringAverage(queryS);
		double cV = extractStringAverage(caseS);
		return 1 - (Math.abs(qV - cV) / _interval);
		
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