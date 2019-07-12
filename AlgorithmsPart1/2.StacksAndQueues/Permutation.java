import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    // https://en.wikipedia.org/wiki/Reservoir_sampling
    public static void main(String[] args) {
        Integer k = new Integer.parseInt(args[0]); // https://stackoverflow.com/a/47095501
        int i = 0;

        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();

            i++;

            if (i <= k) {
                q.enqueue(s);
            } else {
                boolean keepNewItem = StdRandom.uniform() < ((double) k / i);
                if (keepNewItem) {
                    q.dequeue();
                    q.enqueue(s);
                }
            }

        }
        for (int j = 0; j < k; j++) {
            StdOut.println(q.dequeue());
        }
    }
}