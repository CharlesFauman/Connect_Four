package Tournament.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Game.Controller.*;

public class Tournament {

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
			int winner = -2;
			
			System.out.print("Testing: " + first_player[0] + " ");
			
			final ExecutorService service = Executors.newSingleThreadExecutor();
			
			try {
				final Future<Integer> check_goodness = service.submit(() -> {
					return game.start();
	            });
				winner = check_goodness.get(2, TimeUnit.SECONDS);
			} catch(final TimeoutException e){
				// Means the game is running. This is fine
				winner = 1;
			} catch (Exception e) {
				// this might happen on shutdown
			}
			if(winner != -2) {
				players_left.add(first_player);
				System.out.println("PASSED");
			}
			try {
				TimeUnit.SECONDS.sleep(1);
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
			itr = player_infos.iterator();
			while(itr.hasNext()) {
				String[] first_player = itr.next().clone();
				String[] second_player = {};
				if(itr.hasNext()) second_player = itr.next().clone();
				else {
					System.out.println("Moves on: " + first_player[0]);
					players_left.add(first_player);
					break;
				}
				MatchInfo m = new MatchInfo("GUIView", first_player, second_player);
				Game game = new Game(m);
				int winner = -2;
				try {
					System.out.println("Now playing: " + first_player[0] + " vs. " + second_player[0]);
					winner = game.start();
				} catch (IOException e) {
					System.out.println("One of the programs did not load correctly");
				}
				
				if(winner == 1) {
					players_left.add(first_player);
					System.out.println("Moves on: " + first_player[0]);
				}else if(winner == 0 || winner == -1) {
					players_left.add(second_player);
					System.out.println("Moves on: " + second_player[0]);
				}else {
					System.err.println("Hmm this isn't right");
				}
			}
			System.out.println("Players left: " + players_left.size());
			player_infos = new ArrayList<String[]>(players_left);
			players_left.clear();
		}
		System.out.println("We have a winner!");
		System.out.println(player_infos.get(0)[0]);
		
	}

}
