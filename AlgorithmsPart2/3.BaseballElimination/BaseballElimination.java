import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;

class BaseballElimination {
    private String[] teams;
    private int[] w, l, r;
    private int[][] g;

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        if (filename == null)
            throw new IllegalArgumentException();

        In in = new In(filename);
        int n = in.readInt();
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
        return this.teams.length;
    }

    public Iterable<String> teams() {
        // all teams
        SET<String> set = new SET<String>();
        for (String team : teams) set.add(team);
        return set;
    }

    // public int wins(String team) {
    // // number of wins for given team
    // if (team == null)
    // throw new IllegalArgumentException();
    // }

    // public int losses(String team) {
    // // number of losses for given team
    // if (team == null)
    // throw new IllegalArgumentException();
    // }

    // public int remaining(String team) {
    // // number of remaining games for given team
    // if (team == null)
    // throw new IllegalArgumentException();
    // }

    // public int against(String team1, String team2) {
    // // number of remaining games between team1 and team2
    // if (team1 == null || team2 == null)
    // throw new IllegalArgumentException();
    // }

    // public boolean isEliminated(String team) {
    // // is given team eliminated?
    // if (team == null)
    // throw new IllegalArgumentException();
    // }

    // public Iterable<String> certificateOfElimination(String team) {
    // // subset R of teams that eliminates given team; null if not eliminated
    // if (team == null)
    // throw new IllegalArgumentException();
    // }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        // for (String team : division.teams()) {
        // if (division.isEliminated(team)) {
        // StdOut.print(team + " is eliminated by the subset R = { ");
        // for (String t : division.certificateOfElimination(team)) {
        // StdOut.print(t + " ");
        // }
        // StdOut.println("}");
        // } else {
        // StdOut.println(team + " is not eliminated");
        // }
        // }
    }
}