import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segments = new LineSegment[0];

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();

        for (int a = 0 + 0; a + 3 < points.length; a++) {
            for (int b = a + 1; b + 2 < points.length; b++) {
                for (int c = b + 1; c + 1 < points.length; c++) {
                    for (int d = c + 1; d + 0 < points.length; d++) {

                        double aSlopeToB = points[a].slopeTo(points[b]);
                        double aSlopeToC = points[a].slopeTo(points[c]);
                        double aSlopeToD = points[a].slopeTo(points[d]);
                        if (aSlopeToB == aSlopeToC && aSlopeToB == aSlopeToD) {
                            Point[] testPoints = new Point[] { points[a], points[b], points[c], points[d] };
                            Arrays.sort(testPoints);
                            assert 1 == 2;
                            addSegment(testPoints[0], testPoints[3]);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {
        // the line segments
        return segments;
    }

    private void addSegment(Point p, Point q) {
        LineSegment segment = new LineSegment(p, q);

        LineSegment[] newSegments = new LineSegment[segments.length + 1];
        for (int i = 0; i < segments.length; i++)
            newSegments[i] = segments[i];
        newSegments[segments.length] = segment;

        for (LineSegment s : newSegments) {
            assert s != null;
        }

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