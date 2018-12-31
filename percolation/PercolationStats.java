/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double meanThreshold;
    private final double stddevThreshold;
    private final double low;
    private final double high;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            thresholds[i] = doOneExperiment(n) * 1.0 / n / n;
        }
        meanThreshold = StdStats.mean(thresholds);
        stddevThreshold = StdStats.stddev(thresholds);
        double tmp = 1.96 * stddevThreshold / Math.sqrt(trials);
        low = meanThreshold - tmp;
        high = meanThreshold + tmp;

    }

    // sample mean of percolation threshold
    public double mean() {
        return meanThreshold;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevThreshold;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return low;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return high;
    }

    // test client (described below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trails);

        System.out.println("mean\t\t\t\t\t = " + percolationStats.mean());
        System.out.println("stddev\t\t\t\t\t = " + percolationStats.stddev());
        System.out.println("95% confidence interval\t = [" + percolationStats.confidenceLo() +
                                   ", " + percolationStats.confidenceHi() + "]");
    }

    private int doOneExperiment(int n) {
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            percolation.open(row, col);
        }
        return percolation.numberOfOpenSites();
    }
}
