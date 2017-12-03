package Game.Controller;

import java.io.IOException;

import Game.Player.*;
import Game.Player.ComputerPlayers.*;
import Game.View.*;

public class MatchInfo {
	private String view_name;
	String[] first_player_info;
	String[] second_player_info;
	
	public MatchInfo(String view_name, String[] first_player_info, String[] second_player_info) {
		this.view_name = view_name;
		this.first_player_info = first_player_info;
		this.second_player_info = second_player_info;
	}

	public View GenerateView() {
		switch(view_name) {
		case "GUIView": return new GUIView(first_player_info[0], second_player_info[0]); 
		case "TextView": return new TextView(first_player_info[0], second_player_info[0]); 
		default: return new NoView();
		}
	}
	
	
	private Player GeneratePlayer(String[] player_info, boolean first) throws IOException {
		
		switch(player_info[1]) {
		case "TextHumanPlayer": return new TextHumanPlayer(player_info[0]); 
		case "Python3Computer": return new Python3Computer(player_info[0], player_info[2], player_info[3], first);
		case "WindowsExeComputer": return new WindowsExeComputer(player_info[0], player_info[2], player_info[3], first);
			
		default: throw new IOException("This is not a correct player type:" + player_info[1]);
		}
	}

	public Player GenerateFirstPlayer() throws IOException {
		return GeneratePlayer(first_player_info, true);
	}

	public Player GenerateSecondPlayer() throws IOException {
		return GeneratePlayer(second_player_info, false);
	}

}
