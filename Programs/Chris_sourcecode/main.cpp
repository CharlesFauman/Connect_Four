#include <iostream>
#include <set>
#include "board.h"
#include "transposition.h"

std::set<int> movesThatWillLose(Board &b);
int needToBlockMove(Board &b);
int get_score(Board &B, int min, int max, int depth, int decrease_accuracy = 0);
bool is_winning_move(int col, Board b);

TranspositionTable transTable;

int main () {
	// 'p' is printed to indicate to moderator that AI is ready
	std::cout << "p";

	// Read in an integer so you know will go first.  This will be a 1 if your
	// program goes first, a 2 if your program goes second
	int move_first;
	std::cin >> move_first;

	Board b(move_first-1); // creates a new Connect-4 board

	if ( move_first == 1 ) {
		// if you go first, print out your first move
		std::cout << b.make_move(3);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(3);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(3);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(2);
		// std::cout << b.make_move(4);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(0);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(4);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(1);
		// std::cout << b.make_move(0);
	}
	else if ( move_first == 2 ) {
		std::cout << "?";
	}

	// Until the game comes to an end:
	// Read in your opponent's move: an integer between 1 and 7, 1 being the leftmost column and 7 begin the rightmost
	// Output your move, again an integer between 1 and 7 indicating the column you are placing your piece in
	// If the program reads a number <= 0, it is a code indicating the game has ended, and your program should exit
	int opp_move;
	while (std::cin >> opp_move) {
		if (opp_move <= 0) break;
		b.make_move(opp_move-1);
		// b.print();
		int ct = 0;
		int pos = 0;
		for (int i=0; i<7; i++) if (i != 3) ct += b.get_col_height(i);
		if (ct == 0 && b.can_move(3)) pos = 3;
		else if (ct == 0) pos = 2;
		else {
			std::set<int> moves = movesThatWillLose(b);
			int positions[7] = {-500};
			bool skip = false;
			int traverseOrder[7] = {3,4,2,5,1,6,0};
			bool edges = true;
			if (b.get_col_height(6) + b.get_col_height(0) +
					b.get_col_height(5) + b.get_col_height(1) == 0) edges = false;
			for (int i : traverseOrder) {
				// std::cout << "at " << i << std::endl;
				if (!edges && (i == 6 || i == 0)) positions[i] = -500;
				else {
					Board c(b);
					if (c.can_move(i) && is_winning_move(i,b)) {
						pos = i;
						skip = true;
						break;
					}
					else if (moves.find(i) != moves.end()) {
						// std::cout << "Loses at " << i+1 << std::endl;
						pos = i;
						skip = true;
						break;
					}
					else if (c.can_move(i)) {
						c.make_move(i);
						// c.print();
						// std::cout << "SCORE col "<<i+1<<": ";
						// Decreases in move look-ahead in order to speed up the first moves
						int decrease = 24-c.get_num_moves();
						if (decrease < 0) decrease = 0;
						positions[i] = get_score(c,-26,26,0,decrease);
						// std::cout << "SCORE: " << positions[i] << std::endl;
					}
				}
			}
			if (!skip) {
				int greatest = 5000;
				for (int i=0; i<7; i++) {
					if (positions[i] < greatest && positions[i] != -500 && b.can_move(i)) {
						pos = i;
						greatest = positions[i];
					}
				}
			}
		}
		std::cout /*<< "Making move: " */<< b.make_move(pos);
		// std::cout << "Next player: " << 1 + (b.get_num_moves()+b.get_first())%2 << std::endl;
		// b.print();



	}

	return 0;
}

std::set<int> movesThatWillLose(Board &b) {
	std::set<int> moves;
		Board c(b);
		c.offset_player();
		for (size_t j = 0; j < 7; j++) if (is_winning_move(j,c)) {
			moves.insert(j);
		}

	return moves;
}

std::set<int> movesThatWillLoseForGet(Board &b) {
	std::set<int> moves;
		Board c(b);
		for (size_t j = 0; j < 7; j++) {
			c.make_move(j);
			if (is_winning_move(j,c)) {
				moves.insert(j);
			}
		}
	return moves;
}

int get_score(Board &B, int min, int max, int depth, int decrease_accuracy) {
	int next = 7 - movesThatWillLose(B).size();
	if (next == 0) return -(42 - B.get_num_moves())/2;

  if (B.get_num_moves() >= 40-decrease_accuracy)
    return 0;

	int traverseOrder[7] = {3,4,2,5,1,6,0};
	for (int x : traverseOrder)
    if (B.can_move(x) && is_winning_move(x,B)) {
       return (43 - B.get_num_moves())/2;
		}
	// std::cout << " 1  " << min << "   " << max << "   " << depth << std::endl;
	int max_val = (41 - B.get_num_moves())/2;
	if (int val = transTable.get(B.key()))
	  max_val = val - 19;

  if (max > max_val) {
		// std::cout << "max " << max << " is greater than " << max_val << std::endl;
		max = max_val;
		if (min >= max) {
			// std::cout << "min " << min << " is greater than " << max << std::endl;
			return max;
		}
	}
	// std::cout << " 2  " << min << "   " << max << "   " << depth <<std::endl;
  for (int x : traverseOrder) {
    if (B.can_move(x)) {
      Board B2(B);
      B2.make_move(x);
      int score = -get_score(B2,-max,-min,depth+1,decrease_accuracy);
			if (score >= max) return score;
      if (score > min) min = score;
    }
	}
	// std::cout << " 3  " << min << "   " << max << "   " << depth <<std::endl;

	transTable.put(B.key(), min + 18);
  return min;
}

bool is_winning_move(int col, Board b) {
	// fetches current turn
	int current_player = 1 + (b.get_num_moves()+b.get_first())%2;
  // make the move with the duplicated board
  b.make_move(col);
  // go through each column and row and check for a winning move
  for (int i=0; i<7; i++) {
    int v_count = 0;
    for (int j=0; j<6; j++) {
      if (b.board[i][j] == current_player) {
        // check from this position for a winning move
        v_count++;
        if (v_count == 4) return true; // vertical win
        if ((i+3)<7 &&
            b.board[i+1][j] == current_player &&
            b.board[i+2][j] == current_player &&
            b.board[i+3][j] == current_player) return true; // horizontal win
        if ((i+3)<7 && (j+3)<6 &&
            b.board[i+1][j+1] == current_player &&
            b.board[i+2][j+2] == current_player &&
            b.board[i+3][j+3] == current_player) return true; // upwards diagonal win
        if ((i+3)<7 && (j-3)>=0 &&
            b.board[i+1][j-1] == current_player &&
            b.board[i+2][j-2] == current_player &&
            b.board[i+3][j-3] == current_player) return true; // downwards diagonal win
      }
      else {
        v_count = 0;
      }
    }
  }
  return false;
}
