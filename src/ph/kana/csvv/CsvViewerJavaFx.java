package ph.kana.csvv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CsvViewerJavaFx extends Application {

	private static final double APP_WIDTH = 600.0;
	private static final double APP_HEIGHT = 650.0;

	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("fxml/csv-viewer.fxml"));
		Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);

		primaryStage.setTitle("kana0011/csv-viewer");
		primaryStage.setMinWidth(APP_WIDTH);
		primaryStage.setMinHeight(APP_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
