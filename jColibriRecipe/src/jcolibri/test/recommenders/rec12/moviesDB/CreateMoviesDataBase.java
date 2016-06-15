/**
 * CreateMoviesDataBase.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/11/2007
 */
package jcolibri.test.recommenders.rec12.moviesDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import jcolibri.test.recommenders.rec12.moviesDB.User.Gender;
import jcolibri.test.recommenders.rec12.moviesDB.User.Occupation;
import jcolibri.util.FileIO;

/**
 * Class that generates the movies database in a proper format.
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class CreateMoviesDataBase
{
    private static HashMap<Integer,User> users = new HashMap<Integer, User>();
    private static HashMap<Integer,Movie> movies = new HashMap<Integer, Movie>();
    
    
    private static void parseUsers(String filename) throws Exception
    {
	BufferedReader br = null;
	br = new BufferedReader( new InputStreamReader(FileIO.openFile(filename)));
	if (br == null)
		throw new Exception("Error opening file: " + filename);

	String line = "";
	while ((line = br.readLine()) != null) 
	{
	    StringTokenizer st = new StringTokenizer(line,"|");
	    User user = new User();
	    user.setId(Integer.parseInt(st.nextToken()));
	    user.setAge(Integer.parseInt(st.nextToken()));
	    String gender = st.nextToken();
	    if(gender.equals("M"))
		user.setGender(Gender.Male);
	    else
		user.setGender(Gender.Female);
	    user.setOccupation(Occupation.valueOf(st.nextToken()));
	    user.setZipCode(st.nextToken());
	    
	    users.put(user.getId(), user);
	}
	br.close();

    }
    
    
    private static void parseMovies(String filename) throws Exception
    {
	BufferedReader br = null;
	br = new BufferedReader( new InputStreamReader(FileIO.openFile(filename)));
	if (br == null)
		throw new Exception("Error opening file: " + filename);

	String line = "";
	while ((line = br.readLine()) != null) 
	{
	    StringTokenizer st = new StringTokenizer(line,"|");
	    Movie movie = new Movie();
	    movie.setId(Integer.parseInt(st.nextToken()));
	    movie.setTitle(st.nextToken());
	    movie.setReleaseDate(st.nextToken());
	    movie.setVideoReleaseDate(st.nextToken());
	    movie.setURL(st.nextToken());
	    movie.setGenreUnknown(st.nextToken().equals("1"));
	    movie.setGenreAction(st.nextToken().equals("1"));
	    movie.setGenreAdventure(st.nextToken().equals("1"));
	    movie.setGenreAnimation(st.nextToken().equals("1"));
	    movie.setGenreChildren(st.nextToken().equals("1"));
	    movie.setGenreComedy(st.nextToken().equals("1"));
	    movie.setGenreCrime(st.nextToken().equals("1"));
	    movie.setGenreDocumentary(st.nextToken().equals("1"));
	    movie.setGenreDrama(st.nextToken().equals("1"));
	    movie.setGenreFantasy(st.nextToken().equals("1"));
	    movie.setGenreFilmNoir(st.nextToken().equals("1"));
	    movie.setGenreHorror(st.nextToken().equals("1"));
	    movie.setGenreMusical(st.nextToken().equals("1"));
	    movie.setGenreMystery(st.nextToken().equals("1"));
	    movie.setGenreRomance(st.nextToken().equals("1"));
	    movie.setGenreSciFi(st.nextToken().equals("1"));
	    movie.setGenreThriller(st.nextToken().equals("1"));
	    movie.setGenreWar(st.nextToken().equals("1"));
	    movie.setGenreWestern(st.nextToken().equals("1"));
	    
	    movies.put(movie.getId(), movie);
	}
	br.close();
    }
    
    private static String getUser(User user, String sep)
    {
	return user.getId()+sep+user.getAge()+sep+user.getGender()+sep+user.getOccupation()+sep+user.getZipCode();
    }
    private static String getMovie(Movie movie,String sep)
    {
	return movie.getId()+sep+movie.getTitle()+sep+movie.getReleaseDate()+sep+movie.getVideoReleaseDate()+sep+movie.getURL()+sep+movie.getGenreUnknown()+sep+movie.getGenreAction()+sep+movie.getGenreAdventure()+sep+movie.getGenreAnimation()+sep+movie.getGenreChildren()+sep+movie.getGenreComedy()+sep+movie.getGenreCrime()+sep+movie.getGenreDocumentary()+sep+movie.getGenreDrama()+sep+movie.getGenreFantasy()+sep+movie.getGenreFilmNoir()+sep+movie.getGenreHorror()+sep+movie.getGenreMusical()+sep+movie.getGenreMystery()+sep+movie.getGenreRomance()+sep+movie.getGenreSciFi()+sep+movie.getGenreThriller()+sep+movie.getGenreWar()+sep+movie.getGenreWestern();
    }
    
    
    private static void generateNewFile(String ratingsFile, String filename, String separator) throws Exception
    {
	BufferedWriter bw = null;
	bw = new BufferedWriter(new FileWriter(new File(filename), false));
	if (bw == null)
		throw new Exception("Error opening file for writing: "+filename);

	BufferedReader br = null;
	br = new BufferedReader( new InputStreamReader(FileIO.openFile(ratingsFile)));
	if (br == null)
		throw new Exception("Error opening file: " + filename);

	int ratingId = 1;
	String line = "";
	while ((line = br.readLine()) != null) 
	{
	    StringTokenizer st = new StringTokenizer(line,"\t");
	    Integer userId = Integer.parseInt(st.nextToken());
	    Integer movieId = Integer.parseInt(st.nextToken());
	    Integer rat = Integer.parseInt(st.nextToken());
	    
	    User user = users.get(userId);
	    Movie movie = movies.get(movieId);
	    Rating rating = new Rating();
	    rating.setId(ratingId++);
	    rating.setRating(rat);
	    
	    
	    bw.write(getUser(user,separator)+separator+getMovie(movie,separator)+separator+rating.getId()+separator+rating.getRating());
	    bw.newLine();
	}
	
	br.close();
	bw.close();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
	try
	{
	    parseUsers("jcolibri/test/recommenders/rec12/moviesDB/u.user");
	    parseMovies("jcolibri/test/recommenders/rec12/moviesDB/u.item");
	    
	    generateNewFile("jcolibri/test/recommenders/rec12/moviesDB/u.data","src/jcolibri/test/recommenders/rec12/moviesDB/movies.txt","|");
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(CreateMoviesDataBase.class).error(e);
	    
	}
	
	System.out.println("Finished");

    }

}
