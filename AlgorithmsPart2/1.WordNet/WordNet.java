// TODO:
// Throw a java.lang.IllegalArgumentException in the following situations:
//      The input to the constructor does not correspond to a rooted DAG.
//      Any of the noun arguments in distance() or sap() is not a WordNet noun.

public class WordNet {

    // constructor takes the name of the two input files
    // time: linearithmic
    // space: linear
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
    }

    // is the word a WordNet noun?
    // time: logarithmic in the number of nouns
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
    }

    // distance between nounA and nounB (defined below)
    // time: linear in the size of the WordNet digraph
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB in a shortest ancestral path (defined below)
    // time: linear in the size of the WordNet digraph
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}