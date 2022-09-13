# Chinese Checkers Game

### Project Description

A project done as a team project for AI course in FCAI-CU. It's a game where you can challenge the AI computer to get all your marbles to the opposite triangle and win the game. You can learn the game rules from this [video](https://www.youtube.com/watch?v=E0vSvWdNiUg)

### Concepts Used

- Alphabeta pruning algorithm

### Notes

- the heuristic that we use in the project is as follow:
	1. we calculate the distance between all marbles of player and the farest free space in his goal (playertotaldistance).
	2. we calculate the distance between all marbles of Computer and the farset free space in his goal (computertotaldistance).
	3. then the value of heuristic is palyertotaldistance - computertotaldistance , The higher the value, the better the state.

the heuristic means that The farther the player is from his goal and the closer the computer is to his target, the better state is.

- we are using this [representaion](BoardRepresentation.txt) for the board.

### How to run

- as console application
	1. download the source code.
	2. run the main function in Main.java.
	3. choose the difficulty of the game.
	4. start to enter the index of row and column of the marble that you need to move from.
	5. enter the index of row and column of the space that you need to move to (valid space).
	
- as GUI application
	1. download the source code and run GUI.java, or download the [exe](https://drive.google.com/file/d/1nPvznpCFvvC3ML1VouywuZCHL7OnUeOh/view?usp=sharing) and run it.
	2. choose the difficulty of the game.
	3. start playing by moving your marbles with the mouse.

