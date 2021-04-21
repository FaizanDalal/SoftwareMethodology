/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */
package view;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener; //check if need this one

public class SongController {

	@FXML
	Label songLabel, artistLabel, albumLabel, yearLabel;

	@FXML
	Text songText, artistText, albumText, yearText, songDescText, artistDescText, albumDescText, yearDescText;

	@FXML
	TextField songField, artistField, albumField, yearField;

	@FXML
	ListView<Song> songList;

	@FXML
	Button addB, editB, deleteB, saveB, returnB;
	
	private ObservableList<Song> obsList;
	private int index = -1;
	private int tempIndex = -2;
	private int localIndex = -3;
	public ArrayList<Song> openFile(String filename)
	{
		ArrayList<Song> songs = new ArrayList<Song>();
		try {
			BufferedReader ftp = new BufferedReader(new FileReader(filename));
			String current = null;
			String[] songDetails;
			while(true)
			{
				current = ftp.readLine();
				if(current == null) 
				{
					break;
				}
				else
				{
					songDetails = current.split("\\|");
					Song songL = new Song(songDetails[0].trim(), songDetails[1].trim(), songDetails[2].trim(), songDetails[3].trim());
					songs.add(songL);
				}
			}
			ftp.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		songSorter(songs);
		return songs;
	}
	public void songSorter(ArrayList<Song> songs)
	{
		Collections.sort(songs, Song.songArtistName);
	}
	public void start(Stage primaryStage)
	{	
		ArrayList<Song> songs = new ArrayList<Song>();
		String filename = "src/songs.txt";
		songs = openFile(filename);
		/**
		ArrayList<Song> songs = new ArrayList<Song>();
		Song song1 = new Song("Hello", "my", "name", "11");
		Song song2 = new Song("a", "b", "c", "121");
		songs.add(song1);
		songs.add(song2);
		Song song3 = new Song("e", "f", "g", "1121");
		songs.add(song3);
		Song song4 = new Song("adsfe", "bf", "eg", "112331");
		songs.add(song4);
		**/
		obsList = FXCollections.observableArrayList(songs); //Placeholder. Items will be read from a text file
		songList.setItems(obsList);
		songList.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>()
		{
			public ListCell<Song> call(ListView<Song> i)
			{
				ListCell<Song> cell = new ListCell<Song>()
				{
					public void updateItem(Song song, boolean x)
					{
						super.updateItem(song, x);
						if(song != null)
						{
							setText(song.getName() + " - " + song.getArtist());
						}
						else
						{
							setText(null);
						}
					}
				};
				return cell;
			}
		});
		if(!obsList.isEmpty())
		{
			songList.getSelectionModel().select(0);
			index = songList.getSelectionModel().getSelectedIndex();
		}
		
		displaySong();

		songList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> displaySong());
		
