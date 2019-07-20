import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private Point[][] segmentArgs = new Point[0][2];

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
            int start = 0;
            int end = 0;
            while (end < rest.length) {
                if (end + 1 < rest.length && (root.slopeTo(rest[end + 1])) == root.slopeTo(rest[start])) {
                    end = end + 1;
                } else {
                    if ((start - end) >= 3x) {
                        addSegment(root, rest[start]);
                    }
                    start = end + 1;
                    end = start;
                }
            }
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

    private void addSegment(Point root, Point[] points) {
        Point[] newPoints = new Point[points.length + 1];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = points[i];
        }
        newPoints[points.length] = root;
        addSegment(newPoints);
    }

    private void addSegment(Point[] points) {
        assert points.length >= 1;
        Arrays.sort(points);
        Point start = points[0];
        Point end = points[points.length - 1];
        addSegment(start, end);
    }

    private void addSegment(Point a, Point b) {
        assert a.compareTo(b) < 0;
        for (Point[] pair : segmentArgs) {
            assert pair.length == 2;
            assert pair[0].compareTo(pair[1]) < 0;
            if (pair[0].slopeTo(pair[1]) == a.slopeTo(b))
                break;
            if (pair[1].compareTo(b) == 0)
                break;
        }
        Point[][] newSegmentArgs = new Point[2][segmentArgs.length + 1];
        for (int i = 0; i < segmentArgs.length; i++) {
            newSegmentArgs[i] = segmentArgs[i];
        }
        newSegmentArgs[segmentArgs.length][0] = a;
        newSegmentArgs[segmentArgs.length][1] = b;
        segmentArgs = newSegmentArgs;
    }

    // private void addSegment(Point p, Point q) {
    // LineSegment segment = new LineSegment(p, q);

    // LineSegment[] newSegments = new LineSegment[segments.length + 1];
    // for (int i = 0; i < segments.length; i++)
    // newSegments[i] = segments[i];
    // newSegments[segments.length] = segment;
    // this.segments = newSegments;
    // }

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

        // // draw the points
        // StdDraw.enableDoubleBuffering();
        // StdDraw.setXscale(0, 32768);
        // StdDraw.setYscale(0, 32768);
        // for (Point p : points) {
        // p.draw();
        // }
        // StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        // StdDraw.show();
    }
}