package controller;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Picture;
import model.User;

/**
 * 
 * @author Mike Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 *
 */
public class SlideshowPageController {
	@FXML
	private ImageView photoDisplayImageView;
	@FXML
	private Button rightB, leftB, quitB, logoutB, backB;
	
	private Stage nextStage;
	//private ObservableList<Picture> obsList;
	private int index = -1;
	private int tempIndex = -2;
	private int localIndex = -3;
	//TODO: save session
	ArrayList<User> userList;
	User currUser;
	Album currAlbum;
	Picture ap;
	ArrayList<Picture> inSlideshow;
	private int slideshowIndex = -1;
	/**
	 * Starts the slideshow stage
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
//		obsList = FXCollections.observableArrayList(currentAlbum.getPhotos());
		inSlideshow = currentAlbum.getPhotos();
		if(!inSlideshow.isEmpty())
		{
			slideshowIndex = 0;
			displaySlideshow(inSlideshow.get(slideshowIndex));	
		}
		
	}
	/**
	 * Handles displaying and filling out all fields in the page
	 * @param picture
	 */
	private void displaySlideshow(Picture picture)
	{		
		photoDisplayImageView.setImage(picture.getImage());
	}
	/**
	 * Helper method for saving user session data
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
	 * Handles going to the next photo
	 * @param event
	 */
	@FXML private void rightButtonEvent(ActionEvent event) 
	{
		if(slideshowIndex == inSlideshow.size() - 1)
		{
			slideshowIndex = 0;
			displaySlideshow(inSlideshow.get(slideshowIndex));
		}
		
		else
		{
			slideshowIndex++;
			displaySlideshow(inSlideshow.get(slideshowIndex));
		}
		
	}
	/**
	 * Handles going to the previous photo
	 * @param event
	 */
	@FXML private void leftButtonEvent(ActionEvent event) 
	{
		if(slideshowIndex == 0)
		{
			slideshowIndex = inSlideshow.size() - 1;
			displaySlideshow(inSlideshow.get(slideshowIndex));
		}
		else
		{
			slideshowIndex--;
			displaySlideshow(inSlideshow.get(slideshowIndex));
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
}
