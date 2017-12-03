package Game.Player.ComputerPlayers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import Game.Player.ComputerPlayer;

public class WindowsExeComputer extends ComputerPlayer{

	public WindowsExeComputer(String dir, String commandline_input, int player_num) throws IOException {
		File directory = new File(dir);
		// initialize the main variables. If these initializations are invalid, and IOException will be thrown
		program = Runtime.getRuntime().exec(dir + commandline_input, null, directory);
		reader_from_program = new BufferedReader(new InputStreamReader(program.getInputStream()));
		writer_to_program = new BufferedWriter(new OutputStreamWriter(program.getOutputStream()));
		
		// Initiate player
		Initiate(player_num);
	}

}
