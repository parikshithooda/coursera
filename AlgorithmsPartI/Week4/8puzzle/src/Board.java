import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Board {

    private final int [][] tiles;
    private final int n;
    private final int hamming;
    private final int manhattan;
    private int blankRow, blankCol;
    private int numberOfNeighbors;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        /*
        Initialize tiles, n, blankRow and blankCol
         */
        if (tiles == null) throw new IllegalArgumentException();
        n = tiles.length;
        if (n < 2 || n >= 128) throw new IllegalArgumentException();
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (tiles[i] == null || tiles[i].length != n) throw new IllegalArgumentException();
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] < 0 || tiles[i][j] > n * n - 1) throw new IllegalArgumentException();
                this.tiles[i][j] = tiles[i][j];
                if (this.tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
        /*
        Initialize hamming and manhattan distances
         */
        this.hamming = calculateHamming();
        this.manhattan = calculateManhattan();
    }

    private int getFinalTileFromPosition(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) throw new IllegalArgumentException();
        if (row == n - 1 && col == n - 1) return 0;
        return (row * n + col) + 1;
    }
    private int calculateHamming() {
        int hamming = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != 0 && tiles[row][col] != getFinalTileFromPosition(row, col)) {
                    hamming++;
                }
            }
        }
        return hamming;
    }
    private int[] getFinalPositionFromTile(int tile) {
        if (tile < 0 || tile > n * n - 1) throw new IllegalArgumentException();

        if (tile == 0) return new int[]{n - 1, n - 1};
        return new int[]{(tile - 1) / n, (tile - 1) % n};
    }
    private int calculateManhattan() {
        int manhattan = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != 0) {
                    int[] finalPosition = getFinalPositionFromTile(tiles[row][col]);
                    manhattan += finalPosition[0] - row > 0 ? finalPosition[0] - row : row - finalPosition[0];
                    manhattan += finalPosition[1] - col > 0 ? finalPosition[1] - col : col - finalPosition[1];
                }
            }
        }
        return manhattan;
    }

    // string representation of this board
    public String toString() {
        String result = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result += " " + tiles[i][j];
            }
            result += "\n";
        }
        return result;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != Board.class) return false;

        if (this.n != ((Board) y).n) return false;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (this.tiles[row][col] != ((Board) y).tiles[row][col]) return false;
            }
        }
        return true;
    }

    private int[][][] findNeighbors() {
        // Utmost there could be 4 neighbors
        int[][][] neighbors = new int[4][n][n];
        numberOfNeighbors = 0;

        for (int row = blankRow - 1; row <= blankRow + 1; row++) {
            for (int col = blankCol - 1; col <= blankCol + 1; col++) {
                if (!(row == blankRow && col == blankCol) &&
                        row >= 0 && row < n &&
                        col >= 0 && col < n &&
                        !(row == blankRow - 1 && col == blankCol - 1) &&
                        !(row == blankRow - 1 && col == blankCol + 1) &&
                        !(row == blankRow + 1 && col == blankCol - 1) &&
                        !(row == blankRow + 1 && col == blankCol + 1)) {
                    neighbors[numberOfNeighbors] = copyTiles(tiles, n);
                    swap(neighbors[numberOfNeighbors], blankRow, blankCol, row, col);
                    numberOfNeighbors++;
                }
            }
        }
        return neighbors;
    }
    private int[][] copyTiles(int[][] tiles, int n) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[][][] neighbors = findNeighbors();
        return new NeighborsIterable(neighbors);
    }
    private class NeighborsIterable implements Iterable<Board> {
        int[][][] neighbors;
        int i = 0;

        public NeighborsIterable(int[][][] neighbors) {
            this.neighbors = neighbors;
        }

        @Override
        public Iterator<Board> iterator() {
            return new Iterator<Board>() {
                @Override
                public boolean hasNext() {
                    return i < numberOfNeighbors;
                }
                @Override
                public Board next() {
                    return new Board(neighbors[i++]);
                }
            };
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = copyTiles(tiles, n);
        if (copy[0][0] != 0 && copy[0][1] != 0) {
            swap(copy, 0, 0, 0, 1);
        } else {
            swap(copy, 1, 0, 1, 1);
        }
        return new Board(copy);
    }

    private void swap(int[][] tiles, int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = new int[][]{new int[]{1, 0, 3}, new int[]{4, 2, 5}, new int[]{7, 8, 6}};

        Board board = new Board(tiles);
        StdOut.println(board);
        StdOut.println("----------------------- Neighbors ----------------------");
        for (Board neighbor: board.neighbors()) {
            StdOut.println(neighbor);
        }
        StdOut.println("------------------------- Twin -------------------------");
        StdOut.println(board.twin());

        board = new Board(new int[][]{new int[]{8, 1, 3}, new int[]{4, 0, 2}, new int[]{7, 6, 5}});
        StdOut.println("Hamming:: " + board.hamming());
        StdOut.println("Manhattan:: " + board.manhattan());
        StdOut.println("Goal:: " + board.isGoal());

        board = new Board(new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 0}});
        StdOut.println("Goal:: " + board.isGoal());
    }
}