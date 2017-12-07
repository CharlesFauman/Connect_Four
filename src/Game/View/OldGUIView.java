package Game.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class OldGUIView extends View{
	private JFrame frame;
	private JLabel connect_4_board;
	private JLabel[][] pieces;
	private int player_turn;
	private String first_player_name, second_player_name;
	private JLabel notification;
	
	public OldGUIView(String first_player_name, String second_player_name, int width, int height, int connect_number) {
		//set player names
		this.first_player_name = first_player_name;
		this.second_player_name = second_player_name;
		
		// set player turn
		player_turn = 1;
		
		// Create the frame.
		frame = new JFrame("Connect 4");
		
		// Size the frame.
		frame.setSize(920, 920);

		// What happens when the frame closes? End the application
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// let the components arrange by themselves where they should go
		frame.setLayout(new FlowLayout());

		// Create components and put them in the frame.
		JLabel first_name = new JLabel();
		first_name.setText("Green: " + first_player_name);
		first_name.setFont(first_name.getFont().deriveFont(25f));
		first_name.setPreferredSize(new Dimension(460, 40));
		frame.add(first_name);
		
		JLabel second_name = new JLabel();
		second_name.setText("Blue: " + second_player_name);
		second_name.setFont(second_name.getFont().deriveFont(25f));
		second_name.setPreferredSize(new Dimension(400, 40));
		frame.add(second_name);
		
		
		Border connect_4_border = BorderFactory.createLineBorder(Color.BLACK, 40);
		ImageIcon connect_4_board_image = new ImageIcon("src/Game/Resources/connect_4_board.png");
		connect_4_board = new JLabel(connect_4_board_image);
		connect_4_board.setLayout(new GridLayout(height, width, 20, 20));
		connect_4_board.setBorder(connect_4_border);
		frame.add(connect_4_board);
		pieces = new JLabel[width][height];
		for(int row = height-1; row >= 0; --row) {
			for(int col = 0; col < width; ++col) {
				pieces[col][row] = new JLabel();
				pieces[col][row].setPreferredSize(new Dimension(100, 100));
				connect_4_board.add(pieces[col][row]);
			}
		}
		notification = new JLabel();
		notification.setFont(notification.getFont().deriveFont(25f));
		frame.add(notification);
		
		// Show the frame
		frame.setVisible(true);
	}
	
	public OldGUIView(String first_player_name, String second_player_name) {
		this(first_player_name, second_player_name, 7, 6, 4);
	}

	@Override
	public void NotifyTimeout() {
		notification.setText("Current player Timed out!");
		NotifyWin();
	}

	@Override
	public void NotifyIllegalMove(int column) {
		notification.setText("attempt at an illegal move (" + (column+1) + ")!");
		NotifyWin();
	}

	@Override
	public void NotifyDrawn() {
		notification.setText("That's a draw!");
	}

	@Override
	public void NotifyWin() {
		String txt = notification.getText();
		txt += " That's a win for ";
		switch(-player_turn) {
		case 1: txt += "Green, " + first_player_name; break;
		case -1: txt +="Blue, " + second_player_name; break;
		}
		txt += "!";
		notification.setText(txt);
	}

	@Override
	public void PlayMove(int column, int row) {
		// find the piece image
		ImageIcon piece_image;
		if(player_turn == 1) piece_image = new ImageIcon("src/Game/Resources/X_puck.png");
		else piece_image = new ImageIcon("src/Game/Resources/O_puck.png");
		
		// set the piece icon to that image
		pieces[column][row].setIcon(piece_image);

		player_turn = -player_turn;
	}

	@Override
	public void NotifyIOException() {
		notification.setText("Uh Oh! Someone handeled IO incorrectly!");
		NotifyWin();
	}

}
