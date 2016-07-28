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
import java.util.List;
import java.util.Map;

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
		FileChooser.ExtensionFilter csvExtensionFilter = new FileChooser.ExtensionFilter("CSV Files", "csv", "txt");

		fileChooser.setInitialDirectory(userHome);
		fileChooser.setSelectedExtensionFilter(csvExtensionFilter);
		fileChooser.setTitle("Open CSV File");
		return fileChooser;
	}

	private Window fetchWindow() {
		return rootPane.getScene().getWindow();
	}

	private void openCsvTab(File csvFile) {
		List<Tab> tabs = csvTableTabPane.getTabs();
		Tab tab = createCsvTab(csvFile);
		tabs.add(tab);

		csvTableTabPane.getSelectionModel()
			.select(tab);
	}

	private Tab createCsvTab(File csvFile) {
		Tab tab = new Tab();
		tab.setText(csvFile.getName());

		AnchorPane tabContentAnchorPane = new AnchorPane();
		ScrollPane csvTableScrollPane = new ScrollPane();
		tabContentAnchorPane.getChildren().add(csvTableScrollPane);
		AnchorPane.setTopAnchor(csvTableScrollPane, 0.0);
		AnchorPane.setRightAnchor(csvTableScrollPane, 0.0);
		AnchorPane.setBottomAnchor(csvTableScrollPane, 0.0);
		AnchorPane.setLeftAnchor(csvTableScrollPane, 0.0);
		tab.setContent(tabContentAnchorPane);

		csvTableScrollPane.setContent(createCsvTable(csvFile));
		return tab;
	}

	private TableView createCsvTable(File csvFile) {
		CsvData data = CsvFileUtil.readCsv(csvFile);

		TableView csvTable = new TableView();

		List headers = csvTable.getColumns();
		data.getHeaders().stream()
			.map(TableColumn<List<String>, String>::new)
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
}
