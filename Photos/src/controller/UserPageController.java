package controller;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.User;
import model.Picture;

/**
 * 
 * @author Mike Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 *
 */
public class UserPageController {
	@FXML
	private ListView albumListView;
	@FXML
	private Button addB, searchB, editB, deleteB, openB;
	@FXML
	private TextField albumNameField, numPhotoField;
	@FXML
	private DatePicker startDate, endDate;
	@FXML
	private Text usernameText;
	
	private Stage nextStage;
	private ObservableList<Album> obsList;
	private int index = -1;
	private int tempIndex = -2;
	private int localIndex = -3;
	ArrayList<User> userList;
	User currUser;
	Album currAlbum;
	/**
	 * Starts the user view page
	 * @param userStage
	 * @param currentUser
	 * @param users
	 */
	public void start(Stage userStage, User currentUser, ArrayList<User> users)
	{
		nextStage = userStage;
		currUser = currentUser;
		obsList = FXCollections.observableArrayList(currentUser.getAlbums());
		userList = users;
		usernameText.setText(currUser.getName());
		
		startDate.setEditable(false);
		endDate.setEditable(false);
		numPhotoField.setEditable(false);
		startDate.setDisable(true);
		endDate.setDisable(true);
		numPhotoField.setDisable(true);
		startDate.setStyle("-fx-opacity: 1;");
		endDate.setStyle("-fx-opacity: 1;");
		numPhotoField.setStyle("-fx-opacity: 1;");
		
		albumListView.setItems(obsList);
		albumListView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>()
		{
			public ListCell<Album> call(ListView<Album> i)
			{
				ListCell<Album> cell = new ListCell<Album>()
				{
					public void updateItem(Album a, boolean x)
					{
						super.updateItem(a, x);
						if(a != null)
						{
							setText(a.getName());
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
			albumListView.getSelectionModel().select(0);
			index = albumListView.getSelectionModel().getSelectedIndex();
		}
		displayAlbums();

		albumListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> displayAlbums());
	}
	/**
	 * Helper method for saving session data
	 * @param userList
	 */
	private void saveSession(ArrayList<User> userList) 
	{
		try {
			FileOutputStream fOutput = new FileOutputStream("data/data.dat");
			ObjectOutputStream oOutput = new ObjectOutputStream(fOutput);
			oOutput.writeObject(userList);
			oOutput.close();
			fOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Handles displaying and filling out all fields on the page
	 */
	private void displayAlbums()
	{
		if(albumListView.getSelectionModel().getSelectedIndex() < 0)
		{
			return;
		}
		
		Album album = (Album) albumListView.getSelectionModel().getSelectedItem();
		index = albumListView.getSelectionModel().getSelectedIndex();
		
		albumNameField.setText(album.getName());
		
		if(!album.getPhotos().isEmpty()) {
			numPhotoField.setText(Integer.toString(album.getPhotoCount()));
			album.minMaxPhotoAge();
			startDate.setValue(album.getPhotoLocalDate(album.getOldest().getCalendar().getTime()));
			endDate.setValue(album.getPhotoLocalDate(album.getNewest().getCalendar().getTime()));
		}
		else
		{
			albumNameField.setText(album.getName());
			numPhotoField.setText(Integer.toString(0));
			startDate.setValue(null);
			endDate.setValue(null);
		}
		
		
		
		
	}
	/**
	 * Checks for duplicate album name
	 * @param name
	 * @return
	 */
	private int isDuplicate(String name)
	{
		//TODO: maybe other checkers for duplication
		for(Album Al : obsList)
		{ 
			if (Al.getName().equals(name))
			{
				return 1;
			}
		}
		return 0;
	}
	/**
	 * Checks if inputted album name is valid
	 * @param name
	 * @return
	 */
	private String isValid(String name)
	{
		//TODO: date stuff
		int error = -1;
		if(name.isEmpty())
		{
			displayError("Name", "Please provide a valid album name.");
			error = 0;
		}
		else if(isDuplicate(name) == 1)
		{
			displayError("Duplicate", "Please provide a different album name.");
			error = 0;
		}
		
		if(error == -1) {
			return "good";
		}
		return null;
		
	}
	/**
	 * Helper method for displaying errors
	 * @param type
	 * @param message
	 */
	private void displayError(String type, String message)
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(type + " Error");
		alert.setHeaderText(type + " error encountered.");
		alert.setContentText(message);
		alert.showAndWait();
	}
	/**
	 * Handles when a user quits the session
	 * @param event
	 */
	@FXML private void quitButtonEvent(ActionEvent event) 
	{
		saveSession(userList);
		Platform.exit();
	}
	/**
	 * Handles when a user logs out of the session
	 * @param event
	 */
	@FXML private void logoutButtonEvent(ActionEvent event) 
	{
		saveSession(userList);
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
			Parent root = loader.load();
			LoginPageController ctrl = loader.getController();
			ctrl.start(nextStage);
			Scene scene = new Scene(root);
			nextStage.setScene(scene);
			nextStage.setTitle("Photo Album");
			nextStage.setResizable(false);
			nextStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Handles adding an album
	 * @param event
	 */
	@FXML private void addButtonEvent(ActionEvent event)
	{
		displayConfirmation(1);
	}
	/**
	 * Handles searching for picturew within an album
	 * @param event
	 */
	@FXML private void searchButtonEvent(ActionEvent event)
	{
		if(currUser.getAlbums().isEmpty())
		{
			displayError("Search", "Must have at least one album to search.");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/SearchView.fxml"));
			Parent root = loader.load();
			SearchPageController ctrl = loader.getController();
			ctrl.start(nextStage, currUser, userList, currAlbum);
			Scene scene = new Scene(root);
			nextStage.setScene(scene);
			nextStage.setTitle("Photo Album");
			nextStage.setResizable(false);
			nextStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Handles editing an album name
	 * @param event
	 */
	@FXML private void editButtonEvent(ActionEvent event)
	{
		
		if((albumNameField.getText() == null || albumNameField.getText().trim().isEmpty()))
			{
				displayError("Edit", "You haven't selected anything to be edited.");
				return;
			}
			
			albumListView.getSelectionModel().select(tempIndex);
			displayConfirmation(3);
	}
	
	/**
	 * Handles deleting an album
	 * @param event
	 */
	@FXML private void deleteButtonEvent(ActionEvent event)
	{
		if (obsList.isEmpty()) {
			   displayError("Empty", "No albums to be deleted.");
			   return;
		}
		
		displayConfirmation(2);
	}
	/**
	 * Handles opening an album and goes to next stage
	 * @param event
	 */
	@FXML private void openButtonEvent(ActionEvent event)
	{
		currAlbum = (Album) albumListView.getSelectionModel().getSelectedItem();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/AlbumView.fxml"));
			Parent root = loader.load();
			AlbumDisplayPageController ctrl = loader.getController();
			ctrl.start(nextStage, currUser, userList, currAlbum);
			Scene scene = new Scene(root);
			nextStage.setScene(scene);
			nextStage.setTitle("Photo Album");
			nextStage.setResizable(false);
			nextStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Handles confirmation of all actions on the stage
	 * @param x
	 */
	private void displayConfirmation(int x)
	{
		switch(x)
		{
			case 1: //add
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to add this album?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String good = isValid(albumNameField.getText().trim());
						  if(good.equals("good"))
						  {
							  Album alb = new Album(albumNameField.getText().trim());
							  currUser.addAlbum(alb);
							  albumListView.getItems().add(alb);
							  albumListView.getSelectionModel().select(alb);
							  index = albumListView.getSelectionModel().getSelectedIndex();
							  albumNameField.clear();
						  }
						  else
						  {
							  return;
						  }
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
				saveSession(userList);
				break;
			case 2: //delete
				Album albu = (Album) albumListView.getSelectionModel().getSelectedItem();
				localIndex = albumListView.getSelectionModel().getSelectedIndex();
				  
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete this album?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  albumListView.getItems().remove(albu);
						  currUser.remAlbum(albu);
						  if(albumListView.getItems().isEmpty())
						  {
							  albumNameField.setText("");
							  numPhotoField.setText("");
							  startDate.setValue(null);
							  endDate.setValue(null);
						  }
						  else if (localIndex == albumListView.getItems().size() - 1)
						  {
							  albumListView.getSelectionModel().select(localIndex--);
						  }
						  else
						  {
							  albumListView.getSelectionModel().select(localIndex++);
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				displayAlbums();
				saveSession(userList);
				break;
			case 3: //edit
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to save your changes?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  Album blum = (Album) albumListView.getSelectionModel().getSelectedItem();
						  
						  String good = isValid(albumNameField.getText());
						  if(good.equals("good"))
						  {
							  blum.setName(albumNameField.getText().trim());
							  index = -1;
							  tempIndex = -2;
						  }
						  else
						  {
							  return;
						  }
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
			albumListView.refresh();
			displayAlbums();
			saveSession(userList);
			break;
			default:
				return;
				
		}
	}
}
