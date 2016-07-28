package ph.kana.csvv.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ph.kana.csvv.model.CsvData;
import ph.kana.csvv.util.CsvFileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvViewerController {

	@FXML private AnchorPane rootPane;
	@FXML private TabPane csvTableTabPane;

	@FXML
	public void openCsvMenuClick() {
		FileChooser fileChooser = createFileChooser();
		File csvFile = fileChooser.showOpenDialog(fetchWindow());

		if (csvFile != null) {
			openCsvTab(csvFile);
		}
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

	private Window fetchWindow() {
		return rootPane.getScene().getWindow();
	}

	private void openCsvTab(File csvFile) {
		List<Tab> tabs = csvTableTabPane.getTabs();
		Tab tab = createCsvTab(csvFile);

		if (tab != null) {
			tabs.add(tab);
			csvTableTabPane.getSelectionModel()
				.select(tab);
		}
	}

	private Tab createCsvTab(File csvFile) {
		try {
			Tab tab = new Tab();
			tab.setText(csvFile.getName());

			AnchorPane tabContentAnchorPane = new AnchorPane();
			TableView csvTable = createCsvTable(csvFile);

			tabContentAnchorPane.getChildren().add(csvTable);
			AnchorPane.setTopAnchor(csvTable, 0.0);
			AnchorPane.setRightAnchor(csvTable, 0.0);
			AnchorPane.setBottomAnchor(csvTable, 0.0);
			AnchorPane.setLeftAnchor(csvTable, 0.0);
			tab.setContent(tabContentAnchorPane);

			return tab;
		} catch (IOException e) {
			reportError(e);
			return null;
		}
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

	private void reportError(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("CSV Viewer Error!");
		alert.setHeaderText(e.getClass().getSimpleName());
		alert.setContentText("Failed to open file!\nCSV format might be broken.");
		alert.showAndWait();
	}
}
