import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// TODO:
// Throw a java.lang.IllegalArgumentException in the following situations:
//      The input to the constructor does not correspond to a rooted DAG.

@SuppressWarnings({ "rawtypes", "unchecked" })
public class WordNet {
    Integer N; // the number of nouns
    SET<String>[] synsets;
    String[] synstrs;
    Digraph G;

    // constructor takes the name of the two input files
    // time: linearithmic
    // space: linear
    public WordNet(String synsetsPath, String hypernymsPath) {
        if (synsetsPath == null || hypernymsPath == null)
            throw new IllegalArgumentException();

        // String[] hypernyms = (new In(hypernymsPath)).readAllStrings();
        readSynsets(synsetsPath);
        G = new Digraph(synsets.length);
        addEdges(hypernymsPath, G);

    }

    private void readSynsets(String synsetsPath) {
        String[] lines = (new In(synsetsPath)).readAllLines();
        this.synsets = new SET[lines.length];
        this.synstrs = new String[lines.length];
        for (String line : lines) {
            String[] fileds = line.split(",");
            SET<String> synset = new SET<String>();
            for (String noun : fileds[1].split("\\s")) {
                synset.add(noun);
                // StdOut.println(">"+noun+"<");
            }
            int index = Integer.parseInt(fileds[0]);
            this.synsets[index] = synset;
            this.synstrs[index] = fileds[1];
        }
    }

    private void addEdges(String hypernymsPath, Digraph digraph) {

        String[] lines = (new In(hypernymsPath)).readAllLines();
        for (String line : lines) {
            String[] fileds = line.split(",");
            Integer hyponymIdx = Integer.parseInt(fileds[0]);
            for (int i = 1; i < fileds.length; i++) {
                Integer hypernymIdx = Integer.parseInt(fileds[i]);
                digraph.addEdge(hyponymIdx, hypernymIdx);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        SET<String> nouns = new SET<String>();
        for (SET<String> synset : synsets) {
            for (String noun : synset) {
                nouns.add(noun);
            }
        }
        return nouns;
    }

    // is the word a WordNet noun?
    // time: logarithmic in the number of nouns
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        for (String noun : nouns()) {
            if (noun.equals(word)) {
                return true;
            }
        }
        return false;
    }

    // distance between nounA and nounB (defined below)
    // time: linear in the size of the WordNet digraph
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        Iterable<Integer> synidxAs = synidxs(nounA); // All synset indexs that containe nounA
        Iterable<Integer> synidxBs = synidxs(nounB); // All synset indexs that containe nounB
        if (nounA.equals(nounB))
            return 0;
        SAP sap = new SAP(G);
        return sap.length(synidxAs, synidxBs);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB in a shortest ancestral path (defined below)
    // time: linear in the size of the WordNet digraph
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        Iterable<Integer> synidxAs = synidxs(nounA); // All synset indexs that containe nounA
        Iterable<Integer> synidxBs = synidxs(nounB); // All synset indexs that containe nounB
        SAP sap = new SAP(G);
        Integer ancestor = sap.ancestor(synidxAs, synidxBs);
        if (ancestor == -1) {
            return null;
        } else {
            return synstrs[ancestor];
        }
    }

    // Get all synset indexs that containe noun
    private Iterable<Integer> synidxs(String noun) {
        SET<Integer> synidxs = new SET();
        for (int i = 0; i < synsets.length; i++) {
            SET<String> synset = synsets[i];
            if (synset.contains(noun)) {
                synidxs.add(i);
            }
        }
        if (synidxs.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return synidxs;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");
        Integer num = 0;
        for (String noun : net.nouns()) {
            StdOut.println(noun);
            num++;
            if (num > 10) {
                break;
            }
        }
        assert net.isNoun("1780s");
        assert !net.isNoun("THIS_WORD_DOES_NOT_EXIST");

        StdOut.println(net.sap("harm", "jump"));
        StdOut.println(net.distance("harm", "jump"));
    }
}