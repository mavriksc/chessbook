package chessbook.lichess.model;

import java.net.URL;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

import com.google.gson.annotations.SerializedName;


@Entity
public class LiChessUser {
	@Id
	private String id;
	private String username;
	private String title;
	private URL	url;
	private boolean online;
	private String language;
	
	private LiChessUserProfile profile;
	
	@SerializedName("perfs")
	@ElementCollection(fetch=FetchType.EAGER)
	@MapKeyColumn(name="game_type")
	private Map<String, GamePerformanceStatistics> performance;
	
	
	
	public LiChessUser() {
		super();
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	
	public Map<String, GamePerformanceStatistics> getPerformance() {
		return performance;
	}
	public void setPerformance(Map<String, GamePerformanceStatistics> performance) {
		this.performance = performance;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public LiChessUserProfile getProfile() {
		return profile;
	}
	public void setProfile(LiChessUserProfile profile) {
		this.profile = profile;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String favoriteGameType(){
		
		String mostGamesPlayedFor = null;
		Long gamesPlayed = 0L;
		for(Map.Entry<String, GamePerformanceStatistics> entry : performance.entrySet()){
			if(entry.getValue().getNumberOfGames() >gamesPlayed){
				gamesPlayed = entry.getValue().getNumberOfGames();
				mostGamesPlayedFor = entry.getKey();
			}
		}
		return mostGamesPlayedFor;
	}
	
	public Long numberOfRatedGames(){
		Long count = new Long(0);
		for(Map.Entry<String, GamePerformanceStatistics> entry : performance.entrySet()){
			count+=entry.getValue().getNumberOfGames();
		}
		return count;
	}
	
	

}
