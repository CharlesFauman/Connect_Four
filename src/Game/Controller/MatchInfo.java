package Game.Controller;

import java.io.IOException;

import Game.Player.*;
import Game.View.*;

public class MatchInfo {
	private String view_name;
	String[] first_player_info;
	String[] second_player_info;
	
	public MatchInfo(String view_name, String[] first_player_info, String[] second_player_info) {
		this.first_player_info = first_player_info;
		this.second_player_info = second_player_info;
	}

	public View GenerateView() {
		switch(view_name) {
		case "GUIView": return new GUIView(); 
		case "TextView": return new TextView(); 
		default: return new NoView();
		}
	}
	
	
	private Player GeneratePlayer(String[] player_info, boolean first) throws IOException {
		int current = 0;
		switch(player_info[current]) {
		case "TextHumanPlayer": return new TextHumanPlayer(); 
		case "Python3Computer":
		case "WindowsExeComputer":
		default: throw new IOException("This is not a correct player type:" + player_info[current]);
		}
	}

	public Player GenerateFirstPlayer() throws IOException {
		return GeneratePlayer(first_player_info, true);
	}

	public Player GenerateSecondPlayer() throws IOException {
		return GeneratePlayer(second_player_info, false);
	}

}
