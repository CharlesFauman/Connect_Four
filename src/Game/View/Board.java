package Game.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Board extends JPanel{
	private Image connect_4_board_image;
	private Image X_puck_image;
	private Image O_puck_image;
	private int offset = 0;
	
	private ArrayList<Piece> pieces;
	
	public Board(){
		// load images
		X_puck_image = new ImageIcon("src/Game/Resources/X_puck.png").getImage();
		O_puck_image = new ImageIcon("src/Game/Resources/O_puck.png").getImage();
		connect_4_board_image = new ImageIcon("src/Game/Resources/connect_4_board.png").getImage();
		
		// set pieces
		pieces = new ArrayList<Piece>();
		
		
		setPreferredSize(new Dimension(900, 780));
		Border connect_4_border = BorderFactory.createLineBorder(Color.BLACK, 40);
		setBorder(connect_4_border);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(connect_4_board_image, 40, 40, null);
		ArrayList<Piece> copy_of_pieces = new ArrayList<Piece>(pieces); // to prevent concurrency errors
		Iterator<Piece> itr = copy_of_pieces.iterator();
		int ply_num = 0;
		while(itr.hasNext()) {
			++ply_num;
			if(ply_num > copy_of_pieces.size()-offset) break;
			Piece piece = itr.next();
			if(piece.player == 1) {
				g.drawImage(X_puck_image, 40 + 120*(piece.x), 40 + 120*(5-piece.y), null);
			}else {
				g.drawImage(O_puck_image, 40 + 120*(piece.x), 40 + 120*(5-piece.y), null);
			}
		}
	}

	public void PlayMove(int player_turn, int column, int row) {
		pieces.add(new Piece(player_turn, column, row));
		repaint();
	}
	
	public void Backwards() {
		if(offset >= pieces.size()) return;
		offset += 1;
		repaint();
	}
	
	public void Forwards() {
		if(offset <= 0) return;
		offset -= 1;
		repaint();
	}
	
}
