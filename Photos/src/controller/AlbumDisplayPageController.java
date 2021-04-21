package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Picture;
import model.User;
/**
 * 
 * @author Mike Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 *
 */
public class AlbumDisplayPageController {
	@FXML
	private ListView photoListView;
	@FXML
	private Button addB, deleteB, displayB, copyB, moveB, slideshowB, quitB, logoutB, backB, renamePhotoB;
	@FXML
	private ImageView thumbnailImageView;
	@FXML
	private TextField destinationField, renameCopyField, renamePhotoField;
	@FXML
	private TextArea captionField;
	
	private Stage nextStage;
	private ObservableList<Picture> obsList;
	private int index = -1;
	private int tempIndex = -2;
	private int localIndex = -3;
	//TODO: save session
	ArrayList<User> userList;
	User currUser;
	Album currAlbum;
	Picture ap;
	Picture currPicture;
	
	/**
	 * Starts the album content display page
	 * @param loginStage
	 * @param currentUser
	 * @param users
	 * @param currentAlbum
	 */
	public void start(Stage loginStage, User currentUser, ArrayList<User> users, Album currentAlbum)
	{
		nextStage = loginStage;
		userList = users;
		currUser = currentUser;
		currAlbum = currentAlbum;
		obsList = FXCollections.observableArrayList(currentAlbum.getPhotos());
		
		photoListView.setItems(obsList);
		photoListView.setCellFactory(new Callback<ListView<Picture>, ListCell<Picture>>()
		{
			public ListCell<Picture> call(ListView<Picture> i)
			{
				ListCell<Picture> cell = new ListCell<Picture>()
				{
					public void updateItem(Picture p, boolean x)
					{
						super.updateItem(p, x);
						if(p != null)
						{
							setText(p.getName());
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
			photoListView.getSelectionModel().select(0);
			index = photoListView.getSelectionModel().getSelectedIndex();
		}
		displayPictures();

		photoListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> displayPictures());
	}
	
	/**
	 * Helper method to store the user session data
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
	 * Handles displaying and updating page fields
	 */
	private void displayPictures()
	{
		if(photoListView.getSelectionModel().getSelectedIndex() < 0)
		{
			return;
		}
		
		Picture picture = (Picture) photoListView.getSelectionModel().getSelectedItem();
		index = photoListView.getSelectionModel().getSelectedIndex();
		
		
		thumbnailImageView.setImage(picture.getImage());
		captionField.setText(picture.getCaption());
		
		
	}
	
	/**
	 * Checks for duplicate picture name
	 */
	private int isDuplicate(String name)
	{
		for(Picture Pi : obsList)
		{ 
			if (Pi.getName().equals(name))
			{
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * Checks for valid input
	 * @param name
	 * @return int
	 */
	private String isValid(String name)
	{
		int error = -1;
		if(name.isEmpty())
		{
			displayError("Name", "Please provide a valid picture name.");
			error = 0;
		}
		else if(isDuplicate(name) == 1)
		{
			displayError("Duplicate", "Please provide a different picture name.");
			error = 0;
		}
		
		if(error == -1) {
			return "good";
		}
		return null;
		
	}
	
	/**
	 * Handles error display messages
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
	 * Handles when the user quits the session
	 * @param event
	 */
	@FXML private void quitButtonEvent(ActionEvent event) 
	{
		saveSession(userList);
		Platform.exit();
	}
	/**
	 * Handles when the user logs out of the session
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
	 * Handles when the add button is pressed
	 * @param event
	 */
	@FXML private void addButtonEvent(ActionEvent event)
	{
		displayConfirmation(1);
	}
	/**
	 * Handles when the delete button is pressed
	 * @param event
	 */
	@FXML private void deleteButtonEvent(ActionEvent event)
	{
		if (obsList.isEmpty()) {
			   displayError("Empty", "No photos to be deleted.");
			   return;
		}
		
		displayConfirmation(2);
	}
	
	/**
	 * Handles when the display button is pressed. Goes to the tag view stage.
	 * @param event
	 */
	@FXML private void displayButtonEvent(ActionEvent event)
	{
		currPicture = (Picture) photoListView.getSelectionModel().getSelectedItem();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/TagView.fxml"));
			Parent root = loader.load();
			TagPageController ctrl = loader.getController();
			ctrl.start(nextStage, currUser, userList, currAlbum, currPicture);
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
	 * Handles copying a photo
	 * @param event
	 */
	@FXML private void copyButtonEvent(ActionEvent event)
	{
		//TODO: check if valid album name
		
		String destAlbum = destinationField.getText();
		if(destAlbum.compareTo("") == 0)
		{
			displayError("Copy", "Specify a destination album to copy to.");
		}
		if (currUser.albumNameCheck(destAlbum))
		{
			displayConfirmation(3);
		};
	}
	/**
	 * Handles moving a photo
	 * @param event
	 */
	@FXML private void moveButtonEvent(ActionEvent event)
	{
		//TODO: check if valid album name and delete photo from current album
		String destAlbum = destinationField.getText();
		if(destAlbum.compareTo("") == 0)
		{
			displayError("Move", "Specify a destination album to move to.");
		}
		if (currUser.albumNameCheck(destAlbum))
		{
			displayConfirmation(4);
		}
		
	}
	/**
	 * Handles renaming a photo
	 * @param event
	 */
	@FXML private void renameButtonEvent(ActionEvent event)
	{
		displayConfirmation(5);
	}
	/**
	 * Handles moving to the slideshow stage
	 * @param event
	 */
	@FXML private void slideshowButtonEvent(ActionEvent event)
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/SlideshowView.fxml"));
			Parent root = loader.load();
			SlideshowPageController ctrl = loader.getController();
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
	 * Handles going back to the album content display stage
	 * @param event
	 */
	@FXML private void backButtonEvent(ActionEvent event)
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/UserView.fxml"));
			Parent root = loader.load();
			UserPageController ctrl = loader.getController();
			ctrl.start(nextStage, currUser, userList);
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
	 * Handles the confirmation of all the actions in the page
	 * @param x
	 */
	private void displayConfirmation(int x)
	{
		switch(x)
		{
			case 1: //add
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to add a photo?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  FileChooser photoAdder = new FileChooser();
						  photoAdder.setTitle("Select an image to add to your album.");
						  photoAdder.getExtensionFilters().addAll(
									new ExtensionFilter("Image Files", "*.bmp", "*.BMP", "*.gif", "*.GIF", "*.jpg", "*.JPG", "*.png", "*.PNG"),
									new ExtensionFilter("Bitmap Files", "*.bmp", "*.BMP"),
									new ExtensionFilter("GIF Files", "*.gif", "*.GIF"), 
									new ExtensionFilter("JPEG Files", "*.jpg", "*.JPG"),
									new ExtensionFilter("PNG Files", "*.png", "*.PNG"));
						File fileToAdd = photoAdder.showOpenDialog(null);

						if (fileToAdd != null) {
							Image image = new Image(fileToAdd.toURI().toString());
							String photoName = fileToAdd.getName();
							Calendar addDate = Calendar.getInstance();
							addDate.setTimeInMillis(fileToAdd.lastModified());
							Picture addedPicture = new Picture(image, photoName, addDate);
							if (!currAlbum.photoCheck(addedPicture))
							{
								displayError("Duplicate", "Please add another photo not already in the album.");
								return;
							}
							
							currAlbum.addPhoto(addedPicture);
							photoListView.getItems().add(addedPicture);
							photoListView.getSelectionModel().select(addedPicture);
							index = photoListView.getSelectionModel().getSelectedIndex();
						}
							
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
			
				photoListView.refresh();
				displayPictures();
				saveSession(userList);
				break;
			case 2: //delete
				Picture pict = (Picture) photoListView.getSelectionModel().getSelectedItem();
				localIndex = photoListView.getSelectionModel().getSelectedIndex();
				  
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete this photo?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  photoListView.getItems().remove(pict);
						  currAlbum.deletePhoto(pict);
						  if(photoListView.getItems().isEmpty())
						  {
							thumbnailImageView.setImage(null);
							captionField.setText("");
						  }
						  else if (localIndex == photoListView.getItems().size() - 1)
						  {
							  photoListView.getSelectionModel().select(localIndex--);
						  }
						  else
						  {
							  photoListView.getSelectionModel().select(localIndex++);
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				displayPictures();
				saveSession(userList);
				break;
			case 3: //copy picture
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to copy this picture?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String destAlbum = destinationField.getText();
						  Album dAlbum = currUser.getSpecificAlbum(destAlbum);
						  if(!dAlbum.photoNameCheck(renameCopyField.getText()))
						  {
							  displayError("Duplicate Name", "Please enter another photo name not in the destination album.");
							  return;
						  }
						  else if (renameCopyField.getText().compareTo("") == 0 || renameCopyField.getText() == null)
						  {
							  displayError("Invalid Name", "Please provide a valid name for the copied photo.");
							  return;
						  }
						  if (dAlbum != null)
						  {
							  Picture picCopy = (Picture) photoListView.getSelectionModel().getSelectedItem();
							  for(Picture checkP : dAlbum.getPhotos())
							  {
								  if(picCopy.getSerializeImage().equals(checkP.getSerializeImage()))
								  {
									  displayError("Duplicate", "Please copy another photo not already in the destination album.");
									  return;
								  }
							  }
							  Picture toBeAdded = new Picture(picCopy.getImage(), renameCopyField.getText(), picCopy.getCalendar());
							  dAlbum.addPhoto(toBeAdded);
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
				photoListView.refresh();
				displayPictures();
				saveSession(userList);
				break;
			case 4: //move picture
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to move this picture?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String destAlbum = destinationField.getText();
						  Album dAlbum = currUser.getSpecificAlbum(destAlbum);
						  if(!dAlbum.photoNameCheck(renameCopyField.getText()))
						  {
							  displayError("Duplicate Name", "Please enter another photo name not in the destination album.");
							  return;
						  }
						  else if (renameCopyField.getText().compareTo("") == 0 || renameCopyField.getText() == null)
						  {
							  displayError("Invalid Name", "Please provide a valid name for the moved photo.");
							  return;
						  }
						  if (dAlbum != null)
						  {
							  Picture picMove = (Picture) photoListView.getSelectionModel().getSelectedItem();
							  for(Picture checkP : dAlbum.getPhotos())
							  {
								  if(picMove.getSerializeImage().equals(checkP.getSerializeImage()))
								  {
									  displayError("Duplicate", "Please move another photo not already in the destination album.");
									  return;
								  }
							  }
							  dAlbum.addPhoto(picMove);
							  dAlbum.getSpecificPhoto(picMove).setName(renameCopyField.getText());
							  currAlbum.deletePhoto(picMove);
							  photoListView.getItems().remove(picMove);
							  if(photoListView.getItems().isEmpty())
							  {
								thumbnailImageView.setImage(null);
								captionField.setText("");
							  }
							  else if (localIndex == photoListView.getItems().size() - 1)
							  {
								  photoListView.getSelectionModel().select(localIndex--);
							  }
							  else
							  {
								  photoListView.getSelectionModel().select(localIndex++);
							  }
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
				photoListView.refresh();
				displayPictures();
				saveSession(userList);
				break;
			case 5: //rename
				pict = (Picture) photoListView.getSelectionModel().getSelectedItem();
				localIndex = photoListView.getSelectionModel().getSelectedIndex();
				
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to rename this photo?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String good = isValid(renamePhotoField.getText().trim());
						  if(good.equals("good"))
						  {
							  pict.setName(renamePhotoField.getText().trim());
							  index = -1;
							  tempIndex = -2;
						  }
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
				photoListView.refresh();
				displayPictures();
				saveSession(userList);
				break;
				
				
			default:
				return;
				
		}
	}
}
