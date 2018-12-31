/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF union;
    private final int n;
    private int count = 0;
    private final byte[] grid;
    private boolean isPercolates = false;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        grid = new byte[n * n];
        for (int i = 0; i < n * n; i++) {
            grid[i] = 0;
        }
        union = new WeightedQuickUnionUF(n * n);
    }

    private int indexOfRowAndCol(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * n + col - 1;
    }

    private boolean isSiteOpen(int index) {
        return (grid[index] & 1) == 1;
    }

    private boolean isSiteTop(int index) {
        return (grid[index] & 2) == 2;
    }

    private boolean isSiteBottom(int index) {
        return (grid[index] & 4) == 4;
    }

    private void openSite(int index) {
        grid[index] |= 1;
    }

    private void connectToTop(int index) {
        grid[index] |= 2;
    }

    private void connectToBottom(int index) {
        grid[index] |= 4;
    }

    private void connectToOtherSite(int index, int index1) {

        int root = union.find(index);
        if (index1 < 0 || index1 >= n * n || !isSiteOpen(index1))
            return;

        int root1 = union.find(index1);
        boolean isTop = isSiteTop(root);
        boolean isBottom = isSiteBottom(root);

        if (isSiteTop(root)) {
            connectToTop(root1);
        }
        else if (isSiteTop(root1)) {
            connectToTop(root);
            isTop = true;
        }

        if (isSiteBottom(root)) {
            connectToBottom(root1);
        }
        else if (isSiteBottom(root1)) {
            connectToBottom(root);
            isBottom = true;
        }

        isPercolates = isPercolates || (isTop && isBottom);

        union.union(root, root1);

    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = indexOfRowAndCol(row, col);
        if (isSiteOpen(index)) {
            return;
        }

        openSite(index);
        count++;

        int root = union.find(index);
        if (row == 1) {
            connectToTop(root);
            isPercolates |= isSiteBottom(root);
        }
        if (row == n) {
            connectToBottom(root);
            isPercolates |= isSiteTop(root);
        }

        if (row > 1)
            connectToOtherSite(index, index - n);

        if (row < n)
            connectToOtherSite(index, index + n);

        if (col > 1)
            connectToOtherSite(index, index - 1);

        if (col < n)
            connectToOtherSite(index, index + 1);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int index = indexOfRowAndCol(row, col);
        return isSiteOpen(index);
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = indexOfRowAndCol(row, col);
        return isSiteTop(union.find(index));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolates;
    }

    // test client (optional)
    public static void main(String[] args) {
        StdOut.println("test");
    }

    // public void printUnion() {
    //     for (int i = 1; i <= n; i++) {
    //         for (int j = 1; j <= n; j++) {
    //             int index = indexOfRowAndCol(i, j);
    //             int root = union.getParent(index);
    //
    //             if (root == index)
    //                 StdOut.print((char) 27 + "[34m\t(" + root + "," + union.find(index) + ")");
    //             else
    //                 StdOut.print((char) 27 + "[31m\t(" + root + "," + union.find(index) + ")");
    //         }
    //         StdOut.println();
    //     }
    //
    // }

}

