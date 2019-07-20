import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        checkPoints(points);
        for (int i = 0; i + 3 < points.length; i++) {
            Point root = points[i];

            Point[] rest = new Point[points.length - i - 1];
            for (int j = i + 1; j < points.length; j++) {
                rest[j - i - 1] = points[j];
            }

            Arrays.sort(rest, root.slopeOrder());
        }
    }

    private void checkPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        Arrays.sort(points);
        for (Point p : points) {
            if (p == null)
                throw new IllegalArgumentException();
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1] == points[i])
                throw new IllegalArgumentException();
        }
    };

    private boolean isCollinear(Point a, Point b, Point c, Point d) {
        double sb = a.slopeTo(b);
        double sc = a.slopeTo(c);
        double sd = a.slopeTo(d);
        return (sb == sc && sc == sd);
    }

    public int numberOfSegments() {
        // the number of line segments
        return 0;
    }

    public LineSegment[] segments() {
        // the line segments
        return new LineSegment[1];
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        // StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}