package ph.kana.csvv.controller;

import javafx.application.Application;
import javafx.stage.Window;

abstract class AbstractController {
	Window window;
	Application application;

	public void setApplication(Application application) {
		this.application = application;
	}

	public void setWindow(Window window) {
		this.window = window;
	}
}
