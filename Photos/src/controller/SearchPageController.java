package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import model.Album;
import model.Picture;
import model.Tag;
import model.User;
/**
 * 
 * @author Mike Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 *
 */
public class SearchPageController {
	@FXML
	private ListView resultsListView;
	@FXML
	private Button searchB, createAlbumB, quitB, logoutB, backB, andB, orB, singleSearchB;
	@FXML
	private TextField tagTypeField1, tagTypeField2, tagValueField1, tagValueField2, newAlbumNameTextField;
	@FXML
	private DatePicker startDate, endDate;
	@FXML
	private ImageView resultImageView;
	
	private Stage nextStage;
	private ObservableList<Picture> obsList;
	private int index = -1;
	private int tempIndex = -2;
	private int localIndex = -3;
	ArrayList<User> userList;
	User currUser;
	Album currAlbum;
	
	/**
	 * Starts the search stage
	 * @param searchStage
	 * @param currentUser
	 * @param users
	 * @param currentAlbum
	 */
	public void start(Stage searchStage, User currentUser, ArrayList<User> users, Album currentAlbum)
	{
		nextStage = searchStage;
		userList = users;
		currUser = currentUser;
		currAlbum = currentAlbum;

		obsList = FXCollections.observableArrayList(currUser.getAlbums().get(0).getPhotos());
		
		resultsListView.setItems(obsList);
		resultsListView.setCellFactory(new Callback<ListView<Picture>, ListCell<Picture>>()
		{
			public ListCell<Picture> call(ListView<Picture> p)
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
			resultsListView.getSelectionModel().select(0);
			index = resultsListView.getSelectionModel().getSelectedIndex();
		}
		
		resultsListView.getItems().clear();
		displayResults();

		resultsListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> displayResults());
	}
	/**
	 * Helper to save user session data
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
	 * Handles displaying and filling out stage fields
	 */
	private void displayResults()
	{
		if(resultsListView.getSelectionModel().getSelectedIndex() < 0)
		{
			return;
		}
		
		Picture pic = (Picture) resultsListView.getSelectionModel().getSelectedItem();
		index = resultsListView.getSelectionModel().getSelectedIndex();
		
		resultImageView.setImage(pic.getImage());
			
	}
	/**
	 * Checks if a tag is valid
	 * @param tt1
	 * @param tv1
	 * @param tt2
	 * @param tv2
	 * @return String
	 */
	private String isValidTag(String tt1, String tv1, String tt2, String tv2)
	{
		int error = -1;
		if(tt1.isEmpty() || tv1.isEmpty())
		{
			displayError("Tag", "Please provide a valid tag combination.");
			error = 0;
		}
		else if((!tt2.isEmpty() && tv2.isEmpty()) ||
				(tt2.isEmpty() && !tv2.isEmpty())
				)
		{
			displayError("Tag", "Please provide a valid secondary tag combination.");
			error = 0;
		} else if((!tt2.isEmpty() && !tv2.isEmpty()) && 
				(tt1.isEmpty() || tv1.isEmpty())	
			)
		{
			displayError("Tag", "Please provide a valid dual tag combination.");
			error = 0;
		}
		
		if(error == -1) {
			return "good";
		}
		return null;
		
	}
	/**
	 * Checks if the date range is valid
	 * @param start
	 * @param end
	 * @return String
	 */
	private String isValidDateRange(LocalDate start, LocalDate end)
	{
		int error = -1;
		
		if(start != null & end != null)
		{
			if (start.compareTo(end) > 0)
			{
				displayError("Date Range", "Please choose a valid date range.");
				error = 0;
			}
		}
		
		
		if(error == -1)
		{
			return "good";
		}
		return null;
	}
	/**
	 * Handles displaying errors
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
	 * Handles searching for a photo by date
	 * @param event
	 */
	@FXML private void searchButtonEvent(ActionEvent event) 
	{
		LocalDate startD = startDate.getValue();
		LocalDate endD = endDate.getValue();
		
		if(startDate.getValue() == null && endDate.getValue() == null)
		  {
			  displayError("Date", "Please enter in dates to search by.");
			  return;
		  }
		
		if(isValidDateRange(startD, endD).equals("good"))
		{
			displayConfirmation(1);
		}
		else if(isValidDateRange(startD, endD).isEmpty())
		{
			return;
		}
	}
	
/**
 * Handles searching for a photo by a single tag
 * @param event
 */
	@FXML private void singleSearchButtonEvent(ActionEvent event) 
	{
		String type1 = tagTypeField1.getText();
		String type2 = tagTypeField2.getText();
		String value1 = tagValueField1.getText();
		String value2 = tagValueField2.getText();
		
		if (isValidTag(type1, value1, type2, value2).equals("good"))
		{
			displayConfirmation(4);
		}
		else if (isValidTag(type1, value1, type2, value2).isEmpty())
		{
			return;
		}
	}
	/**
	 * Handles searching for a photo by a pair of tags in conjunction
	 * @param event
	 */
	@FXML private void andSearchButtonEvent(ActionEvent event) 
	{
		String type1 = tagTypeField1.getText();
		String type2 = tagTypeField2.getText();
		String value1 = tagValueField1.getText();
		String value2 = tagValueField2.getText();
		
		if (isValidTag(type1, value1, type2, value2).equals("good"))
		{
			displayConfirmation(2);
		}
		else if (isValidTag(type1, value1, type2, value2).isEmpty())
		{
			return;
		}
	}
	
