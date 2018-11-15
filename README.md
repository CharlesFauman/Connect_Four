# Connect_Four

https://docs.google.com/document/d/1rxqRRSIC5Ic6HsEqEffofbZR4_pMisvyv1AVSZvewNU/edit?usp=sharing

import os
import logging
logging.getLogger("tensorflow").setLevel(logging.ERROR)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' 
import warnings
import sys
with warnings.catch_warnings():
	warnings.filterwarnings("ignore",category=FutureWarning)
	stderr = sys.stderr
	sys.stderr = open(os.devnull, 'w')
	from keras.models import load_model
	sys.stderr = stderr
	
import copy
import numpy as np

stdout = sys.stdout
sys.stdout = open(os.devnull, 'w')
score_model = load_model('connect_4_model.hdf5')
sys.stdout = stdout

class Board(object):
	def __init__(self):
		self.x_pieces = np.zeros((7,6))
		self.o_pieces = np.zeros((7,6))
		self.x_turn = True

	def __str__(self):
		line = ''
		line += 'x pieces: \n' + np.array2string(self.x_pieces)
		line += '\no pieces: \n' + np.array2string(self.o_pieces)
		line += '\nturn: ' + ('x' if self.x_turn else 'o')
		return line

	def to_np_array(self):
		return np.stack([self.x_pieces, self.o_pieces], axis = 2)

	def get_nn_scores(self):
		scores = np.zeros(7)-1

		valid_moves = self.get_valid_moves()
		for col in range(len(valid_moves)):
			if valid_moves[col]:
				new_b = copy.deepcopy(b)
				new_b.play_move(col)
				if(self.x_turn): scores[col] = score_model.predict(new_b.to_np_array()[None,:])
				else: scores[col] = 1 - score_model.predict(new_b.to_np_array()[None,:])
					
		return scores

	def play_move(self, col):
		row = 0
		while(row < 6 and not(self.x_pieces[col,row] == 0 and self.o_pieces[col,row] == 0)): row += 1
		if(self.x_turn): self.x_pieces[col,row] = 1
		else: self.o_pieces[col,row] = 1
		self.x_turn = (not self.x_turn)

	def get_valid_moves(self):
		moves = np.zeros(7, dtype = 'int8')
		for i in range(len(moves)):
			while(moves[i] < 6 and not(self.x_pieces[i,moves[i]] == 0 and self.o_pieces[i,moves[i]] == 0)): moves[i] += 1

		return moves < 6

if __name__ == "__main__":
	b = Board()
	
	print("p")
	sys.stdout.flush() 

	first = input("")
	if first == "1":
		my_move = np.argmax(b.get_nn_scores())
		print(my_move)
		b.play_move(my_move)
	elif first == "2":
		print("?")

	while True:
		them_move = int(input("")) - 1
		b.play_move(them_move)
		
		my_move = np.argmax(b.get_nn_scores())
		print(my_move+1)
		b.play_move(my_move)
