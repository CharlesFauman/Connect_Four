import Paul_functions as functions   
import sys
import copy
x = 1
print ("p")  
sys.stdout.flush()

board = [[0]*6 for i in range(7)]
#functions.printer(board)
r = int(input())
if (r == 1):
    AI = 1
    you = 2
    functions.placepiece1(3,board,AI)
    print (4)
    sys.stdout.flush()
elif (r == 2):
    AI = 2
    you = 1
    print ("?")
    sys.stdout.flush()

while(x):
    pos = int(input())
    if (pos == -1 or pos == -2 or pos == -3):
        break
    z = functions.placepiece1(pos - 1,board,you)
    if (z == -1):
        break
    z = functions.checkwin(board)
    #functions.printer(board)
    if(z > 0):
        break
    elif(z < 0):
        pass
    elif(z == 0):
        break
    y = functions.placewin(board,AI)
    if(y > 0):
        functions.placepiece1(y,board,AI)
        print (y+1)
        sys.stdout.flush()
        break
    sims = functions.run_sims(board,AI,you)
    if(sims == -1):
        #functions.printer(board)
        continue
    elif(sims == None):
        for i in range(7):
            cop = copy.deepcopy(board)
            y = functions.placepiece1(i,cop,AI)
            if(y == -1):
                continue
            else:
                board = cop
                print(y+1)
                break
            if(functions.checkwin(board) > 0):
                break
            #functions.printer(board)
    elif(sims >= 0):
        y = functions.placepiece1(sims,board,AI)
        print (y+1)
        sys.stdout.flush()
        if (y == -1):
            break
        if (functions.checkwin(board) > 0):
            break
        elif(functions.checkwin(board) == 0):
            break
        elif(functions.checkwin(board) < 0):
            pass
    #print("")
    #functions.printer(board)




#problem, since it is just randomness, it will place a move even if it is a guarentee that the opponent will win off of it. might need to implement the thing clint 
#was talking about simulating off of opponents move, this way you will see if next move is guarenteed win for them too.