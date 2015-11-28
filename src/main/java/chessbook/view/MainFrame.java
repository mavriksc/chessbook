package chessbook.view;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;///////

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

//TODO watch the video again and layout UI elements like that
//something like create add to panel add listeners....
//I've got a pretty good idea how this works now and can go ahead but leave this note.

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -8730478507930597322L;
	//.UI ELEMENTS
	private JPanel contentPane;
	private JMenuItem mntmLoadGames;
	private JMenuItem mntmSetUser;
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		setTitle("ChessBook");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 732, 559);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSetUser = new JMenuItem("Set User");
		mnFile.add(mntmSetUser);
		
		mntmLoadGames = new JMenuItem("Load Games");
		mntmLoadGames.setEnabled(false);
		mnFile.add(mntmLoadGames);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	public JMenuItem getMntmLoadGames() {
		return mntmLoadGames;
	}
	
	public JMenuItem getMntmSetUser() {
		return mntmSetUser;
	}
}
