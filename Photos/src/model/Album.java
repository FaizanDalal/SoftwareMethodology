package model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */


public class Album implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Picture> photos;
	Picture oldest, newest;

	/**
	 * Constructor for album
	 * @param name
	 */
	public Album(String name) {
		this.name = name;
		photos = new ArrayList<Picture>();
	}

	/**
	 * Returns the String representation of name
	 * @return String
	 */

	public String getName() {
		return this.name;
	}

	/**
	 * Sets String representation of name
	 * @param name
	 */

	public void setName(String name) {
		this.name = name;
	}
	/**

	 * Returns the ArrayList of pictures
	 * @return arraylist
*/
	public ArrayList<Picture> getPhotos() {
		return this.photos;
	}
	/**

	 * Comparator to get a specific photo
	 * @param pic
	 * @return picture

	 */
	public Picture getSpecificPhoto(Picture pic)
	{
		for(Picture picture : photos) {
			if (picture.getSerializeImage().ImageEqual(pic.getSerializeImage())) {
				return picture;
			}
		}
		return null;
	}

	/**
	 * Sets photos into arraylist 
	 * @param photos
	 */
	public void setPhotos(ArrayList<Picture> photos) {
		this.photos = photos;
	}

	/**
	 * returns size of arraylist as an int 
	 * @return int
	 */

	public int getPhotoCount() {
		return this.photos.size();
	}
	/**

	 * equality check for album names
	 * @param other
	 * @return 

	 */
	public boolean equals(Album other) {
		return name.equals(other.name);
	}
	/**
	 * method to delete photo
	 * @param p
	 */
	public void deletePhoto(Picture p) {
		if(photos.contains(p)) {
			photos.remove(p);
		}
		
	}
	/**
	 * method to check photo name
	 * @param name
	 * @return false 
	 */
	public boolean photoNameCheck(String name){
		for(Picture picture : photos) {
			if (picture.getName().compareTo(name) == 0) {
				return false;
			}
		}
		return true;
	}
	/**

	 * method to check if photo already exists
	 * @param pic
	 * @return false if it does true if it doesnt

	 */
	public boolean photoCheck(Picture pic)
	{
		for(Picture picture : photos)
		{
			if(picture.getSerializeImage().ImageEqual(pic.getSerializeImage()))
			{
				return false;
			}
		}
		return true;
	}
	/**

	 * method to add photo

	 * @param p
	 */
	public void addPhoto(Picture p) {
		photos.add(p);
	}
	
	/**
	 * Gets oldest and newest photo in album
	 */
	public void minMaxPhotoAge()
	{
		if(photos.size() == 0 || photos.isEmpty())
		{
			return;
		}
		
		
		oldest = photos.get(0);
		newest = photos.get(0);
		for(Picture pic: photos)
		{
			if(oldest.getCalendar().compareTo(pic.getCalendar()) > 0)
			{
				oldest = pic;
			}
			
			if (newest.getCalendar().compareTo(pic.getCalendar()) < 0)
			{
				newest = pic;
			}
		}
	}
	/**

	 * Getter for oldestPicture
	 * @return oldesPicture

	 */
	public Picture getOldest() {
		return oldest;
	}

	

	/**
	 * Getter for newestPicture

	 * @return Picture
	 */
	public Picture getNewest() {
		return newest;
	}

	

	/**
	 * Getter for date of picture
	 * @param date
	 * @return LocalDate
	 */
	public LocalDate getPhotoLocalDate(Date date) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Instant instant = date.toInstant();
		LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
		return localDate;
	}
}

