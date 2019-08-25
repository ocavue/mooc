import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private String[] synsets;
    private HashMap<String, SET<Integer>> synsMap;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    // time: linearithmic
    // space: linear
    public WordNet(String synsetsPath, String hypernymsPath) {
        if (synsetsPath == null || hypernymsPath == null)
            throw new IllegalArgumentException();

        // String[] hypernyms = (new In(hypernymsPath)).readAllStrings();
        synsMap = new HashMap<String, SET<Integer>>();
        int size = readSynsets(synsetsPath);
        G = new Digraph(size);
        addEdges(hypernymsPath, G);
        validateRootedDGA(G);
        sap = new SAP(G);
    }

    private void validateRootedDGA(Digraph G) {
        if (!(new Topological(G)).hasOrder()) {
            throw new IllegalArgumentException("Not DGA");
        }
        int root = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0 && G.indegree(i) > 0) {
                root++;
            }
        }
        if (root != 1) {
            throw new IllegalArgumentException("Not rooted");
        }
    }

    private int readSynsets(String synsetsPath) {
        String[] lines = (new In(synsetsPath)).readAllLines();
        this.synsets = new String[lines.length];
        for (String line : lines) {
            String[] fileds = line.split(",");
            int index = Integer.parseInt(fileds[0]);
            for (String noun : fileds[1].split("\\s")) {
                if (synsMap.containsKey(noun)) {
                    SET<Integer> set = synsMap.get(noun);
                    set.add(index);
                } else {
                    SET<Integer> set = new SET<Integer>();
                    set.add(index);
                    synsMap.put(noun, set);
                }
            }
            this.synsets[index] = fileds[1];
        }
        return lines.length;
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
        return synsMap.keySet();
    }

    // is the word a WordNet noun?
    // time: logarithmic in the number of nouns
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return synsMap.containsKey(word);
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
        Integer ancestor = sap.ancestor(synidxAs, synidxBs);
        if (ancestor == -1) {
            return null;
        } else {
            return synsets[ancestor];
        }
    }

    // Get all synset indexs that containe noun
    private Iterable<Integer> synidxs(String noun) {
        if (synsMap.containsKey(noun)) {
            return synsMap.get(noun);
        } else {
            throw new IllegalArgumentException();
        }
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