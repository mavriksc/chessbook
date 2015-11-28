package chessbook.lichess.model;

import java.net.URL;
import java.util.Date;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class LiChessGame {
	//
	//Stores a game response from 
	//http://en.lichess.org/api/game?
	//http://en.lichess.org/api/game/"id"
	//
	private String id;
	@SerializedName("initialFen")
	private String initialFEN;
	private String variant;
	private String speed;
	private Date timestamp;
	private boolean rated;
	private String status;
	private int turns;
	private URL url;
	private String winner;
	@SerializedName("fens")
	private String[] FENS;
	private Map<String, LiChessGamePlayer> players;
	private Map<String, String> opening;
	private String moves;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInitialFEN() {
		return initialFEN;
	}
	public void setInitialFEN(String initialFEN) {
		this.initialFEN = initialFEN;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isRated() {
		return rated;
	}
	public void setRated(boolean rated) {
		this.rated = rated;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String[] getFENS() {
		return FENS;
	}
	public void setFENS(String[] fENS) {
		FENS = fENS;
	}
	
	public Map<String, String> getOpening() {
		return opening;
	}
	public Map<String, LiChessGamePlayer> getPlayers() {
		return players;
	}
	public void setPlayers(Map<String, LiChessGamePlayer> players) {
		this.players = players;
	}
	public void setOpening(Map<String, String> opening) {
		this.opening = opening;
	}
	public String getMoves() {
		return moves;
	}
	public void setMoves(String moves) {
		this.moves = moves;
	}
	

}
