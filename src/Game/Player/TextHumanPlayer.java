package Game.Player;

import java.util.Scanner;

/**
 * We will get this players input from System.in. As it is a human player, we don't store much here
 */
public class TextHumanPlayer extends Player{
	@Override
	/**
	 * We get text based human input from System.in using a Scanner
	 */
	public int GetMove() {
		System.out.print("Where do you want to move?: ");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		int column = reader.nextInt() - 1;
		return column;
	}
	
	@Override
	/**
	 * We don't need to do much here because the player should be able to see a view representation
	 * of the position at all times
	 */
	public void PlayOpponentMove(int column, int row) {
		System.out.println("Opponent Move: (" + (column+1) + ", " + (row+1) + ")");
	}

	@Override
	public void NotifyGameOver(){
		System.out.println("Game Over!");
	}
}
