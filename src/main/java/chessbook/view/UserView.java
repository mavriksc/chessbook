package chessbook.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JComboBox;

public class UserView extends JPanel {
	
	private JLabel lblUsername;
	private JComboBox comboBox;
	private JLabel lblGamesPlayed;
	private JLabel lblScoreValue;
	private JLabel lblTrendvalue;
	/**
	 * Create the panel.
	 */
	public UserView() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("135px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("133px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblUname = new JLabel("Username:");
		add(lblUname, "1, 2, right, top");
		
		lblUsername = new JLabel("");
		add(lblUsername, "3, 2, left, top");
		
		JLabel lblGame = new JLabel("Game:");
		add(lblGame, "1, 4, right, center");
		
		comboBox = new JComboBox();
		add(comboBox, "3, 4, fill, default");
		
		JLabel lblPlayed = new JLabel("Played:");
		add(lblPlayed, "1, 6, right, top");
		
		lblGamesPlayed = new JLabel("");
		add(lblGamesPlayed, "3, 6, left, default");
		
		JLabel lblScore = new JLabel("Score:");
		add(lblScore, "1, 8, right, default");
		
		lblScoreValue = new JLabel("");
		add(lblScoreValue, "3, 8, left, default");
		
		JLabel lblTrend = new JLabel("Trend:");
		add(lblTrend, "1, 10, right, default");
		
		lblTrendvalue = new JLabel("");
		add(lblTrendvalue, "3, 10, left, default");

	}
	public JLabel getLblUsername() {
		return lblUsername;
	}
	public JComboBox getComboBox() {
		return comboBox;
	}
	public JLabel getLblGamesPlayed() {
		return lblGamesPlayed;
	}
	public JLabel getLblScoreValue() {
		return lblScoreValue;
	}
	public JLabel getLblTrendvalue() {
		return lblTrendvalue;
	}
	

}
