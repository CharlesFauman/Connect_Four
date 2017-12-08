class Board(object):
    def __init__(self,board0=[['x','x','x','x','x','x'],['x','x','x','x','x','x'],['x','x','x','x','x','x'],['x','x','x','x','x','x'],['x','x','x','x','x','x'],['x','x','x','x','x','x'],['x','x','x','x','x','x']],open_cols0={0,1,2,3,4,5,6}):
        self.row = 6
        self.col = 7
        self.board = board0
        self.open_cols = open_cols0
        self.winner = None
        self.game_over = False
        self.count = 0
    def __str__(self):
        line = ''
        for i in range(self.row-1,-1,-1):
            for j in range(self.col):
                line += self.board[j][i] + ' '
            line += '\n'
        return line
    def __eq__(self,o):
        return Board(self.board,self.open_cols)
    def add(self,move,player,real_player=False):
        if real_player:
            move -= 1
        if not move in self.open_cols:
            #print("The col is full.")
            return
        if move in {-1,-2,-3,-4}:
            exit()
        if move == 99:
            self.game_over = True
            print(str(self))
            return
        move_added = False
        for i in range(len(self.board[move])-2,-1,-1):
            if not self.board[move][5] == 'x':
                print("Error.")
                break
            if self.board[move][i] == 'x':
                continue
            if not self.board[move][i] == 'x':
                self.board[move][i+1] = player
                self.winner = self.winCheck((move,i+1))
                move_added = True
                if i == 4:
                    self.open_cols.remove(move)
                break
        if not move_added:
            self.board[move][0] = player
            self.winner = self.winCheck((move,0))
        if real_player and self.winner:
            self.game_over = True    
    def remove(self, move):
        self.open_cols.add(move)
        self.winner = None
        for i in range(len(self.board[move])-1,-1,-1):
            if not self.board[move][i] == 'x':
                self.board[move][i] = 'x'
                break
        #check for win
    def winCheck(self, spot):
        self.count += 1
        #diagonal bottom left -> top right
        if spot[0]-3 >= 0 and spot[1]-3 >= 0:
            if b.board[spot[0]][spot[1]]==b.board[spot[0]-1][spot[1]-1]==b.board[spot[0]-2][spot[1]-2]==b.board[spot[0]-3][spot[1]-3]: return b.board[spot[0]][spot[1]]
        elif spot[0]-2 >= 0 and spot[1]-2 >= 0 and spot[0]+1<=6 and spot[1]+1<=5:
            if b.board[spot[0]+1][spot[1]+1]==b.board[spot[0]][spot[1]]==b.board[spot[0]-1][spot[1]-1]==b.board[spot[0]-2][spot[1]-2]: return b.board[spot[0]][spot[1]]
        elif spot[0]-1 >= 0 and spot[1]-1 >= 0 and spot[0]+2<=6 and spot[1]+2<=5:
            if b.board[spot[0]+2][spot[1]+2]==b.board[spot[0]+1][spot[1]+1]==b.board[spot[0]][spot[1]]==b.board[spot[0]-1][spot[1]-1]: return b.board[spot[0]][spot[1]]
        elif spot[0] >= 0 and spot[1] >= 0 and spot[0]+3<=6 and spot[1]+3<=5:
            if b.board[spot[0]+3][spot[1]+3]==b.board[spot[0]+2][spot[1]+2]==b.board[spot[0]+1][spot[1]+1]==b.board[spot[0]][spot[1]]: return b.board[spot[0]][spot[1]]
        #diagonal top left -> bottom right
        if spot[0]+3 <= 6 and spot[1]-3 >= 0:
            if b.board[spot[0]][spot[1]]==b.board[spot[0]+1][spot[1]-1]==b.board[spot[0]+2][spot[1]-2]==b.board[spot[0]+3][spot[1]-3]: return b.board[spot[0]][spot[1]]
        elif spot[0]+2 <=6 and spot[1]-2 >= 0 and spot[0]-1<=0 and spot[1]+1<=5:
            if b.board[spot[0]-1][spot[1]+1]==b.board[spot[0]][spot[1]]==b.board[spot[0]+1][spot[1]-1]==b.board[spot[0]+2][spot[1]-2]: return b.board[spot[0]][spot[1]]
        elif spot[0]+1 <=6 and spot[1]-1 >= 0 and spot[0]-2<=0 and spot[1]+2<=5:
            if b.board[spot[0]-2][spot[1]+2]==b.board[spot[0]-1][spot[1]+1]==b.board[spot[0]][spot[1]]==b.board[spot[0]+1][spot[1]-1]: return b.board[spot[0]][spot[1]]
        elif spot[0] <=6 and spot[1] >= 0 and spot[0]-3<=0 and spot[1]+3<=5:
            if b.board[spot[0]-3][spot[1]+3]==b.board[spot[0]-2][spot[1]+2]==b.board[spot[0]-1][spot[1]+1]==b.board[spot[0]][spot[1]]: return b.board[spot[0]][spot[1]]
        #horizontal
        if spot[0]-3>=0:
            if b.board[spot[0]-3][spot[1]]==b.board[spot[0]-2][spot[1]]==b.board[spot[0]-1][spot[1]]==b.board[spot[0]][spot[1]]: return b.board[spot[0]][spot[1]]
        elif spot[0]-2>=0 and spot[0]+1<=6:
            if b.board[spot[0]-2][spot[1]]==b.board[spot[0]-1][spot[1]]==b.board[spot[0]][spot[1]]==b.board[spot[0]+1][spot[1]]: return b.board[spot[0]][spot[1]]
        elif spot[0]-1>=0 and spot[0]+2<=6:
            if b.board[spot[0]-1][spot[1]]==b.board[spot[0]][spot[1]]==b.board[spot[0]+1][spot[1]]==b.board[spot[0]+2][spot[1]]: return b.board[spot[0]][spot[1]]
        elif spot[0]+3<=6:
            if b.board[spot[0]][spot[1]]==b.board[spot[0]+1][spot[1]]==b.board[spot[0]+2][spot[1]]==b.board[spot[0]+3][spot[1]]: return b.board[spot[0]][spot[1]]
    	#vertical
        if spot[1]-3>=0:
            if b.board[spot[0]][spot[1]-3]==b.board[spot[0]][spot[1]-2]==b.board[spot[0]][spot[1]-1]==b.board[spot[0]][spot[1]]: return b.board[spot[0]][spot[1]]
        elif spot[1]-2>=0 and spot[1]+1<=5:
            if b.board[spot[0]][spot[1]-2]==b.board[spot[0]][spot[1]-1]==b.board[spot[0]][spot[1]]==b.board[spot[0]][spot[1]+1]: return b.board[spot[0]][spot[1]]
        elif spot[1]-1>=0 and spot[1]+2<=5:
            if b.board[spot[0]][spot[1]-1]==b.board[spot[0]][spot[1]]==b.board[spot[0]][spot[1]+1]==b.board[spot[0]][spot[1]+2]: return b.board[spot[0]][spot[1]]
        elif spot[0]+3<=6:
            if b.board[spot[0]][spot[1]]==b.board[spot[0]][spot[1]+1]==b.board[spot[0]][spot[1]+2]==b.board[spot[0]][spot[1]+3]: return b.board[spot[0]][spot[1]]
    #yes i can use recursion, but this seemed easier believe it or not
    def look_ahead(self,):
        good_moves = list()
        bad_moves = list()
        for i in self.open_cols:
            self.add(i, us)
            if self.winner == us:
                good_moves = ["{}".format(i)]
                bad_moves = []
                break
            elif self.open_cols:
                for j in self.open_cols:
                    self.add(j, them)
                    if self.winner == them:
                        bad_moves.append("{}".format(i))
                    elif self.open_cols:
                        for k in self.open_cols:
                            self.add(k, us)
                            if self.winner == us:
                                good_moves.append("{}{}{}".format(i,j,k))
                            elif self.open_cols:
                                for l in self.open_cols:
                                    self.add(l, them)
                                    if self.winner == them:
                                        bad_moves.append("{}{}{}".format(i,j,k))
                                    elif self.open_cols:
                                        for m in self.open_cols:
                                            self.add(m, us)
                                            if self.winner == us:
                                                good_moves.append("{}{}{}{}{}".format(i,j,k,l,m))
                                            elif self.open_cols:
                                                for n in self.open_cols:
                                                    self.add(n, them)
                                                    if self.winner == them:
                                                        bad_moves.append("{}{}{}{}{}".format(i,j,k,l,m))
                                                    elif self.open_cols:
                                                        for o in self.open_cols:
                                                            self.add(o, us)
                                                            if self.winner == us:
                                                                good_moves.append("{}{}{}{}{}{}{}".format(i,j,k,l,m,n,o))
                                                            """
                                                            else:
                                                                for p in self.open_cols:
                                                                    self.add(p, them)
                                                                    if self.winner == them:
                                                                        bad_moves.append("{}{}{}{}{}{}{}".format(i,j,k,l,m,n,o))
                                                                    self.remove(p)
                                                            """
                                                            self.remove(o)
                                                    self.remove(n)
                                            self.remove(m)
                                    self.remove(l)
                            self.remove(k)
                    self.remove(j)
            self.remove(i)
        return good_moves, bad_moves                                  

them = 't'
us = 'u'
game_over = False

if __name__ == "__main__":
    b = Board()
    print("p")
    first = input("")
    if first == "2":
        print("?")
        them_move = int(input(""))
        b.add(them_move,them,True)
    elif first == "1":
        us_move = 4
        b.add(us_move,us,True)
        print(us_move)
        them_move = int(input(""))
        b.add(them_move,them,True)	
	

    while not b.game_over:
        good,bad = b.look_ahead()
        counts = [0,0,0,0,0,0,0]
        for move in good:
            counts[int(move[0])] += (7 - len(move))**3
        for move in bad:
            counts[int(move[0])] -= ((7 - len(move))**3)
        if not max(counts) == 0:
            us_move = counts.index(max(counts)) + 1
        else:
            counts.remove(0)
            us_move = counts.index(max(counts)) + 1
        b.add(us_move,us,True)
        print(us_move)
        if not b.game_over:
            #print(str(b))
            them_move = int(input(""))
            b.add(them_move,them,True)

    exit()