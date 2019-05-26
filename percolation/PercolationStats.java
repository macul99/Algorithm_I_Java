/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int gridSize;
    private int trialNum;
    private double[] results;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if ((n <= 0) || (trials <= 0))
            throw new IllegalArgumentException("n and trials must be greater than 0!!!");

        gridSize = n;
        trialNum = trials;
        results = new double[trialNum];
        for (int i = 0; i < trialNum; i++) {
            results[i] = 0.0;
        }
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return StdStats.mean(results) / (gridSize * gridSize);
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(results) / (gridSize * gridSize);
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return mean() - 1.96 * stddev() / Math.sqrt(trialNum);
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return mean() + 1.96 * stddev() / Math.sqrt(trialNum);
    }

    public static void main(String[] args) {
        int gridSize = Integer.parseInt(args[0]);
        int trialNum = Integer.parseInt(args[1]);
        int bufSize = gridSize * gridSize;

        PercolationStats percStats = new PercolationStats(gridSize, trialNum);
        for (int i = 0; i < trialNum; i++) {
            System.out.print("trialNum: ");
            System.out.println(i);
            Percolation perc = new Percolation(gridSize);
            int idx = 0;
            int rdnIdx = 0;
            while (!perc.percolates()) {
                idx = 0;
                rdnIdx = StdRandom.uniform(bufSize - perc.numberOfOpenSites());
                for (int row = 1; row <= gridSize; row++) {
                    for (int col = 1; col <= gridSize; col++) {
                        if (!perc.isOpen(row, col)) {
                            if (idx == rdnIdx) {
                                //System.out.println(perc.isOpen(row, col));
                                perc.open(row, col);
                                //System.out.println(perc.isOpen(row, col));
                                //System.out.print("row: ");
                                //System.out.print(row);
                                //System.out.print(", col: ");
                                //System.out.print(col);
                                //System.out.print(", idx: ");
                                //System.out.println(idx);
                                break;
                            }
                            //System.out.print("row: ");
                            //System.out.print(row);
                            //System.out.print(", col: ");
                            //System.out.print(col);
                            //System.out.print(", idx: ");
                            //System.out.println(idx);
                            idx++;
                        }
                    }
                    if (idx == rdnIdx)
                        break;
                }
            }
            percStats.results[i] = perc.numberOfOpenSites();
        }
        System.out.println(percStats.mean());
        System.out.println(percStats.stddev());
        System.out.println(percStats.confidenceLo());
        System.out.println(percStats.confidenceHi());
    }
}
