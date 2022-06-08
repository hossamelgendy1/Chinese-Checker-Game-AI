the heuristic that we use in the project is as follow:
1-we calculate the distance between all marbles of player and the farest free space in his goal (playertotaldistance).
2-we calculate the distance between all marbles of Computer and the farset free space in his goal (computertotaldistance).
3-then the value of heuristic is palyertotaldistance - computertotaldistance , The higher the value, the better the state.

the heuristic means that The farther the player is from his goal and the closer the computer is to his target, the better state is.


we are using this representaion for the board:-

- - - - - - - - - - - - g - - - -
- - - - - - - - - - - g g - - - -
- - - - - - - - - - g g g - - - -
- - - - - - - - - g g g g - - - -
- - - - b b b b * * * * * y y y y
- - - - b b b * * * * * * y y y -
- - - - b b * * * * * * * y y - -
- - - - b * * * * * * * * y - - -
- - - - * * * * * * * * * - - - -
- - - p * * * * * * * * o - - - -
- - p p * * * * * * * o o - - - -
- p p p * * * * * * o o o - - - -
p p p p * * * * * o o o o - - - -
- - - - r r r r - - - - - - - - -
- - - - r r r - - - - - - - - - -
- - - - r r - - - - - - - - - - -
- - - - r - - - - - - - - - - - -


--to run the code--
1-choose the difficulty of the game
2-start to enter the index of row and column of the marble that you need to move from.
3-enter the index of row and column of the space that you need to move to (valid space).

