
class Board {
public:
  // RUCTORS
  Board();
  Board(int first_);
  Board(const Board& b); // copy constructor

  // ACCESSOR
  int get_col_height(int col) const;
  int get_num_moves() const { return moves; }
  int get_first() const { return first; }
  unsigned long long key() const;

  // MODIFIER
  // Returns the move that has been made. Pass in opponent's previous move.
  // If opponent's previous was 0, that means that no move has been made.
  int make_move(int col=0);
  void push_move(int col, int player);
  void offset_player();

  void record_move(int col);

  // HELPERS
  bool can_move(int col) const;
  void print(); // for debugging purposes

  int board[7][6]; // indication of board
  int moves; // moves made
  int first; // who moved first
};
