import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;;

public class KdTree {
   private SET<Point2D> set;

   public KdTree() {
      // construct an empty set of points
      set = new SET<Point2D>();
   }

   public boolean isEmpty() {
      // is the set empty?
      return set.isEmpty();
   }

   public int size() {
      // number of points in the set
      return set.size();
   }

   public void insert(Point2D p) {
      // add the point to the set (if it is not already in the set)
      if (p == null)
         throw new IllegalArgumentException();

      if (set.contains(p)) {
         return;
      }
      set.add(p);
   }

   public boolean contains(Point2D p) {
      // does the set contain point p?
      if (p == null)
         throw new IllegalArgumentException();

      return set.contains(p);
   }

   public void draw() {
      // draw all points to standard draw
      for (Point2D p : set) {
         p.draw();
      }
   }

   public Iterable<Point2D> range(RectHV rect) {
      // all points that are inside the rectangle (or on the boundary)
      if (rect == null)
         throw new IllegalArgumentException();

      SET<Point2D> result = new SET<Point2D>();
      for (Point2D p : set) {
         if (rect.contains(p)) {
            result.add(p);
         }
      }
      return result;
   }

   public Point2D nearest(Point2D p0) {
      // a nearest neighbor in the set to point p; null if the set is empty
      if (p0 == null)
         throw new IllegalArgumentException();

      double nearestDistance = Double.MAX_VALUE;
      Point2D nearestPoint = null;
      for (Point2D p : set) {
         double distance = p0.distanceTo(p);
         if (distance < nearestDistance) {
            nearestDistance = distance;
            nearestPoint = p;
         }
      }

      return nearestPoint;
   }

   public static void main(String[] args) { // unit testing of the methods (optional)
   }
}