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

public class GUIView extends View{
	private JFrame frame;
	private JLabel connect_4_board;
	private JLabel[][] pieces;
	private JLabel notification;
	private int player_turn;
	
	private int last_x, last_y;
	
	public GUIView(int width, int height, int connect_number) {
		// set player turn
		player_turn = 1;
		
		// Create the frame.
		frame = new JFrame("Connect 4");
		
		// Size the frame.
		frame.setSize(920, 880);

		// What happens when the frame closes? End the application
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// let the components arrange by themselves where they should go
		frame.setLayout(new FlowLayout());

		// Create components and put them in the frame.
		Border connect_4_border = BorderFactory.createLineBorder(Color.BLACK, 40);
		
		ImageIcon connect_4_board_image = new ImageIcon("connect_4_board.png");
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
	
	public GUIView() {
		this(7, 6, 4);
	}

	@Override
	public void NotifyTimeout() {
		notification.setText("Current player Timed out!");
		NotifyWin();
	}

	@Override
	public void NotifyIllegalMove(int column) {
		notification.setText("Current player attempted an illegal move! ");
		NotifyWin();
	}

	@Override
	public void NotifyDrawn() {
		notification.setText("That's a draw!");
	}

	@Override
	public void NotifyWin() {
		String txt = notification.getText();
		txt += " That's a win for player ";
		switch(-player_turn) {
		case 1: txt += "Green"; break;
		case -1: txt +="Blue"; break;
		}
		txt += "!";
		notification.setText(txt);
	}

	@Override
	public void PlayMove(int column, int row) {
		// find the piece image
		ImageIcon piece_image;
		if(player_turn == 1) piece_image = new ImageIcon("X_puck.png");
		else piece_image = new ImageIcon("O_puck.png");
		
		// set the piece icon to that image
		pieces[column][row].setIcon(piece_image);
		
		//pieces[column][row].setBorder(new Border);
		//pieces[last_x][last_y].setForeground(Color.CYAN);
		
		last_x = column;
		last_y = row;

		player_turn = -player_turn;
	}

	@Override
	public void NotifyIOException() {
		notification.setText("Uh Oh! Someone handeled IO incorrectly!");
		NotifyWin();
	}

}
