package View;

import java.io.IOException;

/**
 * A view is a class that will allow any human users to view the position and other important game states
 * @author faumac
 *
 */
public abstract class View {
	public abstract void NotifyTimeout();
	public abstract void NotifyIllegalMove(int column);
	public abstract void NotifyDrawn();
	public abstract void NotifyWin();
	public abstract void PlayMove(int column, int row);
	public abstract void NotifyIOException();
	
}
