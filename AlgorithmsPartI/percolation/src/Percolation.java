import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int STATE_OPEN = 1;
    private static final int STATE_CLOSED = 2;
    private static final int STATE_TOP_CONNECTED = 4;
    private static final int STATE_BOTTOM_CONNECTED = 8;


    private final int size;
    private int[][] siteStates;
    private int numberOfOpenSites;
    private boolean percolates;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    // maintains states of the root of site represented by index
    private int[] rootStates;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be greater than 0");
        this.size = n;
        this.numberOfOpenSites = 0;
        this.siteStates = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.siteStates[i][j] = STATE_CLOSED;
            }
        }
        this.percolates = false;
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(this.size * this.size + 2);
        this.rootStates = new int[this.size * this.size + 2];
        for (int i = 0; i < this.size * this.size + 2; i++) {
            this.rootStates[i] = STATE_CLOSED;
        }
    }

    private int getIndex(int row, int col) {
        return ((row - 1) * (this.size)) + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > this.size) throw new IllegalArgumentException("Row value must be between 1 to N");
        if (col <= 0 || col > this.size) throw new IllegalArgumentException("Col value must be between 1 to N");

        // If the site already open, nothing to do.
        if (isOpen(row, col))
            return;

        // Open the site
        this.siteStates[row - 1][col - 1] = STATE_OPEN;
        this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))] = STATE_OPEN;

        // If the site is in first row, connect it to the virtual site 1
        if (row == 1) {
            this.weightedQuickUnionUF.union(this.size * this.size, getIndex(row, col));
            this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))] =
                    this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))] | STATE_TOP_CONNECTED;
            this.siteStates[row - 1][col - 1] = this.siteStates[row - 1][col - 1] | STATE_TOP_CONNECTED;
        }
        if (row == this.size) {
            this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))] =
                    this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))] | STATE_BOTTOM_CONNECTED;
            this.siteStates[row - 1][col - 1] = this.siteStates[row - 1][col - 1] | STATE_BOTTOM_CONNECTED;
        }

        // Connect the site with all open neighboring sites
        for (int i = row - 1; i <= row + 1; i++) {
            if (i <= 0 || i > this.size) continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (j <= 0 || j > this.size) continue;
                if (i == row - 1 && (j == col - 1 || j == col + 1)) continue;
                if (i == row + 1 && (j == col - 1 || j == col + 1)) continue;

                if (isOpen(i, j)) {
                    int newState = this.rootStates[this.weightedQuickUnionUF.find(getIndex(i, j))] |
                                    this.siteStates[row - 1][col - 1];
                    this.weightedQuickUnionUF.union(getIndex(i, j), getIndex(row, col));
                    this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))] = newState;
                    this.siteStates[row - 1][col - 1] = newState;
                }
            }
        }

        int rootState = this.rootStates[this.weightedQuickUnionUF.find(getIndex(row, col))];
        if ((rootState & STATE_TOP_CONNECTED) != 0 && (rootState & STATE_BOTTOM_CONNECTED) != 0) {
            this.percolates = true;
            this.weightedQuickUnionUF.union((this.size * this.size + 1), getIndex(row, col));
        }

        this.numberOfOpenSites += 1;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > this.size) throw new IllegalArgumentException("Row value must be between 1 to N");
        if (col <= 0 || col > this.size) throw new IllegalArgumentException("Col value must be between 1 to N");
        return (this.siteStates[row - 1][col - 1] & STATE_OPEN) != 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > this.size) throw new IllegalArgumentException("Row value must be between 1 to N");
        if (col <= 0 || col > this.size) throw new IllegalArgumentException("Col value must be between 1 to N");
        return (isOpen(row, col) && this.weightedQuickUnionUF.find(getIndex(row, col)) == this.weightedQuickUnionUF.find(this.size * this.size));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.percolates;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(5, 4);
        percolation.open(4, 2);
        percolation.open(5, 2);
        StdOut.println(percolation.isFull(4, 2));
        percolation.open(3, 2);
        percolation.open(2, 2);
        StdOut.println(percolation.isFull(4, 2));
        StdOut.println(percolation.isFull(5, 2));
        StdOut.println(percolation.isFull(5, 4));
        StdOut.println(percolation.percolates());
    }
}