		primaryStage.setOnCloseRequest(event -> 
		{
			PrintWriter w2f;
	    	try {
	    		File songStorage = new File ("src/songs.txt");
	    	  	songStorage.createNewFile();
	    	  	w2f = new PrintWriter(songStorage);
				for(Song song: obsList)
				{
					w2f.println(song.getName() + "|" + song.getArtist() + "|" + song.getAlbum() + "|" + song.getYear());  
				}
				w2f.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}   
		 });
		
	}
	
	private void displaySong()
	{
		if(songList.getSelectionModel().getSelectedIndex() < 0)
		{
			return;
		}
		
		Song song = songList.getSelectionModel().getSelectedItem();
		index = songList.getSelectionModel().getSelectedIndex();

		songLabel.setText(song.getName());
		artistLabel.setText(song.getArtist());
		albumLabel.setText(song.getAlbum());
		yearLabel.setText(song.getYear());
	}
	
	@FXML
	private void resetButtonEvent(ActionEvent event)
	{
		songField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();
		
	}
	@FXML
	private void editButtonEvent(ActionEvent event)
	{
		tempIndex = index;
		songField.setText(songLabel.getText());
		artistField.setText(artistLabel.getText());
		albumField.setText(albumLabel.getText());
		yearField.setText(yearLabel.getText());
		
	}
	@FXML
	private void saveButtonEvent(ActionEvent event)
	{
		if((songField.getText() == null || songField.getText().trim().isEmpty()) && 
			(artistField.getText() == null || artistField.getText().trim().isEmpty()) &&
			(albumField.getText() == null || albumField.getText().trim().isEmpty()) &&
			(yearField.getText() == null || yearField.getText().trim().isEmpty()))
		{
			displayError("Edit", "You haven't selected anything to be edited.");
			return;
		}
		
		songList.getSelectionModel().select(tempIndex);
		displayConfirmation(3);
	}
	@FXML 
	private void deleteButtonEvent(ActionEvent event)
	{
		if (obsList.isEmpty()) {
			   displayError("Empty", "No songs to be deleted.");
			   return;
		}
		
		displayConfirmation(2);
		
	}
	@FXML
	private void addButtonEvent(ActionEvent event) {
		displayConfirmation(1);
	}

	private int isDuplicate(String name, String artist, String album, String year)
	{
		for(Song s : obsList)
		{ 
			if (s.getName().toLowerCase().equals(name.toLowerCase()) && s.getArtist().toLowerCase().equals(artist.toLowerCase()))
			{
				return 1;
			}
		}
		return 0;
	}
	private void displayError(String type, String message)
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(type + " Error");
		alert.setHeaderText(type + " error encountered.");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private String isValid(String name, String artist, String album, String year)
	{
		int error = -1;
		if(name.isEmpty())
		{
			displayError("Name", "Please provide a valid title.");
			error = 0;
		}
		else if(artist.isEmpty())
		{
			displayError("Artist", "Please provide a valid artist.");
			error = 0;
		}
		else if(isDuplicate(name, artist, album, year) == 1)
		{
			displayError("Duplicate", "Please provide a different song and artist not currently in the library.");
			error = 0;
		}
		else if(!year.isEmpty())
		{
			if(!year.matches("[0-9]+"))
			{
				displayError("Year", "Please provide a valid date.");
				error = 0;
			}
			else
			{
				if(Integer.parseInt(year) < 0)
				{
					displayError("Year", "Please provide a valid date.");
					error = 0;
				}
			}
		}
		if(error == -1) {
			return "good";
		}
		return null;
		
	}
	@FXML
	public void mouseSelection(MouseEvent click) {
        if (click.getClickCount() == 2) {
        	displaySong();
        }
    }
	
	private void displayConfirmation(int x)
	{
		switch(x)
		{
			case 1: //add
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to add this song?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String good = isValid(songField.getText().trim(), artistField.getText().trim(), albumField.getText().trim(), yearField.getText().trim());
						  if(good.equals("good"))
						  {
							  Song song = new Song(songField.getText().trim(), artistField.getText().trim(), albumField.getText().trim(), yearField.getText().trim());
							  songList.getItems().add(song);
							  //sort goes here
							
							  songList.getSelectionModel().select(song);
							  index = songList.getSelectionModel().getSelectedIndex();
							  songField.clear();
								artistField.clear();
								albumField.clear();
								yearField.clear();
						  }
						  else
						  {
							  return;
						  }
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
				break;
			case 2: //delete
				Song song = songList.getSelectionModel().getSelectedItem();
				localIndex = songList.getSelectionModel().getSelectedIndex();
				  
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete this song?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  songList.getItems().remove(song);
						  if(songList.getItems().isEmpty())
						  {
							  songLabel.setText("");
							  artistLabel.setText("");
							  albumLabel.setText("");
							  yearLabel.setText("");
						  }
						  else if (localIndex == songList.getItems().size() - 1)
						  {
							  songList.getSelectionModel().select(localIndex--);
						  }
						  else
						  {
							  songList.getSelectionModel().select(localIndex++);
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				//sort goes here
				displaySong();
				break;
			case 3: //edit
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to save your changes?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  Song songn = songList.getSelectionModel().getSelectedItem();
						  
						  String good = isValid(songField.getText().trim(), artistField.getText().trim(), albumField.getText().trim(), yearField.getText().trim());
						  if(good.equals("good"))
						  {
							  songn.setName(songField.getText().trim());
							  songn.setArtist(artistField.getText().trim());
							  songn.setAlbum(albumField.getText().trim());
							  songn.setYear(yearField.getText().trim());
							  index = -1;
							  tempIndex = -2;
							  //sort goes here
						  }
						  else
						  {
							  return;
						  }
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
				songList.refresh();
				displaySong();
				break;
			default:
				return;
				
		}
	}
	
}
