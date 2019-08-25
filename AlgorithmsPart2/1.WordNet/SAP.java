import java.util.Arrays;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;

// All methods (and the constructor) should take time at most proportional to E + V in the worst case, where E and V are the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to E + V.

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    private class DoubleBFS {
        private static final int INFINITY = Integer.MAX_VALUE;
        private boolean[] markedA;
        private boolean[] markedB;
        private int[] edgeToA;
        private int[] distToA;
        private int[] edgeToB;
        private int[] distToB;
        private int ancestor; // Shortest ancestor, -1 if not exist

        private void init(Digraph G) {
            distToA = new int[G.V()];
            edgeToA = new int[G.V()];
            distToB = new int[G.V()];
            edgeToB = new int[G.V()];
            for (int v = 0; v < G.V(); v++) {
                distToA[v] = INFINITY;
                distToB[v] = INFINITY;
            }
            markedA = new boolean[G.V()];
            markedB = new boolean[G.V()];
        }

        private void getAncestor() {
            int bestDist = INFINITY;
            int bestAncestor = -1;

            // if (G.V() < 100) {
            // for (int i = 0; i < G.V(); i++) {
            // StdOut.println(" " + i + " A " + markedA[i] + " B " + markedB[i]);
            // }
            // }

            for (int i = 0; i < G.V(); i++) {
                if (markedA[i] && markedB[i]) {
                    int dist = distToA[i] + distToB[i];
                    if (dist < bestDist) {
                        bestDist = dist;
                        bestAncestor = i;
                    }
                }
            }
            ancestor = bestAncestor;
        }

        public DoubleBFS(Digraph G, int a, int b) {
            assert a != b;
            init(G);
            bfs(G, a, distToA, edgeToA, markedA);
            bfs(G, b, distToB, edgeToB, markedB);
            getAncestor();
        }

        public DoubleBFS(Digraph G, Iterable<Integer> as, Iterable<Integer> bs) {
            init(G);
            bfs(G, as, distToA, edgeToA, markedA);
            bfs(G, bs, distToB, edgeToB, markedB);
            getAncestor();
        }

        // BFS from single source.
        // Stop if hit another source's mark and return the index;
        // Return -1 if not.
        private void bfs(Digraph G, int s, int[] distTo, int[] edgeTo, boolean[] marked) {
            Queue<Integer> q = new Queue<Integer>();
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);

            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        // BFS from multiple sources
        // Stop if hit another source's mark and return the index;
        // Return -1 if not.
        private void bfs(Digraph G, Iterable<Integer> sources, int[] distTo, int[] edgeTo, boolean[] marked) {
            Queue<Integer> q = new Queue<Integer>();
            for (int s : sources) {
                assert marked[s] == false;
                marked[s] = true;
                distTo[s] = 0;
                q.enqueue(s);
            }

            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        public int ancestor() {
            return ancestor;
        }

        public int distToA(int v) {
            validateVertex(v);
            return distToA[v];
        }

        public int distToB(int v) {
            validateVertex(v);
            return distToB[v];
        }

        // public Iterable<Integer> pathToA(int v) {
        // validateVertex(v);

        // if (!hasPathTo(v))
        // return null;
        // Stack<Integer> path = new Stack<Integer>();
        // int x;
        // for (x = v; distToA[x] != 0; x = edgeToA[x])
        // path.push(x);
        // path.push(x);
        // return path;
        // }

        // public Iterable<Integer> pathToB(int v) {
        // validateVertex(v);

        // if (!hasPathTo(v))
        // return null;
        // Stack<Integer> path = new Stack<Integer>();
        // int x;
        // for (x = v; distToB[x] != 0; x = edgeToB[x])
        // path.push(x);
        // path.push(x);
        // return path;
        // }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            int V = markedA.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    private void validateVertex(int v) {
        int V = G.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private void validateVertex(Iterable<Integer> s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        int V = G.V();
        for (Integer v : s) {
            if (v == null)
                throw new IllegalArgumentException();
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int a, int b) {
        validateVertex(a);
        validateVertex(b);

        DoubleBFS bfs = bfs(a, b);
        int ancestor = bfs.ancestor();
        if (ancestor == -1) {
            return -1;
        } else {
            return (bfs.distToA(ancestor) + bfs.distToB(ancestor));
        }
    }

    private DoubleBFS bfs(int a, int b) {
        return new DoubleBFS(G, a, b);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int a, int b) {
        validateVertex(a);
        validateVertex(b);

        DoubleBFS bfs = bfs(a, b);
        return bfs.ancestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> a, Iterable<Integer> b) {
        validateVertex(a);
        validateVertex(b);

        DoubleBFS bfs = new DoubleBFS(G, a, b);
        int ancestor = bfs.ancestor();
        if (ancestor == -1) {
            return -1;
        } else {
            return bfs.distToA(ancestor) + bfs.distToB(ancestor);
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> a, Iterable<Integer> b) {
        validateVertex(a);
        validateVertex(b);

        DoubleBFS bfs = new DoubleBFS(G, a, b);
        return bfs.ancestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        if (args[0].equals("digraph1.txt")) {
            for (Integer[] nums : new Integer[][] { { 3, 11, 4, 1 }, { 9, 12, 3, 5 }, { 7, 2, 4, 0 },
                    { 1, 6, -1, -1 } }) {
                int v = nums[0];
                int w = nums[1];
                int expectedLength = nums[2];
                int expectedAncestor = nums[3];
                int length = sap.length(v, w);
                int ancestor = sap.ancestor(v, w);
                StdOut.printf("%d %d\n", v, w);
                StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

                DoubleBFS bfs = sap.bfs(v, w);
                if (bfs.ancestor() != -1) {
                    StdOut.printf("distToA = %d, distToB = %d\n", bfs.distToA(ancestor), bfs.distToB(ancestor));
                }

                assert length == expectedLength;
                assert ancestor == expectedAncestor;
            }
        }
        if (args[0].equals("digraph2.txt")) {
            SET<Integer> A = new SET<Integer>();
            A.add(13);
            A.add(23);
            A.add(24);
            SET<Integer> B = new SET<Integer>();
            B.add(6);
            B.add(16);
            B.add(17);
            int length = sap.length(A, B);
            int ancestor = sap.ancestor(A, B);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            assert length == 4;
            assert ancestor == 3;

            assert sap.length(7, 9) == 2;
            assert sap.ancestor(7, 9) == 3;

            assert sap.length(22, 23) == 10;
            assert sap.ancestor(22, 23) == 0;

            assert sap.length(13, 3) == 2;
            assert sap.ancestor(13, 3) == 3;
        }
    }
}
