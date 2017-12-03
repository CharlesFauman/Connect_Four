package Game.View;

import java.io.IOException;
import java.util.Scanner;

/**
 * This text based view will display everything in text to System.out
 * every time the board state is changed, it will be retyped,
 * and if any game ending events occur, this view will type
 * out the information for that event
 */
public class TextView extends View{

	private int[][] board;
	private int width, height;
	private int player_turn;
	
	private void PrintBoard() {
		System.out.println("board:");
		for(int row = height-1; row >= 0; --row) {
			for(int col = 0; col < width; ++col) {
				System.out.print(" ");
				switch(board[col][row]) {
					case 1: System.out.print("X"); break;
					case -1: System.out.print("O"); break;
					case 0: System.out.print("_"); break;
				}
				System.out.print(" ");
			}
			System.out.println();
		}
		for(int col = 0; col < width; ++col) System.out.print(" " + (col+1) + " ");
		System.out.println();
	}
	
	public TextView(int width, int height, int connect_number) {
		System.out.println("Welcome to Connect 4 text view!");
		this.width = width;
		this.height = height;
		this.board = new int[width][height];
		this.player_turn = 1;
		PrintBoard();
	}
	
	public TextView() {
		this(7,6,4);
	}
	
	
	
	@Override
	public void PlayMove(int column, int row) {
		board[column][row] = player_turn;
		player_turn = -player_turn;
		PrintBoard();
	}
	
	@Override
	public void NotifyIllegalMove(int column) {
		System.out.println("Oops! that was an illegal move: " + column);
		NotifyWin();
	}

	@Override
	public void NotifyDrawn() {
		System.out.println("That's a draw!");
	}

	@Override
	public void NotifyWin() {
		System.out.print("That's a win for player ");
		switch(-player_turn) {
		case 1: System.out.print("X"); break;
		case -1: System.out.print("O"); break;
		}
		System.out.println("!");
	}

	@Override
	public void NotifyTimeout() {
		System.out.println("Oh no! Player timed out!");
		NotifyWin();
	}

	@Override
	public void NotifyIOException() {
		System.out.println("Oh no! Player did not handle IO appropriately!");
		NotifyWin();
	}

}
