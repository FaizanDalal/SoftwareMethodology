package model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String username;
	private ArrayList<Album> albums;
	/**
<<<<<<< HEAD
	 * Constructor for a user
=======
	 * User Constructor
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 * @param username
	 */
	public User(String username){
		this.username = username;
		this.albums = new ArrayList<Album>();
	}
	/**
<<<<<<< HEAD
	 * Sets the user name
=======
	 * Sets String representation of username
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 * @param username
	 */
	public void setname(String username){
		this.username = username;
	}
	/**
<<<<<<< HEAD
	 * Gets the user name
=======
	 * Returns the String representation of username
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 * @return String
	 */
	public String getName(){
		return this.username;
	}
	/**
<<<<<<< HEAD
	 * Adds and album to a user
=======
	 * Adds album to arraylist
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 * @param album
	 */
	public void addAlbum(Album album){
		albums.add(album);
	}
	/**
<<<<<<< HEAD
	 * Removes an album from a user
	 * @param album
	 * @return boolean
=======
	 * removes album from arraylist
	 * @param album
	 * @return true if album can be false if not
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 */
	public boolean remAlbum(Album album){
		if(albumNameCheck(album.getName())) {
		albums.remove(album);
		return true;
	}
		else return false;
	}
	/**
<<<<<<< HEAD
	 * Edits album name
	 * @param albumName
	 * @param album
=======
	 * Method to change album name
	 * @param album 
	 * @param albumName
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 */
	public void editAlbum(String albumName, Album album) {
		album.setName(albumName);
	}
	/**
<<<<<<< HEAD
	 * Checks for duplicate album name
	 * @param name
	 * @return boolean
=======
	 * Method to check album name
	 * @param name
	 * @return true if compareto is 0 else return false
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 */
	public boolean albumNameCheck(String name){
		for(Album album : albums) {
			if (album.getName().compareTo(name) == 0) {
				return true;
			}
		}
		return false;
	}
	/**
<<<<<<< HEAD
	 * Gets the albums a user has
	 * @return ArrayList<Albums>
=======
	 * Getter for albums
	 * @return ArrayList
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 */
	public ArrayList<Album> getAlbums(){
		return this.albums;
	}
	/**
<<<<<<< HEAD
	 * Gets an album by name
	 * @param albumName
	 * @return Album
=======
	 * Method to get specific album
	 * @param albumName
	 * @return alb if name is there if not null
>>>>>>> branch 'master' of https://mtc166@bitbucket.org/mtc166/cs213.git
	 */
	public Album getSpecificAlbum(String albumName) {
		for(Album alb : albums) {
			if (alb.getName().compareTo(albumName) == 0) {
				return alb;
			}
		}
		return null;
	}
}
