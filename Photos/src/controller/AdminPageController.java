package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.User;
/**
 * 
 * @author Mike Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 *
 */
public class AdminPageController {
	@FXML 
	private Button quitB, loginB, deleteB, addUserB;
	
	@FXML
	private ListView<User> userListView;
	
	@FXML
	private TextField newUsername;
	
	private Stage nextStage;
	private ObservableList<User> obsList;
	private int index = -1;
	private int localIndex = -3;
	
	/**
	 * Starts the Admin Page
	 * @param adminStage
	 * @param userList
	 */
	public void start(Stage adminStage, ArrayList<User> userList)
	{
		nextStage = adminStage;
		obsList = FXCollections.observableArrayList(userList);
		
		userListView.setItems(obsList);
		userListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>()
		{
			public ListCell<User> call(ListView<User> i)
			{
				ListCell<User> cell = new ListCell<User>()
				{
					public void updateItem(User u, boolean x)
					{
						super.updateItem(u, x);
						if(u != null)
						{
							setText(u.getName());
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
			userListView.getSelectionModel().select(0);
			index = userListView.getSelectionModel().getSelectedIndex();
		}
		displayUsers();

		userListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> displayUsers());
// saving the users
//		adminStage.setOnCloseRequest(event -> 
//		{
//			PrintWriter w2f;
//	    	try {
//	    		File songStorage = new File ("src/songs.txt");
//	    	  	songStorage.createNewFile();
//	    	  	w2f = new PrintWriter(songStorage);
//				for(User user: obsList)
//				{
//					w2f.println(song.getName() + "|" + song.getArtist() + "|" + song.getAlbum() + "|" + song.getYear());  
//				}
//				w2f.close(); 
//			} catch (Exception e) {
//				e.printStackTrace();
//			}   
//		 });
	}
	
	/**
	 * Displays and updates fields in Admin Page
	 */
	private void displayUsers()
	{
		if(userListView.getSelectionModel().getSelectedIndex() < 0)
		{
			return;
		}
		
		User user = (User) userListView.getSelectionModel().getSelectedItem();
		index = userListView.getSelectionModel().getSelectedIndex();

		newUsername.setText(user.getName());
	}
	
	/**
	 * Checks for duplicate username
	 * @param name
	 * @return int
	 */
	private int isDuplicate(String name)
	{
		for(User l : obsList)
		{ 
			if (l.getName().equals(name))
			{
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * Checks for valid input
	 * @param name
	 * @return String
	 */
	private String isValid(String name)
	{
		int error = -1;
		if(name.isEmpty())
		{
			displayError("Name", "Please provide a valid username.");
			error = 0;
		}
		else if(isDuplicate(name) == 1)
		{
			displayError("Duplicate", "Please provide a different user not currently registered.");
			error = 0;
		}
		
		if(error == -1) {
			return "good";
		}
		return null;
		
	}
	
	/**
	 * Handles a user quitting the session
	 * @param event
	 */
	@FXML private void quitButtonEvent(ActionEvent event) 
	{
		saveSession();
		Platform.exit();
	}
	
	
	/**
	 * Handles a user logging out of the session
	 * @param event
	 */
	@FXML private void logoutButtonEvent(ActionEvent event) 
	{
		saveSession();
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
	 * Delete a user when the button is pressed
	 * @param event
	 */
	@FXML private void deleteUserButtonEvent(ActionEvent event) 
	{
		if (obsList.isEmpty()) {
			   displayError("Empty", "No songs to be deleted.");
			   return;
		}
		
		displayConfirmation(2);
	}
	
	/**
	 * Add a user when the button is pressed
	 * @param event
	 */
	@FXML private void addUserButtonEvent(ActionEvent event)
	{
		displayConfirmation(1);
	}
	
	/**
	 * Helper method to store the user session data
	 */
	private void saveSession() 
	{
		try {
			FileOutputStream fOutput = new FileOutputStream("data/data.dat");
			ObjectOutputStream oOutput = new ObjectOutputStream(fOutput);
			oOutput.writeObject(new ArrayList<>(Arrays.asList(userListView.getItems().toArray())));
			oOutput.close();
			fOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * Handles the confirmation and actions when the buttons are pressed
	 * @param x
	 */
	private void displayConfirmation(int x)
	{
		switch(x)
		{
			case 1: //add
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to add this user?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  String good = isValid(newUsername.getText().trim());
						  if(good.equals("good"))
						  {
							  User oops = new User(newUsername.getText().trim());
							  userListView.getItems().add(oops);
							  userListView.getSelectionModel().select(oops);
							  index = userListView.getSelectionModel().getSelectedIndex();
							  newUsername.clear();
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
				User ops = userListView.getSelectionModel().getSelectedItem();
				localIndex = userListView.getSelectionModel().getSelectedIndex();
				  
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete this user?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  userListView.getItems().remove(ops);
						  if(userListView.getItems().isEmpty())
						  {
							  newUsername.setText("");
						  }
						  else if (localIndex == userListView.getItems().size() - 1)
						  {
							  userListView.getSelectionModel().select(localIndex--);
						  }
						  else
						  {
							  userListView.getSelectionModel().select(localIndex++);
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				displayUsers();
				break;
			default:
				return;
				
		}
	}
}
