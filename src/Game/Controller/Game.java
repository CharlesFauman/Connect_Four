package Game.Controller;

import java.io.IOException; 	// if an IO process doesn't execute correctly, throw an error
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

		// initializing the model
		Model model = new Model();
		
		// using matches
//		View view = match_info.GenerateView();
//		Player first_player = match_info.GenerateFirstPlayer();
//		Player second_player = match_info.GenerateSecondPlayer();
				
		//using old stuff
		View view = new GUIView();
		// DECLARE PLAYERS HERE
		
		Player second_player = new TextHumanPlayer();
		Player first_player = new TextHumanPlayer();
//		Player second_player = new TextHumanPlayer();
			
//		Player first_player = new WindowsExeComputer("/Users/faumac/Desktop/Connect_4_bot/", "Charlie.exe", 1);
//		Player second_player = new WindowsExeComputer("/Users/faumac/Desktop/Connect_4_bot/Connect-4 Programs/AIsFromPastYears/2015/", "Lisheng.exe", 2);
		
//		Player first_player = new Python3Computer("/Users/faumac/Desktop/Connect_4_bot/Connect-4 Programs/AIsFromPastYears/2015/", "python Sinclair.py", 1);
//		Player second_player = new Python3Computer("/Users/faumac/Desktop/Connect_4_bot/Connect-4 Programs/AIsFromPastYears/2015/", "python Ben.py", 2);
				
		// play the game. Set move to first player
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
					column = move_getter.get(10, TimeUnit.SECONDS);
				} catch(final TimeoutException e){
					view.NotifyTimeout();
					win = -move;
					break;
				} catch (InterruptedException e) {
					System.err.println("Well, this wasn't supposed to happen!");
					e.printStackTrace();
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
		
		first_player.NotifyGameOver();
		second_player.NotifyGameOver();
		
		
		return win;
	}

	public static void main(String[] args) {
		// this is where we start. Attempt to start the game
		try {
			
			MatchInfo m = new MatchInfo("GUIView", new String[]{"TextHumanPlayer"}, new String[]{"TextHumanPlayer"});
			Game game = new Game(m);
			System.out.println(game.start());
		} catch (IOException e) {
			System.err.println("A player did not load correctly!");
			e.printStackTrace();
			System.exit(0);
		}
		
		// now the game is done, exit system so it will shut off the external calls we made with as little hastle
		// for us as possible. Note that, in general, this is not good coding practice,
		// but as we only need to run 1 game it is the easiest solution
	}
}
