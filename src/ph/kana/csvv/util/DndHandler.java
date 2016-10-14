package ph.kana.csvv.util;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.function.Consumer;

public final class DndHandler {

	private DndHandler() {}

	private static DndHandler INSTANCE = null;

	private Consumer<File> openCsvTab;

	public static DndHandler getInstance(Consumer<File> openCsvTab) {
		if (null == INSTANCE) {
			INSTANCE = new DndHandler();
			INSTANCE.openCsvTab = openCsvTab;
		}
		return INSTANCE;
	}

	public void handleRootPaneDragOver(DragEvent event) {
		boolean dragFromOutside = event.getGestureSource() == null;
		boolean draggingFiles = dragFromOutside && event.getDragboard().hasFiles();
		if (dragFromOutside && draggingFiles) {
			event.acceptTransferModes(TransferMode.ANY);
		}
		event.consume();
	}

	public void handleRootPaneDragDropped(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if (dragboard.hasFiles()) {
			dragboard.getFiles()
				.stream()
				.forEach(openCsvTab);
		}
		event.setDropCompleted(true);
		event.consume();
	}
}
