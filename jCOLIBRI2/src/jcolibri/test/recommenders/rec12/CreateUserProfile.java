/**
 * CreateUserProfile.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/11/2007
 */
package jcolibri.test.recommenders.rec12;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.recommendation.ContentBasedProfile.CreateProfile;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.test.recommenders.rec12.moviesDB.User;

/**
 * Creates the user profile.
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class CreateUserProfile
{
    public static void main(String[] args) {
	CBRQuery query = new CBRQuery();
	query.setDescription(new User());
	ObtainQueryWithFormMethod.obtainQueryWithoutInitialValues(query,null,null);
	CreateProfile.createProfile(query, "src/jcolibri/test/recommenders/rec12/profile.xml");
    }
}
