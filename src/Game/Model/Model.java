package Game.Model;

public class Model {
	private int width, height; // width and height of board
	private int connect_number; // number needed connected to win
	private int[][] board; /* representation below:
	 * 6 [0][0][0][0][0][0][0]
	 * 5 [0][0][0][0][0][0][0]
	 * 4 [0][0][0][0][0][0][0]
	 * 3 [0][0][0][0][0][0][0]
	 * 2 [0][0][0][0][0][0][0]
	 * 1 [0][0][0][0][0][0][0]
	 * 0 [0][0][0][0][0][0][0]
	 * -  1  2  3  4  5  6  7
	 * DEFAULT VALUE:	0
	 * PLAYER red VALUE:	1
	 * PLAYER blue VALUE: -1
	 */
	int player_turn; // 1: player red, -1: player blue
	
	boolean player_won; // true if a current player has won
	int num_plys; // used to check if game is drawn
	
	/**
	 * Constructs a connect four state with the specified values
	 * @param width
	 * @param height
	 * @param connect_number
	 */
	public Model(int width, int height, int connect_number) {
		this.width = width;
		this.height = height;
		this.board = new int[width][height];
		this.connect_number = connect_number;
		this.player_turn = 1;
		this.player_won = false;
		this.num_plys = 0;
	}
	
	/**
	 * Constructs a connect four state with
	 * width = 7,
	 * height = 6,
	 * connect_number = 4
	 */
	public Model() {
		this(7,6,4);
	}
	
	/**
	 * checks if game is drawn
	 * @return
	 */
	public boolean isDrawn() {
		return num_plys >= height*width;
	}
	
	/**
	 * checks if the current player won
	 * @return
	 */
	public boolean isWon() {
		return player_won;
	}
	
	/**
	 * checks legality of a move
	 * @param column
	 * @return
	 */
	public boolean isLegalMove(int column) {
		if( column < 0 || column >= width) return false;
		return (board[column][height-1] == 0);
	}
	
	/**
	 * makes sure a move is within bounds
	 * @param column
	 * @param row
	 * @return
	 */
	private boolean isInBounds(int column, int row) {
		return (0 <= column && column < width) && (0 <= row && row < height);
		
	}
	
	/**
	 * used to check if the move we just made was a win
	 * @param column
	 * @param row
	 */
	private void checkWin(int column, int row) {
		int consecutive;
		int curr_column;
		int curr_row;
		//across
		/*
		 * column increment: if the column changes when going to the left or right
		 * row increment: if the row changes when going to the left or right
		 * left_side: -1 if we're checking the left side
		 * 
		 * (1,  1) or (-1, -1) right  diagonal
		 * (1, -1) or (-1, 1) left diagonal
		 * (1,  0) or (-1, 0) vertical
		 * (0,  1) or (0, -1) horizontal
		 */
		
		for(int column_increment = 1; column_increment >= 0; --column_increment) {
			for(int row_increment = -1; row_increment <= 1; ++row_increment) {
				
				// this should not happen, but we also happen to need a case where we check the diagonal this way
				// here, we substitute in this null case for the one we want
				if(column_increment == 0) {
					row_increment = 1;
				}
				
				//adds up the position we're in with all its left connections, then its right connections
				consecutive = 1;
				for(int left_side = -1; left_side <= 1; left_side += 2) {
					for(int i = 1; i < connect_number; ++i){
						curr_column = column + (left_side*column_increment*i);
						curr_row = row + (left_side*row_increment*i);
						if(!isInBounds(curr_column, curr_row)) break;
						if(board[curr_column][curr_row] != player_turn) break;
						++consecutive;
					}
				}
				if(consecutive >= connect_number) {
					player_won = true;
					break;
				}
			}
		}
	}
	
	/**
	 * finds the proper row for a move, returns false if illegal
	 * @param column
	 * @return
	 */
	public int properRow(int column) {
		// make sure the move is legal
		if(!isLegalMove(column)) return -1;
		
		// go through the rows from 0 up until we find the first empty one
		int proper_row = 0;
		while(board[column][proper_row] != 0) ++proper_row;
		
		// return the right row
		return proper_row;
	}
	
	/**
	 * attempts to move in the specified column, returns the row moved in
	 * @param column
	 * @return
	 */
	public int move(int column) {
		// make sure the game is not over
		if((isDrawn() || isWon())) return -1;
		
		// make sure the move is legal
		if(!isLegalMove(column)) return -1;
		
		// find where the piece goes
		int proper_row = properRow(column);
		
		// place the piece
		board[column][proper_row] = player_turn;
		num_plys += 1;
		
		// recalculate if player won
		checkWin(column, proper_row);
		
		// switch to the next turn if game not over
		if(!(isDrawn() || isWon())) player_turn = -player_turn;
		
		// now we've completed a move!
		return proper_row;
		
	}
	
	
}
