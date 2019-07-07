import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int n;
    private int trials;
    private double[] results;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        this.trials = trials;
        this.results = new double[trials];
        for (int i = 0; i < trials; i++) {
            this.results[i] = experiment();
        }
    }

    // Run one experiment and get estimate of the percolation
    private double experiment() {
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!p.isOpen(row, col))
                p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (n * n);
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean == 0)
            mean = StdStats.mean(results);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev == 0)
            stddev = StdStats.stddev(results);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
    }

}