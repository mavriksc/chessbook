package chessbook;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import chessbook.dao.UserDao;
import chessbook.lichess.model.GamePerformanceStatistics;
import chessbook.lichess.model.LiChessUser;
import chessbook.service.LiChessService;
import chessbook.view.MainFrame;
import chesspresso.game.Game;

public class ChessBook {
	//Currently set up according to below site. good stuff
	//http://www.cs.wcupa.edu/rkline/java/mvc-design.html
	//will call lichess and get user when entered. 
	//working on updating display with user info. 
	//interface may not be the best way to enumerate game types.
	//maybe if creating a user view pass in list of game types.
	//will be usefull when have view for both players. 
	//TODO
	//then can start downloading games. 
	//once downloading games need to start persisting user and game info to DB
	// 
	
	//VIEW
	private final MainFrame frame = new MainFrame();
	//DATA MODEL
	private String username;
	private LiChessUser currentUser;
	private Game currGame;
	//DATABASE STUFF
	private UserDao userDao;
	
	//HIBERNATE DATA
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
	
	
	public ChessBook(){
		frame.getMntmSetUser().addActionListener(e -> {
			do {
			username = (String)JOptionPane.showInputDialog(frame, "Enter your username:");
			currentUser=LiChessService.getUser(username);
			
			} while (currentUser == null);
			//TODO display dialog with userinfo to confirm.
			//TODO then save to preferences.
			//TODO  Maybe create new user view. or have set user on userview create new combobox???
			//frame.getLblUsername().setText(currentUser.getUsername());
			//save to DB
			userDao.save(currentUser);
			frame.getMntmLoadGames().setEnabled(true);
		});
		frame.getMntmLoadGames().addActionListener(e -> LiChessService.getUsersGames(username));
		
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
	
	public static void main(String[] args) {
		
		final ChessBook cb = new ChessBook();		
		sessionFactory = createSessionFactory();
		cb.userDao = new UserDao();
		cb.userDao.setSessionFactory(sessionFactory);
		
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                cb.frame.setVisible(true);
            }
        });
				
    }
	
	
	

}


