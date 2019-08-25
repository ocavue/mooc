import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// All methods (and the constructor) should take time at most proportional to E + V in the worst case, where E and V are the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to E + V.

public class SAP {
    private class DoubleBFS {
        private static final int INFINITY = Integer.MAX_VALUE;
        // if marked[x] == A, then there is a path from a to x
        // if marked[x] == B, then there is a path from b to x
        private int[] marked;
        private int A = 1;
        private int B = 2;
        private int[] edgeToA;
        private int[] distToA;
        private int[] edgeToB;
        private int[] distToB;
        private int ancestor; // Shortest ancestor, -1 if not exist

        public DoubleBFS(Digraph G, int a, int b) {
            assert a != b;
            distToA = new int[G.V()];
            edgeToA = new int[G.V()];
            distToB = new int[G.V()];
            edgeToB = new int[G.V()];
            for (int v = 0; v < G.V(); v++) {
                distToA[v] = INFINITY;
                distToB[v] = INFINITY;
            }
            marked = new int[G.V()];

            int hitA = bfs(G, a, A, distToA, edgeToA);
            assert hitA == -1;
            int hitB = bfs(G, b, B, distToB, edgeToB);
            ancestor = hitB;
        }

        public DoubleBFS(Digraph G, Iterable<Integer> as, Iterable<Integer> bs) {
            distToA = new int[G.V()];
            edgeToA = new int[G.V()];
            distToB = new int[G.V()];
            edgeToB = new int[G.V()];
            for (int v = 0; v < G.V(); v++) {
                distToA[v] = INFINITY;
                distToB[v] = INFINITY;
            }
            marked = new int[G.V()];

            int hitA = bfs(G, as, A, distToA, edgeToA);
            assert hitA == -1;
            int hitB = bfs(G, bs, B, distToB, edgeToB);
            ancestor = hitB;
        }

        // BFS from single source.
        // Stop if hit another source's mark and return the index;
        // Return -1 if not.
        private int bfs(Digraph G, int s, int mark, int[] distTo, int[] edgeTo) {
            Queue<Integer> q = new Queue<Integer>();
            marked[s] = mark;
            distTo[s] = 0;
            q.enqueue(s);

            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (marked[w] == 0) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = mark;
                        q.enqueue(w);
                    } else if (marked[w] == mark) {
                        // already marked by s, skip
                        continue;
                    } else {
                        // already marked by others, return
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        return w;
                    }
                }
            }
            return -1;
        }

        // BFS from multiple sources
        // Stop if hit another source's mark and return the index;
        // Return -1 if not.
        private int bfs(Digraph G, Iterable<Integer> sources, int mark, int[] distTo, int[] edgeTo) {
            Queue<Integer> q = new Queue<Integer>();
            for (int s : sources) {
                assert marked[s] == 0;
                marked[s] = mark;
                distTo[s] = 0;
                q.enqueue(s);
            }

            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (marked[w] == 0) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = mark;
                        q.enqueue(w);
                    } else if (marked[w] == mark) {
                        // already marked by s, skip
                        continue;
                    } else {
                        // already marked by others, return
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        return w;
                    }
                }
            }
            return -1;
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
            int V = marked.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    Digraph G; // One-dimension Digraph

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int a, int b) {
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
        DoubleBFS bfs = bfs(a, b);
        return bfs.ancestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> a, Iterable<Integer> b) {
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
        DoubleBFS bfs = new DoubleBFS(G, a, b);
        return bfs.ancestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        for (Integer[] nums : new Integer[][] { { 3, 11, 4, 1 }, { 9, 12, 3, 5 }, { 7, 2, 4, 0 }, { 1, 6, -1, -1 } }) {
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
}
