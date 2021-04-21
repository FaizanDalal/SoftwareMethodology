package model;
import controller.LoginPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */

public class Photos extends Application {
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			
			//loader.setLocation(getClass().getResource("/view/AdminView.fxml"));
			loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
			//loader.setLocation(getClass().getResource("/view/AlbumView.fxml"));
			//loader.setLocation(getClass().getResource("/view/SearchView.fxml"));
			//loader.setLocation(getClass().getResource("/view/SlideshowView.fxml"));
			//loader.setLocation(getClass().getResource("/view/TagView.fxml"));
			//loader.setLocation(getClass().getResource("/view/UserView.fxml"));
			
			Parent root = loader.load();
			LoginPageController loginController = loader.getController();
			loginController.start(stage);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Photo Album");
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/**
 * Main method
 * @param args
 */
	public static void main(String[] args) {
		launch(args);
	}
}
