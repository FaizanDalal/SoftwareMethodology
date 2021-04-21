package model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.Image;
/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */
public class Picture implements Serializable{ 

	private static final long serialVersionUID = 1L;
	private SerializeImage image;
	private String caption;
	private ArrayList<Tag> tags;
	private Calendar calendar;
	private String name;

	/**
	 * Constructor for Picture

	 */
	public Picture() {
		name = "";
		caption = "";
		tags = new ArrayList<Tag>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		image = new SerializeImage();
	}
	/**

	 * Constructor for Picture
	 * @param i
	 * @param name
	 * @param calendar
	 */
	public Picture(Image i, String name, Calendar calendar) {
		this();
		this.name = name;
		image.setImage(i);
		this.calendar = calendar;
		this.calendar.set(Calendar.MILLISECOND, 0);
	}
	/**

	 * Returns the String representation of name
	 * @return String
	 */

	public String getName() 
	{
		return this.name;
	}

	/**
	 * Sets the name

	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * Returns the String representation of caption

	 * @return String
	 */
	public String getCaption() {
		return this.caption;
	}
	/**

	 * Sets the caption

	 * @param caption
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}
	
	/**

	 * Getter for calendar

	 * @return Calendar
	 */
	public Calendar getCalendar() {
		return this.calendar;
	}
	/**

	 * adding a tag

	 * @param t
	 */
	public void addTag(Tag t) {
		this.tags.add(t);
	}

	/**

	 * deleting a tag
	 * contains check to see if target is there

	 * @param t
	 */
	public void deleteTag(Tag t) {
		if(this.tags.contains(t)) {
			this.tags.remove(t);
		}
	}
	/**

	 * Getter for tags
	 * @return arrayList Tags

	 */
	public ArrayList<Tag> getTags() 
	{
		return this.tags;
	}
	/**

	 * Getter for serializeimage
	 * @return SerializedImage

	 */
	public SerializeImage getSerializeImage(){
		return image;
	}
	/**

	 * Getter for image
	 * @return image

	 */
	public Image getImage() {
		return image.getImage();
	}

	/**
	 * Getter for LocalDate
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

