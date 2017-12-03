package Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * We will get this player's input from whatever external program is running
 */
public class ComputerPlayer extends Player {
	
	// "protected" allows subclasses to access these varaibles (to set them)
	protected Process program;			// The player's program
	protected BufferedReader reader_from_program;	// How we will get input from the program
	protected BufferedWriter writer_to_program;	// How we will give input to the program
	
	// Proper syntax for writing to a program
	private void WriteToProgram(String str) throws IOException {
		writer_to_program.write(str);
		writer_to_program.newLine();
		writer_to_program.flush();
	}
	
	// Proper syntax for reading from a program
	private char ReadFromProgram() throws IOException {
		char ans = ' ';
		while(Character.isWhitespace(ans)) ans = (char) reader_from_program.read();
		return ans;
	}
	
	protected void Initiate(int player_num) throws IOException {
		// This will be used to make sure this program is initiated in the right amount of time!
				final ExecutorService service = Executors.newSingleThreadExecutor();
				
				// attempt to get the proper initial response from the program
				// a few checks are made to insure that the program responds correctly
				// and within a reasonable amount of time, and that nothing
				// weird happens
				try {
					final Future<Boolean> set_up = service.submit(() -> {
						try {
							if(ReadFromProgram() != 'p') throw new IOException("did not respond with p");
							WriteToProgram(String.valueOf(player_num));
							if(player_num == 2) if(ReadFromProgram() != '?') throw new IOException("did not respond with ?");
						} catch(IOException e){
							e.printStackTrace();
							return false;
						}
						return true;
		            });
					if(!set_up.get(2, TimeUnit.SECONDS)) throw new IOException();
				} catch(final TimeoutException e){
					throw new IOException("did not respond to initial requests in time");
				} catch (InterruptedException e) {
					System.err.println("Well, this wasn't supposed to happen!");
					e.printStackTrace();
				} catch (ExecutionException e) {
					System.err.println("Well, this wasn't supposed to happen!");
					e.printStackTrace();
				}
	}
	
	@Override
	/**
	 * this will read the move from the program
	 */
	public int GetMove() throws IOException {
		char ans = ReadFromProgram();
		return Character.getNumericValue(ans) -1;
	}

	@Override
	/**
	 * this will send opponent moves to the program
	 */
	public void PlayOpponentMove(int column, int row) throws IOException {
		WriteToProgram(String.valueOf(column+1));
	}

	@Override
	public void NotifyGameOver() throws IOException {
		WriteToProgram("-1");
		program.destroy();
	}
	
}
