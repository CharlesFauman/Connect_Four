import copy
from random import randint
import numpy as np
import time
import sys


#places a piece of a certain color that I tell it to. The "player" is defined by a 1 or a 2 depending on if i have first move or not
def placepiece1(pos,board,player):
    if(board[pos][5] > 0):
        #print("No")
        return -1
    for j in range (6):
        if (board[pos][j] > 0):
            pass
        elif(board[pos][j] == 0):
            board[pos][j] = player
            break
        elif(j == 6 and board[pos][j] == 0):
            board[pos][j] = player
            break
    j = 0
    if(board[pos][j] == board[pos][j+1] == board[pos][j+2] == board[pos][j+3] == board[pos][j+4] == board[pos][j+5] and board[pos][j] == 0):
        board[pos][0] = player
    return pos

#checks to see if there is a possible win 
def checkwin(board):
    #horizontal win:
    for i in range(6):
        for j in range(4):
            #print(i,"  ",j,"   ",board[j][i])
            if(board[j][i] == board[j+1][i] == board[j+2][i] == board[j+3][i] and board[j][i] > 0):
                #print("horizontal")
                return board[j][i]
    #vertical win
    for j in range(7):
        for i in range(3):
            if(board[j][i] ==  board[j][i+1] == board[j][i+2] == board[j][i+3] and board[j][i] > 0):
                #print("vertical")
                return board[j][i]
    #diagonal,up --> right
    for j in range(4):
        for i in range(3):
            if(board[j][i] == board[j+1][i+1] == board[j+2][i+2] == board[j+3][i+3] and board[j][i] > 0):
                #print("d,upright")
                return board[j][i]
    #diagonal, down --> left
    for j in range(4):
        for i in range(3):
            if(board[j][5-i] == board[j+1][4-i] == board[j+2][3-i] == board[j+3][2-i] and board[j][5-i] > 0):
                #print("d,downright")
                return board[j][i]
    #check for tie
    if(board[0][5] and board[1][5] and board[2][5] and board[3][5] and board[4][5] and board[5][5] and board[6][5]):
        return 0

    return -1

#this function checks to see if there is an obvious win for me and then makes that move. If it doesn't find one it just keeps going
def placewin(board,player):
    cpy = copy.deepcopy(board)
    for j in range(7):
        placepiece1(j,cpy,player)
        x = checkwin(cpy)
        cpy = copy.deepcopy(board)
        if(x == -1):
            y = -1
        elif(x == 0):
            y = 0
        elif(x > 0):
            y = j
            break
    return y
    
def treesearch(board,player):
    x = randint(0,6)
    y = placepiece1(x,board,player)
    if (y == 0):
        return 0

def printer(board):
    for i in range(6):
        for j in range(7):
            print(board[j][5-i],"",end = "")
            if(j == 6):
                print()

def secondsmallest(winnumyou):
    x1,x2 = float('inf'),float('inf')
    for i in winnumyou:
        #print("i",i,"","x1,x2",x1,x2)
        if i < x1:
            #print("here")
            x1,x2 = i,x1
        elif i == x2:
            continue 
        elif (i < x2 and i > x1):
            #print("suck it")
            x2 = i
        #print("i",i,"","x1,x2",x1,x2)
    return x2

def run_sims(board,AI,you):
    y = placewin(board,you)
    #print("win for them", y)
    if(y >= 0):
        placepiece1(y,board,AI)
        #print("in place")
        print (y+1)
        sys.stdout.flush()
        #print("yyyy")
        return -1
    #simcop = copy.deepcopy(board)
    winnumAI = [0,0,0,0,0,0,0]
    winnumyou = [0,0,0,0,0,0,0]
    for i in range(7):
        icop = copy.deepcopy(board)
        #printer(simcop)
        if(placepiece1(i,icop,AI) == -1):
            #print(i)
            #print("here")
            continue
        if(checkwin(icop) > 0):
            return i
        y = placewin(icop,you)
        if(y >= 0):
            #print("here",i)
            continue
        start = time.time()
        while(time.time() - start < 10/7):
            if(AI == 1):
                x = you
            elif(AI == 2):
                x = AI
            simcop = copy.deepcopy(icop)
            #printer(simcop)
            while(True):
                y = randint(0,6)
                if(placepiece1(y,simcop,x) == 0):
                    #print("here")
                    continue
                if(checkwin(simcop) == AI):
                    winnumAI[i] = winnumAI[i] + 1
                    #print("ayoooo")
                    #printer(simcop)
                    #print("")
                    break
                elif(checkwin(simcop) == you):
                    winnumyou[i] = winnumyou[i] + 1
                    #print("alsk")
                    break
                elif(checkwin(simcop) == 0):
                    winnumAI[i] = winnumAI[i] + randint(0,1)
                    break
                #print("check")
                #printer(simcop)
                #print("")
                if (x == AI):
                    x = you
                else:
                    x = AI
    mx = abs(max(winnumAI))
    mn = abs(max(winnumyou))
    if(mx > mn):
        index_min = np.argmax(winnumAI)
    elif(mx < mn):
        index_min = np.argmin(winnumyou)
        if(min(winnumyou) == 0):
           sec = secondsmallest(winnumyou)
           index_min = winnumyou.index(sec)
           #print(sec)
    #print(winnumAI)
    #print(winnumyou)
    #print("index_min",index_min)
    return index_min 

        
