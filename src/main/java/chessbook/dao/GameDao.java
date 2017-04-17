package chessbook.dao;

import java.util.List;

import org.hibernate.Session;
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
	
	@SuppressWarnings("unchecked")
	public List<LiChessGame> gamesForUser(String username){
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.beginTransaction();
		List<LiChessGame> games =  currentSession.createCriteria(LiChessGame.class)
						.add(Restrictions.or(Restrictions.eq("players.white.userId", username), 
						Restrictions.eq("players.black.userId",username))).list();
		currentSession.getTransaction().commit();
		return games;
	}
	
	@SuppressWarnings("unchecked")
	public List<LiChessGame> gamesForUser(String username,String variant){
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.beginTransaction();
		List<LiChessGame> games =  currentSession.createCriteria(LiChessGame.class)
						.add(Restrictions.or(Restrictions.eq("players.white.userId", username), 
						Restrictions.eq("players.black.userId",username))).add(Restrictions.eq("variant", variant)).list();
		currentSession.getTransaction().commit();
		return games;
	}
	
	@SuppressWarnings("unchecked")
	public List<LiChessGame> gamesForUser(String username,String variant,String speed){
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.beginTransaction();
		List<LiChessGame> games =  currentSession.createCriteria(LiChessGame.class)
						.add(Restrictions.or(Restrictions.eq("players.white.userId", username), 
						Restrictions.eq("players.black.userId",username))).add(Restrictions.eq("variant", variant))
						.add(Restrictions.eq("speed", speed)).list();
		currentSession.getTransaction().commit();
		return games;
	}
	

}

