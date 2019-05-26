import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[] id;
    //private int[] treeSize;
    private int[] openFlag;
    private int totalLen;
    private int openSiteCount;
    private int nRow;
    private int nCol;
    private int top;
    private int btm;
    private WeightedQuickUnionUF wqu;

    public Percolation(int n)                // create n-by-n grid, with all sites blocked
    {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0!!!");

        totalLen = n * n + 2;
        wqu = new WeightedQuickUnionUF(totalLen);
        openSiteCount = 0;
        //id = new int[totalLen];
        //treeSize = new int[totalLen];
        openFlag = new int[totalLen - 2];
        nRow = n;
        nCol = n;
        top = totalLen - 2;
        btm = totalLen - 1;

        for (int i = 0; i < totalLen - 2; i++) {
            //    id[i] = i;
            //    treeSize[i] = 0;
            openFlag[i] = 0;
        }

        //treeSize[top] = 1;
        //treeSize[btm] = 1;

    }

    /*
    public int root(int i) {
        while (id[i] != i) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    public void union(int src, int dst) {
        int i = root(src);
        int j = root(dst);
        if (i == j) return;

        if (treeSize[i] <= treeSize[j]) {
            id[i] = j;
            treeSize[j] += treeSize[i];
        }
        else {
            id[j] = i;
            treeSize[i] += treeSize[j];
        }
    }
    */

    public void open(int row, int col)    // open site (row, col) if it is not open already
    {
        if ((row < 1) || (row > nRow) || (col < 1) || (col > nCol))
            throw new IllegalArgumentException("row/col out of range!!!");

        if (!isOpen(row, col)) {
            //treeSize[(row - 1) * nCol + col - 1] = 1;
            openFlag[(row - 1) * nCol + col - 1] = 1;
            openSiteCount += 1;

            if (row == 1) {
                wqu.union((row - 1) * nCol + col - 1, top);
            }
            else {
                if (isOpen(row - 1, col)) {
                    wqu.union((row - 1) * nCol + col - 1, (row - 2) * nCol + col - 1);
                }
            }

            if (row == nRow) {
                wqu.union((row - 1) * nCol + col - 1, btm);
            }
            else {
                if (isOpen(row + 1, col)) {
                    wqu.union((row - 1) * nCol + col - 1, row * nCol + col - 1);
                }
            }

            if (col < nCol) {
                if (isOpen(row, col + 1)) {
                    wqu.union((row - 1) * nCol + col - 1, (row - 1) * nCol + col);
                }
            }

            if (col > 1) {
                if (isOpen(row, col - 1)) {
                    wqu.union((row - 1) * nCol + col - 1, (row - 1) * nCol + col - 2);
                }
            }
        }
    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        if ((row < 1) || (row > nRow) || (col < 1) || (col > nCol))
            throw new IllegalArgumentException("row/col out of range!!!");
        return openFlag[(row - 1) * nCol + col - 1] > 0;
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        if ((row < 1) || (row > nRow) || (col < 1) || (col > nCol))
            throw new IllegalArgumentException("row/col out of range!!!");
        return wqu.find((row - 1) * nCol + col - 1) == wqu.find(top);
    }

    public int numberOfOpenSites()       // number of open sites
    {
        return openSiteCount;
    }

    public boolean percolates()              // does the system percolate?
    {
        return wqu.find(top) == wqu.find(btm);
    }

    //public static void main(String[] args)   // test client (optional)
}
