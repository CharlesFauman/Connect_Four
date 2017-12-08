//basic includes
#include <stdio.h>	// std
#include <stdlib.h>	// std
#include <iostream>	// std::cout, std::cin

#include <time.h>	// time
#include <chrono>	// time

#include <climits>	// LL_MAX

#include <cstring>	// std::memset 
#include <vector>	// std::vector

#include <bitset>	// for printing nicely

// definitions:
typedef long long int state_type;
/*
06 13 20 27 34 41 48 // extra space for key
05 12 19 26 33 40 47 // top_mask(col i), will make the i'th column in this top 1
04 11 18 25 32 39 46
03 10 17 24 31 38 45
02 09 16 23 30 37 44
01 08 15 22 29 36 43
00 07 14 21 28 35 42 // bottom_mask, all in this bottom row are 1, rest are 0

*/

// this class will store the current board using the bitboard representation shown above
class Position {
private:
	// variables:
	state_type current_mask;
	state_type current_position;
	int num_moves;

	state_type top_mask(int column) const {
		// take a value of 1, shift it to the top row, then shift to the correct column
		return ((state_type(1) << 5) << column * 7);
	}

	state_type bottom_mask(int column) const {
		// take a value of 1, it's at the bottom row, then shift to the correct column
		return (state_type(1) << column * 7);
	}

	state_type column_mask(int column) const {
		// take a value of 1, bitshift it to the top of a column and subtract 1 to create a column of 1's,
		// and then shift this column to the correct one.
		return ((state_type(1) << 6) - 1) << column * 7;
	}

	bool anyWin(state_type position) const {
		state_type neighbors_mask; // will store if a node has a neightbor in a certain direction

								   // horizontal
		neighbors_mask = (position & (position >> 7)); // each element in the mask will be 1 if both it and its left neighbor are 1 in position
		if (neighbors_mask & (neighbors_mask >> 14)) return true; // if this is true, it means there is an element with 3 left neighbors being 1 in position

																  // vertical
		neighbors_mask = (position & (position >> 1)); // each element in the mask will be 1 if both it and its down neighbor are 1 in position
		if (neighbors_mask & (neighbors_mask >> 2)) return true; // if this is true, it means there is an element with 3 down neighbors being 1 in position


																 // diagonal up (from left to right)
		neighbors_mask = (position & (position >> 8)); // each element in the mask will be 1 if both it and its diag down to the left neighbor are 1 in position
		if (neighbors_mask & (neighbors_mask >> 16)) return true; // if this is true, it means there is an element with 3 diag down to the left neighbors being 1 in position


																  // diagonal down (from left to right) 
		neighbors_mask = (position & (position >> 6)); // each element in the mask will be 1 if both it and its diag up to the left neighbor are 1 in position
		if (neighbors_mask & (neighbors_mask >> 12)) return true; // if this is true, it means there is an element with 3 diag up to the left neighbors being 1 in position

		return false;
	}

public:
	Position() {
		current_mask = state_type(0);
		current_position = state_type(0);
		num_moves = 0;
	}

	Position(const Position &p) {
		current_mask = p.current_mask;
		current_position = p.current_position;
		num_moves = p.num_moves;
	}

	void print() const{
		std::cout << std::endl << "board: " << std::endl;
		std::bitset<49> us(current_position);
		std::bitset<49> them(current_position^current_mask);
		for (int x = 1; x < 7; ++x) {
			for (int y = 0; y < 7; ++y) {
				int place = 48 - ((6 - y) * 7 + x);
				if(num_moves%2 == 0){
					if(us[place]){
						std::cout << "X";
					}else if(them[place]){
						std::cout << "O";
					}else{
						std::cout << "-";
					}
				}else{
					if(us[place]){
						std::cout << "O";
					}else if(them[place]){
						std::cout << "X";
					}else{
						std::cout << "-";
					}
				}
				std::cout << " ";
			}
			std::cout << std::endl;
		}
	}

	state_type key() const {
		return (current_position + current_mask); // will result in a unique hash for each board
	}


	bool canPlay(int column) const {
		return 0 == (current_mask & top_mask(column)); // if the top of a column is a 1, then we cannot play there
	}

	bool isWinningMove(int column) const {
		state_type position = current_position; // create a copy of the position so we can alter the copy
		position |= ((current_mask + bottom_mask(column)) & column_mask(column)); // simulate doing this move while removing the top element of the column
		return anyWin(position);
	}

	int numMoves() const {
		return num_moves;
	}

