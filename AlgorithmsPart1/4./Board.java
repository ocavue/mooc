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

    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
    }

    // is this board the goal board?
    public boolean isGoal() {
    }

    // does this board equal y?
    public boolean equals(Object that) {
        if (!(that instanceof Board))
            return false;

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

    // all neighboring boards
    public Iterable<Board> neighbors() {
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
    }

    // unit testing (not graded)
    public static void main(String[] args) {
    }

}