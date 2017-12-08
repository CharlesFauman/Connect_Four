#include <map>
#include <iostream>

class TranspositionTable {
private:

  // of type <key, value>
  std::map<unsigned long long, int> entries;

public:

  void put(unsigned long long key_, int val_) {
    entries[key_] = val_;
  }

  int get(unsigned long long key_) {
    if (entries.find(key_) != entries.end()) {
      return entries[key_];}
    return 0;
  }

};
