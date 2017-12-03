package Game.Player;

import java.io.IOException;

/**
 * A player is a class from which we can get a move. They are responsible for keeping track of how they will make
 * their move. They are provided with the opponents moves and nothing more
 *
 */
public abstract class Player {
	protected String name = "not named :(";
	public String GetName() {
		return name;
	}
	public abstract int GetMove() throws IOException;
	public abstract void PlayOpponentMove(int column, int row) throws IOException;
	public abstract void NotifyGameOver() throws IOException;

}