	void Play(int column) {
		current_position ^= current_mask; // change current position to be the other player's moves
		current_mask |= (current_mask + bottom_mask(column));
		// the current mask must now include the new move. adding the bottom will propogate
		// that new 1 upwards. We then must or it with the original mask to replace the 0 
		// created in the bottom with a 1
		++num_moves;
	}
	
	
	void printStuff(state_type win) const{
		std::bitset<49> s(win);
		std::cout << "mask: " << std::endl;
		for (int x = 0; x < 7; ++x) {
			for (int y = 0; y < 7; ++y) {
				int place = 48 - ((6 - y) * 7 + x);
				std::cout << s[place];
				std::cout << " ";
			}
			std::cout << std::endl;
		}
		for(int i=0; i<49;++i) std::cout << s[i];
		std::cout << std::endl;
	}
	
	
	int staticEvaluation() const{
		static state_type reverse_xor(LLONG_MAX);
		//static state_type only_viable(0b1111111111111111111111111111111111111111111111111);
		static state_type all_top_mask(0b1000000100000010000001000000100000010000001000000);
		// odd rows
		static state_type third_row(0b0000100000010000001000000100000010000001000000100);
		static state_type fifth_row(0b0010000001000000100000010000001000000100000010000);
		// even rows
		static state_type second_row(0b0000010000001000000100000010000001000000100000010);
		static state_type fourth_row(0b0001000000100000010000001000000100000010000001000);
		static state_type sixth_row(0b0100000010000001000000100000010000001000000100000);
		state_type opposite_mask = (current_mask^all_top_mask^reverse_xor);
		int count = 0;
		state_type winning_moves;
		
		state_type player_position = current_position; // opponent
		for(int p=-1;p<=1;p+=2){
			// vertical up (other not possible)
			winning_moves = ((player_position << 3) & (player_position << 2) & (player_position << 1) & opposite_mask);
			
			// horizontal, diag up, diag down
			static int shift_num[3] = {7, 8, 6};
			for(int i=0; i < 3; ++i){
				winning_moves |= (opposite_mask & (player_position >> shift_num[i]) & (player_position >> 2*shift_num[i]) & (player_position >> 3*shift_num[i]) );
				winning_moves |= ((player_position << shift_num[i]) & opposite_mask & (player_position >> shift_num[i]) & (player_position >> 2*shift_num[i]) );
				winning_moves |= ((player_position << 2*shift_num[i]) & (player_position << shift_num[i]) & opposite_mask & (player_position >> shift_num[i]) );
				winning_moves |= ((player_position << 3*shift_num[i]) & (player_position << 2*shift_num[i]) & (player_position << shift_num[i]) & opposite_mask );
			}
			
			state_type third_win = winning_moves & third_row;
			state_type fifth_win = winning_moves & fifth_row;
			
			state_type second_win = winning_moves & second_row;
			state_type fourth_win = winning_moves & fourth_row;
			state_type sixth_win = winning_moves & sixth_row;
			
			//std::cout << "num_moves: " << num_moves << std::endl;
			if(((num_moves%2)*2-1) == p){
				while(third_win){
					third_win &= third_win-1;
					count += 18*p;
				}
				while(fifth_win){
					fifth_win &= fifth_win-1;
					count += 14*p;
				}
				
				while(second_win){
					second_win &= second_win-1;
					count += 5*p;
				}
				while(fourth_win){
					fourth_win &= fourth_win-1;
					count += 3*p;
				}
				while(sixth_win){
					sixth_win &= sixth_win-1;
					count += p;
				}
				
			}else{
				while(second_win){
					second_win &= second_win-1;
					count += 20*p;
				}
				while(fourth_win){
					fourth_win &= fourth_win-1;
					count += 16*p;
				}
				while(sixth_win){
					sixth_win &= sixth_win-1;
					count += 12*p;
				}
				
				while(third_win){
					third_win &= third_win-1;
					count += 4*p;
				}
				while(fifth_win){
					fifth_win &= fifth_win-1;
					count += 2*p;
				}
			}
			
			player_position = (current_position^current_mask); // me
		}
		
		return count;
	}

};


// this class will store positions that we have already analyzed, but only the most recently used ones to save on memory
class TranspositionTable {
private:
	struct Entry {
		state_type key;
		int val;
	};

	std::vector<Entry> T;

	unsigned int index(const state_type &key) const {
		return key%T.size();
	}

public:

	TranspositionTable(unsigned int size) : T(size) {}

	void reset() {
		std::memset(&T[0], 0, T.size() * sizeof(Entry));
	}

	void put(state_type key, int val) {
		unsigned int i = index(key);
		T[i].key = key;
		T[i].val = val;
	}

	int get(state_type key) const {
		unsigned int i = index(key);
		if (T[i].key == key)
			return T[i].val;
		else
			return 11111;
	}
};

// this function will explore the search tree, returning the maximum value of the position for the current player
// usually it is called as -negamax because since we just made a move, it will return an evaluation from the other player's
// perspective, and we need to reverse it.
int negamax(const Position &p, TranspositionTable &TT, int best_score, int best_next, const int &max_depth, int depth) {
	if (p.numMoves() == 42) return 0; // it's a draw
	if (depth == max_depth){
		//p.print();
		//std::cout << "Evaluation: " << p.staticEvaluation() << std::endl;
		return -p.staticEvaluation(); // return static evaluation: assume equal position if no definite answer
	}

	state_type key = p.key();

	//int TT_val = TT.get(key);
	//if (TT_val != 11111) return TT_val;

	//check if any of the moves win. If so, return how many moves that will take.
	for (int x = 0; x < 7; ++x) {
		if (p.canPlay(x) && p.isWinningMove(x)){
			//std::cout << "SOMEONE WINS HERE: ";
			//p.print();
			return 100*(42 - depth);
		}
	}
	
	// find the best score of the best move to play from this position
	static int x_array[7] = { 3,4,2,5,1,6,0 };
	int best_value = -4300;
	for (int x = 0; x < 7; ++x) { 
		if (p.canPlay(x_array[x])) {
			Position new_p(p);
			new_p.Play(x_array[x]);
			int score = -negamax(new_p, TT, -best_next, -best_score, max_depth, depth + 1);
			if (score > best_value) best_value = score;
			if (score > best_score) best_score = score;
			if (best_score >= best_next) break;
		}
	}
	//TT.put(key, best_value);
	return best_value;
}