	/**
	 * Handles searching for a photo by a pair of tags in disjunction
	 * @param event
	 */
	@FXML private void orSearchButtonEvent(ActionEvent event) 
	{
		String type1 = tagTypeField1.getText();
		String type2 = tagTypeField2.getText();
		String value1 = tagValueField1.getText();
		String value2 = tagValueField2.getText();
		
		if (isValidTag(type1, value1, type2, value2).equals("good"))
		{
			displayConfirmation(3);
		}
		else if (isValidTag(type1, value1, type2, value2).isEmpty())
		{
			return;
		}
	}
	/**
	 * Handles creating a new album from the results of the search
	 * @param event
	 */
	@FXML private void createAlbumButtonEvent(ActionEvent event) 
	{
		displayConfirmation(5);
	}
	/**
	 * Handles when the user wants to go back to the user view stage
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
	 * Handles confirmation of all the actions in the page
	 * @param x
	 */
	private void displayConfirmation(int x)
	{
		switch(x)
		{
			case 1: //search by date
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to search by date?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  resultsListView.getItems().clear();
						  
						  for(Album a : currUser.getAlbums())
						  {
							  for(Picture b: a.getPhotos())
							  {
								  LocalDate temp = b.getPhotoLocalDate(b.getCalendar().getTime());
								  
								  if(startDate.getValue() == null)
								  {
									  if(endDate.getValue().compareTo(temp) >= 0)
									  {
										  resultsListView.getItems().add(b);
										  index = resultsListView.getSelectionModel().getSelectedIndex();
									  }
								  }
								  else if(endDate.getValue() == null)
								  {
									  if(startDate.getValue().compareTo(temp) <= 0)
									  {
										  resultsListView.getItems().add(b);
										  index = resultsListView.getSelectionModel().getSelectedIndex();
									  }
								  }
								  else
								  {
									  if(startDate.getValue().compareTo(temp) <= 0 && endDate.getValue().compareTo(temp) >= 0)
									  {
										  resultsListView.getItems().add(b);
										  index = resultsListView.getSelectionModel().getSelectedIndex();
									  }
								  }
								
							  }
						  }
							
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
				});
			
				resultsListView.refresh();
				displayResults();
				saveSession(userList);
				break;
			case 2: //search by AND
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to search by conjunction?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  resultsListView.getItems().clear();
						  ArrayList<Picture> inner = new ArrayList<Picture>();
							String type1 = tagTypeField1.getText();
							String type2 = tagTypeField2.getText();
							String value1 = tagValueField1.getText();
							String value2 = tagValueField2.getText();
							Tag tag1 = new Tag(type1, value1);
							Tag tag2 = new Tag(type2, value2);
						  
