import Paul_functions as functions  
import sys
x = 1
print ("p")  
sys.stdout.flush()

board = [[0]*6 for i in range(7)]
for j in range(7):
    board[j][5] = 0
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

#functions.printer(board)
while(x):
    pos = int(input()) - 1
    z = functions.placepiece1(pos,board,you)
    if (z == -1):
        #print("heeeeeeeeeeer")
        break
    z = functions.checkwin(board)
    #functions.printer(board)
    if(z > 0):
        #print("skinny penis")
        break
    #print(board)
    elif(z < 0):
        pass
    elif(z == 0):
        #print("asshole")
        break
    #print(board)
    y = functions.placewin(board,AI)
    if(y > 0):
        functions.placepiece1(y,board,AI)
        #print("your dumb")
        #print("y",y)
        print (y+1)
        sys.stdout.flush()
        break
    #print("here")
    sims = functions.run_sims(board,AI,you)
    if(sims == -1):
        #print("ahaha")
        #functions.printer(board)
        continue
    #print("aaaa")
    y = functions.placepiece1(sims,board,AI)
    #print("why neg 1")
    #print("y", y)
    print (y+1)
    sys.stdout.flush()
    if (y == -1):
        #print("here")
        #print(y)
        break
    if (functions.checkwin(board) > 0):
        #print("fuckoff")
        break
    elif(functions.checkwin(board) == 0):
        #print("suck the d")
        break
    elif(functions.checkwin(board) < 0):
        pass
    #print("")
    #functions.printer(board)
#print("")
#functions.printer(board)
#print("PINGUS")




#problem, since it is just randomness, it will place a move even if it is a guarentee that the opponent will win off of it. might need to implement the thing clint 
#was talking about simulating off of opponents move, this way you will see if next move is guarenteed win for them too.