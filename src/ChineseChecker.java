import java.util.ArrayList;
import java.util.Scanner;

public class ChineseChecker {

    private State currentState;
    private State startState;
    private int level;

    public ChineseChecker() {
        currentState = new State();
        currentState.board = new Character[17][17];
        startState = new State();
        startState.board = new Character[17][17];
        // the initial state with every player at his starting position
        int greenEnd = 12;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 17; j++) {
                if (j < greenEnd || j > 12) {
                    currentState.board[i][j] = '-';
                    startState.board[i][j] = '-';
                } else {
                    currentState.board[i][j] = 'g';
                    startState.board[i][j] = 'g';
                }
            }
            greenEnd--;
        }
        int blueEnd = 8, yellowEnd = 17;
        for (int i = 4; i < 9; i++) {
            for (int j = 0; j < 17; j++) {
                if (j < 4 || j >= yellowEnd) {
                    currentState.board[i][j] = '-';
                    startState.board[i][j] = '-';
                } else if (j >= 4 && j < blueEnd) {
                    currentState.board[i][j] = 'b';
                    startState.board[i][j] = 'b';
                } else if (j >= 13 && j < yellowEnd) {
                    currentState.board[i][j] = 'y';
                    startState.board[i][j] = 'y';
                } else {
                    currentState.board[i][j] = '*';
                    startState.board[i][j] = '*';
                }
            }
            blueEnd--;
            yellowEnd--;
        }
        int purpleStart = 3, orangeStart = 12;
        for (int i = 9; i < 13; i++) {
            for (int j = 0; j < 17; j++) {
                if (j < purpleStart || j > 12) {
                    currentState.board[i][j] = '-';
                    startState.board[i][j] = '-';
                } else if (j >= purpleStart && j < 4) {
                    currentState.board[i][j] = 'p';
                    startState.board[i][j] = 'p';
                } else if (j >= orangeStart && j <= 12) {
                    currentState.board[i][j] = 'o';
                    startState.board[i][j] = 'o';
                } else {
                    currentState.board[i][j] = '*';
                    startState.board[i][j] = '*';
                }
            }
            purpleStart--;
            orangeStart--;
        }
        int redEnd = 8;
        for (int i = 13; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (j < 4 || j >= redEnd) {
                    currentState.board[i][j] = '-';
                    startState.board[i][j] = '-';
                } else {
                    currentState.board[i][j] = 'r';
                    startState.board[i][j] = 'r';
                }
            }
            redEnd--;
        }
    }

    private ArrayList<State> allMoves(int player, State state) {
        ArrayList<State> moves = new ArrayList<State>();
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (player == 0) {
                    if (state.board[i][j] == 'r' || state.board[i][j] == 'o' || state.board[i][j] == 'y') {
                        if (canMoveMarble(state, i, j)) {
                            ArrayList<State> possibleMovesForThisState = getPossibleMoves(state, i, j);
                            for (State possibleMove : possibleMovesForThisState) {
                                moves.add(possibleMove);
                            }
                            ArrayList<State> possibleJumpMovesForThisState = getPossibleJumpMoves(state, i, j,
                                    new ArrayList<State>());
                            for (State possibleJumpMove : possibleJumpMovesForThisState) {
                                moves.add(possibleJumpMove);
                            }
                        }
                    }
                } else {
                    if (state.board[i][j] == 'g' || state.board[i][j] == 'b' || state.board[i][j] == 'p') {
                        if (canMoveMarble(state, i, j)) {
                            ArrayList<State> possibleMovesForThisState = getPossibleMoves(state, i, j);
                            for (State possibleMove : possibleMovesForThisState) {
                                moves.add(possibleMove);
                            }
                            ArrayList<State> possibleJumpMovesForThisState = getPossibleJumpMoves(state, i, j,
                                    new ArrayList<State>());
                            for (State possibleJumpMove : possibleJumpMovesForThisState) {
                                moves.add(possibleJumpMove);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    private boolean isValid(State state, int i, int j, int newI, int newJ) {
        // check if the new position is inside the board
        if (newI < 0 || newI > 16 || newJ < 0 || newJ > 16) {
            return false;
        }
        // check if the new position is empty
        if (state.board[newI][newJ] != '*')
            return false;
        // check if the marble is not moving to a triangle other than the goal
        char color = state.board[i][j];
        if (color == 'o') {
            if (startState.board[newI][newJ] == 'o' || startState.board[newI][newJ] == 'b'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;
        } else if (color == 'r') {
            if (startState.board[newI][newJ] == 'r' || startState.board[newI][newJ] == 'g'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;
        } else if (color == 'y') {
            if (startState.board[newI][newJ] == 'y' || startState.board[newI][newJ] == 'p'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;

        } else if (color == 'g') {
            if (startState.board[newI][newJ] == 'g' || startState.board[newI][newJ] == 'r'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;

        } else if (color == 'b') {
            if (startState.board[newI][newJ] == 'b' || startState.board[newI][newJ] == 'o'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;

        } else if (color == 'p') {
            if (startState.board[newI][newJ] == 'p' || startState.board[newI][newJ] == 'y'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;

        }
        return false; // should never reach here
    }

    private boolean isPossibleMove(State state, int i, int j, int newI, int newJ) {
        boolean isPossible = false;
        // normal moves
        if ((newI == i && newJ == j + 1) || (newI == i && newJ == j - 1) || (newI == i + 1 && newJ == j)
                || (newI == i + 1 && newJ == j - 1) || (newI == i - 1 && newJ == j)
                || (newI == i - 1 && newJ == j + 1)) {
            isPossible = true;
        } else { // jumps
            if ((newI == i && newJ == j + 2 && state.board[i][j + 1] != '*')
                    || (newI == i && newJ == j - 2 && state.board[i][j - 1] != '*')
                    || (newI == i + 2 && newJ == j && state.board[i + 1][j] != '*')
                    || (newI == i + 2 && newJ == j - 2 && state.board[i + 1][j - 1] != '*')
                    || (newI == i - 2 && newJ == j && state.board[i - 1][j] != '*')
                    || (newI == i - 2 && newJ == j + 2 && state.board[i - 1][j + 1] != '*')) {
                isPossible = true;
            }
        }
        return isPossible;
    }

    private boolean playerValidMove(State state, int i, int j, int newI, int newJ) {
        // check if the coordinates are inside the board
        if (newI < 0 || newI > 16 || newJ < 0 || newJ > 16 || i < 0 || i > 16 || j < 0 || j > 16) {
            return false;
        }
        // check if the new position is empty
        if (state.board[newI][newJ] != '*')
            return false;
        // check that the marble to move is valid
        if (state.board[i][j] != 'g' && state.board[i][j] != 'b' && state.board[i][j] != 'p')
            return false;
        // check if the move is possible
        if (!isPossibleMove(state, i, j, newI, newJ)) {
            return false;
        }
        // check if the marble is not moving to a triangle other than the goal
        char color = state.board[i][j];
        if (color == 'g') {
            if (startState.board[newI][newJ] == 'g' || startState.board[newI][newJ] == 'r'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;
        } else if (color == 'b') {
            if (startState.board[newI][newJ] == 'b' || startState.board[newI][newJ] == 'o'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;
        } else if (color == 'p') {
            if (startState.board[newI][newJ] == 'p' || startState.board[newI][newJ] == 'y'
                    || startState.board[newI][newJ] == '*') {
                return true;
            } else
                return false;
        }
        return false; // should never reach here
    }

    private boolean canMoveMarble(State state, int row, int col) {
        char color = state.board[row][col];
        if (color == 'r') {
            // the goal is green position
            int greenStart = 12, greenEnd = 12;
            for (int i = 0; i < 4; i++) {
                for (int j = greenStart; j <= greenEnd; j++) {
                    if (i == row && j == col) {
                        return false;
                    }
                    if (state.board[i][j] != 'r') {
                        return true;
                    }
                }
                greenStart--;
            }
            return false; // should never reach here
        } else if (color == 'g') {
            // the goal is red position
            int redStart = 4, redEnd = 4;
            for (int i = 16; i > 12; i--) {
                for (int j = redStart; j <= redEnd; j++) {
                    if (i == row && j == col) {
                        return false;
                    }
                    if (state.board[i][j] != 'g') {
                        return true;
                    }
                }
                redEnd++;
            }
            return false; // should never reach here
        } else if (color == 'y') {
            // the goal is purple position
            int purpleStart = 12, purpleEnd = 12;
            for (int j = 0; j <= 3; j++) {
                for (int i = purpleStart; i >= purpleEnd; i--) {
                    if (i == row && j == col) {
                        return false;
                    }
                    if (state.board[i][j] != 'y') {
                        return true;
                    }
                }
                purpleEnd--;
            }
            return false; // should never reach here
        } else if (color == 'p') {
            // the goal is yellow position
            int yellowStart = 4, yellowEnd = 4;
            for (int j = 16; j >= 13; j--) {
                for (int i = yellowStart; i <= yellowEnd; i++) {
                    if (i == row && j == col) {
                        return false;
                    }
                    if (state.board[i][j] != 'p') {
                        return true;
                    }
                }
                yellowEnd++;
            }
            return false; // should never reach here
        } else if (color == 'b') {
            // the goal is blue position
            if (row == 12 && col == 12) {
                return false;
            }
            if (state.board[12][12] != 'b') {
                return true;
            }
            if (row == 12 && col == 11) {
                return false;
            }
            if (state.board[12][11] != 'b') {
                return true;
            }
            if (row == 11 && col == 12) {
                return false;
            }
            if (state.board[11][12] != 'b') {
                return true;
            }
            if (row == 12 && col == 10) {
                return false;
            }
            if (state.board[12][10] != 'b') {
                return true;
            }
            if (row == 11 && col == 11) {
                return false;
            }
            if (state.board[11][11] != 'b') {
                return true;
            }
            if (row == 10 && col == 12) {
                return false;
            }
            if (state.board[10][12] != 'b') {
                return true;
            }
            if (row == 12 && col == 9) {
                return false;
            }
            if (state.board[12][9] != 'b') {
                return true;
            }
            if (row == 11 && col == 10) {
                return false;
            }
            if (state.board[11][10] != 'b') {
                return true;
            }
            if (row == 10 && col == 11) {
                return false;
            }
            if (state.board[10][11] != 'b') {
                return true;
            }
            if (row == 9 && col == 12) {
                return false;
            }
            if (state.board[9][12] != 'b') {
                return true;
            }
            return false; // should never reach here
        } else { // color == 'o'
            // the goal is orange position
            if (row == 4 && col == 4) {
                return false;
            }
            if (state.board[4][4] != 'o') {
                return true;
            }
            if (row == 5 && col == 4) {
                return false;
            }
            if (state.board[5][4] != 'o') {
                return true;
            }
            if (row == 4 && col == 5) {
                return false;
            }
            if (state.board[4][5] != 'o') {
                return true;
            }
            if (row == 6 && col == 4) {
                return false;
            }
            if (state.board[6][4] != 'o') {
                return true;
            }
            if (row == 5 && col == 5) {
                return false;
            }
            if (state.board[5][5] != 'o') {
                return true;
            }
            if (row == 4 && col == 6) {
                return false;
            }
            if (state.board[4][6] != 'o') {
                return true;
            }
            if (row == 7 && col == 4) {
                return false;
            }
            if (state.board[7][4] != 'o') {
                return true;
            }
            if (row == 6 && col == 5) {
                return false;
            }
            if (state.board[6][5] != 'o') {
                return true;
            }
            if (row == 5 && col == 6) {
                return false;
            }
            if (state.board[5][6] != 'o') {
                return true;
            }
            if (row == 4 && col == 7) {
                return false;
            }
            if (state.board[4][7] != 'o') {
                return true;
            }
            return false; // should never reach here
        }
    }

    private ArrayList<State> getPossibleMoves(State state, int i, int j) {
        ArrayList<State> moves = new ArrayList<>();
        // normal moves
        if (isValid(state, i, j, i, j - 1)) {
            moves.add(move(state, i, j, i, j - 1));
        }
        if (isValid(state, i, j, i, j + 1)) {
            moves.add(move(state, i, j, i, j + 1));
        }
        if (isValid(state, i, j, i - 1, j)) {
            moves.add(move(state, i, j, i - 1, j));
        }
        if (isValid(state, i, j, i + 1, j)) {
            moves.add(move(state, i, j, i + 1, j));
        }
        if (isValid(state, i, j, i - 1, j + 1)) {
            moves.add(move(state, i, j, i - 1, j + 1));
        }
        if (isValid(state, i, j, i + 1, j - 1)) {
            moves.add(move(state, i, j, i + 1, j - 1));
        }
        return moves;
    }

    private ArrayList<State> getPossibleJumpMoves(State state, int i, int j, ArrayList<State> visitedMoves) {
        ArrayList<State> moves = new ArrayList<>();
        visitedMoves.add(state);
        // jump moves
        if (isValid(state, i, j, i, j - 2)) {
            if (state.board[i][j - 1] != '*' && state.board[i][j - 1] != '-') {
                if (!itemInList(visitedMoves, move(state, i, j, i, j - 2))) {
                    moves.add(move(state, i, j, i, j - 2));
                    ArrayList<State> nextMoves = getPossibleJumpMoves(move(state, i, j, i, j - 2), i, j - 2,
                            visitedMoves);
                    for (State s : nextMoves) {
                        moves.add(s);
                    }
                }
            }
        }
        if (isValid(state, i, j, i, j + 2)) {
            if (state.board[i][j + 1] != '*' && state.board[i][j + 1] != '-') {
                if (!itemInList(visitedMoves, move(state, i, j, i, j + 2))) {
                    moves.add(move(state, i, j, i, j + 2));
                    ArrayList<State> nextMoves = getPossibleJumpMoves(move(state, i, j, i, j + 2), i, j + 2,
                            visitedMoves);
                    for (State s : nextMoves) {
                        moves.add(s);
                    }
                }
            }
        }
        if (isValid(state, i, j, i - 2, j)) {
            if (state.board[i - 1][j] != '*' && state.board[i - 1][j] != '-') {
                if (!itemInList(visitedMoves, move(state, i, j, i - 2, j))) {
                    moves.add(move(state, i, j, i - 2, j));
                    ArrayList<State> nextMoves = getPossibleJumpMoves(move(state, i, j, i - 2, j), i - 2, j,
                            visitedMoves);
                    for (State s : nextMoves) {
                        moves.add(s);
                    }
                }
            }
        }
        if (isValid(state, i, j, i + 2, j)) {
            if (state.board[i + 1][j] != '*' && state.board[i + 1][j] != '-') {
                if (!itemInList(visitedMoves, move(state, i, j, i + 2, j))) {
                    moves.add(move(state, i, j, i + 2, j));
                    ArrayList<State> nextMoves = getPossibleJumpMoves(move(state, i, j, i + 2, j), i + 2, j,
                            visitedMoves);
                    for (State s : nextMoves) {
                        moves.add(s);
                    }
                }
            }
        }
        if (isValid(state, i, j, i - 2, j + 2)) {
            if (state.board[i - 1][j + 1] != '*' && state.board[i - 1][j + 1] != '-') {
                if (!itemInList(visitedMoves, move(state, i, j, i - 2, j + 2))) {
                    moves.add(move(state, i, j, i - 2, j + 2));
                    ArrayList<State> nextMoves = getPossibleJumpMoves(move(state, i, j, i - 2, j + 2), i - 2, j + 2,
                            visitedMoves);
                    for (State s : nextMoves) {
                        moves.add(s);
                    }
                }
            }
        }
        if (isValid(state, i, j, i + 2, j - 2)) {
            if (state.board[i + 1][j - 1] != '*' && state.board[i + 1][j - 1] != '-') {
                if (!itemInList(visitedMoves, move(state, i, j, i + 2, j - 2))) {
                    moves.add(move(state, i, j, i + 2, j - 2));
                    ArrayList<State> nextMoves = getPossibleJumpMoves(move(state, i, j, i + 2, j - 2), i + 2, j - 2,
                            visitedMoves);
                    for (State s : nextMoves) {
                        moves.add(s);
                    }
                }
            }
        }
        return moves;
    }

    private boolean itemInList(ArrayList<State> list, State state) {
        for (State s : list) {
            // check if boards are equal
            boolean equal = true;
            for (int i = 0; i < 17; i++) {
                for (int j = 0; j < 17; j++) {
                    if (s.board[i][j] != state.board[i][j]) {
                        equal = false;
                    }
                }
            }
            if (equal) {
                return true;
            }
        }
        return false;
    }

    private State move(State state, int i, int j, int i2, int j2) {
        State newState = new State();
        newState.board = new Character[17][17];
        for (int k = 0; k < 17; k++) {
            for (int l = 0; l < 17; l++) {
                newState.board[k][l] = state.board[k][l];
            }
        }
        newState.board[i][j] = '*';
        newState.board[i2][j2] = state.board[i][j];
        return newState;
    }

    private void utility(State state, int depth) {
        int playerTotalDistance = playerTotalDistance(state);
        int computerTotalDistance = computerTotalDistance(state);
        state.alpha = depth * (playerTotalDistance - computerTotalDistance);
        state.beta = depth * (playerTotalDistance - computerTotalDistance);
    }

    private int playerTotalDistance(State state) {
        // calculate the total distance between the player marbles and goal position
        int[] greenGoal = getFarestSpaceInGoal(currentState, 'g');
        int[] blueGoal = getFarestSpaceInGoal(currentState, 'b');
        int[] purpleGoal = getFarestSpaceInGoal(currentState, 'p');
        int totalDistance = 0;
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == 'g') {
                    totalDistance += Math.abs(i - greenGoal[0]) + Math.abs(j - greenGoal[1]);
                } else if (state.board[i][j] == 'b') {
                    totalDistance += Math.abs(i - blueGoal[0]) + Math.abs(j - blueGoal[1]);
                } else if (state.board[i][j] == 'p') {
                    totalDistance += Math.abs(i - purpleGoal[0]) + Math.abs(j - purpleGoal[1]);
                }
            }
        }
        return totalDistance;
    }

    private int computerTotalDistance(State state) {
        // calculate the total distance between the computer marbles and the goal
        // position
        int[] redGoal = getFarestSpaceInGoal(currentState, 'r');
        int[] orangeGoal = getFarestSpaceInGoal(currentState, 'o');
        int[] yellowGoal = getFarestSpaceInGoal(currentState, 'y');
        int totalDistance = 0;
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == 'r') {
                    totalDistance += Math.abs(i - redGoal[0]) + Math.abs(j - redGoal[1]);
                } else if (state.board[i][j] == 'o') {
                    totalDistance += Math.abs(i - orangeGoal[0]) + Math.abs(j - orangeGoal[1]);
                } else if (state.board[i][j] == 'y') {
                    totalDistance += Math.abs(i - yellowGoal[0]) + Math.abs(j - yellowGoal[1]);
                }
            }
        }
        return totalDistance;
    }

    private int[] getFarestSpaceInGoal(State state, char color) {
        if (color == 'r') {
            int redStart = 12, redEnd = 12;
            for (int i = 0; i <= 3; i++) {
                for (int j = redStart; j <= redEnd; j++) {
                    if (state.board[i][j] == '*') {
                        return new int[] { i, j };
                    }
                }
                redStart--;
            }
        } else if (color == 'g') {
            int greenStart = 4, greenEnd = 4;
            for (int i = 16; i >= 13; i--) {
                for (int j = greenStart; j <= greenEnd; j++) {
                    if (state.board[i][j] == '*') {
                        return new int[] { i, j };
                    }
                }
                greenEnd++;
            }
        } else if (color == 'p') {
            int yellowStart = 4, yellowEnd = 4;
            for (int j = 16; j >= 13; j--) {
                for (int i = yellowStart; i <= yellowEnd; i++) {
                    if (state.board[i][j] == '*') {
                        return new int[] { i, j };
                    }
                }
                yellowEnd++;
            }
        } else if (color == 'y') {
            int purpleStart = 12, purpleEnd = 12;
            for (int j = 0; j <= 3; j++) {
                for (int i = purpleStart; i >= purpleEnd; i--) {
                    if (state.board[i][j] == '*') {
                        return new int[] { i, j };
                    }
                }
                purpleEnd--;
            }
        } else if (color == 'b') {
            if (state.board[12][12] == '*') {
                return new int[] { 12, 12 };
            } else if (state.board[12][11] == '*') {
                return new int[] { 12, 11 };
            } else if (state.board[11][12] == '*') {
                return new int[] { 11, 12 };
            } else if (state.board[12][10] == '*') {
                return new int[] { 12, 10 };
            } else if (state.board[11][11] == '*') {
                return new int[] { 11, 11 };
            } else if (state.board[10][12] == '*') {
                return new int[] { 10, 12 };
            } else if (state.board[12][9] == '*') {
                return new int[] { 12, 9 };
            } else if (state.board[11][10] == '*') {
                return new int[] { 11, 10 };
            } else if (state.board[10][11] == '*') {
                return new int[] { 10, 11 };
            } else if (state.board[9][12] == '*') {
                return new int[] { 9, 12 };
            }

        }

        else if (color == 'o') {
            if (state.board[4][4] == '*') {
                return new int[] { 4, 4 };
            } else if (state.board[5][4] == '*') {
                return new int[] { 5, 4 };
            } else if (state.board[4][5] == '*') {
                return new int[] { 4, 5 };
            } else if (state.board[6][4] == '*') {
                return new int[] { 6, 4 };
            } else if (state.board[5][5] == '*') {
                return new int[] { 5, 5 };
            } else if (state.board[4][6] == '*') {
                return new int[] { 4, 6 };
            } else if (state.board[7][4] == '*') {
                return new int[] { 7, 4 };
            } else if (state.board[6][5] == '*') {
                return new int[] { 6, 5 };
            } else if (state.board[5][6] == '*') {
                return new int[] { 5, 6 };
            } else if (state.board[4][7] == '*') {
                return new int[] { 4, 7 };
            }
        }
        if (color == 'r') {
            return new int[] { 2, 11 };
        } else if (color == 'o') {
            return new int[] { 5, 5 };
        } else if (color == 'y') {
            return new int[] { 11, 2 };
        } else if (color == 'g') {
            return new int[] { 14, 5 };
        } else if (color == 'b') {
            return new int[] { 11, 11 };
        } else if (color == 'p') {
            return new int[] { 5, 14 };
        }

        return null;

    }

    private State alphabeta(int player, State state, int level, int depth, boolean isMax) {

        if (depth == 0) {
            utility(state, depth + 1);
            return state;
        }
        ArrayList<State> possibleMoves = allMoves(player, state);
        if (possibleMoves.size() == 0)
            utility(state, depth + 1);
        for (State s : possibleMoves) {
            s.alpha = state.alpha;
            s.beta = state.beta;
            alphabeta(nextPlayer(player), s, level, depth - 1, !isMax);
            if (isMax) {
                if (s.beta > state.alpha) {
                    state.alpha = s.beta;
                }
            } else {
                if (s.alpha < state.beta) {
                    state.beta = s.alpha;
                }
            }
            if (state.beta <= state.alpha) {
                break;
            }
        }
        if (depth == level) {
            State bestState = bestState(possibleMoves, state.alpha);
            return bestState;
        }
        return state;

    }

    private State bestState(ArrayList<State> possibleMoves, int alpha) {
        for (State state : possibleMoves) {
            if (state.beta == alpha)
                return state;
        }
        return null;
    }

    private void printBoard(State state) {
        System.out.println("\t\t\t  a b c d e f g h i j k l m n o p q r s t u v w x y");
        for (int i = 0; i < 17; i++) {
            System.out.print((char) (i + 97));
            for (int j = 0; j < i; j++) {
                System.out.print("  ");
            }
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == '-') {
                    System.out.print("    ");
                } else {
                    System.out.print(" " + state.board[i][j] + "  ");
                }
            }
            System.out.println();
        }
    }

    private int nextPlayer(int player) {
        return (player + 1) % 2; // player 0 or 1
    }

    public void startGame() {
        this.printBoard(currentState);
        System.out.println(
                "Welcome to chinese checker game! please choose the dificulty level\n 1-Easy\n 2-Medium\n 3-Hard");
        Scanner sc = new Scanner(System.in);
        int level = sc.nextInt();
        if (level == 1) {
            this.level = 1;
        } else if (level == 2) {
            this.level = 3;
        } else if (level == 3) {
            this.level = 5;
        } else {
            System.out.println("Invalid input");
            this.startGame();
        }
        System.out.println("you are now playing with marbles of colors Green, Blue and Purple");
        System.out.println(
                "you can move your marbles by typing the coordinates of the marble you want to move and the coordinates of the place you want to move it to");
        int player = 1;
        play(player);

    }

    private void computerTurn() {
        State bestState = alphabeta(0, currentState, level, level, true);
        currentState = bestState;
        printBoard(currentState);
    }

    private void play(int player) {
        if (player == 1) {

            System.out.println("this is your turn");
            System.out.print("enter the row and column of the marble you want to move: ");
            Scanner sc = new Scanner(System.in);
            char row = sc.next().charAt(0);
            char col = sc.next().charAt(0);
            System.out.print("enter the row and column of the Position you want to move to (Must be valid move!): ");
            char row2 = sc.next().charAt(0);
            char col2 = sc.next().charAt(0);
            int[] from = new int[2], to = new int[2], toCheck = new int[2];
            try {
                from = getCoordinates(row, col);
                to = getCoordinates(row2, col2);
            } catch (Exception e) {
                System.out.println(e.toString());
                play(player);
            }
            if (playerValidMove(currentState, from[0], from[1], to[0], to[1])) {
                // update the board
                currentState = move(currentState, from[0], from[1], to[0], to[1]);
                printBoard(currentState);
                // if it's a jump move, you can jump again
                if (Math.abs(from[0] - to[0]) == 2 || Math.abs(from[1] - to[1]) == 2) {
                    char jampChoice = 'y';
                    while (jampChoice != 'n') {
                        // if there are possible jumps, you can jump again
                        if (getPossibleJumpMoves(currentState, to[0], to[1], new ArrayList<State>())
                                .size() > 0) {
                            System.out.println("you can jump again");
                            System.out.print("Do you want to jump again? (y/n): ");
                            jampChoice = sc.next().charAt(0);
                            if (jampChoice == 'y') {
                                System.out.print("enter the row and column of the marble you want to move: ");
                                row = sc.next().charAt(0);
                                col = sc.next().charAt(0);
                                System.out.print(
                                        "enter the row and column of the Position you want to move to (Must be valid move!): ");
                                row2 = sc.next().charAt(0);
                                col2 = sc.next().charAt(0);
                                try {
                                    from = getCoordinates(row, col);
                                    toCheck = getCoordinates(row2, col2);
                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                    continue;
                                }
                                if (playerValidMove(currentState, from[0], from[1], toCheck[0], toCheck[1])) {
                                    // check if the move is a jump move
                                    if (Math.abs(from[0] - toCheck[0]) == 2 || Math.abs(from[1] - toCheck[1]) == 2) {
                                        // add currentState to visited moves
                                        // update the board
                                        to[0] = toCheck[0];
                                        to[1] = toCheck[1];
                                        currentState = move(currentState, from[0], from[1], to[0], to[1]);
                                        printBoard(currentState);
                                        // check if the player has won
                                        if (isPlayerWinner()) {
                                            System.out.println("You won!");
                                            System.exit(0);
                                        }
                                    } else {
                                        System.out.println("You can only make jump moves now!");
                                        continue;
                                    }
                                } else {
                                    System.out.println("Invalid move!");
                                    continue;
                                }
                            } else if (jampChoice == 'n') {
                                continue;
                            } else {
                                System.out.println("Invalid input");
                            }
                        } else {
                            System.out.println("You can't jump again!");
                            break;
                        }
                    }

                }
                if (isPlayerWinner()) {
                    System.out.println("You won!");
                    System.exit(0);
                }
                play(nextPlayer(player));
            } else {
                System.out.println("invalid move, try again");
                play(player);
            }
        } else {
            System.out.println("Computer's turn");
            System.out.println("Computer is thinking");
            computerTurn();
            if (isComputerWinner()) {
                System.out.println("Computer won!");
                System.exit(0);
            }
            play(nextPlayer(player));
        }
    }

    private boolean isPlayerWinner() {
        int counter = 0;
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (currentState.board[i][j] != '*' || currentState.board[i][j] != '-') {
                    if (startState.board[i][j] == 'r' && currentState.board[i][j] == 'g')
                        counter++;
                    else if (startState.board[i][j] == 'o' && currentState.board[i][j] == 'b')
                        counter++;
                    else if (startState.board[i][j] == 'y' && currentState.board[i][j] == 'p')
                        counter++;
                }
            }
        }
        return counter == 30;
    }

    private boolean isComputerWinner() {
        int counter = 0;
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (currentState.board[i][j] != '*' || currentState.board[i][j] != '-') {
                    if (startState.board[i][j] == 'g' && currentState.board[i][j] == 'r')
                        counter++;
                    else if (startState.board[i][j] == 'b' && currentState.board[i][j] == 'o')
                        counter++;
                    else if (startState.board[i][j] == 'p' && currentState.board[i][j] == 'y')
                        counter++;
                }
            }
        }
        return counter == 30;
    }

    private int[] getCoordinates(char row, char col) throws Exception {
        if (row % 2 != col % 2)
            throw new Exception("Invalid coordinates, try again");
        int i = row - 97;
        int j = (col - 97) / 2 + (6 - i / 2);
        return new int[] { i, j };
    }
}
