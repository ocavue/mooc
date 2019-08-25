import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// TODO:
// Throw a java.lang.IllegalArgumentException in the following situations:
//      The input to the constructor does not correspond to a rooted DAG.
//      Any of the noun arguments in distance() or sap() is not a WordNet noun.

public class WordNet {
    Integer N; // the number of nouns

    // constructor takes the name of the two input files
    // time: linearithmic
    // space: linear
    public WordNet(String synsetsPath, String hypernymsPath) {
        if (synsetsPath == null || hypernymsPath == null)
            throw new IllegalArgumentException();

        // String[] hypernyms = (new In(hypernymsPath)).readAllStrings();
        SET<String>[] synsets = readSynsets(synsetsPath);

        // Digraph digraph = new Digraph(N);
    }

    private SET<String>[] readSynsets(String synsetsPath) {
        String[] lines = (new In(synsetsPath)).readAllLines();
        SET<String>[] synsets = new SET[lines.length];
        for (String line : lines) {
            String[] fileds = line.split(",");
            SET<String> synset = new SET<String>();
            for (String noun : fileds[1].split(" ")) {
                synset.add(noun);
            }
            int index = Integer.parseInt(fileds[0]);
            synsets[index] = synset;
        }
        return synsets;
    }

    // returns all WordNet nouns
    // public Iterable<String> nouns() {
    // return new String[] { "", "" };// TODO
    // }

    // is the word a WordNet noun?
    // time: logarithmic in the number of nouns
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return false;// TODO
    }

    // distance between nounA and nounB (defined below)
    // time: linear in the size of the WordNet digraph
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        return 0;// TODO
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB in a shortest ancestral path (defined below)
    // time: linear in the size of the WordNet digraph
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        return "";// TODO
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets_small.txt", "");
    }
}