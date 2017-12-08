import random
import sys
import copy

def point(board, i, j):
    if(len(board[i])>j):
        return board[i][j]
    else:
        return '0'

def threes(board, player, movesOut): #checks for open lines of length 3 for player to play into in movesOut moves
    tres=[]
    for i in range(7):
        for j in range(6):
            if i<=3: #if we're on the left, check for rows
                check = point(board, i, j)+point(board, i+1, j)+point(board, i+2, j)+point(board, i+3, j)
                if check.count('0')==1 and check.count(player)==3:
                    spot = check.index('0')+i
                    var = j-len(board[spot])
                    if var == movesOut:
                        tres.append([i, j, spot])
            if j<=2: #if we're on the bottom, check for columns
                check = point(board, i, j)+point(board, i, j+1) + point(board, i, j+2) + point(board, i, j+3)
                if check.count('0')==1 and check.count(player)==3:
                    tres.append([i,j,i])
            if j<=2 and i<=3: #if we're in the lower left, check for positive-slope diagonals
                check = point(board, i, j)+point(board, i+1, j+1) + point(board, i+2, j+2) + point(board, i+3, j+3)
                if check.count(player)==3 and check.count('0')==1:
                    spot = check.index('0')+i
                    var = j+check.index('0')-len(board[spot])
                    if var == movesOut:
                        tres.append([i,j,spot])
            if j<=2 and i>=3: #if we're in the upper left, check for negative-slope diagonals
                check = point(board, i, j) + point(board, i-1, j+1)+point(board, i-2, j+2)+point(board, i-3, j+3)
                if check.count('0')==1 and check.count(player)==3:
                    spot = i-check.index('0')
                    var = j + check.index('0')-len(board[spot])
                    if var == movesOut:
                        tres.append([i,j,spot])
    return tres

def posVal(board, d): 
    if len(threes(board, '1', 0))>1:
        return 1000
    oppWin = threes(board, '2', 0)
    if len(oppWin)>0:
        return -1000
    if d == 0:
        return 10*(len(threes(board, '1', 1))-len(threes(board, '2', 1)))+len(threes(board, '1', 2))-len(threes(board, '2', 2))
    oppVar = [0,0,0,0,0,0,0]
    for i in range(7):
        var = [0,0,0,0,0,0,0]
        for j in range(7):
            nBoard = copy.deepcopy(board)
            nBoard[i]+='2'
            nBoard[j]+='1'
            var[j] = posVal(nBoard, d-1)
        oppVar[i] = max(var)
    return min(oppVar)

def getMove(board, depth):
    winningMove = threes(board, '1', 0) #checks for a winning move
    for possibleMove in winningMove:
        if len(board[possibleMove[2]])<6:
            return possibleMove[2]

    blockingMove = threes(board, '2', 0) #checks for a blocking move
    for possibleMove in blockingMove:
        if len(board[possibleMove[2]])<6:
            return possibleMove[2]

    if depth==0:
        while True: #simple random move generator
            randMove = random.randint(0, 6)
            if len(board[randMove])<6:
                return randMove
    posVals = [0,0,0,0,0,0,0]
    for i in range(7):
        nBoard = copy.deepcopy(board)
        nBoard[i]+='1'
        posVals[i] = posVal(nBoard, depth)
        if len(board[i])>5:
            posVals[i] = -10000
    bestWeight = max(posVals)
    numMoves = posVals.count(bestWeight)
    moveNum = random.randint(1,numMoves)
    for i in range(len(posVals)):
        if posVals[i] == bestWeight:
            moveNum = moveNum-1
            if moveNum == 0:
                if len(board[i])<6:
                    return i
    while True:
        randMove = random.randint(0, 6)
        if len(board[randMove])<6:
            return randMove

board=['','','','','','','']
print('p')
sys.stdout.flush()
inOne = int(input())
if inOne == 1:
    board[3]+='1'
    print('4')
    sys.stdout.flush()    
else:
    print("?")
    sys.stdout.flush()
oppMove = int(input())-1
while oppMove>=0:
    board[oppMove]+='2'
    move = getMove(board, 2)
    board[move]+='1'
    print(str(move+1))
    sys.stdout.flush()
    oppMove = int(input())-1
