package chessbook.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import chessbook.lichess.model.LiChessUser;

public class UserDao extends GenericDao{
	 
	 public void save(LiChessUser user) {
		 if (user.getProfile().getBio().length()>254){
			 user.getProfile().setBio(user.getProfile().getBio().substring(0, 254));
			 System.out.println("Trimmed bio to 255.");
		 }
		 	Session currentSession = sessionFactory.getCurrentSession(); 
		 	currentSession.beginTransaction();
		    currentSession.saveOrUpdate(user);
		    currentSession.getTransaction().commit();
		 
	 }
	 public LiChessUser getByUsername(String username){
		 Session currentSession = sessionFactory.getCurrentSession();
		 currentSession.beginTransaction();
		 LiChessUser user = (LiChessUser) currentSession.createCriteria(LiChessUser.class).
				 add(Restrictions.eq("username", username)).
				 uniqueResult();
		 user.getPerformance();
		 currentSession.getTransaction().commit();
		 return user;
	 }

	

}
