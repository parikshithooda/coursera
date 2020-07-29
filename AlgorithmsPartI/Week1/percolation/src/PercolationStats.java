import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private static final double CONF_CONST = 1.96;

    private final double[] perlocationThresholds;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("N must be greater than 0");
        if (trials <= 0) throw new IllegalArgumentException("Trials must be greater than 0");

        this.trials = trials;
        perlocationThresholds = new double[trials];

        for (int t = 0; t < this.trials; t++) {
            int[] nodes = new int[n * n];
            for (int i = 0; i < (n) * (n); i++) {
                nodes[i] = i;
            }
            StdRandom.shuffle(nodes);
            Percolation percolation = new Percolation(n);
            for (int i = 0; i < n * n; i++) {
                percolation.open((nodes[i] / n) + 1, (nodes[i] % n) + 1);
                if (percolation.percolates()) break;
            }
            if (percolation.percolates()) {
                perlocationThresholds[t] = (double) percolation.numberOfOpenSites() / (n * n);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.perlocationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.perlocationThresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (PercolationStats.CONF_CONST * stddev() / Math.sqrt(this.trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (PercolationStats.CONF_CONST * stddev() / Math.sqrt(this.trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.println("mean\t\t\t\t\t = " + percolationStats.mean());
        StdOut.println("stddev\t\t\t\t\t = " + percolationStats.stddev());
        StdOut.println("95% confidence interval\t = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }

}