package Tournament.Controller;

import java.util.Comparator;

public class Player implements Comparable<Player>{
	public String[] player_info;
	public double score;
	Player(String[] player_info, double score){
		this.player_info = player_info;
		this.score = score;
	}
	@Override
	public int compareTo(Player p2) {
		if(this.score < p2.score) return 1;
		if(this.score > p2.score) return -1;
		return 0;
	}
}