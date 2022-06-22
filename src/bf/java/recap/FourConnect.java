package bf.java.recap;

import java.util.Scanner;

import static bf.java.recap.FourConnect.PLAYER.RED;
import static bf.java.recap.FourConnect.PLAYER.YELLOW;

public class FourConnect {

    public static int MIN_CHAIN = 4;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static char[][] board;
    private static final int BOARD_SIZE = 7;

    static enum PLAYER {
        RED,YELLOW
        
    }

    private static PLAYER turn = RED;

    public static final Scanner INPUT = new Scanner(System.in);

    private static void initBoard() {
        board = new char[BOARD_SIZE][];
        for (int i = 0; i< BOARD_SIZE; ++i) {
            board[i] = new char[BOARD_SIZE];
            for (int j = 0; j< BOARD_SIZE; ++j) {
                board[i][j] = '-';
            }
        }
    }

    private static void displayBoard() {
        for (int i = BOARD_SIZE -1; i>=0; i--) {
            for (int j = 0; j< BOARD_SIZE; ++j) {
                if(board[i][j]=='Y') {
                    System.out.print(ANSI_YELLOW +  board[i][j]+" " + ANSI_RESET);
                } else if (board[i][j]=='R') {
                    System.out.print(ANSI_RED +  board[i][j]+" " + ANSI_RESET);
                }else {
                    System.out.print(board[i][j]+" ");
                }
            }
            System.out.println();
        }
    }

    private static boolean putJeton(int column) {
        boolean moveOk = false;
        int line=0;
        while(line < BOARD_SIZE && board[line][column]!='-') {
            ++line;
        }
        if(line< BOARD_SIZE) {
            if(turn==YELLOW) {
                board[line][column] = 'Y';
            }else {
                board[line][column] = 'R';
            }
            moveOk = true;
        }
        return moveOk;
    }

    public static boolean hasGameEnded() {
        boolean end = false;
        int line=0;
        int col = 0;
        while(!end && line < BOARD_SIZE) {
            end = checkLine(line);
            ++line;
        }
        while(!end && col < BOARD_SIZE) {
            end = checkColumn(col);
            ++col;
        }
        line=0;
        col =0;
        while(!end && col < BOARD_SIZE) {
            if(col < BOARD_SIZE - MIN_CHAIN + 1) {end = checkDirection(line,col,1,1);}
            if(!end && col > BOARD_SIZE - MIN_CHAIN -1) {end = checkDirection(line,col,1,-1);}
            ++col;
        }
        line= BOARD_SIZE -1;
        col=0;
        while(!end && col < BOARD_SIZE) {
            if(col < BOARD_SIZE - MIN_CHAIN + 1) {end = checkDirection(line,col,-1,1);}
            if(!end && col > BOARD_SIZE - MIN_CHAIN -1) {end = checkDirection(line,col,-1,-1);}
            ++col;
        }
        return end;
    }

    public static boolean checkLine(int line) {
        return checkDirection(line,0,0,1);
    }

    public static boolean checkColumn(int col) {
        return checkDirection(0,col,1,0);
    }

    public static boolean checkDirection(int i, int j, int stepI, int stepJ) {
        int chain = 1;
        i +=stepI;
        j +=stepJ;
        while(chain< MIN_CHAIN &&  i>=0 && i< BOARD_SIZE &&  j>=0 && j < BOARD_SIZE) {
            if(board[i][j]=='-') {
                chain=0;
            }else if( (board[i][j]=='Y' || board[i][j]=='R') && board[i][j] == board[i-stepI][j-stepJ]) {
                chain++;
            }else if( (board[i][j]=='Y' || board[i][j]=='R') && board[i][j] != board[i-stepI][j-stepJ]  ) {
                chain = 1;
            }
            i+=stepI;
            j+=stepJ;
        }
        return chain==MIN_CHAIN;
    }

    public static void playTurn() {
        displayBoard();
        System.out.printf("Player %s choose a column to put your piece :\n",turn);
        while(!INPUT.hasNext("[1-7]")) {
            INPUT.next();
        }
        int column = INPUT.nextInt()-1;
        boolean isMovePossible = putJeton(column);
        if(isMovePossible) {
           switchPlayer();
        }else {
            System.out.println("Column already full. Replay\n");
        }
    }

    public static void switchPlayer() {
        if(turn==YELLOW) {turn = RED;}
        else {turn = YELLOW;}
    }
    
    public static boolean askReplay() {
        boolean replay = true;
        System.out.println("Do you want to replay ? Y/N");
        while(!INPUT.hasNext("[YN]")) {
            System.out.println("A valid option please.\n");
            INPUT.next();
        }
        String answer =  INPUT.next();
        if(answer.charAt(0)=='N') {
            replay = false;
        }
        return replay;
    }

    public static boolean isBoardFull() {
        boolean isFull = true;
        int i =0;
        while (isFull && i<BOARD_SIZE*BOARD_SIZE) {
            int line = i/BOARD_SIZE;
            int col = i%BOARD_SIZE;
            isFull = !(board[line][col]=='-');
            ++i;
        }
        return isFull;
    }

    public static void endGame() {
        System.out.println("Final board : ");
        displayBoard();
        if (!isBoardFull()) {
            switchPlayer();
            System.out.printf("Congratulation to %s\n",turn);
        }else {
            System.out.println("Game ended because the board is full\n");
        }
    }

    public static void main(String[] args) {
        initBoard();
        boolean replay = true;
        while (replay) {
            initBoard();
            while(!hasGameEnded() && !isBoardFull()) {
                playTurn();
            }
            endGame();
            replay = askReplay();
        }
    }
}
