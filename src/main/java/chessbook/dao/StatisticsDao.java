package chessbook.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import chessbook.lichess.model.LiChessUser;
import chessbook.lichess.model.UserGameStatistics;

public class StatisticsDao extends GenericDao {

	public UserGameStatistics userGameStatistics(LiChessUser user){
		
		String player = user.getId();
		String hql = 	"select sum(case when players.white.userId = :player or players.black.userId = :player then 1 else 0 end) as games,"+
				"sum(case when players.white.userId = :player then 1 else 0 end) as gamesWhite,"+
				"sum(case when players.black.userId = :player then 1 else 0 end) as gamesBlack,"+
				"sum(case when players.white.userId = :player and winner = 'white' then 1 else 0 end) as gamesWonWhite,"+
				"sum(case when players.black.userId = :player and winner = 'black' then 1 else 0 end) as gamesWonBlack,"+
				"sum(case when players.white.userId = :player and winner = 'black' then 1 else 0 end) as gamesLostWhite,"+
				"sum(case when players.black.userId = :player and winner = 'white' then 1 else 0 end) as gamesLostBlack,"+
				"sum(case when players.white.userId = :player and winner is null then 1 else 0 end) as gamesDrawnWhite,"+
				"sum(case when players.black.userId = :player and winner is null then 1 else 0 end) as gamesDrawnBlack from LiChessGame "+
				"where variant  = 'standard'";
		Session currentSession = sessionFactory.getCurrentSession(); 
	 	currentSession.beginTransaction();
	 	
		UserGameStatistics stats = 	(UserGameStatistics) currentSession.createQuery(hql).setParameter("player", player).
									setResultTransformer(Transformers.aliasToBean(UserGameStatistics.class)).uniqueResult();
		currentSession.getTransaction().commit();
		stats.setGamesWon(stats.getGamesWonWhite()+stats.getGamesWonBlack());
		stats.setGamesLost(stats.getGamesLostWhite()+stats.getGamesLostBlack());
		stats.setGamesDrawn(stats.getGamesDrawnWhite()+stats.getGamesDrawnBlack());
		//calculate percentages. 
		stats.setGamesWhitePercent(100 * stats.getGamesWhite().floatValue()/stats.getGames().floatValue());
		stats.setGamesBlackPercent(100 * stats.getGamesBlack().floatValue()/stats.getGames().floatValue());
		
		stats.setGamesWonPercent(100 * stats.getGamesWon().floatValue()/stats.getGames().floatValue());
		stats.setGamesWonWhitePercent(100 * stats.getGamesWonWhite().floatValue()/stats.getGamesWhite().floatValue());
		stats.setGamesWonBlackPercent(100 * stats.getGamesWonBlack().floatValue()/stats.getGamesBlack().floatValue());
		
		stats.setGamesLostPercent(100 * stats.getGamesLost().floatValue()/stats.getGames().floatValue());
		stats.setGamesLostWhitePercent(100 * stats.getGamesLostWhite().floatValue()/stats.getGamesWhite().floatValue());
		stats.setGamesLostBlackPercent(100 * stats.getGamesLostBlack().floatValue()/stats.getGamesBlack().floatValue());
		
		stats.setGamesDrawnPercent(100 * stats.getGamesDrawn().floatValue()/stats.getGames().floatValue());
		stats.setGamesDrawnWhitePercent(100 * stats.getGamesDrawnWhite().floatValue()/stats.getGamesWhite().floatValue());
		stats.setGamesDrawnBlackPercent(100 * stats.getGamesDrawnBlack().floatValue()/stats.getGamesBlack().floatValue());
		
		return stats;
	}
	public UserGameStatistics userGameStatisticsForGameSpeed(LiChessUser user,String speed){
		String player = user.getId();
		String hql = 	"select sum(case when players.white.userId = :player or players.black.userId = :player then 1 else 0 end) as games,"+
				"sum(case when players.white.userId = :player then 1 else 0 end) as gamesWhite,"+
				"sum(case when players.black.userId = :player then 1 else 0 end) as gamesBlack,"+
				"sum(case when players.white.userId = :player and winner = 'white' then 1 else 0 end) as gamesWonWhite,"+
				"sum(case when players.black.userId = :player and winner = 'black' then 1 else 0 end) as gamesWonBlack,"+
				"sum(case when players.white.userId = :player and winner = 'black' then 1 else 0 end) as gamesLostWhite,"+
				"sum(case when players.black.userId = :player and winner = 'white' then 1 else 0 end) as gamesLostBlack,"+
				"sum(case when players.white.userId = :player and winner is null then 1 else 0 end) as gamesDrawnWhite,"+
				"sum(case when players.black.userId = :player and winner is null then 1 else 0 end) as gamesDrawnBlack from LiChessGame "+
				"where variant  = 'standard' and speed = :speed";
		Session currentSession = sessionFactory.getCurrentSession(); 
	 	currentSession.beginTransaction();
	 	
		UserGameStatistics stats = 	(UserGameStatistics) currentSession.createQuery(hql).setParameter("player", player).setParameter("speed", speed).
									setResultTransformer(Transformers.aliasToBean(UserGameStatistics.class)).uniqueResult();
		currentSession.getTransaction().commit();
		stats.setGamesWon(stats.getGamesWonWhite()+stats.getGamesWonBlack());
		stats.setGamesLost(stats.getGamesLostWhite()+stats.getGamesLostBlack());
		stats.setGamesDrawn(stats.getGamesDrawnWhite()+stats.getGamesDrawnBlack());
		//calculate percentages. 
		stats.setGamesWhitePercent(100 * stats.getGamesWhite().floatValue()/stats.getGames().floatValue());
		stats.setGamesBlackPercent(100 * stats.getGamesBlack().floatValue()/stats.getGames().floatValue());
		
		stats.setGamesWonPercent(100 * stats.getGamesWon().floatValue()/stats.getGames().floatValue());
		stats.setGamesWonWhitePercent(100 * stats.getGamesWonWhite().floatValue()/stats.getGamesWhite().floatValue());
		stats.setGamesWonBlackPercent(100 * stats.getGamesWonBlack().floatValue()/stats.getGamesBlack().floatValue());
		
		stats.setGamesLostPercent(100 * stats.getGamesLost().floatValue()/stats.getGames().floatValue());
		stats.setGamesLostWhitePercent(100 * stats.getGamesLostWhite().floatValue()/stats.getGamesWhite().floatValue());
		stats.setGamesLostBlackPercent(100 * stats.getGamesLostBlack().floatValue()/stats.getGamesBlack().floatValue());
		
		stats.setGamesDrawnPercent(100 * stats.getGamesDrawn().floatValue()/stats.getGames().floatValue());
		stats.setGamesDrawnWhitePercent(100 * stats.getGamesDrawnWhite().floatValue()/stats.getGamesWhite().floatValue());
		stats.setGamesDrawnBlackPercent(100 * stats.getGamesDrawnBlack().floatValue()/stats.getGamesBlack().floatValue());
		
		return stats;
	}
}
