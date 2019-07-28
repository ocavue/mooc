import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;
import java.util.Iterator;

public class Solver {
    private MinPQ<SearchNode> thisMQ;
    private MinPQ<SearchNode> twinMQ;
    private boolean isSolvable;
    private SearchNode solvedNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        Comparator<SearchNode> comparator = new ComparatorByHammingPriority();
        thisMQ = new MinPQ<SearchNode>(comparator);
        twinMQ = new MinPQ<SearchNode>(comparator);

        thisMQ.insert(new SearchNode(initial, 0, null));
        twinMQ.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode thisMinNode, twinMinNode;
        thisMinNode = thisMQ.delMin();
        twinMinNode = twinMQ.delMin();

        while (!(thisMinNode.board.isGoal() || twinMinNode.board.isGoal())) {
            for (Board neighbor : thisMinNode.board.neighbors()) {
                if (thisMinNode.previous == null || !neighbor.equals(thisMinNode.previous.board)) {
                    thisMQ.insert(new SearchNode(neighbor, thisMinNode.move + 1, thisMinNode));
                }
            }
            for (Board neighbor : twinMinNode.board.neighbors()) {
                if (twinMinNode.previous == null || !neighbor.equals(twinMinNode.previous.board)) {
                    twinMQ.insert(new SearchNode(neighbor, twinMinNode.move + 1, twinMinNode));
                }
            }
            thisMinNode = thisMQ.delMin();
            twinMinNode = twinMQ.delMin();
        }

        if (thisMinNode.board.isGoal()) {
            isSolvable = true;
            solvedNode = thisMinNode;
        } else {
            isSolvable = false;
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
            this.previous = previous;
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
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable) {
            return solvedNode.move;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return new SolutionIterable();
    }

    private class SolutionIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }
    }

    private class SolutionIterator implements Iterator<Board> {
        Board[] boards;
        int currentIndex = 0;

        SolutionIterator() {
            int length = 0;
            SearchNode node = solvedNode;
            while (node.previous != null) {
                length++;
                node = node.previous;
            }

            StdOut.println("xxxxx length");
            StdOut.println(length);

            boards = new Board[length];

            int index = length - 1;
            while (node.previous != null) {
                boards[index] = node.board;
                node = node.previous;
                index--;
            }
        }

        public boolean hasNext() {
            return currentIndex <= boards.length - 1;
        }

        public Board next() {
            Board next = boards[currentIndex];
            currentIndex++;
            return next;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}