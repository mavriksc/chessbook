package chessbook.dao;

import org.hibernate.SessionFactory;

public class GenericDao {
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
