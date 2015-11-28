package chessbook;

import java.util.Map;
import java.util.Optional;
import java.util.prefs.Preferences;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import chessbook.dao.UserDao;
import chessbook.lichess.model.GamePerformanceStatistics;
import chessbook.lichess.model.LiChessUser;
import chessbook.service.LiChessService;
import chessbook.view.ChessBookViewController;
import chesspresso.game.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChessBookFX extends Application {
	
	private static final String CURRENT_USER = "username";
	private ChessBookViewController viewController;
	//DATA MODEL
	//TODO add username to preferences and current gametype
	// when the program loads set the ui for both
	private Preferences prefs;
	private String username;
	private LiChessUser currentUser;
	private String currGameType;
	private Game currGame;
	//DATABASE STUFF
	//DAOS
	private static UserDao userDao;
	
	//HIBERNATE DATA
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		setup();
		initView(primaryStage);	 	
	}
	
	private void setup(){
		prefs = Preferences.userNodeForPackage(ChessBookFX.class);
		sessionFactory = createSessionFactory();
		userDao = new UserDao();
		userDao.setSessionFactory(sessionFactory);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private void initView(Stage window) throws Exception{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/ChessBookView.fxml"));
		
		Pane root = fxmlLoader.load();
		viewController = (ChessBookViewController)fxmlLoader.getController();
		window.setTitle("ChessBook");
		window.setScene(new Scene(root, 800, 600));
		//TODO look up initializable
		//set values.
		username = prefs.get(CURRENT_USER, "");
		if (!username.isEmpty()){
			viewController.setLblUsername(username);
			currentUser = userDao.getByUsername(username);
			viewController.setGameTypes(FXCollections.observableSet(currentUser.getPerformance().keySet()));
			currGameType=favoriteGameType(currentUser.getPerformance());
			viewController.getCbxGameType().setValue(currGameType);
			setGameType(currGameType);
			viewController.getMnuItLoadGames().setDisable(false);
		}
		//set up listeners
		window.setOnCloseRequest(e->{e.consume();closeProgram();});
		viewController.getMnuItSetUser().setOnAction(e-> getUserinfo());
		viewController.getCbxGameType().getSelectionModel().
			selectedIndexProperty().addListener((observable, oldValue, newValue) -> setGameType(newValue));
		
		
		window.show();		
	}
	
	private void getUserinfo(){
		TextInputDialog dialog = new TextInputDialog(username = prefs.get(CURRENT_USER, ""));
		dialog.setTitle("Username");
		dialog.setContentText("Please enter your username:");
		do {
			Optional<String> result=dialog.showAndWait();
			result.ifPresent(uname->username=uname); 
			if(currentUser==null || currentUser.getUsername().compareTo(username) != 0){
				currentUser=LiChessService.getUser(username);
				userDao.save(currentUser);
				prefs.put(CURRENT_USER, currentUser.getUsername());
				viewController.setGameTypes(FXCollections.observableSet(currentUser.getPerformance().keySet()));
				viewController.getCbxGameType().setValue(favoriteGameType(currentUser.getPerformance()));
				viewController.setLblUsername(username);
			}
		} while (currentUser == null);
		//TODO display dialog with userinfo to confirm.
		
		viewController.getMnuItLoadGames().setDisable(false);
		
	}
	private void setGameType(String gameType){
		GamePerformanceStatistics gPS = currentUser.getPerformance().get(gameType);
		viewController.setGamePerformanceLabels(gPS.getNumberOfGames(), gPS.getScore(), gPS.getRd(), gPS.getProg());
	}
	
	private void setGameType(Number cbxIndex){
		String gameType = viewController.getCbxGameType().getItems().get(cbxIndex.intValue());
		GamePerformanceStatistics gPS = currentUser.getPerformance().get(gameType);
		viewController.setGamePerformanceLabels(gPS.getNumberOfGames(), gPS.getScore(), gPS.getRd(), gPS.getProg());
	}
	private String favoriteGameType(Map<String, GamePerformanceStatistics> stats){
		String mostGamesPlayedFor = null;
		Long gamesPlayed = 0L;
		for(Map.Entry<String, GamePerformanceStatistics> entry : stats.entrySet()){
			if(entry.getValue().getNumberOfGames() >gamesPlayed){
				gamesPlayed = entry.getValue().getNumberOfGames();
				mostGamesPlayedFor = entry.getKey();
			}
		}
		return mostGamesPlayedFor;
	}
	
	private void closeProgram(){
		
		Platform.exit();
        System.exit(0);
	}
	public static SessionFactory createSessionFactory(){
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(LiChessUser.class);
		configuration.configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
	            configuration.getProperties()).build();
		SessionFactory  sf = configuration.buildSessionFactory(serviceRegistry);
		return sf;
		
	}
}
