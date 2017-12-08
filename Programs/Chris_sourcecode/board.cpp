#include "board.h"
#include <iostream>
#include <math.h>

Board::Board() : moves(0), first(0) {
  // initialize a blank board
  for (int i=0; i<7; i++) for (int j=0; j<6; j++) board[i][j] = 0;
}

Board::Board(int first_) : moves(0) , first(first_) {
  // initialize a blank board
  for (int i=0; i<7; i++) for (int j=0; j<6; j++) board[i][j] = 0;
}

Board::Board(const Board& b) : moves(b.moves) {
  for (int i=0; i<7; i++) for (int j=0; j<6; j++) board[i][j] = b.board[i][j];
  moves = b.moves;
  first = b.first;
}

int Board::make_move(int col) {
  if (can_move(col)) {
    push_move(col,1 + (get_num_moves()+get_first())%2);
    return col + 1;
  }
  return 0;
}

void Board::push_move(int col, int player) {
  for (int i = 0; i < 6; i++) {
    if (board[col][i] == 0) {
      board[col][i] = player;
      break;
    }
  }
  moves++;
}

unsigned long long Board::key() const {
  unsigned long long key = 0;
  for (int i=0; i<7; i++)
    for (int j=0; j<6; j++) {
      if (board[i][j] == 1) key++;
      else if (board[i][j] == 2) key+=2;
      key=key*10;
    }
  return key;
}

void Board::offset_player() {
  moves--;
}

int Board::get_col_height(int col) const {
  int count = 0;
  for (int i=0; i<6; i++) {
    if (board[col][i] != 0) count++;
    else break;
  }
  return count;
}

bool Board::can_move(int col) const {
  return get_col_height(col) < 6;
}

void Board::print() {
  std::cout << std::endl << "BOARD" << std::endl;
  for (int i=0; i<7; i++){
    std::cout << "    ";
    for (int j=0; j<6; j++) {
      std::cout << board[i][j] << " ";
    }
    std::cout << std::endl;
  }
}
