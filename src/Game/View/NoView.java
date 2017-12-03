package Game.View;

public class NoView extends View{

	@Override
	public void NotifyTimeout() {
		// NOTHING
	}

	@Override
	public void NotifyIllegalMove(int column) {
		// NOTHING
		
	}

	@Override
	public void NotifyDrawn() {
		// NOTHING
		
	}

	@Override
	public void NotifyWin() {
		// NOTHING
		
	}

	@Override
	public void PlayMove(int column, int row) {
		// NOTHING
		
	}

	@Override
	public void NotifyIOException() {
		// NOTHING
		
	}

}
