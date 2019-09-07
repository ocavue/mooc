import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class BaseballElimination {
    private String[] teams;
    private int[] w, l, r;
    private int[][] g;
    private int n;

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        if (filename == null)
            throw new IllegalArgumentException();

        In in = new In(filename);
        this.n = in.readInt();
        this.teams = new String[n];
        this.w = new int[n];
        this.l = new int[n];
        this.r = new int[n];
        this.g = new int[n][n];
        for (int i = 0; i < n; i++) {
            teams[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                this.g[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        // number of teams
        return this.n;
    }

    public Iterable<String> teams() {
        // all teams
        Queue<String> q = new Queue<String>();
        for (String team : teams)
            q.enqueue(team);
        return q;
    }

    private int find(String team) {
        if (team == null)
            throw new IllegalArgumentException("team == null");
        for (int i = 0; i < teams.length; i++)
            if (team.equals(teams[i]))
                return i;
        throw new IllegalArgumentException();
    }

    public int wins(String team) {
        // number of wins for given team
        return w[find(team)];
    }

    public int losses(String team) {
        // number of losses for given team
        return l[find(team)];
    }

    public int remaining(String team) {
        // number of remaining games for given team
        return r[find(team)];
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        return g[find(team1)][find(team2)];
    }

    private int maxFlow(int x) {
        int maxFlow = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i == x || j == x)
                    continue;
                maxFlow += g[i][j];
            }
        }
        return maxFlow;
    }

    private boolean isEqual(double a, double b) {
        double diff = a - b;
        return -0.001 < diff && diff < 0.001;
    }

    private Queue<Integer> trivialElimination(int x) {
        Queue<Integer> q = new Queue<Integer>();
        for (int i = 0; i < n; i++) {
            if (i == x)
                continue;

            if (w[x] + r[x] < w[i]) {
                q.enqueue(i);
            }
        }
        return q;
    }

    private Queue<Integer> nontrivialElimination(int x) {
        Queue<Integer> q = new Queue<Integer>();

        int[][] gameVertices = new int[n][n];
        int[] teamVertices = new int[n];

        int s = 0;
        int t = 1;
        int V = 2;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i == x || j == x)
                    continue;
                gameVertices[i][j] = V;
                V++;
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == x)
                continue;
            teamVertices[i] = V;
            V++;
        }

        FlowNetwork network = new FlowNetwork(V);

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i == x || j == x)
                    continue;
                // connect vertice s to the game vertice
                network.addEdge(new FlowEdge(s, gameVertices[i][j], g[i][j]));
                // connect the game vertice to the team vertices
                network.addEdge(new FlowEdge(gameVertices[i][j], teamVertices[i], Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(gameVertices[i][j], teamVertices[j], Double.POSITIVE_INFINITY));
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == x)
                continue;
            // connect the team vertices to t
            assert w[x] + r[x] - w[i] >= 0;
            network.addEdge(new FlowEdge(teamVertices[i], t, w[x] + r[x] - w[i]));
        }

        FordFulkerson ff = new FordFulkerson(network, s, t);
        if (isEqual(ff.value(), maxFlow(x))) {
            return q;
        }

        for (int i = 0; i < n; i++) {
            if (i == x)
                continue;
            if (ff.inCut(teamVertices[i])) {
                q.enqueue(i);
            }
        }
        assert q.size() > 0;
        return q;
    }

    public boolean isEliminated(String team) {
        int x = find(team);
        return trivialElimination(x).size() > 0 || nontrivialElimination(x).size() > 0;
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        int x = find(team);

        Queue<String> q = new Queue<String>();
        for (int i : trivialElimination(x))
            q.enqueue(teams[i]);
        if (q.size() > 0)
            return q;
        for (int i : nontrivialElimination(x))
            q.enqueue(teams[i]);
        if (q.size() > 0)
            return q;
        return null;
    }

    public static void main(String[] args) {
        String filename = args[0];
        BaseballElimination division = new BaseballElimination(filename);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}