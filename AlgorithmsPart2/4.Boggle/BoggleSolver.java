import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.SET;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)

    private TST<Integer> dict;

    public BoggleSolver(String[] dictionary) {
        this.dict = new TST<Integer>();

        for (String word : dictionary) {
            if (word.length() >= 3) {
                this.dict.put(word, points(word.length()));
            }
        }
    }

    private int points(int wordLength) {
        if (wordLength < 3)
            throw new IllegalArgumentException("wordLength < 3");
        if (wordLength <= 4)
            return 1;
        if (wordLength == 5)
            return 2;
        if (wordLength == 6)
            return 3;
        if (wordLength == 7)
            return 5;
        return 11;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException("board == null");

        SET<String> words = new SET<String>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                char letter = board.getLetter(row, col);
                // StdOut.println("letter: " + letter);
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                marked[row][col] = true;
                dfs("" + letter, marked, row, col, board, words);
            }
        }
        return words;
    }

    // Use Depth First Search to find all word starts with prefix
    private void dfs(String prefix, boolean[][] marked, int row, int col, BoggleBoard board, SET<String> words) {
        if (isWord(prefix)) {
            words.add(prefix);
        }
        if (!hasPrefix(prefix))
            return;
        marked[row][col] = true;
        for (int r = Math.max(0, row - 1); r <= Math.min(board.rows() - 1, row + 1); r++) {
            for (int c = Math.max(0, col - 1); c <= Math.min(board.cols() - 1, col + 1); c++) {
                if (marked[r][c])
                    continue;
                char letter = board.getLetter(r, c);
                dfs(pushLetter(prefix, letter), marked, r, c, board, words);
            }
        }
        marked[row][col] = false;
    }

    private String pushLetter(String prefix, char letter) {
        // StdOut.println("pushLetter: " + letter);
        char q = 'Q';
        if (letter == q){
            // StdOut.println( letter == q);
            return prefix + "QU";}
        else
            return prefix + letter;
    }

    private boolean hasPrefix(String prefix) {
        for (String word : dict.keysWithPrefix(prefix)) {
            return true;
        }
        return false;
    }

    private boolean isWord(String str) {
        return (str.length() >= 3) && dict.contains(str);
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new IllegalArgumentException("word == null");
        if (dict.contains(word)) {
            return dict.get(word);
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
