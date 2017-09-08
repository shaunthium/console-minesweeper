import java.util.*;

public class Minesweeper {
    public static void main(String[] args) {
        System.out.println("Welcome to Minesweeper!");
        do {
            System.out.println("Choose your game mode: (easy, medium or hard)");
            Scanner sc = new Scanner(System.in);
            String mode = sc.nextLine();
            switch (mode.trim()) {
                case "easy":
                case "medium":
                case "hard":
                    startGame(mode.trim());
                    return;
            }
        } while (true);
    }

    private static void startGame(String mode) {
        int[][] board = constructBoard(mode);
        printBoard(board);
    }

    private static void printBoard(int[][] board) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                System.out.printf("%3d", board[i][j]);
            }
            System.out.println();
        }
    }

    private static void incrementMines(int[][] board, int row, int col) {
        for (int i=row-1; i<=row+1; i++) {
            if (i>=0 && i<board.length) {
                for (int j=col-1; j<=col+1; j++) {
                    if (j>=0 && j<board[0].length) {
                        if (board[i][j] != -1) board[i][j]++;
                    }
                }
            }
        }
    }

    private static int[][] constructBoard(String mode) {
        int numMines = 0;
        int boardSize = 0;
        switch (mode) {
            case "easy": {
                numMines = 10;
                boardSize = 8;
                break;
            }
            case "medium": {
                numMines = 40;
                boardSize = 16;
                break;
            }
            case "hard": {
                numMines = 99;
                boardSize = 24;
                break;
            }
        }
        int[][] board = new int[boardSize][boardSize];
        Random r = new Random();
        while (numMines > 0) {
            int row = r.nextInt(boardSize);
            int col = r.nextInt(boardSize);
            if (board[row][col] == 0) {
                // No mine there yet, add mine
                board[row][col] = -1;
                incrementMines(board, row, col);
                numMines--;
            }
        }
        return board;
    }
}
