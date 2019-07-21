import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] segments = new LineSegment[0];
    private int numberOfSegments = 0;

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

            double[] SlopeOrderBySlope = new double[points.length];
            for (int i = 0; i < points.length; i++) {
                SlopeOrderBySlope[i] = p.slopeTo(pointsOrderBySlope[i]);
            }

            int i = 0;
            int j = 0;

            for (j = 0; j < SlopeOrderBySlope.length; j++) {
                assert isFirstCollinearPoint(SlopeOrderBySlope, p, i);
                if (SlopeOrderBySlope[i] == SlopeOrderBySlope[j]) {
                    if (isLastCollinearPoint(SlopeOrderBySlope, p, j)) {
                        checkSegment(pointsOrderBySlope, p, i, j);
                    }
                } else {
                    i = j;
                }
            }
        }
    }

    private boolean isFirstCollinearPoint(double[] SlopeOrderBySlope, Point p, int index) {
        if (index == 0)
            return true;
        return SlopeOrderBySlope[index - 1] != SlopeOrderBySlope[index];
    }

    private boolean isLastCollinearPoint(double[] SlopeOrderBySlope, Point p, int index) {
        if (index == SlopeOrderBySlope.length - 1)
            return true;
        return SlopeOrderBySlope[index + 1] != SlopeOrderBySlope[index];
    }

    private void checkSegment(Point[] pointsOrderBySlope, Point root, int start, int end) {
        assert start <= end;
        for (int i = start; i < end; i++) {
            assert root.slopeTo(pointsOrderBySlope[i]) == root.slopeTo(pointsOrderBySlope[i + 1]);
            assert pointsOrderBySlope[i].compareTo(pointsOrderBySlope[i + 1]) < 0;
        }

        if (root.compareTo(pointsOrderBySlope[start]) >= 0)
            return;

        assert root.compareTo(pointsOrderBySlope[start]) < 0;
        assert pointsOrderBySlope[start].compareTo(pointsOrderBySlope[end]) < 0;

        if (end - start + 2 < 4)
            return;

        addSegment(root, pointsOrderBySlope[end]);
    }

    private Point[] checkPoints(Point[] originPoints) {
        if (originPoints == null)
            throw new IllegalArgumentException();
        for (Point p : originPoints) {
            if (p == null)
                throw new IllegalArgumentException();
        }

        // data type should have no side effects unless documented in API
        Point[] points = new Point[originPoints.length];
        for (int i = 0; i < originPoints.length; i++) {
            points[i] = originPoints[i];
        }

        Arrays.sort(points);
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
        LineSegment newSegment = new LineSegment(p, q);

        if (segments.length == 0) {
            segments = new LineSegment[] { newSegment };
            numberOfSegments++;
            return;
        }

        LineSegment[] newSegments;
        if (numberOfSegments == segments.length) {
            assert numberOfSegments >= 1;
            newSegments = new LineSegment[2 * numberOfSegments];
            for (int i = 0; i < numberOfSegments; i++) {
                newSegments[i] = segments[i];
            }
            this.segments = newSegments;
        }
        this.segments[numberOfSegments] = newSegment;
        numberOfSegments++;
    }

    public int numberOfSegments() {
        // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] mutableSegments = new LineSegment[segments.length];
        for (int i = 0; i < numberOfSegments; i++) {
            assert segments[i] != null;
            mutableSegments[i] = segments[i];
        }
        return mutableSegments;
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

        points = new Point[] { new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4),
                new Point(1, -1), new Point(2, -2), new Point(3, -3), new Point(4, -4), };

        collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }

    }
}