package ph.kana.csvv.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static ph.kana.csvv.util.Constants.APP_NAME;
import static ph.kana.csvv.util.Constants.VERSION;

public class AboutDialogController extends AbstractController {

	@FXML private Label appNameLabel;
	@FXML private Label versionLabel;

	public void initialize() {
		appNameLabel.setText(APP_NAME);
		versionLabel.setText(VERSION);
	}

	@FXML
	public void licenseLinkClicked() {
		openLink("http://www.apache.org/licenses/LICENSE-2.0");
	}

	@FXML
	public void sourceCodeLinkClicked() {
		openLink("https://github.com/kana0011/csv-viewer");
	}

	@FXML
	public void closeButtonClicked() {
		((Stage) window).close();
	}

	private void openLink(String url) {
		application.getHostServices()
			.showDocument(url);
	}
}
