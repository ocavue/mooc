import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private boolean[] sites;
    private int top;
    private int bottom;
    private int n;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;
        this.top = n * n;
        this.bottom = n * n + 1;
        this.uf = new WeightedQuickUnionUF(n * n + 2);

        this.sites = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            sites[i] = false;
        }

        for (int col = 1; col <= n; col++) {
            uf.union(index(1, col), top);
        }
        for (int col = 1; col <= n; col++) {
            uf.union(index(n, col), bottom);
        }
    }

    private int index(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        sites[index(row, col)] = true;

        if (row - 1 >= 1 && isOpen(row - 1, col)) {
            uf.union(index(row - 1, col), index(row, col));
        }
        if (row + 1 <= n && isOpen(row + 1, col)) {
            uf.union(index(row + 1, col), index(row, col));
        }
        if (col - 1 >= 1 && isOpen(row, col - 1)) {
            uf.union(index(row, col - 1), index(row, col));
        }
        if (col + 1 <= n && isOpen(row, col + 1)) {
            uf.union(index(row, col + 1), index(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return sites[index(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return uf.connected(index(row, col), top);
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
        return uf.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        StdOut.println(!p.percolates());
        p.open(5, 5);
        StdOut.println(!p.percolates());
        StdOut.println(p.isOpen(5, 5));
        StdOut.println(!p.isOpen(4, 5));
        StdOut.println(p.numberOfOpenSites() == 1);
        p.open(4, 5);
        StdOut.println(!p.isOpen(1, 5));
        StdOut.println(p.isOpen(4, 5));
        StdOut.println(p.numberOfOpenSites() == 2);
        p.open(1, 5);
        p.open(2, 5);
        p.open(3, 5);
        p.open(4, 5);
        p.open(5, 5);
        StdOut.println(p.numberOfOpenSites() == 5);
        StdOut.println(p.percolates());
    }
}