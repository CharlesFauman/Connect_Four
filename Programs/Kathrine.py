import sys
import copy

b0 = [[' ',' ',' ',' ',' ',' ',' '],
      [' ',' ',' ',' ',' ',' ',' '],
      [' ',' ',' ',' ',' ',' ',' '],
      [' ',' ',' ',' ',' ',' ',' '],
      [' ',' ',' ',' ',' ',' ',' '],
      [' ',' ',' ',' ',' ',' ',' '],
      [' ',' ',' ',' ',' ',' ',' ']]

def print_board(b):
	for r in range(len(b)-1,-1,-1):
		print(b[r])

def v_streak(p, rc, cc, b):
	s = 1
	trig = False
	for r in range(rc-1, -1, -1):
		if b[r][cc] == p:
			s += 1
		else:
			break
	return s

def h_streak(p, rc, cc, b):
	s = 1
	for c in range(cc+1, 7):
		if b[rc][c] == p:
			s += 1
		else:
			break
	for c in range(cc-1, -1, -1):
		if b[rc][c] == p:
			s += 1
		else:
			break
	return s

def d_streak(p, rc, cc, b):
	s = 1
	i = 1
	while rc+i < 6 and cc+i < 7:
		if b[rc+i][cc+i] == p:
			s += 1
			i += 1 
		else:
			break
	i = 1
	while rc-1 >= 0 and cc-i >= 0:
		if b[rc-i][cc-i] == p:
			s += 1 
			i += 1
		else:
			break
	return s

def dn_streak(p, rc, cc, b):
	s = 1
	i = 1
	while rc+i < 6 and cc-i >= 0:
		if b[rc+i][cc-i] == p:
			s += 1
			i += 1
		else:
			break
	i = 1
	while rc-i >= 0 and cc+i < 7:
		if b[rc-i][cc+i] == p:
			s += 1 
			i += 1
		else:
			break
	return s

def check_board(p, b):
	for r in range(6):
		for c in range(7):
			if b[r][c] == p:
				if h_streak(p, r, c, b) == 4 or v_streak(p, r, c, b) == 4 or d_streak(p, r, c, b) == 4 or dn_streak(p, r, c, b) == 4:
					return False
	return True

def via_r(cc, b):
	for r in range(6):
		if b[r][cc] == ' ': 
			return r
	return 9

def update_board(p, cc, bi):
	b = copy.deepcopy(bi)
	if via_r(cc-1, b) != 9:
		b[via_r(cc-1, b)][cc-1] = p
	return b

def compute(p, o, m, b):
	v = [.7, .8, .9, .95, .9, .8, .7]
	for c in range(7):
		r = via_r(c, b)
		if r != 9:		
			m[c] += h_streak(p, r, c, b) * v[c]
			m[c] += h_streak(o, r, c, b)
			
			m[c] += v_streak(p, r, c, b) * v[c]
			m[c] += v_streak(o, r, c, b)	
		
			m[c] += d_streak(p, r, c, b) * v[c]
			m[c] += d_streak(o, r, c, b)
		
			m[c] += dn_streak(p, r, c, b) * v[c]
			m[c] += dn_streak(o, r, c, b)
	return m
		
def winning(p, c, b):
	r =  via_r(c,b)
	if h_streak(p, r, c, b) == 4:
		return True
	if v_streak(p, r, c, b) == 4:
		return True
	if d_streak(p, r, c, b) == 4:
		return True
	if dn_streak(p, r, c, b) == 4:
		return True
	return False

def bestie(m):
	b = 3
	for c in range(7):
		if m[c] > m[b]:
			b = c
	return b
			
def do_it(p, o, b):
	for c in range(7):
		if winning(p, c, b):
			return c+1
	for c in range(7):
		if winning(o, c, b):
			return c+1
		
	m0 = [0,0,0,0,0,0,0]
	tb0 = copy.deepcopy(b)
	m0 = compute(p, o, m0, tb0)
		
	for c1 in range(7):
		if via_r(c1, tb0) != 9:
			tb1 = copy.deepcopy(tb0)
			tb1 = update_board(p, c1+1, tb1)
			
			m1 = [0,0,0,0,0,0,0]
			for c1o in range(7):
				if via_r(c1o, tb1) != 9:
					tb1o = copy.deepcopy(tb1)
					tb1o = update_board(o, c1o+1, tb1o)
					
					m1 = compute(p, o, m1, tb1o)
					m0[c1] += sum(m1)/74
		
	return bestie(m0)+1

print ('p')
sys.stdout.flush() 
first_or_second = int(input())

if first_or_second == 1:
	you = 'X'
	bad_guy = 'O'
	
	print('4')
	b0 = update_board(you, 4, b0)
	
	sys.stdout.flush()
else: 
	you = 'X'
	bad_guy = 'O'
	
	print('?') 
	sys.stdout.flush()
	
game = True

while game:
	opponent_move = int(input())
	b0 = update_board(bad_guy, opponent_move, b0)
	game = check_board(bad_guy, b0)
	
	if not game:
		break
	
	print(do_it(you, bad_guy, b0))
	b0 = update_board(you, do_it(you, bad_guy, b0), b0)
	game = check_board(you, b0)
	
	
	sys.stdout.flush()