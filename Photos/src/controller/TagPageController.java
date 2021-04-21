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
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import model.Album;
import model.Picture;
import model.Tag;
import model.User;

public class TagPageController {
	@FXML
	private ListView photoTagListView;
	@FXML
	private Button addCaptionB, addTagB, removeTagB, doneB, quitB, logoutB, editCaptionB, removeCaptionB, editTagB;
	@FXML
	private ImageView photoThumnailView;
	@FXML
	private TextField nameField, valueField, captionField;	
	@FXML
	private TextArea captionFieldArea, dateField;
	@FXML
	private ImageView photoThumbnailView;
	
	private Stage nextStage;
	private ObservableList<Tag> obsList;
	private int index = -1;
	private int tempIndex = -2;
	private int localIndex = -3;
	ArrayList<User> userList;
	User currUser;
	Album currAlbum;
	Picture currPicture;
	/**
	 * Starts the tag view stage
	 * @param loginStage
	 * @param currentUser
	 * @param users
	 * @param currentAlbum
	 * @param currentPicture
	 */
	public void start(Stage loginStage, User currentUser, ArrayList<User> users, Album currentAlbum, Picture currentPicture)
	{
		nextStage = loginStage;
		userList = users;
		currUser = currentUser;
		currAlbum = currentAlbum;
		currPicture = currentPicture;
		obsList = FXCollections.observableArrayList(currPicture.getTags());
		
		photoTagListView.setItems(obsList);
		photoTagListView.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>()
		{
			public ListCell<Tag> call(ListView<Tag> i)
			{
				ListCell<Tag> cell = new ListCell<Tag>()
				{
					public void updateItem(Tag t, boolean x)
					{
						super.updateItem(t, x);
						if(t != null)
						{
							setText(t.getTagType() + " - " + t.getTagValue());
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
			photoTagListView.getSelectionModel().select(0);
			index = photoTagListView.getSelectionModel().getSelectedIndex();
		}
		displayPreliminary();
		displayTags();

		photoTagListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> displayTags());
	}
	/**
	 * Helper method to display initial data
	 */
	private void displayPreliminary()
	{
		captionField.setText(currPicture.getCaption());
		photoThumbnailView.setImage(currPicture.getImage());
		captionFieldArea.setText(currPicture.getCaption());
		dateField.setText(currPicture.getPhotoLocalDate(currPicture.getCalendar().getTime()).toString());
	}
	
	/**
	 * Helper method to store user session data
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
	private void displayTags()
	{
		if(photoTagListView.getSelectionModel().getSelectedIndex() < 0)
		{
			return;
		}
		
		Tag tag = (Tag) photoTagListView.getSelectionModel().getSelectedItem();
		index = photoTagListView.getSelectionModel().getSelectedIndex();
		
		
		nameField.setText(tag.getTagType());
		valueField.setText(tag.getTagValue());
		captionField.setText(currPicture.getCaption());
		photoThumbnailView.setImage(currPicture.getImage());
		captionFieldArea.setText(currPicture.getCaption());
		dateField.setText(currPicture.getPhotoLocalDate(currPicture.getCalendar().getTime()).toString());
		
	}
	/**
	 * Checks for duplicate tags
	 * @param type
	 * @param value
	 * @return String
	 */
	private int isDuplicate(String type, String value)
	{
		for(Tag ta : obsList)
		{ 
			if (ta.getTagType().equals(type) && ta.getTagValue().equals(value))
			{
				return 1;
			}
		}
		return 0;
	}
	/**
	 * Checks if inputted tag is valid
	 * @param type
	 * @param value
	 * @return String
	 */
	private String isValid(String type, String value)
	{
		int error = -1;
		if(type.isEmpty() || type.isBlank())
		{
			displayError("Type", "Please provide a valid tag type.");
			error = 0;
		}
		else if(value.isEmpty() || value.isBlank())
		{
			displayError("Value", "Please provide a valid tag value.");
			error = 0;
		}
		else if(isDuplicate(type, value) == 1)
		{
			displayError("Duplicate", "Please provide a different tag combination.");
			error = 0;
		}
		
		if(error == -1) {
			return "good";
		}
		return null;
		
	}
	/**
	 * Helper method to display errors
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
	 * Handles adding a caption
	 * @param event
	 */
	@FXML private void addCaptionButtonEvent(ActionEvent event) 
	{
		if(captionField.getText().isEmpty() || captionField.getText().isBlank())
		{
			displayError("Blank", "Please provide a valid caption.");
		}
		displayConfirmation(1);
	}
	/**
	 * Handles editing a caption
	 * @param event
	 */
	@FXML private void editCaptionButtonEvent(ActionEvent event) 
	{
		
		if(captionField.getText().isEmpty() || captionField.getText().isBlank())
		{
			displayError("Blank", "Please provide a valid caption.");
		}
		displayConfirmation(2);
	}
	/**
	 * Handles removing the caption
	 * @param event
	 */
	@FXML private void removeCaptionButtonEvent(ActionEvent event) 
	{
		displayConfirmation(3);
	}
	/**
	 * Handles adding a tag
	 * @param event
	 */
	@FXML private void addTagButtonEvent(ActionEvent event) 
	{
		displayConfirmation(4);
	}
	/**
	 * Handles editing a tag
	 * @param event
	 */
	@FXML private void editTagButtonEvent(ActionEvent event) 
	{
		displayConfirmation(5);
	}
	/**
	 * Handles removing a tag
	 * @param event
	 */
	@FXML private void removeTagButtonEvent(ActionEvent event) 
	{
		displayConfirmation(6);
	}
	/**
	 * Handles going back to the album content view stage
	 * @param event
	 */
	@FXML private void doneButtonEvent(ActionEvent event) 
	{
		saveSession(userList);
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
	 * Handles confirmation of all actions in the stage
	 * @param x
	 */
	private void displayConfirmation(int x)
	{
		switch(x)
		{
			case 1: //add caption
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to add a caption?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  currPicture.setCaption(captionField.getText());
						  captionFieldArea.setText(captionField.getText());
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
			
				photoTagListView.refresh();
				displayTags();
				saveSession(userList);
				break;
			case 2: //edit caption 
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to edit this caption?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  currPicture.setCaption(captionField.getText());
						  captionFieldArea.setText(captionField.getText());
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				photoTagListView.refresh();
				displayTags();
				saveSession(userList);
				break;
			case 3: //delete caption 
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete this caption?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  currPicture.setCaption("");
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }		  		  
				});
				photoTagListView.refresh();
				displayTags();
				saveSession(userList);
				break;	
			case 4: //add tag 
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to add a tag?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String good = isValid(nameField.getText(), valueField.getText());
						  if(good.equals("good"))
						  {
							  Tag ree = new Tag(nameField.getText(), valueField.getText());
							  currPicture.addTag(ree);
							  photoTagListView.getItems().add(ree);
							  photoTagListView.getSelectionModel().select(ree);
							  index = photoTagListView.getSelectionModel().getSelectedIndex();
							  nameField.clear();
							  valueField.clear();
						  }
						  else
						  {
							  return;
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				photoTagListView.refresh();
				displayTags();
				saveSession(userList);
				break;
			case 5: //edit tag
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to edit this tag?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String good = isValid(nameField.getText(), valueField.getText());
						  if(good.equals("good"))
						  {
							  Tag ree = (Tag) photoTagListView.getSelectionModel().getSelectedItem();
							  ree.setTagType(nameField.getText());
							  ree.setTagValue(valueField.getText());
							  nameField.clear();
							  valueField.clear();
						  }
						  else
						  {
							  return;
						  }
					 
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				photoTagListView.refresh();
				displayTags();
				saveSession(userList);
				break;
			case 6: //delete tag 
				Tag d = (Tag) photoTagListView.getSelectionModel().getSelectedItem();
				localIndex = photoTagListView.getSelectionModel().getSelectedIndex();
				
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete this tag?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  Tag ree = new Tag(nameField.getText(), valueField.getText());
						  currPicture.deleteTag(ree);
						  photoTagListView.getItems().remove(ree);
						  
						  
						  if(photoTagListView.getItems().isEmpty())
						  {
							  nameField.clear();
							  valueField.clear();
						  }
						  else if (localIndex == photoTagListView.getItems().size() - 1)
						  {
							  photoTagListView.getSelectionModel().select(localIndex--);
						  }
						  else
						  {
							  photoTagListView.getSelectionModel().select(localIndex++);
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				photoTagListView.refresh();
				displayTags();
				saveSession(userList);
				break;
			default:
				return;
				
		}
	}
}