						  for(Album a : currUser.getAlbums())
						  {
							  for(Picture b: a.getPhotos())
							  {
								if(b.getTags().contains(tag1) && b.getTags().contains(tag2))
								{
									resultsListView.getItems().add(b);
									index = resultsListView.getSelectionModel().getSelectedIndex();
								}
//								for(Tag c : b.getTags())
//								{
//									if(c.getTagType().equals(type1) && c.getTagValue().equals(value1))
//									{
//										inner.add(b);
//									}
//								}
							  }
						  }
						  
//						  for(Picture d : inner)
//						  {
//							  for(Tag e : d.getTags())
//							  {
//									if(e.getTagType().equals(type2) && e.getTagValue().equals(value2))
//									{
//										resultsListView.getItems().add(d);
//										index = resultsListView.getSelectionModel().getSelectedIndex();
//									}
//							  }
//						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				resultsListView.refresh();
				displayResults();
				saveSession(userList);
				break;
			case 3: //search by OR
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to search by disjunction?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					
					  if (btnType == ButtonType.OK) {
						  resultsListView.getItems().clear();
						  ArrayList<Picture> inner1;
							String type11 = tagTypeField1.getText();
							String type22 = tagTypeField2.getText();
							String value11 = tagValueField1.getText();
							String value22 = tagValueField2.getText();
							
							Tag tag11 = new Tag(type11, value11);
							Tag tag22 = new Tag(type22, value22);
						  
						  for(Album a : currUser.getAlbums())
						  {
							  for(Picture b: a.getPhotos())
							  {
								if(b.getTags().contains(tag11) || b.getTags().contains(tag22))  
								{
									resultsListView.getItems().add(b);
									index = resultsListView.getSelectionModel().getSelectedIndex();
								}
							  }
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				resultsListView.refresh();
				displayResults();
				saveSession(userList);
				break;
			case 4: //single search
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to search by this tag pair?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						  resultsListView.getItems().clear();
						  
					  String type111 = tagTypeField1.getText();
						String value111 = tagValueField1.getText();
						
						Tag singTag = new Tag(type111, value111);
						  
						  for(Album a : currUser.getAlbums())
						  {
							  for(Picture b: a.getPhotos())
							  {
								  if(b.getTags().contains(singTag))  
									{
										resultsListView.getItems().add(b);
										index = resultsListView.getSelectionModel().getSelectedIndex();
									}
									
//								for(Tag c: b.getTags())
//								{
//									if(c.getTagType().equals(type111) && c.getTagValue().equals(value111))
//									{
//										resultsListView.getItems().add(b);
//										index = resultsListView.getSelectionModel().getSelectedIndex();
//									}
//								}
							  }
						  }
						  
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				resultsListView.refresh();
				displayResults();
				saveSession(userList);
				break;
			case 5: //create album
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you want to create an album with these photos?");
				alert.setContentText("Please press OK to continue, or CANCEL to cancel the action.");
				alert.showAndWait().ifPresent((btnType) -> {
					  if (btnType == ButtonType.OK) {
						if(resultsListView.getItems().isEmpty())
						{
							displayError("Creation", "There are no pictures to add to a new album.");
						}
						
						if(newAlbumNameTextField.getText().isEmpty())
						{
							displayError("Name", "Please enter a valid album name.");
						}
						else
						{
							for(Album z : currUser.getAlbums())
							{
								if(z.getName().equals(newAlbumNameTextField.getText()))
								{
									displayError("Name", "Please enter a different album name.");
								}
							}
						}
						
						Album newAa = new Album(newAlbumNameTextField.getText());
						
						
						for(int y = 0; y < resultsListView.getItems().size() - 1; y++)
						{
							newAa.addPhoto((Picture) resultsListView.getItems().get(y));
						}
						
						
						currUser.addAlbum(newAa);
					  } else if (btnType == ButtonType.CANCEL) {
						  return;
					  }
					  
					  
				});
				resultsListView.refresh();
				displayResults();
				saveSession(userList);
				break;
				
				
			default:
				return;
				
		}
	}
}
