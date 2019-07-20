import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

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
        LineSegment[] newSegments= new LineSegment[segments.length + 1];
        for (int i = 0 ; i < segments.length ; i++) newSegments[i] = segments[i];
        newSegments[segments.length] = new LineSegment(p, q);
        this.segments = newSegments;
    }

    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}