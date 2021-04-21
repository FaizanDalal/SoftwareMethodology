/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */
package view;

import java.util.Comparator;

public class Song {

	private String name;
	private String artist;
	private String album;
	private String year;

	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public void editSong(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbum() {
		return album;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYear() {
		return year;
	}
	
    public static Comparator<Song> songArtistName = new Comparator<Song>()
    {
        public int compare(Song s1, Song s2) {
        	String s1Name = s1.getName().toLowerCase();
        	String s2Name = s2.getName().toLowerCase();
        	String s1Artist = s1.getArtist().toLowerCase();
        	String s2Artist = s2.getArtist().toLowerCase();
        	if(s1Name.compareTo(s2Name) != 0)
        	{
        		return s1Name.compareTo(s2Name);
        	}
        	else
        	{
        		return s1Artist.compareTo(s2Artist);
        	}
        	
        }
    };

}