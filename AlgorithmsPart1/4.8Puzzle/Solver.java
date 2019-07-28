import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;

public class Solver {
    private MinPQ<SearchNode> mq;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        Comparator<SearchNode> comparator = new ComparatorByHammingPriority();
        mq = new MinPQ<SearchNode>(comparator);

        mq.insert(new SearchNode(initial, 0, null));
        SearchNode minNode;
        minNode = mq.delMin();

        while (!minNode.board.isGold()) {
            for (Board neighbor : minNode.board.neighbors()) {
                if (!neighbor.equals(inNode.previous.board)) {
                    mq.insert(new SearchNode(neighbor, minNode.move + 1, minNode));
                }
            }
            minNode = mq.delMin();
        }
    }

    private class SearchNode {
        public Board board;
        public int move;
        public SearchNode previous;

        SearchNode(Board board, int move, SearchNode previous) {
            if (board == null)
                throw new IllegalArgumentException();

            this.board = board;
            this.move = move;
            this.previous = SearchNode;
        }

    }

    private class ComparatorByHammingPriority implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return (a.board.hamming() + a.move) - (b.board.hamming() + b.move);
        }
    }

    private class ComparatorByManhattanPriority implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return (a.board.manhattan() + a.move) - (b.board.manhattan() + b.move);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        return 0;
    }

    // // sequence of boards in a shortest solution
    // public Iterable<Board> solution() {
    // }

    // test client (see below)
    public static void main(String[] args) {
        // // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] tiles = new int[n][n];
        // for (int i = 0; i < n; i++)
        // for (int j = 0; j < n; j++)
        // tiles[i][j] = in.readInt();
        // Board initial = new Board(tiles);

        // // solve the puzzle
        // Solver solver = new Solver(initial);

        // // print solution to standard output
        // if (!solver.isSolvable())
        // StdOut.println("No solution possible");
        // else {
        // StdOut.println("Minimum number of moves = " + solver.moves());
        // for (Board board : solver.solution())
        // StdOut.println(board);
        // }
    }

}