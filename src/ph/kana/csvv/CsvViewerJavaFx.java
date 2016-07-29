package ph.kana.csvv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ph.kana.csvv.controller.CsvViewerController;

import static ph.kana.csvv.util.Constants.APP_NAME;
import static ph.kana.csvv.util.Constants.VERSION;


public class CsvViewerJavaFx extends Application {

	private static final double APP_WIDTH = 600.0;
	private static final double APP_HEIGHT = 650.0;

	@Override
	public void start(Stage primaryStage) throws Exception {
		setUserAgentStylesheet(STYLESHEET_MODENA);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/csv-viewer.fxml"));
		Scene scene = new Scene(loader.load(), APP_WIDTH, APP_HEIGHT);

		CsvViewerController controller = loader.<CsvViewerController>getController();
		controller.setApplication(this);
		controller.setWindow(primaryStage);
		controller.openFileCliArgs(getParameters().getUnnamed());

		primaryStage.setTitle("kana0011/csv-viewer");
		primaryStage.setMinWidth(APP_WIDTH);
		primaryStage.setMinHeight(APP_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		if (args.length == 1 && "--version".equals(args[0])) {
			System.out.println(APP_NAME);
			System.out.print(VERSION);
		} else {
			launch(args);
		}
	}
}
