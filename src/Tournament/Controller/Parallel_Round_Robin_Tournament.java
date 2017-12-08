package Tournament.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Game.Controller.Game;
import Game.Controller.MatchInfo;

/**
 * Each player will player each other player exactly twice (for p1 vs. p2 and p2 vs. p1)
 */
public class Parallel_Round_Robin_Tournament {
	public static void main(String[] args){
		// construct list of all players (player string + score int)
		LinkedList<Player> player_infos = new LinkedList<Player>();
		
		try(BufferedReader tournament_reader = new BufferedReader( new FileReader("src/Tournament/Resources/tournament_info.txt"));){
			String line;
			while((line = tournament_reader.readLine()) != null) {
				String[] split_line = line.split(",");
				player_infos.add(new Player(split_line, 0));
			}
		} catch (IOException e) {
			System.err.println("Couldn't read in the tourny file");
			e.printStackTrace();
		}
		
		// TODO: verify vaildity of players and remove the bad ones
		System.out.println("Starting Players: " + player_infos.size());
		for(int current_player = 0; current_player < player_infos.size(); ++current_player) {
			System.out.println(player_infos.get(current_player).player_info[0]);
		}
		
		// randomize list of players
		Collections.shuffle(player_infos);
		
		// play out tournament
		for(int starting_right_first = 0; starting_right_first <= 1; ++starting_right_first) {
			int num_rounds = player_infos.size()-1;
			for(int current_round = 0; current_round < num_rounds; ++current_round) {
				System.out.println(" --- Current Traversal: " + (starting_right_first+1) + " / Current Round:" + (current_round+1) + "  ---");
				
				// split list of players in half to left and right
				int midpoint = Math.floorDiv(player_infos.size(), 2);
				List<Player> left = player_infos.subList(0, midpoint);
				List<Player> right = player_infos.subList(midpoint, 2*midpoint);
				assert(left.size() == right.size());
				// create a BYE player with last element if odd list
				if(2*midpoint < player_infos.size()) {
					player_infos.getLast().score += 1;
					System.out.println("Giving a BYE to: " + player_infos.getLast().player_info[0]);
					assert(left.size() + right.size() + 1 == player_infos.size());
				}else {
					assert(left.size() + right.size() == player_infos.size());
				}
				
				// create a service thread for the round:
				ExecutorService service = Executors.newFixedThreadPool(midpoint);
				List<Future<Integer>> ongoing_games = new ArrayList<Future<Integer>>();
				
				// Match up the players
				boolean reversed = (starting_right_first == 0 && current_round%2 == 0 ) || (starting_right_first == 1 && current_round%2 == 1);
				for(int current_match = 0; current_match < midpoint; ++current_match) {
					Player first_player, second_player;
					if(reversed) {
						first_player = right.get(current_match);
						second_player = left.get(midpoint-current_match-1);
					}else{
						second_player = right.get(current_match);
						first_player = left.get(midpoint-current_match-1);
					}
					// Start the Match
					System.out.println("Starting Match: " + first_player.player_info[0] + " vs. " + second_player.player_info[0]);
					
					MatchInfo m = new MatchInfo("GUIView", first_player.player_info, second_player.player_info);
					Game game = new Game(m);
					
					final Future<Integer> check_goodness = service.submit(() -> {
						int winner = game.start();
						System.out.print("Match Over: " + first_player.player_info[0] + " vs. " + second_player.player_info[0] + ": ");
						if(winner == 1) {
							System.out.println("First Player Won!");
							first_player.score += 1;
						}else if(winner == 0) {
							System.out.println("Draw!");
							first_player.score += .25;
							second_player.score += .75;
						}else if(winner == -1) {
							System.out.println("Second Player Won!");
							second_player.score += 1;
						}
						return winner;
		            });
					ongoing_games.add(check_goodness);
				}
				
				// get the game scores
				// if first player wins, their score += 1
				// if draw, first player score +=.25, second player score +=.75
				// if second player wins, their score += 1
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
							current_game_itr.remove();
						}
					}
				}
				
				// move the last player in the list of players to the beginning
				Player moving_player = player_infos.removeLast();
				player_infos.add(1, moving_player);
				System.out.println();
				service.shutdownNow();
			}
			System.out.println();
		}
		Collections.sort(player_infos);
		System.out.println("Final Scores:");
		for(int current_player = 0; current_player < player_infos.size(); ++current_player) {
			System.out.println(player_infos.get(current_player).player_info[0] + ": " + player_infos.get(current_player).score);
		}
	}
}
