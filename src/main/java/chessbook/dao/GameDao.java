package chessbook.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import chessbook.lichess.model.LiChessGame;

public class GameDao extends GenericDao {
	public void save(LiChessGame game){
		Session currentSession = sessionFactory.getCurrentSession(); 
	 	Transaction trans= currentSession.beginTransaction();
	 	currentSession.saveOrUpdate(game);
	    trans.commit();
	}

}
