package chessbook;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.Preferences;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import chessbook.dao.GameDao;
import chessbook.dao.StatisticsDao;
import chessbook.dao.UserDao;
import chessbook.lichess.model.GameList;
import chessbook.lichess.model.GamePerformanceStatistics;
import chessbook.lichess.model.LiChessGame;
import chessbook.lichess.model.LiChessUser;
import chessbook.lichess.model.UserGameStatistics;
import chessbook.service.LiChessService;
import chessbook.view.ChessBookViewController;
import chessbook.view.StatisticsViewController;
import chesspresso.game.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
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
	
	//VIEWS
	private ChessBookViewController viewController;
	private StatisticsViewController statViewController;
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
	private static StatisticsDao statisticsDao;
	
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
		statisticsDao = new StatisticsDao();
		statisticsDao.setSessionFactory(sessionFactory);
		
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
	
	private Tab newstatTab(){
		FXMLLoader statfxmlLoader= new FXMLLoader(getClass().getResource("view/StatisticsView.fxml"));
		Pane statisticsPane;
		try {
			statisticsPane = statfxmlLoader.load();
			statViewController = (StatisticsViewController) statfxmlLoader.getController();
			ScrollPane s1 = new ScrollPane();
			s1.setContent(userStatsView(primaryUser));
			
			statViewController.getBdrPane().setCenter(s1);
			Tab statTab= new Tab("Statistics",statisticsPane);
			
			return statTab;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	private GridPane userStatsView(LiChessUser user) {
		List<String> statsToCalc = gameTypesToGetStatisticsFor(user);
		Map<String, UserGameStatistics> userStats = calculateUserGameStatistics(user, statsToCalc);
		//TODO make strings for setting stst labels;
		String countFormat = " %d (%d / %d) ";
		String percentFormat = " %.0f%% (%.0f%% / %.0f%%) ";
		
		String  games = String.format(countFormat,userStats.get("total").getGames(),
				userStats.get("total").getGamesWhite(),userStats.get("total").getGamesBlack() );
		String wins = String.format(countFormat,userStats.get("total").getGamesWon(),
				userStats.get("total").getGamesWonWhite(),userStats.get("total").getGamesWonBlack());
		String losses = String.format(countFormat,userStats.get("total").getGamesLost(),
				userStats.get("total").getGamesLostWhite(),userStats.get("total").getGamesLostBlack());
		String draws = String.format(countFormat,userStats.get("total").getGamesDrawn(),
				userStats.get("total").getGamesDrawnWhite(),userStats.get("total").getGamesDrawnBlack());
		
		String  gamesPercent = String.format(percentFormat,userStats.get("total").getGamesPercent(),
				userStats.get("total").getGamesWhitePercent(),userStats.get("total").getGamesBlackPercent() );
		String winsPercent = String.format(percentFormat,userStats.get("total").getGamesWonPercent(),
				userStats.get("total").getGamesWonWhitePercent(),userStats.get("total").getGamesWonBlackPercent());
		
		String lossesPercent = String.format(percentFormat,userStats.get("total").getGamesLostPercent(),
				userStats.get("total").getGamesLostWhitePercent(),userStats.get("total").getGamesLostBlackPercent());
		String drawsPercent = String.format(percentFormat,userStats.get("total").getGamesDrawnPercent(),
				userStats.get("total").getGamesDrawnWhitePercent(),userStats.get("total").getGamesDrawnBlackPercent());
		
		
		
		GridPane gridPane = new GridPane();
		//SIDE LABELS
		gridPane.add(new Label("Games(w/b)"), 0, 1);
		gridPane.add(new Label("Wins(w/b)"), 0, 2);
		gridPane.add(new Label("Losses(w/b)"), 0, 3);
		gridPane.add(new Label("Draws(w/b)"), 0, 4);
		//TOP LABELS
		gridPane.add(new Label("Total"), 1, 0);
		gridPane.add(new Label("%"), 2, 0);
		
		//TOTAL stats
		gridPane.add(new Label(games), 1, 1);
		gridPane.add(new Label(wins), 1, 2);
		gridPane.add(new Label(losses), 1, 3);
		gridPane.add(new Label(draws), 1, 4);
		//TOTAL %'s
		gridPane.add(new Label(gamesPercent), 2, 1);
		gridPane.add(new Label(winsPercent), 2, 2);
		gridPane.add(new Label(lossesPercent), 2, 3);
		gridPane.add(new Label(drawsPercent), 2, 4);
		
		//TODO stats calculated need to populate grid 
		for (int i = 0; i < statsToCalc.size()*2; i+=2) {
			//TOP LABELS of different speeds
			String currentSpeed = statsToCalc.get(i/2);
			gridPane.add(new Label(currentSpeed), 3+i, 0);
			gridPane.add(new Label("%"), 4+i, 0);
			
			games = String.format(countFormat,userStats.get(currentSpeed).getGames(),
					userStats.get(currentSpeed).getGamesWhite(),userStats.get(currentSpeed).getGamesBlack() );
			wins = String.format(countFormat,userStats.get(currentSpeed).getGamesWon(),
					userStats.get(currentSpeed).getGamesWonWhite(),userStats.get(currentSpeed).getGamesWonBlack());
			
			losses = String.format(countFormat,userStats.get(currentSpeed).getGamesLost(),
					userStats.get(currentSpeed).getGamesLostWhite(),userStats.get(currentSpeed).getGamesLostBlack());
			draws = String.format(countFormat,userStats.get(currentSpeed).getGamesDrawn(),
					userStats.get(currentSpeed).getGamesDrawnWhite(),userStats.get(currentSpeed).getGamesDrawnBlack());
			
			gamesPercent = String.format(percentFormat,userStats.get(currentSpeed).getGamesPercent(),
					userStats.get(currentSpeed).getGamesWhitePercent(),userStats.get(currentSpeed).getGamesBlackPercent() );
			winsPercent = String.format(percentFormat,userStats.get(currentSpeed).getGamesWonPercent(),
					userStats.get(currentSpeed).getGamesWonWhitePercent(),userStats.get(currentSpeed).getGamesWonBlackPercent());
			
			lossesPercent = String.format(percentFormat,userStats.get(currentSpeed).getGamesLostPercent(),
					userStats.get(currentSpeed).getGamesLostWhitePercent(),userStats.get(currentSpeed).getGamesLostBlackPercent());
			drawsPercent = String.format(percentFormat,userStats.get(currentSpeed).getGamesDrawnPercent(),
					userStats.get(currentSpeed).getGamesDrawnWhitePercent(),userStats.get(currentSpeed).getGamesDrawnBlackPercent());
			//TOTAL stats
			gridPane.add(new Label(games), 3+i, 1);
			gridPane.add(new Label(wins), 3+i, 2);
			gridPane.add(new Label(losses), 3+i, 3);
			gridPane.add(new Label(draws), 3+i, 4);
			//TOTAL %'s
			gridPane.add(new Label(gamesPercent), 4+i, 1);
			gridPane.add(new Label(winsPercent), 4+i, 2);
			gridPane.add(new Label(lossesPercent), 4+i, 3);
			gridPane.add(new Label(drawsPercent), 4+i, 4);
		}
		gridPane.setVgap(10);
		gridPane.setHgap(8);
		gridPane.setPadding(new Insets(10));
		return gridPane;
	}
	
	private List<String> gameTypesToGetStatisticsFor(LiChessUser user){
		//Intersects this list with the game types they have played. 
		List<String> statsToCalc = new ArrayList<String>();
		statsToCalc.add("bullet");
		statsToCalc.add("blitz");
		statsToCalc.add("classical");
		statsToCalc.add("correspondence");
		statsToCalc.retainAll(user.getPerformance().keySet());
		return statsToCalc;
	}
	
	private Map<String, UserGameStatistics> calculateUserGameStatistics(LiChessUser user,List<String> statsToCalc){
		Map<String, UserGameStatistics> statsMap= new HashMap<String, UserGameStatistics>();
		statsMap.put("total", statisticsDao.userGameStatistics(user));
		for (String speed:statsToCalc){
			UserGameStatistics stats = statisticsDao.userGameStatisticsForGameSpeed(user, speed);
			stats.setGamesPercent(100 * stats.getGames().floatValue()/statsMap.get("total").getGames().floatValue());
			
			statsMap.put(speed, stats);
		}
		return statsMap;
	}
	
	private void initView(Stage window) throws Exception{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/ChessBookView.fxml"));
		
		Pane root = fxmlLoader.load();
		viewController = (ChessBookViewController)fxmlLoader.getController();
		window.setTitle("ChessBook");
		window.setScene(new Scene(root, 1100, 800));
		//window.getScene().getStylesheets().add("view/darkTheme.css");
		TabPane tabPane = new TabPane(newstatTab());
		
		viewController.getBorderPane().setCenter(tabPane);
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
		//TODO other save state stuff. ?!?!?!?!
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
