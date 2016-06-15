/**
 * Movie.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/11/2007
 */
package jcolibri.test.recommenders.rec12.moviesDB;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Bean that represents a movie.
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class Movie implements CaseComponent
{
    Integer id;
    String title;
    String releaseDate;
    String videoReleaseDate;
    String URL;
    Boolean genreUnknown;
    Boolean genreAction;
    Boolean genreAdventure;
    Boolean genreAnimation;
    Boolean genreChildren;
    Boolean genreComedy;
    Boolean genreCrime;
    Boolean genreDocumentary;
    Boolean genreDrama;
    Boolean genreFantasy;
    Boolean genreFilmNoir;
    Boolean genreHorror;
    Boolean genreMusical;
    Boolean genreMystery;
    Boolean genreRomance;
    Boolean genreSciFi;
    Boolean genreThriller;
    Boolean genreWar;
    Boolean genreWestern;
    
    public String toString()
    {
	String res = id+","+title+","+releaseDate+","+videoReleaseDate+","+URL;
	if(genreUnknown)
	    res+= ",Unknown";
	if(genreAction)
	    res+= ",Action";
	if(genreAdventure)
	    res+= ",Adventure";
	if(genreAnimation)
	    res+= ",Animation";
	if(genreChildren)
	    res+= ",Children";
	if(genreComedy)
	    res+= ",Comedy";
	if(genreCrime)
	    res+= ",Crime";
	if(genreDocumentary)
	    res+= ",Documentary";
	if(genreDrama)
	    res+= ",Drama";
	if(genreFantasy)
	    res+= ",Fantasy";
	if(genreFilmNoir)
	    res+= ",FilmNoir";
	if(genreHorror)
	    res+= ",Horror";
	if(genreMusical)
	    res+= ",Musical";
	if(genreMystery)
	    res+= ",Mystery";
	if(genreRomance)
	    res+= ",Romance";
	if(genreSciFi)
	    res+= ",SciFi";
	if(genreThriller)
	    res+= ",Thriller";
	if(genreWar)
	    res+= ",War";
	if(genreWestern)
	    res+= ",Western";
	
	return res;
    }
    
    /**
     * @return Returns the genreAction.
     */
    public Boolean getGenreAction()
    {
        return genreAction;
    }
    /**
     * @param genreAction The genreAction to set.
     */
    public void setGenreAction(Boolean genreAction)
    {
        this.genreAction = genreAction;
    }
    /**
     * @return Returns the genreAdventure.
     */
    public Boolean getGenreAdventure()
    {
        return genreAdventure;
    }
    /**
     * @param genreAdventure The genreAdventure to set.
     */
    public void setGenreAdventure(Boolean genreAdventure)
    {
        this.genreAdventure = genreAdventure;
    }
    /**
     * @return Returns the genreAnimation.
     */
    public Boolean getGenreAnimation()
    {
        return genreAnimation;
    }
    /**
     * @param genreAnimation The genreAnimation to set.
     */
    public void setGenreAnimation(Boolean genreAnimation)
    {
        this.genreAnimation = genreAnimation;
    }
    /**
     * @return Returns the genreChildren.
     */
    public Boolean getGenreChildren()
    {
        return genreChildren;
    }
    /**
     * @param genreChildren The genreChildren to set.
     */
    public void setGenreChildren(Boolean genreChildren)
    {
        this.genreChildren = genreChildren;
    }
    /**
     * @return Returns the genreComedy.
     */
    public Boolean getGenreComedy()
    {
        return genreComedy;
    }
    /**
     * @param genreComedy The genreComedy to set.
     */
    public void setGenreComedy(Boolean genreComedy)
    {
        this.genreComedy = genreComedy;
    }
    /**
     * @return Returns the genreCrime.
     */
    public Boolean getGenreCrime()
    {
        return genreCrime;
    }
    /**
     * @param genreCrime The genreCrime to set.
     */
    public void setGenreCrime(Boolean genreCrime)
    {
        this.genreCrime = genreCrime;
    }
    /**
     * @return Returns the genreDocumentary.
     */
    public Boolean getGenreDocumentary()
    {
        return genreDocumentary;
    }
    /**
     * @param genreDocumentary The genreDocumentary to set.
     */
    public void setGenreDocumentary(Boolean genreDocumentary)
    {
        this.genreDocumentary = genreDocumentary;
    }
    /**
     * @return Returns the genreDrama.
     */
    public Boolean getGenreDrama()
    {
        return genreDrama;
    }
    /**
     * @param genreDrama The genreDrama to set.
     */
    public void setGenreDrama(Boolean genreDrama)
    {
        this.genreDrama = genreDrama;
    }
    /**
     * @return Returns the genreFantasy.
     */
    public Boolean getGenreFantasy()
    {
        return genreFantasy;
    }
    /**
     * @param genreFantasy The genreFantasy to set.
     */
    public void setGenreFantasy(Boolean genreFantasy)
    {
        this.genreFantasy = genreFantasy;
    }
    /**
     * @return Returns the genreFilmNoir.
     */
    public Boolean getGenreFilmNoir()
    {
        return genreFilmNoir;
    }
    /**
     * @param genreFilmNoir The genreFilmNoir to set.
     */
    public void setGenreFilmNoir(Boolean genreFilmNoir)
    {
        this.genreFilmNoir = genreFilmNoir;
    }
    /**
     * @return Returns the genreHorror.
     */
    public Boolean getGenreHorror()
    {
        return genreHorror;
    }
    /**
     * @param genreHorror The genreHorror to set.
     */
    public void setGenreHorror(Boolean genreHorror)
    {
        this.genreHorror = genreHorror;
    }
    /**
     * @return Returns the genreMusical.
     */
    public Boolean getGenreMusical()
    {
        return genreMusical;
    }
    /**
     * @param genreMusical The genreMusical to set.
     */
    public void setGenreMusical(Boolean genreMusical)
    {
        this.genreMusical = genreMusical;
    }
    /**
     * @return Returns the genreMystery.
     */
    public Boolean getGenreMystery()
    {
        return genreMystery;
    }
    /**
     * @param genreMystery The genreMystery to set.
     */
    public void setGenreMystery(Boolean genreMystery)
    {
        this.genreMystery = genreMystery;
    }
    /**
     * @return Returns the genreRomance.
     */
    public Boolean getGenreRomance()
    {
        return genreRomance;
    }
    /**
     * @param genreRomance The genreRomance to set.
     */
    public void setGenreRomance(Boolean genreRomance)
    {
        this.genreRomance = genreRomance;
    }
    /**
     * @return Returns the genreSciFi.
     */
    public Boolean getGenreSciFi()
    {
        return genreSciFi;
    }
    /**
     * @param genreSciFi The genreSciFi to set.
     */
    public void setGenreSciFi(Boolean genreSciFi)
    {
        this.genreSciFi = genreSciFi;
    }
    /**
     * @return Returns the genreThriller.
     */
    public Boolean getGenreThriller()
    {
        return genreThriller;
    }
    /**
     * @param genreThriller The genreThriller to set.
     */
    public void setGenreThriller(Boolean genreThriller)
    {
        this.genreThriller = genreThriller;
    }
    /**
     * @return Returns the genreUnknown.
     */
    public Boolean getGenreUnknown()
    {
        return genreUnknown;
    }
    /**
     * @param genreUnknown The genreUnknown to set.
     */
    public void setGenreUnknown(Boolean genreUnknown)
    {
        this.genreUnknown = genreUnknown;
    }
    /**
     * @return Returns the genreWar.
     */
    public Boolean getGenreWar()
    {
        return genreWar;
    }
    /**
     * @param genreWar The genreWar to set.
     */
    public void setGenreWar(Boolean genreWar)
    {
        this.genreWar = genreWar;
    }
    /**
     * @return Returns the genreWestern.
     */
    public Boolean getGenreWestern()
    {
        return genreWestern;
    }
    /**
     * @param genreWestern The genreWestern to set.
     */
    public void setGenreWestern(Boolean genreWestern)
    {
        this.genreWestern = genreWestern;
    }
    /**
     * @return Returns the id.
     */
    public Integer getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }
    /**
     * @return Returns the releaseDate.
     */
    public String getReleaseDate()
    {
        return releaseDate;
    }
    /**
     * @param releaseDate The releaseDate to set.
     */
    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    /**
     * @return Returns the uRL.
     */
    public String getURL()
    {
        return URL;
    }
    /**
     * @param url The uRL to set.
     */
    public void setURL(String url)
    {
        URL = url;
    }
    /**
     * @return Returns the videoReleaseDate.
     */
    public String getVideoReleaseDate()
    {
        return videoReleaseDate;
    }
    /**
     * @param videoReleaseRate The videoReleaseDate to set.
     */
    public void setVideoReleaseDate(String videoReleaseRate)
    {
        this.videoReleaseDate = videoReleaseRate;
    }

    public Attribute getIdAttribute()
    {
	return new Attribute("id",Movie.class);
    }
    
    
}
