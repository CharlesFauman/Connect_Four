package Tournament.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Game.Controller.*;

public class Parallel_Elimination_Tournament {

	public static void main(String[] args){
		List<String[]> player_infos = new ArrayList<String[]>();
		List<String[]> players_left = new ArrayList<String[]>();
		
		try(BufferedReader tournament_reader = new BufferedReader( new FileReader("src/Tournament/Resources/tournament_info.txt"));){
			String line;
			while((line = tournament_reader.readLine()) != null) {
				String[] split_line = line.split(",");
				player_infos.add(split_line);
			}
		} catch (IOException e) {
			System.err.println("Couldn't read in the tourny file");
			e.printStackTrace();
		}
		
		
		// eliminate the players that don't work:
		Iterator<String[]> itr = player_infos.iterator();
		while(itr.hasNext()) {
			String[] first_player = itr.next().clone();
			String[] second_player = first_player.clone();
			
			MatchInfo m = new MatchInfo("NoView", first_player, second_player);
			Game game = new Game(m);
			Integer winner = null;
			
			System.out.print("Testing: " + first_player[0] + " ");
			
			final ExecutorService service = Executors.newSingleThreadExecutor();
			
			try {
				final Future<Integer> check_goodness = service.submit(() -> {
					return game.start();
	            });
				winner = check_goodness.get(2000, TimeUnit.MILLISECONDS);
			} catch(final TimeoutException e){
				// Means the game is running. This is fine
				winner = 1;
			} catch (Exception e) {
				// Means the game handled something weirdly, not fine
				e.printStackTrace();
			}
			if(winner != null) {
				players_left.add(first_player);
				System.out.println("PASSED");
			}else {
				System.out.println("FAILED");
			}
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				System.err.println("shouldn't break here...");
				e.printStackTrace();
			}
			service.shutdownNow();
		}
		System.out.println("Starting Players: " + players_left.size());
		player_infos = new ArrayList<String[]>(players_left);
		players_left.clear();
		
		
		// play out the tournament
		while(player_infos.size() > 1) {
			Collections.shuffle(player_infos);
			itr = player_infos.iterator();
			ExecutorService service = Executors.newFixedThreadPool(player_infos.size());
			List<Future<Integer>> ongoing_games = new ArrayList<Future<Integer>>();
			// submit games
			while(itr.hasNext()) {
				String[] first_player = itr.next().clone();
				String[] second_player;
				if(itr.hasNext()) second_player = itr.next().clone();
				else {
					System.out.println("Has no opponent and moves on: " + first_player[0]);
					players_left.add(first_player);
					break;
				}
				MatchInfo m = new MatchInfo("GUIView", first_player, second_player);
				Game game = new Game(m);

				System.out.println("New game started! " + first_player[0] + " vs. " + second_player[0]);
				final Future<Integer> check_goodness = service.submit(() -> {
					Integer current_winner = game.start();
					if(current_winner != -2) {
						if(current_winner == 1) {
							players_left.add(first_player);
							System.out.println("Moves on: " + first_player[0]);
						}else {
							players_left.add(second_player);
							System.out.println("Moves on: " + second_player[0]);
						}
					}else {
						System.err.println("Hmm this isn't right");
					}
					return current_winner;
	            });
				ongoing_games.add(check_goodness);
			}
			
			// check games
			while(ongoing_games.size() > 0) {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					System.err.println("shouldn't break here...");
					e.printStackTrace();
				}
				Iterator<Future<Integer>> current_game_itr = ongoing_games.iterator();
				while(current_game_itr.hasNext()) {
					Future<Integer> current_game = current_game_itr.next();
					if(current_game.isDone()) {
						Integer current_winner = null;
						try {
							current_winner = current_game.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						current_game_itr.remove();
					}
				}
			}
			
			System.out.println("Players left: " + players_left.size());
			player_infos = new ArrayList<String[]>(players_left);
			players_left.clear();
		}
		System.out.println("We have a winner!");
		System.out.println(player_infos.get(0)[0]);
		
		System.out.println("Press Enter to end game:");
		Scanner reader = new Scanner(System.in);
		reader.nextLine();
		reader.close();
		
		System.exit(0);
	}
}
