package chessbook.lichess.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.gson.annotations.SerializedName;

@Embeddable
public class GamePerformanceStatistics  implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 148458133L;
	@SerializedName("games")
	private Long numberOfGames;
	@SerializedName("rating")
	private Integer score;// Glicko2 rating
	private Long rd;// Glicko2 rating deviation
	private Integer prog;// progress over the last twelve games
	
	
	public Long getNumberOfGames() {
		return numberOfGames;
	}
	public void setNumberOfGames(Long numberOfGames) {
		this.numberOfGames = numberOfGames;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Long getRd() {
		return rd;
	}
	public void setRd(Long rd) {
		this.rd = rd;
	}
	public Integer getProg() {
		return prog;
	}
	public void setProg(Integer prog) {
		this.prog = prog;
	}
	
}
