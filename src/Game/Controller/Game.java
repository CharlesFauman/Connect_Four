package Game.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; 	// if an IO process doesn't execute correctly, throw an error
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// to make sure external calls happen fast enough
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Game.Model.*;
import Game.View.*;
import Game.Player.*;
import Game.Player.ComputerPlayers.*;


public class Game {
	MatchInfo match_info;
	
	public Game(MatchInfo match_info) {
		this.match_info = match_info;
	}
	
	/**
	 * this will run the connect 4 game
	 * @throws IOException
	 */
	public int start() throws IOException {
		// This will be used to make sure external calls (to programs) run in the right amount of time!
		final ExecutorService service = Executors.newSingleThreadExecutor();

		// initialize the model (always the same)
		Model model = new Model();
		
		// initialize the players (dependent on match_info)
		Player first_player = match_info.GenerateFirstPlayer();
		Player second_player = match_info.GenerateSecondPlayer();
		
		// initialize the view (dependent on match_info)
		View view = match_info.GenerateView();
		
		
		// play the game. Set win to a nonsense value and set move to first player
		int win = -2;
		int move = 1;
		try {
			// each loop will get 1 move
			while(true) {
				// get the player move. Looks complicated because we have to make sure it executes within a certain amount
				// of time, and this requires catching a few errors that could occur
				int column = -1;
				try {
					int curr_move = move;
					final Future<Integer> move_getter = service.submit(() -> {
						if(curr_move == 1) return first_player.GetMove();
						else return second_player.GetMove();
		            });
					column = move_getter.get(15, TimeUnit.SECONDS);
				} catch(final TimeoutException e){
					view.NotifyTimeout();
					win = -move;
					break;
				} catch (InterruptedException e) {
					// will happen on service shutdown if program doesnt terminate itself
					// System.err.print("Program did not terminate itself. But it's fine. ");
				} catch (ExecutionException e) {
					System.err.println("Well, this wasn't supposed to happen!");
					e.printStackTrace();
				}
				
				// make sure the move is legal! If it isn't, then let the view know and end the game
				if(!model.isLegalMove(column)) {
					view.NotifyIllegalMove(column);
					win = -move;
					break;
				}
				
				// now that we know the move is good, add it to the model
				int row = model.move(column);
				
				// now display the move
				view.PlayMove(column, row);
				
				// now tell the opponent what move was just made
				if(move == -1) first_player.PlayOpponentMove(column, row);
				else second_player.PlayOpponentMove(column, row);
	
				// check if the game is a draw, and if so notify the view and end the game
				if(model.isDrawn()) {
					view.NotifyDrawn();
					win = 0;
					break;
				}
				
				// check if the game is a won, and if so notify the view and end the game
				if(model.isWon()) {
					view.NotifyWin();
					win = move;
					break;
				}
				
				// now it's the other players move
				move = -move;
			}
		} catch(IOException e) {
			// this will occur if the player gives us weird input or if we can't give them input for some reason
			win = move;
			view.NotifyIOException();
		}
		
		//first_player.NotifyGameOver();
		//second_player.NotifyGameOver();
		
		return win;
	}

	public static void main(String[] args) {
		List<String[]> player_infos = new ArrayList<String[]>();
		
		try(BufferedReader tournament_reader = new BufferedReader( new FileReader("src/Game/Resources/match_info.txt"));){
			String line;
			while((line = tournament_reader.readLine()) != null) {
				String[] split_line = line.split(",");
				player_infos.add(split_line);
			}
		} catch (IOException e) {
			System.err.println("Couldn't read in the match file");
			e.printStackTrace();
		}
		
		MatchInfo m = new MatchInfo("GUIView", player_infos.get(0), player_infos.get(1));
		Game game = new Game(m);
		System.out.println("Press Enter to end game:");
		Scanner reader = new Scanner(System.in);
		reader.nextLine();
		reader.close();
		System.exit(0);
		
		// now the game is done, exit system so it will shut off the external calls we made with as little hastle
		// for us as possible. Note that, in general, this is not good coding practice,
		// but as we only need to run 1 game it is the easiest solution
	}
}
