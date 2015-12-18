package chessbook.view;


import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class ChessBookViewController {
	@FXML
	private Label lblUsername;
	@FXML
	private ChoiceBox<String> cbxGameType;
	@FXML
	private MenuItem mnuItSetUser;
	@FXML
	private MenuItem mnuItInitialLoadOfGames;
	@FXML
	private MenuItem mnuItLoadGames;
	@FXML
	private Label lblNumGames;
	@FXML
	private Label lblRating;
	@FXML
	private Label lblRange;
	@FXML
	private Label lblProg;
	@FXML
	private TabPane tabPane;
	@FXML
	private BorderPane borderPane;
	
	public void setGameTypes(Set<String> gameTypes) {
		cbxGameType.getItems().clear();
		cbxGameType.getItems().addAll(gameTypes);
	}
	public void setLblUsername(String text){
		lblUsername.setText(text);
	}
	public void setGamePerformanceLabels(Long numGames, Integer score, Long range, Integer prog){
		lblProg.setText(prog.toString());
		lblNumGames.setText(numGames.toString());
		lblRange.setText(range.toString());
		lblRating.setText(score.toString());
	}
	public Label getLblUsername() {
		return lblUsername;
	}
	
	public ChoiceBox<String> getCbxGameType() {
		return cbxGameType;
	}
	public MenuItem getMnuItSetUser() {
		return mnuItSetUser;
	}
	public MenuItem getMnuItInitialLoadOfGames() {
		return mnuItInitialLoadOfGames;
	}
	public MenuItem getMnuItLoadGames() {
		return mnuItLoadGames;
	}
	
	public Label getLblNumGames() {
		return lblNumGames;
	}
	public Label getLblRating() {
		return lblRating;
	}
	public Label getLblRange() {
		return lblRange;
	}
	public Label getLblProg() {
		return lblProg;
	}
	public TabPane getTabPane() {
		return tabPane;
	}
	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}
	public BorderPane getBorderPane() {
		return borderPane;
	}
	public void setBorderPane(BorderPane borderPane) {
		this.borderPane = borderPane;
	}
	
	
	

}
