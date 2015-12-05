package chessbook.lichess.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Players {
	@AttributeOverrides({
		@AttributeOverride (name = "userId", column = @Column(name="WHITE_USER_ID")),
		@AttributeOverride (name = "name", column = @Column(name="WHITE_NAME")),
		@AttributeOverride (name = "rating", column = @Column(name="WHITE_RATING")),
		@AttributeOverride (name = "analysis", column = @Column(name="WHITE_ANALYSIS")),
		@AttributeOverride (name = "analysis.blunder", column = @Column(name="WHITE_BLUNDERS")),
		@AttributeOverride (name = "analysis.inaccuracy", column = @Column(name="WHITE_INACCURACIES")),
		@AttributeOverride (name = "analysis.mistake", column = @Column(name="WHITE_MISTAKES"))})
	LiChessGamePlayer white;
	@AttributeOverrides({
		@AttributeOverride (name = "userId", column = @Column(name="BLACK_USER_ID")),
		@AttributeOverride (name = "name", column = @Column(name="BLACK_NAME")),
		@AttributeOverride (name = "rating", column = @Column(name="BLACK_RATING")),
		@AttributeOverride (name = "analysis", column = @Column(name="BLACK_ANALYSIS")),
		@AttributeOverride (name = "analysis.blunder", column = @Column(name="BLACK_BLUNDERS")),
		@AttributeOverride (name = "analysis.inaccuracy", column = @Column(name="BLACK_INACCURACIES")),
		@AttributeOverride (name = "analysis.mistake", column = @Column(name="BLACK_MISTAKES"))})
	LiChessGamePlayer black;

}
