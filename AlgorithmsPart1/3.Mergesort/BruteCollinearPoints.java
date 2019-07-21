import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segments = new LineSegment[0];

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        points = this.checkPoints(points);

        for (int a = 0 + 0; a + 3 < points.length; a++) {
            for (int b = a + 1; b + 2 < points.length; b++) {
                for (int c = b + 1; c + 1 < points.length; c++) {
                    for (int d = c + 1; d + 0 < points.length; d++) {
                        if (isCollinear(points[a], points[b], points[c], points[d])) {
                            addSegment(points[a], points[d]);
                        }
                    }
                }
            }
        }
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

    public int numberOfSegments() {
        // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {
        // the line segments
        for (LineSegment s : segments) {
            assert s != null;
        }
        return segments;
    }

    private void addSegment(Point p, Point q) {
        LineSegment segment = new LineSegment(p, q);

        LineSegment[] newSegments = new LineSegment[segments.length + 1];
        for (int i = 0; i < segments.length; i++)
            newSegments[i] = segments[i];
        newSegments[segments.length] = segment;
        this.segments = newSegments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}