package chessbook.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class StatisticsViewController {
	@FXML
	private Label lblUsers;
	@FXML
	private Label lblGames;
	@FXML
	private Label lblPositions;
	@FXML
	private BorderPane bdrPane;
	public Label getLblUsers() {
		return lblUsers;
	}
	public Label getLblGames() {
		return lblGames;
	}
	public Label getLblPositions() {
		return lblPositions;
	}
	public BorderPane getBdrPane() {
		return bdrPane;
	}
	
}
