import java.util.ArrayList;
import java.util.Scanner;

public class ChineseChecker {

    private State currentState;
    private State startState;

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

    public ArrayList<State> allMoves(State state) {
        ArrayList<State> moves = new ArrayList<State>();
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == 'r' || state.board[i][j] == 'o' || state.board[i][j] == 'y') {
                    ArrayList<State> possibleMovesForThisState = getPossibleMoves(state, i, j);
                    for (State possibleMove : possibleMovesForThisState) {
                        moves.add(possibleMove);
                    }
                }
            }
        }
        return moves;
    }

    public boolean isValid(State state, int newI, int newJ) {
        return state.board[newI][newJ] == '*';
    }

    public ArrayList<State> getPossibleMoves(State state, int i, int j) {
        ArrayList<State> moves = new ArrayList<>();
        // normal moves
        if (isValid(state, i, j - 1)) {
            moves.add(move(state, i, j, i, j - 1));
        }
        if (isValid(state, i, j + 1)) {
            moves.add(move(state, i, j, i, j + 1));
        }
        if (isValid(state, i - 1, j)) {
            moves.add(move(state, i, j, i - 1, j));
        }
        if (isValid(state, i + 1, j)) {
            moves.add(move(state, i, j, i + 1, j));
        }
        if (isValid(state, i - 1, j + 1)) {
            moves.add(move(state, i, j, i - 1, j + 1));
        }
        if (isValid(state, i + 1, j - 1)) {
            moves.add(move(state, i, j, i + 1, j - 1));
        }
        return moves;
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
        newState.parent = state;
        return newState;
    }

    private void utility(State state) {
        int playerTotalDistance = playerTotalDistance(state);
        int computerTotalDistance = computerTotalDistance(state);
        state.utility = playerTotalDistance - computerTotalDistance;
    }

    private int playerTotalDistance(State state) {
        //calculate the total distance between the player marbles and the middle point of the goal
        int totalDistance = 0;
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == 'g') {
                    totalDistance += Math.abs(i - 14) + Math.abs(j - 5);
                } else if (state.board[i][j] == 'b') {
                    totalDistance += Math.abs(i - 11) + Math.abs(j - 11);
                } else if (state.board[i][j] == 'p') {
                    totalDistance += Math.abs(i - 5) + Math.abs(j - 14);
                }
            }
        }
        return totalDistance;
    }

    private int computerTotalDistance(State state) {
        //calculate the total distance between the computer marbles and the middle point of the goal
        int totalDistance = 0;
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == 'r') {
                    totalDistance += Math.abs(i - 2) + Math.abs(j - 11);
                } else if (state.board[i][j] == 'o') {
                    totalDistance += Math.abs(i - 5) + Math.abs(j - 5);
                } else if (state.board[i][j] == 'y') {
                    totalDistance += Math.abs(i - 11) + Math.abs(j - 2);
                }
            }
        }
        return totalDistance;
    }

    private State bestState(ArrayList<State> possibleMoves) {
        State bestState = possibleMoves.get(0);
        for (State state : possibleMoves) {
            if (state.utility > bestState.utility) {
                bestState = state;
            }
        }
        return bestState;
    }

    private State minimax(State state, int depth, boolean isMax) {
        // TODO: set the state parent to null
        ArrayList<State> possibleMoves = allMoves(state);
        State bestState = bestState(possibleMoves);
        // TODO: loop over depth to get the parent of the state
        return bestState;
    }

    private void printBoard(State state) {
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < 17; j++) {
                if (state.board[i][j] == '-') {
                    System.out.print("  ");
                } else {
                    System.out.print(state.board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private int nextPlayer(int player) {
        return (player + 1) % 2 + 1; // player 1 or 2
    }

    private void startGame() {
        this.printBoard(currentState);
        System.out.println("you are now playing with marbles of colors Green, Blue and Purple");
        System.out.println(
                "you can move your marbles by typing the coordinates of the marble you want to move and the coordinates of the place you want to move it to");
        int player = 1;
        play(player);

    }

    private void computerTurn() {
        State bestState = minimax(currentState, 0, false);
        currentState = bestState;
        printBoard(currentState);
    }

    private void play(int player) {
        if (player == 1) {
            System.out.println("this is your turn");
            System.out.print("enter the row and column of the marble you want to move: ");
            Scanner sc = new Scanner(System.in);
            int row = sc.nextInt();
            int col = sc.nextInt();
            System.out.print("enter the row and column of the Position you want to move to (Must be valid move!): ");
            int row2 = sc.nextInt();
            int col2 = sc.nextInt();
            sc.close();
            if ((currentState.board[row][col] == 'g' || currentState.board[row][col] == 'p'
                    || currentState.board[row][col] == 'b')
                    && isValid(currentState, row2, col2)) {
                currentState = move(currentState, row, col, row2, col2);
                printBoard(currentState);
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

    /*
     * public static void main(String[] args) {
     * Game game = new Game();
     * State state = new State();
     * game.initializeBoard(state);
     * game.printBoard(state);
     * int player = 1;
     * while (true) {
     * int i = 0, j = 0;
     * Scanner sc = new Scanner(System.in);
     * System.out.println("Player " + player + " enter the row and column");
     * i = sc.nextInt();
     * j = sc.nextInt();
     * if (state.board[i][j] == '*') {
     * state.board[i][j] = 'r';
     * game.printBoard(state);
     * player = game.nextPlayer(player);
     * } else {
     * System.out.println("Invalid move");
     * }
     * }
     * }
     */

    public static void main(String[] args) {
        ChineseChecker chineseChecker = new ChineseChecker();
        chineseChecker.printBoard(chineseChecker.currentState);
    }

}
