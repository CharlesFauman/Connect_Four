package Game.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class GUIView extends View{
	
	private JFrame frame;
	private Board board;
	private JLabel notification;
	
	private int player_turn;
	private String first_player_name, second_player_name;
	
	public GUIView(String first_player_name, String second_player_name, int width, int height, int connect_number) {
		
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
		
		// name labels
		JLabel first_name = new JLabel();
		first_name.setText("Green: " + first_player_name);
		first_name.setFont(first_name.getFont().deriveFont(25f));
		first_name.setPreferredSize(new Dimension(370, 40));
		frame.add(first_name);
		
		JLabel second_name = new JLabel();
		second_name.setText("Blue: " + second_player_name);
		second_name.setFont(second_name.getFont().deriveFont(25f));
		second_name.setPreferredSize(new Dimension(370, 40));
		frame.add(second_name);
		
		// button labels
		JButton backwards_button = new JButton("<-- ");
		backwards_button.setFont(backwards_button.getFont().deriveFont(15f));
		backwards_button.setPreferredSize(new Dimension(60, 40));
		backwards_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				board.Backwards();
			}
			
		});
		frame.add(backwards_button);
		
		JButton forwards_button = new JButton(" -->");
		forwards_button.setFont(forwards_button.getFont().deriveFont(15f));
		forwards_button.setPreferredSize(new Dimension(60, 40));
		forwards_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				board.Forwards();
			}
			
		});
		frame.add(forwards_button);
		
		// Board
		board = new Board();
		frame.add(board);
		
		// notifications
		notification = new JLabel();
		notification.setFont(notification.getFont().deriveFont(25f));
		frame.add(notification);
		
		// Show the frame
		frame.setVisible(true);
	}
	
	public GUIView(String first_player_name, String second_player_name) {
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
		// set the piece icon to that image
		board.PlayMove(player_turn, column, row);

		player_turn = -player_turn;
	}

	@Override
	public void NotifyIOException() {
		notification.setText("Uh Oh! Someone handeled IO incorrectly!");
		NotifyWin();
	}
}
