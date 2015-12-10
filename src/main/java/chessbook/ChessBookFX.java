package chessbook;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.Preferences;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import chessbook.dao.GameDao;
import chessbook.dao.UserDao;
import chessbook.lichess.model.GameList;
import chessbook.lichess.model.GamePerformanceStatistics;
import chessbook.lichess.model.LiChessGame;
import chessbook.lichess.model.LiChessUser;
import chessbook.service.LiChessService;
import chessbook.view.ChessBookViewController;
import chesspresso.game.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author seancarlisle
 *	The issue i think i had with updating uid was wrong
 *	all of my code is on the main thread currently
 *	when i start fetching in the background though...
 *	that is when i will need to use the runLater to udate the UI.
 */
public class ChessBookFX extends Application {
	
	private static final String PRIMARY_USER = "username.primary";
	private static final String CURRENT_USER = "username.current";
	private ChessBookViewController viewController;
	//DATA MODEL
	//TODO add username to preferences and current gametype
	// when the program loads set the ui for both
	private Preferences prefs;
	private String username;
	private LiChessUser primaryUser;
	private LiChessUser currentUser;
	private String currGameType;
	private Game currGame;
	//DATABASE STUFF
	//DAOS
	private static UserDao userDao;
	private static GameDao gameDao;
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
		gameDao = new GameDao();
		gameDao.setSessionFactory(sessionFactory);
		username = prefs.get(PRIMARY_USER, "");
		if(!username.isEmpty()){
			primaryUser = userDao.getByUsername(username);
		}
		username = prefs.get(CURRENT_USER, "");
		if (!username.isEmpty()){
			currentUser = userDao.getByUsername(username);
		}
		
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
		username = prefs.get(PRIMARY_USER, "");
		if (!username.isEmpty()){
			viewController.setLblUsername(username);
			currentUser = userDao.getByUsername(username);
			viewController.setGameTypes(FXCollections.observableSet(currentUser.getPerformance().keySet()));
			currGameType = currentUser.favoriteGameType();
			viewController.getCbxGameType().setValue(currGameType);
			setGameType(currGameType);
			viewController.getMnuItLoadGames().setDisable(false);
			viewController.getMnuItInitialLoadOfGames().setDisable(false);
		}
		//set up listeners
		window.setOnCloseRequest(e->{e.consume();closeProgram();});
		viewController.getMnuItSetUser().setOnAction(e-> getPrimaryUserInfo());
		viewController.getCbxGameType().getSelectionModel().
			selectedIndexProperty().addListener((observable, oldValue, newValue) -> setGameType(newValue));
		viewController.getMnuItLoadGames().setOnAction(e -> loadGames(username));
		viewController.getMnuItInitialLoadOfGames().setOnAction(e-> initialLoadOfGames());
		window.show();		
	}
	
	private void loadGames(String uname) {
		GameList games = LiChessService.getRecentRatedGamesForUser(primaryUser,null);
		for(LiChessGame game:games.getList()){
			gameDao.save(game);
		}
	}
	
	private void initialLoadOfGames(){
		GameList games = LiChessService.getAllRatedGamesForUser(primaryUser);
		for(LiChessGame game:games.getList()){
			gameDao.save(game);
		}
	}
	
	
	private void getPrimaryUserInfo(){
		primaryUser=dialogForUser("Enter your username.", prefs.get(PRIMARY_USER, ""));
		primaryUser.setPerformance(removeUnwantedGameTypes(primaryUser.getPerformance()));
		userDao.save(primaryUser);
		currentUser = primaryUser;
		prefs.put(PRIMARY_USER, currentUser.getUsername());
		viewController.setGameTypes(FXCollections.observableSet(currentUser.getPerformance().keySet()));
		viewController.getCbxGameType().setValue(currentUser.favoriteGameType());
		viewController.setLblUsername(currentUser.getUsername());
		viewController.getMnuItLoadGames().setDisable(false);
		viewController.getMnuItInitialLoadOfGames().setDisable(false);
		
	}
	
	
	private LiChessUser dialogForUser(String who,String defaultUsername){
		LiChessUser user = null;
		String contentText = who;
		String defUN = defaultUsername;
		boolean keepAsking = true;
		do {
			TextInputDialog dialog = new TextInputDialog(defUN);
			dialog.setTitle("Username");
			dialog.setContentText(contentText);
			Optional<String> result=dialog.showAndWait();
			if(result.isPresent()){
				
				user=LiChessService.getUser(result.get());
				if (user != null){
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm User");
					alert.setHeaderText("Is this the user you're looking for:");
					alert.setContentText("Username: " + user.getUsername() + 
										"\nFavorite Game: " + user.favoriteGameType() +
										"\nPlayed: " + user.getPerformance().get(user.favoriteGameType()).getNumberOfGames().toString() +
										"\nRating: " + user.getPerformance().get(user.favoriteGameType()).getScore().toString());
	
					Optional<ButtonType> result1 = alert.showAndWait();
					if (result1.get() == ButtonType.OK){
					    keepAsking = false;
					} else {
						defUN = result.get();
					}
				} else {
					contentText = "That user wasn't found.  " + who;
					defUN = "";
				}
				
			}else {
				keepAsking = false;
			}
		} while (keepAsking) ;
		return user;
		
	}
	
	
	
	
	private void getUserinfo(){
		currentUser=dialogForUser("Enter your username.", prefs.get(PRIMARY_USER, ""));
		currentUser.setPerformance(removeUnwantedGameTypes(primaryUser.getPerformance()));
		userDao.save(currentUser);
		prefs.put(PRIMARY_USER, currentUser.getUsername());
		viewController.setGameTypes(FXCollections.observableSet(currentUser.getPerformance().keySet()));
		viewController.getCbxGameType().setValue(currentUser.favoriteGameType());
		viewController.setLblUsername(currentUser.getUsername());
		viewController.getMnuItLoadGames().setDisable(false);
	}
	
	private void setGameType(String gameType){
		GamePerformanceStatistics gPS = currentUser.getPerformance().get(gameType);
		viewController.setGamePerformanceLabels(gPS.getNumberOfGames(), gPS.getScore(), gPS.getRd(), gPS.getProg());
	}
	
	private void setGameType(Number cbxIndex){
		if (cbxIndex.intValue() != -1){
			String gameType = viewController.getCbxGameType().getItems().get(cbxIndex.intValue());
			GamePerformanceStatistics gPS = currentUser.getPerformance().get(gameType);
			viewController.setGamePerformanceLabels(gPS.getNumberOfGames(), gPS.getScore(), gPS.getRd(), gPS.getProg());
		}
	}
	
	private Map<String, GamePerformanceStatistics> removeUnwantedGameTypes(Map<String, GamePerformanceStatistics> stats){
		//there will be no games for puzzle and opening. 
		//also don't keep game types with no games. 
		Map<String, GamePerformanceStatistics> newStats = new HashMap<String, GamePerformanceStatistics>();
		stats.remove("puzzle");
		stats.remove("opening");
		for(Map.Entry<String, GamePerformanceStatistics> entry : stats.entrySet()){
			if(entry.getValue().getNumberOfGames() >0){
				newStats.put(entry.getKey(), entry.getValue());
			}
		}
		return newStats;
	}
	

		
	private void closeProgram(){
		
		Platform.exit();
        System.exit(0);
	}
	
	public static SessionFactory createSessionFactory(){
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(LiChessUser.class);
		configuration.addAnnotatedClass(LiChessGame.class);
		configuration.configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
	            configuration.getProperties()).build();
		SessionFactory  sf = configuration.buildSessionFactory(serviceRegistry);
		return sf;
		
	}


}