// actually get the best move of a position
int bestMove(const Position &p) {
	std::chrono::steady_clock::time_point start = std::chrono::steady_clock::now();
	static TranspositionTable TT(1000000);

	if (p.numMoves() == 42) return -1; // it's a draw

									   //check if any of the moves win. If so, return how many moves that will take.
	for (int x = 0; x < 7; ++x) {
		if (p.canPlay(x) && p.isWinningMove(x)) return x;
	}

	std::vector<int> best_moves;
	int curr_depth = 10;
	float prev_diff = 1000000;
	while (true) {
		TT.reset();
		best_moves.clear();
		// find the best score of the best move to play from this position
		int best_score = -4300;
		static int x_array[7] = { 3,4,2,5,1,6,0 };
		//std::cout << "________________________" << std::endl;
		for (int x = 0; x < 7; ++x) {
			if (p.canPlay(x_array[x])) {
				Position new_p(p);
				new_p.Play(x_array[x]);
				int score = -negamax(new_p, TT, -4300, 4300, curr_depth, 0);
				if (score > best_score) {
					best_score = score;
					best_moves.clear();
					for(int i = 0; i<((8 - x) / 2)*((8 - x) / 2)*((8 - x) / 2); ++i) best_moves.push_back(x_array[x]);
				}
				else if (score == best_score) {
					//for (int i = 0; i<((8 - x) / 2)*((8 - x) / 2)*((8 - x) / 2); ++i) best_moves.push_back(x_array[x]);
				}
				//std::cout << "move: " << x_array[x] +1 << ", val: " << score << std::endl;
			}else{
				//std::cout << "can't play: " << x_array[x] +1 << std::endl;
			}
		}
		float diff = std::chrono::duration_cast<std::chrono::microseconds>(std::chrono::steady_clock::now() - start).count();
		//std::cout << "time: " << diff;
		//std::cout << ", time_next est: " << diff*(diff / prev_diff) << std::endl;
		if ( best_score > 99 || best_score < -99 || diff*(diff / prev_diff) > 9000000 - diff) break;
		//break; // COMMENT THIS ONE OUT
		++curr_depth;
		prev_diff = diff;
	}
	//std::cout << "depth: " << curr_depth << std::endl;
	//for (int i = 0; i < best_moves.size(); ++i) std::cout << best_moves[i]+1 << " ";
	//std::cout << std::endl;
	return best_moves[std::rand() % best_moves.size()];
	//return best_moves[best_moves.size()-1];
}

void test(){
	Position p;
	
	p.print();
	std::cout << "1 2 3 4 5 6 7" << std::endl;
	std::cout << "move: "; 
	int move;
	while (std::cin >> move){
		p.Play(move-1);
		p.print();
		std::cout << "1 2 3 4 5 6 7" << std::endl;
		std::cout << "Evaluation: " << p.staticEvaluation() << std::endl;
		for(int i=0; i<7; ++i) std::cout << "move " << i+1 << ": " << p.canPlay(i) << std::endl;
		std::cout << "move: "; 
	}
	
	exit(0);
}

// main game player
int main(int argc, char *argv[]) {
	//test();
	std::srand(time(0));
	Position p;

	// First, print 'p' to the terminal so the moderator program knows that your program is there
	std::cout << "p";

	// Read in an integer so you know will go first.  This will be a 1 if your program goes first, a 2 if your program goes second
	int move_first;
	std::cin >> move_first;
	if (move_first == 1)
	{
		// if you go first, print out your first move
		int my_move = bestMove(p);
		p.Play(my_move);
		std::cout << my_move + 1;
	}
	else
	{
		// if you go second, print out a ? so the moderator knows your program is ready
		std::cout << "?";
	}

	// Until the game comes to an end:
	// Read in your opponent's move: an integer between 1 and 7, 1 being the leftmost column and 7 begin the rightmost
	// Output your move, again an integer between 1 and 7 indicating the column you are placing your piece in
	// If the program reads a number <= 0, it is a code indicating the game has ended, and your program should exit
	int opp_move;
	bool win = false;
	while (std::cin >> opp_move)
	{
		if (win) break;

		if (opp_move <= 0)
			break;

		if (p.isWinningMove(opp_move - 1)) win = true;
		p.Play(opp_move - 1);
		p.print();

		int my_move = bestMove(p) + 1;
		if (my_move <= 0)
			break;
		if (p.isWinningMove(my_move - 1)) win = true;
		p.Play(my_move - 1);
		std::cout << my_move;
		p.print();
	}

	return 0;
}