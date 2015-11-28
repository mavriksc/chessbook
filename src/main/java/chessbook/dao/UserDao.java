package chessbook.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import chessbook.lichess.model.LiChessUser;

public class UserDao {
	 SessionFactory sessionFactory;
	 
	 public void save(LiChessUser user) {
		 	Session currentSession = sessionFactory.getCurrentSession(); 
		 	Transaction trans= currentSession.beginTransaction();
		    currentSession.saveOrUpdate(user);
		    trans.commit();
		 
	 }
	 public LiChessUser getByUsername(String username){
		 Session currentSession = sessionFactory.getCurrentSession();
		 Transaction trans= currentSession.beginTransaction();
		 LiChessUser user =(LiChessUser)currentSession.get(LiChessUser.class,username);	
		 user.getPerformance();
		 trans.commit();
		 return user;
	 }

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

}
