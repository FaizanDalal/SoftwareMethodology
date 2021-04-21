/**
 * @author Michael Chiang (mtc166)
 * @author Faizan Dalal (fsd15)
 */
package model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.SongController;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Layout.fxml"));
			Parent root = loader.load();
			SongController songController = loader.getController();
			songController.start(primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Song Library");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
