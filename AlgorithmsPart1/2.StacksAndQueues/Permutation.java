import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    // https://en.wikipedia.org/wiki/Reservoir_sampling
    public static void main(String[] args) {
        int k = new Integer(args[0]);
        int i = 0;

        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();

            i++;

            if (i <= k) {
                q.enqueue(s);
            } else {
                boolean keepNewItem = StdRandom.uniform() < (k / i);
                if (keepNewItem) {
                    q.dequeue();
                    q.enqueue(s);
                }
            }

        }
        for (int j = 0; j < k; ji++) {
            StdOut.println(q.dequeue());
        }
    }
}