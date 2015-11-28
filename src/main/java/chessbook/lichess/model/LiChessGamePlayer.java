package chessbook.lichess.model;

public class LiChessGamePlayer {
	private String userId;
	private String name;
	private int rating;
	private AnalysisSumary analysis;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public AnalysisSumary getAnalysis() {
		return analysis;
	}
	public void setAnalysis(AnalysisSumary analysis) {
		this.analysis = analysis;
	}

}
