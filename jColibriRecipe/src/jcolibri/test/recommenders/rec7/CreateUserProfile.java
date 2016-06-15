/**
 * CreateUserProfile.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/11/2007
 */
package jcolibri.test.recommenders.rec7;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.recommendation.ContentBasedProfile.CreateProfile;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;

/**
 * Class to generate the user profile
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class CreateUserProfile
{
    public static void main(String[] args) {
	CBRQuery query = new CBRQuery();
	query.setDescription(new RestaurantDescription());
	ObtainQueryWithFormMethod.obtainQueryWithoutInitialValues(query,null,null);
	CreateProfile.createProfile(query, "src/jcolibri/test/recommenders/rec7/profile.xml");
    }
}
