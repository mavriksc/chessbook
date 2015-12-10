package chessbook.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import chessbook.lichess.model.LiChessGame;

public class GameDao extends GenericDao {
	
	public void save(LiChessGame game){
		Session currentSession = sessionFactory.getCurrentSession(); 
	 	currentSession.beginTransaction();
	 	currentSession.saveOrUpdate(game);
	    currentSession.getTransaction().commit();
	}
	//	count games  TODO test
	public Long countGames(){
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.beginTransaction();
		Long answer= (Long) currentSession.createCriteria(LiChessGame.class).
						setProjection(Projections.rowCount()).uniqueResult();
		currentSession.getTransaction().commit();
		return answer;
	}
	//count user games TODO test
	public Long countGamesForUser(String username){
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.beginTransaction();
		Long answer= (Long) currentSession.createCriteria(LiChessGame.class)
						.add(Restrictions.or(Restrictions.eq("players.white.userId", username), 
						Restrictions.eq("players.black.userId",username)))
						.setProjection(Projections.rowCount()).uniqueResult();
		currentSession.getTransaction().commit();
		return answer;
	}
	//TODO MAYBE count stats for user. 
	//		counts all games, wins losses, draws, white wins/losses/draws, black wins/losses/draws 
	//TODO	count wins of user
	//TODO	count losses of user
	//TODO 
	//TODO	
}

