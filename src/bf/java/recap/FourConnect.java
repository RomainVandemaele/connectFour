package bf.java.recap;

import java.util.Scanner;

import static bf.java.recap.FourConnect.PLAYER.RED;
import static bf.java.recap.FourConnect.PLAYER.YELLOW;

public class FourConnect {

    public static int MIN_CHAIN = 4;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static char[][] board;
    private static final int SIZE = 7;

    static enum PLAYER {
        RED,YELLOW
    }

    private static PLAYER turn = RED;

    private static void initBoard() {
        board = new char[SIZE][];
        for (int i=0;i<SIZE;++i) {
            board[i] = new char[SIZE];
            for (int j=0;j<SIZE;++j) {
                board[i][j] = '-';
            }
        }
    }

    private static void displayBoard() {
        for (int i=SIZE-1;i>=0;i--) {
            for (int j=0;j<SIZE;++j) {
                if(board[i][j]=='Y') {
                    System.out.print(ANSI_YELLOW +  board[i][j]+" " + ANSI_RESET); ;
                } else if (board[i][j]=='R') {
                    System.out.print(ANSI_RED +  board[i][j]+" " + ANSI_RESET); ;
                }else {
                    System.out.print(board[i][j]+" "); ;
                }
            }
            System.out.println();
        }
    }

    private static boolean putJeton(int column) {
        boolean moveOk = true;
        int line=0;
        while(line < SIZE && board[line][column]!='-') {
            ++line;
        }
        if(line<SIZE) {
            if(turn==YELLOW) {
                board[line][column] = 'Y';
            }else {
                board[line][column] = 'R';
            }

        }else {
            System.out.println("Column already full");
            moveOk = false;
        }
        return moveOk;
    }

    public static boolean hasGameEnded() {
        boolean end = false;

        int i=0;
        while(!end && i < SIZE) {
            //end = checkLine(i);
            end = checkLine(i);
            ++i;
        }
        i=0;
        while(!end && i < SIZE) {
            end = checkColumn(i);
            ++i;
        }
        i=0;
        int j = 0;
        while(!end && j < SIZE) {
            if(j < SIZE- MIN_CHAIN + 1) {end = checkDirection(i,j,1,1);}
            if(!end && j > SIZE - MIN_CHAIN -1) {end = checkDirection(i,j,1,-1);}
            ++j;
        }
        i=SIZE-1;
        j=0;
        while(!end && j < SIZE) {
            if(j < SIZE- MIN_CHAIN + 1) {end = checkDirection(i,j,-1,1);}
            if(!end && j > SIZE - MIN_CHAIN -1) {end = checkDirection(i,j,-1,-1);}
            ++j;
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
        while(chain< 4 &&  i>=0 && i<SIZE &&  j>=0 && j < SIZE) {
            if(board[i][j]=='-') {
                chain=0;
            }else if( (board[i][j]=='Y' || board[i][j]=='R') && board[i][j] == board[i-stepI][j-stepJ]) {
                chain++;
            }else if( (board[i-stepI][j-stepJ]=='Y' || board[i-stepI][j-stepJ]=='R') && board[i][j] != board[i-stepI][j-stepJ]  ) {
                chain = 1;
            }
            i+=stepI;
            j+=stepJ;
        }
        return chain==MIN_CHAIN;
    }

    public static void main(String[] args) {
        initBoard();
        boolean replay = true;
        Scanner input = new Scanner(System.in);
        while (replay) {
            initBoard();
            System.out.println(PLAYER.YELLOW);
            turn = YELLOW;
            while(!hasGameEnded()) {
                displayBoard();

                System.out.printf("Player %s choose a column to put your piece :\n",turn);
                int column = -1;
                while(!input.hasNext("[1-7]")) {
                    input.next();
                }
                column = input.nextInt()-1;
                boolean res = putJeton(column);
                if(res) {
                    if(turn==YELLOW) {
                        turn = RED;
                    }else {
                        turn = YELLOW;
                    }
                }
            }
            System.out.println("Final board");
            displayBoard();
            if(turn==YELLOW) {
                System.out.println("Contratulation to RED\n");
            }else {
                System.out.println("Contratulation to YELLOW\n");
            }
            System.out.println("Do you want to replay ? Y/N");
            while(!input.hasNext("[YN]")) {
                System.out.println("A valid option please.\n");
                input.next();
            }
            String answer =  input.next();
            if(answer.charAt(0)=='N') {
                replay = false;
            }
        }


    }
}
