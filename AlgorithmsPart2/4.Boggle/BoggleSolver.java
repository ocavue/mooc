import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)

    private TrieST26 dict;

    public class TrieST26 {
        private static final int R = 256; // A-Z

        private Node root; // root of trie
        private int n; // number of keys in trie

        // R-way trie node
        public class Node {
            private Integer val;
            private Node[] next = new Node[R];
        }

        /**
         * Initializes an empty string symbol table.
         */
        public TrieST26() {
        }

        /**
         * Returns the value associated with the given key.
         *
         * @param key the key
         * @return the value associated with the given key if the key is in the symbol
         *         table and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public Integer get(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to get() is null");
            Node x = get(root, key, 0);
            if (x == null)
                return null;
            return x.val;
        }

        /**
         * Does this symbol table contain the given key?
         *
         * @param key the key
         * @return {@code true} if this symbol table contains {@code key} and
         *         {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x;
            char c = key.charAt(d);
            return get(x.next[c], key, d + 1);
        }

        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table. If the value is
         * {@code null}, this effectively deletes the key from the symbol table.
         *
         * @param key the key
         * @param val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(String key, Integer val) {
            assert 65 <= val && val<= 65 + 26;

            root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Integer val, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                if (x.val == null)
                    n++;
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c] = put(x.next[c], key, val, d + 1);
            return x;
        }

        /**
         * Returns the number of key-value pairs in this symbol table.
         *
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return n;
        }

        /**
         * Is this symbol table empty?
         *
         * @return {@code true} if this symbol table is empty and {@code false}
         *         otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }

        /**
         * Returns all keys in the symbol table as an {@code Iterable}. To iterate over
         * all of the keys in the symbol table named {@code st}, use the foreach
         * notation: {@code for (Key key : st.keys())}.
         *
         * @return all keys in the symbol table as an {@code Iterable}
         */
        public Iterable<String> keys() {
            return keysWithPrefix("");
        }

        /**
         * Returns all of the keys in the set that start with {@code prefix}.
         *
         * @param prefix the prefix
         * @return all of the keys in the set that start with {@code prefix}, as an
         *         iterable
         */
        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> results = new Queue<String>();
            Node x = get(root, prefix, 0);
            collect(x, new StringBuilder(prefix), results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, Queue<String> results) {
            if (x == null)
                return;
            if (x.val != null)
                results.enqueue(prefix.toString());
            for (char c = 0; c < R; c++) {
                prefix.append(c);
                collect(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        /**
         * Returns all of the keys in the symbol table that match {@code pattern}, where
         * . symbol is treated as a wildcard character.
         *
         * @param pattern the pattern
         * @return all of the keys in the symbol table that match {@code pattern}, as an
         *         iterable, where . is treated as a wildcard character.
         */
        public Iterable<String> keysThatMatch(String pattern) {
            Queue<String> results = new Queue<String>();
            collect(root, new StringBuilder(), pattern, results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
            if (x == null)
                return;
            int d = prefix.length();
            if (d == pattern.length() && x.val != null)
                results.enqueue(prefix.toString());
            if (d == pattern.length())
                return;
            char c = pattern.charAt(d);
            if (c == '.') {
                for (char ch = 0; ch < R; ch++) {
                    prefix.append(ch);
                    collect(x.next[ch], prefix, pattern, results);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            } else {
                prefix.append(c);
                collect(x.next[c], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        /**
         * Returns the string in the symbol table that is the longest prefix of
         * {@code query}, or {@code null}, if no such string.
         *
         * @param query the query string
         * @return the string in the symbol table that is the longest prefix of
         *         {@code query}, or {@code null} if no such string
         * @throws IllegalArgumentException if {@code query} is {@code null}
         */
        public String longestPrefixOf(String query) {
            if (query == null)
                throw new IllegalArgumentException("argument to longestPrefixOf() is null");
            int length = longestPrefixOf(root, query, 0, -1);
            if (length == -1)
                return null;
            else
                return query.substring(0, length);
        }

        // returns the length of the longest string key in the subtrie
        // rooted at x that is a prefix of the query string,
        // assuming the first d character match and we have already
        // found a prefix match of given length (-1 if no such match)
        private int longestPrefixOf(Node x, String query, int d, int length) {
            if (x == null)
                return length;
            if (x.val != null)
                length = d;
            if (d == query.length())
                return length;
            char c = query.charAt(d);
            return longestPrefixOf(x.next[c], query, d + 1, length);
        }

        /**
         * Removes the key from the set if the key is present.
         *
         * @param key the key
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void delete(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to delete() is null");
            root = delete(root, key, 0);
        }

        private Node delete(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length()) {
                if (x.val != null)
                    n--;
                x.val = null;
            } else {
                char c = key.charAt(d);
                x.next[c] = delete(x.next[c], key, d + 1);
            }

            // remove subtrie rooted at x if it is completely empty
            if (x.val != null)
                return x;
            for (int c = 0; c < R; c++)
                if (x.next[c] != null)
                    return x;
            return null;
        }

    }

    public BoggleSolver(String[] dictionary) {
        this.dict = new TrieST26();

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

        TrieSET words = new TrieSET();
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                char letter = board.getLetter(row, col);
                // StdOut.println("letter: " + letter);
                marked[row][col] = true;
                dfs(pushLetter("", letter), marked, row, col, board, words);
                marked[row][col] = false;
            }
        }
        return words;
    }

    // Use Depth First Search to find all word starts with prefix
    private void dfs(String prefix, boolean[][] marked, int row, int col, BoggleBoard board, TrieSET words) {
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
        if (letter == q) {
            // StdOut.println( letter == q);
            return prefix + "QU";
        } else
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
