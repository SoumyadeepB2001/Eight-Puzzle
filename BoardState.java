import java.util.*;

public class BoardState implements Comparable<BoardState> {
    int[] board; // 1D array representing 3x3 board
    BoardState parent; // Previous state
    int g; // Cost from start
    int h; // Heuristic (Manhattan distance) i.e cost to goal

    // Constructor to create a new state
    public BoardState(int[] board, BoardState parent, int g) {
        this.board = new int[9];
        for (int i = 0; i < 9; i++) {
            this.board[i] = board[i]; // Copy board values manually
        }
        this.parent = parent;
        this.g = g;
        this.h = calculateHeuristic();
    }

    // Override the compareTo method to use in the priority queue in AutoSolve class
    @Override
    public int compareTo(BoardState other) { // Used for the priority queue
        return Integer.compare(this.getF(), other.getF());
    }

    // Check if current state is goal
    public boolean isGoal() {
        for (int i = 0; i < 8; i++) {
            if (board[i] != i + 1) {
                return false;
            }
        }
        return board[8] == 0;
    }

    // Generate possible next moves
    public ArrayList<BoardState> generateNeighbors() {
        ArrayList<BoardState> neighbors = new ArrayList<>();
        int zeroIndex = findZeroIndex();

        int row = zeroIndex / 3;
        int col = zeroIndex % 3;

        int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // up, down, left, right

        for (int i = 0; i < 4; i++) {
            int newRow = row + moves[i][0];
            int newCol = col + moves[i][1];

            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
                int newIndex = newRow * 3 + newCol;

                int[] newBoard = new int[9];
                for (int j = 0; j < 9; j++) {
                    newBoard[j] = board[j];
                }

                // Swap zero with the new position
                newBoard[zeroIndex] = newBoard[newIndex];
                newBoard[newIndex] = 0;

                BoardState neighbor = new BoardState(newBoard, this, this.g + 1);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    // Find the index of the blank tile (0)
    private int findZeroIndex() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                return i;
            }
        }
        return -1; // Should never happen
    }

    // Manhattan distance heuristic
    private int calculateHeuristic() {
        int total = 0;
        for (int i = 0; i < 9; i++) {
            int value = board[i];
            if (value != 0) {
                int correctRow = (value - 1) / 3;
                int correctCol = (value - 1) % 3;
                int currentRow = i / 3;
                int currentCol = i % 3;
                total += Math.abs(currentRow - correctRow) + Math.abs(currentCol - correctCol);
            }
        }
        return total;
    }

    // Total estimated cost (f = g + h)
    public int getF() {
        return g + h;
    }

    // Compare two states
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof BoardState))
            return false;
        BoardState other = (BoardState) obj;
        for (int i = 0; i < 9; i++) {
            if (this.board[i] != other.board[i]) {
                return false;
            }
        }
        return true;
    }

    // Use string version of board for hash code
    @Override
    public int hashCode() {
        String s = "";
        for (int i = 0; i < 9; i++) {
            s += board[i];
        }
        return s.hashCode();
    }
}
