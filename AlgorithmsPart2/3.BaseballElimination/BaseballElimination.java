import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;

class BaseballElimination {
    private String[] teams;
    private int[] w, l, r;
    private int[][] g;
    private int n, s, t, V;

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

        // Number of vertices = s and t + team vertices + game vertices
        // .................. = 2 + n + C(2,n)
        V = 2 + n + n * (n - 1) / 2;
        s = V - 2;
        t = V - 1;
    }

    private int gameVertice(int team1, int team2) {
        // TODO BAD MATH
        assert 0 <= team1 && team1 < team2 && team2 < n;
        int result = n;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (team1 == i && team2 == j)
                    return result;
                result++;
            }
        }
        return -1;
    }

    private int teamVertice(int team) {
        assert 0 <= team && team < n;
        return team;
    }

    public int numberOfTeams() {
        // number of teams
        return this.n;
    }

    public Iterable<String> teams() {
        // all teams
        SET<String> set = new SET<String>();
        for (String team : teams)
            set.add(team);
        return set;
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
        if (team1.equals(team2))
            throw new IllegalArgumentException("team1.equals(team2)");
        return g[find(team1)][find(team2)];
    }

    public boolean isEliminated(String team) {
        int x = find(team);
        FlowNetwork network = new FlowNetwork(V);

        for (int i = 0; i < n && i != x; i++) {
            if (w[x] + r[x] < w[i]) {
                return true;
            }
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                // connect vertice s to the game vertice
                network.addEdge(new FlowEdge(s, gameVertice(i, j), g[i][j]));
                // connect the game vertice to the team vertices
                network.addEdge(new FlowEdge(gameVertice(i, j), teamVertice(i), Integer.MAX_VALUE));
                network.addEdge(new FlowEdge(gameVertice(i, j), teamVertice(j), Integer.MAX_VALUE));
            }
        }
        for (int i = 0; i < n; i++) {
            // connect the team vertices to t
            network.addEdge(new FlowEdge(teamVertice(i), t, w[x] + r[x] - w[i]));
        }

        FordFulkerson ff = new FordFulkerson(network, s, t);
        return !ff.inCut(t);
    }

    // public Iterable<String> certificateOfElimination(String team) {
    // // subset R of teams that eliminates given team; null if not eliminated
    // if (team == null)
    // throw new IllegalArgumentException();
    // }

    public static void main(String[] args) {
        String filename = args[0];
        BaseballElimination division = new BaseballElimination(filename);
        if (division.n == 4) {
            assert division.teamVertice(0) == 0;
            assert division.teamVertice(3) == 3;
            assert division.gameVertice(0, 1) == 4;
            assert division.gameVertice(0, 2) == 5;
            assert division.gameVertice(0, 3) == 6;
            assert division.gameVertice(1, 2) == 7;
            assert division.gameVertice(1, 3) == 8;
            assert division.gameVertice(2, 3) == 9;
            assert division.s == 10;
            assert division.t == 11;
        }
        if (division.n == 5) {
            assert division.teamVertice(0) == 0;
            assert division.teamVertice(4) == 4;
            assert division.gameVertice(0, 1) == 5;
            assert division.gameVertice(0, 2) == 6;
            assert division.gameVertice(0, 3) == 7;
            assert division.gameVertice(0, 4) == 8;
            assert division.gameVertice(1, 2) == 9;
            assert division.gameVertice(1, 3) == 10;
            assert division.gameVertice(1, 4) == 11;
            assert division.gameVertice(2, 3) == 12;
            assert division.gameVertice(2, 4) == 13;
            assert division.gameVertice(3, 4) == 14;
            assert division.s == 15;
            assert division.t == 16;
        }
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { \n");
                // for (String t : division.certificateOfElimination(team)) {
                // StdOut.print(t + " ");
                // }
                // StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}