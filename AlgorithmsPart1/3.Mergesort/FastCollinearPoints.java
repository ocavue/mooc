import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] segments = new LineSegment[0];

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        points = checkPoints(points);

        for (Point p : points) {
            int start = 0;
            int end = 0;

            Point[] pointsOrderBySlope = new Point[points.length];
            for (int i = 0; i < points.length; i++) {
                pointsOrderBySlope[i] = points[i];
            }
            Arrays.sort(pointsOrderBySlope, p.slopeOrder());

            while (end < pointsOrderBySlope.length) {
                if (p.slopeTo(pointsOrderBySlope[start]) == p.slopeTo(pointsOrderBySlope[end])) {
                    end = end + 1;
                } else {
                    checkSegment(pointsOrderBySlope, p, start, end - 1);
                    start = end;
                    // end = end + 1;
                }
            }
        }
    }

    private void checkSegment(Point[] pointsOrderBySlope, Point root, int start, int end) {
        for (int i = start; i < end; i++) {
            assert root.slopeTo(pointsOrderBySlope[i]) == root.slopeTo(pointsOrderBySlope[i + 1]);
        }

        if ((end - start) < 2)
            return;
        // StdOut.println("checkSegment");
        Point[] collinearPoints = new Point[end - start + 2];
        for (int i = start; i <= end; i++) {
            collinearPoints[i - start] = pointsOrderBySlope[i];
        }
        collinearPoints[collinearPoints.length - 1] = root;
        Arrays.sort(collinearPoints);
        if ((root.compareTo(collinearPoints[0])) == 0)
            addSegment(collinearPoints[0], collinearPoints[collinearPoints.length - 1]);
    }

    private Point[] checkPoints(Point[] originPoints) {
        if (originPoints == null)
            throw new NullPointerException();

        // data type should have no side effects unless documented in API
        Point[] points = new Point[originPoints.length];
        for (int i = 0; i < originPoints.length; i++) {
            points[i] = originPoints[i];
        }

        Arrays.sort(points);
        for (Point p : points) {
            if (p == null)
                throw new NullPointerException();
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].compareTo(points[i]) == 0)
                throw new IllegalArgumentException();
        }

        return points;
    };

    private boolean isCollinear(Point a, Point b, Point c, Point d) {
        double sb = a.slopeTo(b);
        double sc = a.slopeTo(c);
        double sd = a.slopeTo(d);
        return (sb == sc && sc == sd);
    }

    private void addSegment(Point p, Point q) {
        // StdOut.println("addSegment");
        LineSegment segment = new LineSegment(p, q);

        LineSegment[] newSegments = new LineSegment[segments.length + 1];
        for (int i = 0; i < segments.length; i++)
            newSegments[i] = segments[i];
        newSegments[segments.length] = segment;
        this.segments = newSegments;
    }

    public int numberOfSegments() {
        // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {
        // the line segments
        return segments;
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
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}