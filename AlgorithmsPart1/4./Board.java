import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] tiles;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
    }

    // string representation of this board
    public String toString() {
        String s = "";
        s += String.valueOf(n);
        s += "\n";
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                s += " ";
                s += String.valueOf(tiles[row][col]);
            }
            s += "\n";
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int sum = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int actual = tiles[row][col];
                int except = exceptVal(row, col);
                if (except == 0)
                    continue;
                if (actual != except)
                    sum++;
            }
        }
        return sum;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int row = 0; row <= n - 1; row++) {
            for (int col = 0; col <= n - 1; col++) {
                int value = tiles[row][col];
                int[] exceptCoordinate = exceptCoordinate(value);
                int exceptRow = exceptCoordinate[0];
                int exceptCol = exceptCoordinate[1];
                if (exceptRow == n - 1 && exceptCol == n - 1)
                    continue;
                sum += Math.abs(row - exceptRow);
                sum += Math.abs(col - exceptCol);
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != exceptVal(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    private int exceptVal(int row, int col) {
        assert 0 <= row && row <= n - 1;
        if (row == n - 1 && col == n - 1) {
            return 0;
        }
        return row * n + col + 1;
    }

    private int[] exceptCoordinate(int val) {
        if (val == 0) {
            return new int[] { n - 1, n - 1 };
        }
        return new int[] { (val - 1) / n, (val - 1) % n };
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board))
            return false;

        Board that = (Board) y;
        if (that.dimension() != n)
            return false;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (this.tiles[row][col] != that.tiles[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    // // all neighboring boards
    // public Iterable<Board> neighbors() {
    // }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                newTiles[row][col] = tiles[row][col];
            }
        }
        newTiles[0][0] = tiles[0][1];
        newTiles[0][1] = tiles[0][0];
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board b3;

        // @formatter:off
        b3 = new Board(new int[][]{
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 0 },
        });
        // @formatter:on
        StdOut.println(b3.toString());

        assert b3.dimension() == 3;
        assert b3.isGoal();
        assert b3.hamming() == 0;
        assert b3.manhattan() == 0;

        assert b3.exceptVal(0, 0) == 1;
        assert b3.exceptVal(0, 1) == 2;
        assert b3.exceptVal(0, 2) == 3;
        assert b3.exceptVal(1, 0) == 4;
        assert b3.exceptVal(1, 1) == 5;
        assert b3.exceptVal(1, 2) == 6;
        assert b3.exceptVal(2, 0) == 7;
        assert b3.exceptVal(2, 1) == 8;
        assert b3.exceptVal(2, 2) == 0;

        assert b3.exceptCoordinate(1)[0] == 0 && b3.exceptCoordinate(1)[1] == 0;
        assert b3.exceptCoordinate(2)[0] == 0 && b3.exceptCoordinate(2)[1] == 1;
        assert b3.exceptCoordinate(3)[0] == 0 && b3.exceptCoordinate(3)[1] == 2;
        assert b3.exceptCoordinate(4)[0] == 1 && b3.exceptCoordinate(4)[1] == 0;
        assert b3.exceptCoordinate(5)[0] == 1 && b3.exceptCoordinate(5)[1] == 1;
        assert b3.exceptCoordinate(6)[0] == 1 && b3.exceptCoordinate(6)[1] == 2;
        assert b3.exceptCoordinate(7)[0] == 2 && b3.exceptCoordinate(7)[1] == 0;
        assert b3.exceptCoordinate(8)[0] == 2 && b3.exceptCoordinate(8)[1] == 1;
        assert b3.exceptCoordinate(0)[0] == 2 && b3.exceptCoordinate(0)[1] == 2;

        // @formatter:off
        assert b3.equals(new Board(new int[][]{
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 0 },
        }));
        assert !b3.equals(new Board(new int[][]{
            { 0, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 1 },
        }));
        // @formatter:on

        // @formatter:off
        b3 = new Board(new int[][]{
            { 8, 1, 3 },
            { 4, 0, 2 },
            { 7, 6, 5 },
        });
        // @formatter:on
        assert b3.dimension() == 3;
        assert !b3.isGoal();
        assert b3.hamming() == 5;
        assert b3.manhattan() == 10;
    }

}