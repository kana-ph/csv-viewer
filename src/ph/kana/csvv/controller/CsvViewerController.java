package ph.kana.csvv.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ph.kana.csvv.model.CsvData;
import ph.kana.csvv.util.CsvFileUtil;
import sun.plugin.javascript.navig.Anchor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.control.Alert.AlertType;

public class CsvViewerController extends AbstractController {

	@FXML private AnchorPane rootPane;
	@FXML private TabPane csvTableTabPane;

	private final Map<File, Tab> ACTIVE_CSV_FILES = new HashMap();

	public void initialize() {
		rootPane.setOnDragOver(this::handleRootPaneDragOver);
		rootPane.setOnDragDropped(this::handleRootPaneDragDropped);
	}

	@FXML
	public void openCsvMenuClick() {
		FileChooser fileChooser = createFileChooser();
		List<File> csvFiles = fileChooser.showOpenMultipleDialog(window);

		if (csvFiles != null) {
			csvFiles.stream()
				.forEach(this::openCsvTab);
		}
	}

	@FXML
	public void openAboutDialog() {
		String fxmlLocation = "/ph/kana/csvv/fxml/about-dialog.fxml";

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlLocation));
			Scene scene = new Scene(loader.load());

			Stage dialog = new Stage();
			dialog.initStyle(StageStyle.UNIFIED);
			dialog.initModality(Modality.APPLICATION_MODAL);

			dialog.initOwner(window);
			dialog.setTitle("About CSV Viewer");
			dialog.setScene(scene);
			dialog.sizeToScene();
			dialog.setResizable(false);

			AboutDialogController aboutController = loader.getController();
			aboutController.setWindow(dialog);
			aboutController.setApplication(application);

			dialog.show();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	public void openFileCliArgs(List<String> args) {
		args.stream()
			.map(File::new)
			.forEach(this::openCsvTab);
	}

	private FileChooser createFileChooser() {
		FileChooser fileChooser = new FileChooser();
		File userHome = new File(System.getProperty("user.home"));

		fileChooser.setInitialDirectory(userHome);
		fileChooser.getExtensionFilters()
			.add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
		fileChooser.setTitle("Open CSV File");
		return fileChooser;
	}

	private void openCsvTab(File csvFile) {
		Tab tab;
		if (ACTIVE_CSV_FILES.containsKey(csvFile)) {
			tab = ACTIVE_CSV_FILES.get(csvFile);
		} else {
			tab = createCsvTab(csvFile);

			if (tab != null) {
				csvTableTabPane.getTabs()
					.add(tab);
				ACTIVE_CSV_FILES.put(csvFile, tab);
				tab.setOnClosed(event -> ACTIVE_CSV_FILES.remove(csvFile));
			}
		}
		csvTableTabPane.getSelectionModel()
			.select(tab);
	}

	private Tab createCsvTab(File csvFile) {
		try {
			Tab tab = new Tab();
			tab.setText(csvFile.getName());

			AnchorPane tabContentAnchorPane = new AnchorPane();
			TableView csvTable = createCsvTable(csvFile);
			AnchorPane bottomControl = createBottomControl(csvFile);

			tabContentAnchorPane.getChildren()
				.addAll(csvTable, bottomControl);

			AnchorPane.setTopAnchor(csvTable, 0.0);
			AnchorPane.setRightAnchor(csvTable, 0.0);
			AnchorPane.setBottomAnchor(csvTable, 30.0);
			AnchorPane.setLeftAnchor(csvTable, 0.0);

			AnchorPane.setRightAnchor(bottomControl, 2.0);
			AnchorPane.setBottomAnchor(bottomControl, 2.0);
			AnchorPane.setLeftAnchor(bottomControl, 2.0);

			tab.setContent(tabContentAnchorPane);

			return tab;
		} catch (IOException e) {
			reportIOException(e, csvFile);
			return null;
		}
	}

	private AnchorPane createBottomControl(File csvFile) {
		AnchorPane bottomControl = new AnchorPane();
		Label fileLabel = new Label("Location");
		fileLabel.setPrefWidth(60.0);

		TextField filenameTextField = new TextField();
		filenameTextField.setDisable(true);
		filenameTextField.setText(csvFile.getAbsolutePath());

		Button editSaveButton = new Button("Edit CSV");
		editSaveButton.setOnAction(null); //TODO add edit logic
		editSaveButton.setPrefWidth(80.0);

		bottomControl.getChildren()
			.addAll(fileLabel, filenameTextField, editSaveButton);

		AnchorPane.setTopAnchor(fileLabel, 1.0);
		AnchorPane.setBottomAnchor(fileLabel, 1.0);
		AnchorPane.setLeftAnchor(fileLabel, 1.0);

		AnchorPane.setTopAnchor(filenameTextField, 1.0);
		AnchorPane.setRightAnchor(filenameTextField, 85.0);
		AnchorPane.setBottomAnchor(filenameTextField, 1.0);
		AnchorPane.setLeftAnchor(filenameTextField, 65.0);

		AnchorPane.setTopAnchor(editSaveButton, 1.0);
		AnchorPane.setRightAnchor(editSaveButton, 1.0);
		AnchorPane.setBottomAnchor(editSaveButton, 1.0);

		return bottomControl;
	}

	private TableView createCsvTable(File csvFile) throws IOException {
		CsvData data = CsvFileUtil.readCsv(csvFile);

		TableView csvTable = new TableView();

		List headers = csvTable.getColumns();
		data.getHeaders().stream()
			.map(TableColumn::new)
			.peek(this::setupCellColumn)
			.forEach(headers::add);
		List tableItems = csvTable.getItems();
		tableItems.addAll(data.getValues());
		return csvTable;
	}

	private void setupCellColumn(TableColumn column) {
		MapValueFactory mapValueFactory = new MapValueFactory(column.getText());
		column.setCellValueFactory(mapValueFactory);
		column.setMinWidth(100.0);
	}

	private void reportIOException(IOException e, File f) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("CSV Viewer Error!");
		alert.setHeaderText("Failed to Open File!");
		alert.setContentText(String.format("Failed to open file: %s\nFile might be corrupted or not a valid CSV.", f.getName()));

		e.printStackTrace(System.err);
		alert.showAndWait();
	}

	private void handleRootPaneDragOver(DragEvent event) {
		boolean dragFromOutside = event.getGestureSource() == null;
		boolean draggingFiles = dragFromOutside && event.getDragboard().hasFiles();
		if (dragFromOutside && draggingFiles) {
			event.acceptTransferModes(TransferMode.ANY);
		}
		event.consume();
	}

	private void handleRootPaneDragDropped(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if (dragboard.hasFiles()) {
			dragboard.getFiles()
				.stream()
				.forEach(this::openCsvTab);
		}
		event.setDropCompleted(true);
		event.consume();
	}
}
