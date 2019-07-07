import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private boolean[] sites;
    private int top;
    private int bottom;
    private int n;
    private WeightedQuickUnionUF puf; // UF for check percolates
    private WeightedQuickUnionUF fuf; // UF for check full

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        this.top = n * n;
        this.bottom = n * n + 1;
        this.puf = new WeightedQuickUnionUF(n * n + 2);
        this.fuf = new WeightedQuickUnionUF(n * n + 1);

        this.sites = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            sites[i] = false;
        }
    }

    private int index(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * n + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        sites[index(row, col)] = true;

        if (row - 1 >= 1 && isOpen(row - 1, col)) {
            puf.union(index(row - 1, col), index(row, col));
            fuf.union(index(row - 1, col), index(row, col));
        }
        if (row + 1 <= n && isOpen(row + 1, col)) {
            puf.union(index(row + 1, col), index(row, col));
            fuf.union(index(row + 1, col), index(row, col));
        }
        if (col - 1 >= 1 && isOpen(row, col - 1)) {
            puf.union(index(row, col - 1), index(row, col));
            fuf.union(index(row, col - 1), index(row, col));
        }
        if (col + 1 <= n && isOpen(row, col + 1)) {
            puf.union(index(row, col + 1), index(row, col));
            fuf.union(index(row, col + 1), index(row, col));
        }

        if (row == 1) {
            puf.union(index(row, col), top);
            fuf.union(index(row, col), top);
        }
        if (row == n) {
            puf.union(index(row, col), bottom);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return sites[index(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return fuf.connected(index(row, col), top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < sites.length; i++) {
            if (sites[i]) {
                count += 1;
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return puf.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        StdOut.println(!p.percolates());

        StdOut.println(!p.isFull(1, 1));
        p.open(1, 1);
        StdOut.println(p.isFull(1, 1));

        p.open(5, 5);
        StdOut.println(!p.percolates());
        StdOut.println(p.isOpen(5, 5));
        StdOut.println(!p.isOpen(4, 5));
        StdOut.println(p.numberOfOpenSites() == 2);
        p.open(4, 5);
        StdOut.println(!p.isOpen(1, 5));
        StdOut.println(p.isOpen(4, 5));
        StdOut.println(p.numberOfOpenSites() == 3);
        p.open(1, 5);
        p.open(2, 5);
        p.open(3, 5);
        p.open(4, 5);
        p.open(5, 5);
        StdOut.println(p.numberOfOpenSites() == 6);
        StdOut.println(p.percolates());

        p.open(4, 2);
        p.open(5, 2);
        StdOut.println(!p.isFull(4,2));

        Percolation p1 = new Percolation(1);
        StdOut.println(!p1.percolates());
        p1.open(1, 1);
        StdOut.println(p1.percolates());
    }
}