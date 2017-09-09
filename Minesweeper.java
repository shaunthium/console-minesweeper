import java.util.*;

class Pair {
    int row;
    int col;

    public Pair(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

public class Minesweeper {
    public static void main(String[] args) {
        System.out.println("Welcome to Minesweeper!");
        do {
            System.out.println("Choose your game mode: (easy, medium or hard)");
            Scanner sc = new Scanner(System.in);
            String mode = sc.nextLine().trim();
            switch (mode) {
                case "easy":
                case "e":
                case "medium":
                case "m":
                case "hard":
                case "h":
                    startGame(sc, mode);
                    return;
            }
        } while (true);
    }

    private static void startGame(Scanner sc, String mode) {
        int[][] board = constructBoard(mode);
        int[][] status = new int[board.length][board.length];
        while (true) {
            printBoard(board, status);
            System.out.println("Make your move: (uncover/flag/unflag) (row) (col)");
            String move = sc.nextLine().trim();
            String[] moveArr = move.split(" ");
            if (moveArr.length != 3) {
                System.out.println("Invalid command, try again.");
                continue;
            }
            String action = moveArr[0];
            int row = Integer.parseInt(moveArr[1]);
            int col = Integer.parseInt(moveArr[2]);
            if (!performAction(action, board, status, row, col)) break;
        }
    }

    private static void uncoverSurrounding(int[][] board, int[][] status, Queue<Pair> queue) {
        while (!queue.isEmpty()) {
            Pair p = queue.poll();
            int row = p.row;
            int col = p.col;
            for (int i=row-1; i<=row+1; i++) {
                if (i < 0 || i >=board.length) continue;
                for (int j=col-1; j<=col+1; j++) {
                    if (i == row && j == col) continue; // Don't check itself
                    if (j < 0 || j >= board[0].length) continue; // Invalid index
                    if (board[i][j] == -1) return; // Stop uncovering once we hit a mine
                    if (board[i][j] != -1 && status[i][j] == 0) {
                        // No mine
                        status[i][j] = 1;
                        queue.offer(new Pair(i, j));
                    }
                }
            }
        }
    }

    private static boolean performAction(String action, int[][] board, int[][] status, int row, int col) {
        if (row < 0 || row >= status.length || col < 0 || col >= status[0].length) {
            System.out.println("Row/col out of bounds!");
            return true;
        }
        if (action.equals("flag") || action.equals("f")) {
            status[row][col] = -1;
            return true;
        }
        if (action.equals("unflag") || action.equals("uf")) {
            if (status[row][col] == -1) status[row][col] = 0;
            return true;
        }
        if (action.equals("uncover") || action.equals("u")) {
            int val = board[row][col];
            if (val == -1) {
                System.out.println("You uncovered a mine :(");
                printBoardCheatCode(board);
                System.out.println("Try again next time!");
                return false;
            }
            if (status[row][col] == 1) {
                System.out.println("Already uncovered!");
                return true;
            }
            status[row][col] = 1;
            Queue<Pair> queue = new LinkedList<Pair>();
            queue.offer(new Pair(row, col));
            uncoverSurrounding(board, status, queue);
            return checkComplete(board, status);
        }
        System.out.println("Invalid move!");
        return true;
    }

    private static boolean checkComplete(int[][] board, int[][] status) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if (board[i][j] != -1 && status[i][j] == 0) return true;
            }
        }
        System.out.println("Congratulations! You've won the game (:");
        return false;
    }

    private static void printBoard(int[][] board, int[][] status) {
        System.out.printf("%3s", " ");
        for (int i=0; i<board.length; i++) System.out.printf("%3d", i);
        System.out.println();
        for (int i=0; i<board.length; i++) {
            System.out.printf("%3d", i);
            for (int j=0; j<board[0].length; j++) {
                int stat = status[i][j];
                String s = "";
                if (stat == 0) s = "X"; // Still covered
                else if (stat == -1) s = "F"; // Flagged
                else if (stat == 1) {
                    // Uncovered
                    if (board[i][j] == 0) s = "O";
                    else s = String.valueOf(board[i][j]);
                }
                System.out.printf("%3s", s);
            }
            System.out.println();
        }
    }

    private static void printBoardCheatCode(int[][] board) {
        System.out.printf("%3s", " ");
        for (int i=0; i<board.length; i++) System.out.printf("%3d", i);
        System.out.println();
        for (int i=0; i<board.length; i++) {
            System.out.printf("%3d", i);
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
            case "easy":
            case "e":
                numMines = 10;
                boardSize = 8;
                break;
            case "medium":
            case "m":
                numMines = 40;
                boardSize = 16;
                break;
            case "hard":
            case "h":
                numMines = 99;
                boardSize = 24;
                break;
        }
        int[][] board = new int[boardSize][boardSize];
        Random r = new Random();
        while (numMines > 0) {
            int row = r.nextInt(boardSize);
            int col = r.nextInt(boardSize);
            if (row == 0 && col == 0) continue; // Allow top left corner
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
