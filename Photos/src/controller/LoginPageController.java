package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert.AlertType;
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
public class LoginPageController {
	@FXML 
	private Button quitB, loginB;
	@FXML
	private TextField usernameField;
	
	private Stage nextStage;
	private final String userStoragePath = "data/data.dat";
	private final File stockPhotos = new File("data/stock");
	ArrayList<User> userList;
	String username;
	Album currAlbum;

    static final String[] EXTENSIONS = new String[]{
            "png", "jpg", "jpeg" 
    };

    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
    	@Override
 
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
	/**
	 * Starts the login stage
	 * @param loginStage
	 */
	public void start(Stage loginStage)
	{
		nextStage = loginStage;
		
		File userData = new File(userStoragePath);
		if(!userData.isFile() || !userData.exists())
		{
			try {
				userData.createNewFile();
				String stockPhotoPath = "data/stock";
				Album stockA = new Album("stock");
				User stockUser = new User("stock");
				stockUser.addAlbum(stockA);
				User adminUser = new User("admin");
				userList = new ArrayList<User>();
				userList.add(stockUser);
				userList.add(adminUser);
				
				for (final File f : stockPhotos.listFiles(IMAGE_FILTER)) {
	                BufferedImage img = null;
					img = ImageIO.read(f);
					WritableImage wr = null;
					if(img != null)
					{
						wr = new WritableImage(img.getWidth(), img.getHeight());
						PixelWriter pw = wr.getPixelWriter();
						for (int i = 0; i < img.getWidth(); i++)
						{
							for (int j = 0; j < img.getHeight(); j ++)
							{
								pw.setArgb(i,  j,  img.getRGB(i,  j));
							}
						}
					}
					
					Image buffToIm = new ImageView(wr).getImage();
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis(f.lastModified());
					Picture stockPic = new Picture(buffToIm, f.getName(), date);
					stockA.addPhoto(stockPic);
				}
				
				FileOutputStream fOutput = new FileOutputStream(userStoragePath);
				ObjectOutputStream oOutput = new ObjectOutputStream(fOutput);
				oOutput.writeObject(userList);
				oOutput.close();
				fOutput.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * Handles a user logging in
	 * @param event
	 */
	@FXML private void loginButtonEvent(ActionEvent event) 
	{
		
		String username = usernameField.getText();
 		
		try {
			FileInputStream fInput = new FileInputStream(userStoragePath);
			ObjectInputStream oInput = new ObjectInputStream(fInput);
			userList = (ArrayList<User>) oInput.readObject();
			User userInput = null;
			for (User temp : userList) {
				if (temp.getName().equals(username)) {
					userInput = temp;
				}
			}
	
			FXMLLoader loader = new FXMLLoader();
			if(username.equals("admin"))
			{
				loader.setLocation(getClass().getResource("/view/AdminView.fxml"));
				Parent root = loader.load();
				AdminPageController ctrl = loader.getController();
				ctrl.start(nextStage, userList);
				Scene scene = new Scene(root);
				nextStage.setScene(scene);
				nextStage.setTitle("Admin's funhouse");
				nextStage.setResizable(false);
				nextStage.show();
			}
			else if (userInput == null)
			{
				displayError("User", "The username inputted does not exist, please try again.");
			}
			else
			{
				loader.setLocation(getClass().getResource("/view/UserView.fxml"));
				Parent root = loader.load();
				UserPageController ctrl = loader.getController();
				ctrl.start(nextStage, userInput, userList);
				Scene scene = new Scene(root);
				nextStage.setScene(scene);
				nextStage.setTitle("Photo Album");
				nextStage.setResizable(false);
				nextStage.show();
			}		
			
			oInput.close();
			fInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Handles a user quitting the session
	 * @param event
	 */
	@FXML private void quitButtonEvent(ActionEvent event) 
	{
		Platform.exit();
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
}
