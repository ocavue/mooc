import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
   private Integer size = 0;
   private Node root;

   private boolean vertical = true; // 垂直
   private boolean horizontal = false; // 水平

   public KdTree() {
      // construct an empty set of points
   }

   private class Node {
      private Point2D point;
      public RectHV rect; // rect contains point
      public boolean diecrtion;
      public Node lb; // left or bottom node
      public Node rt; // right or top node

      public Node(Point2D point, RectHV rect, boolean diecrtion) {
         this.point = point;
         this.rect = rect;
         this.diecrtion = diecrtion;
      }

      public RectHV lbRect() {
         if (lb != null)
            return lb.rect;

         if (diecrtion == vertical) {
            RectHV left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            return left;
         } else {
            RectHV bottom = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            return bottom;
         }
      }

      public RectHV rtRect() {
         if (rt != null)
            return rt.rect;

         if (diecrtion == vertical) {
            RectHV right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            return right;
         } else {
            RectHV top = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            return top;
         }
      }

      public void draw() {
         this.point.draw();

         if (this.diecrtion == vertical) {
            StdDraw.setPenColor(255, 0, 0);
            StdDraw.line(this.point.x(), this.rect.ymin(), this.point.x(), this.rect.ymax());
         } else {
            StdDraw.setPenColor(0, 0, 255);
            StdDraw.line(this.rect.xmin(), this.point.y(), this.rect.xmax(), this.point.y());
         }

         if (this.lb != null)
            this.lb.draw();
         if (this.rt != null)
            this.rt.draw();
      }

   }

   public boolean isEmpty() {
      // is the set empty?
      return size == 0;
   }

   public int size() {
      // number of points in the set
      return size;
   }

   private Node getParent(Node root, Point2D p) {
      if (p == null)
         throw new IllegalArgumentException();
      if (root == null)
         throw new IllegalArgumentException();
      if (!root.rect.contains(p))
         throw new IllegalArgumentException();

      if (root.rt != null && root.rt.rect.contains(p))
         return getParent(root.rt, p);
      if (root.lb != null && root.lb.rect.contains(p))
         return getParent(root.lb, p);

      return root;
   }

   public void insert(Point2D p) {
      // add the point to the set (if it is not already in the set)
      if (p == null)
         throw new IllegalArgumentException();

      if (contains(p))
         return;

      if (size == 0) {
         root = new Node(p, new RectHV(0, 0, 1, 1), vertical);
      } else {
         Node parent = getParent(root, p);
         assert parent.rect.contains(p);

         if (parent.lbRect().contains(p)) {
            Node node = new Node(p, parent.lbRect(), !parent.diecrtion);
            assert parent.lb == null;
            parent.lb = node;
         } else {
            Node node = new Node(p, parent.rtRect(), !parent.diecrtion);
            assert parent.rt == null;
            parent.rt = node;
         }
      }
      size++;
   }

   private boolean contains(Node root, Point2D p) {
      // does the set contain point p?
      if (root == null) {
         return false;
      }

      if (!root.rect.contains(p)) {
         return false;
      }

      if (root.point.compareTo(p) == 0) {
         return true;
      }

      return contains(root.lb, p) || contains(root.rt, p);
   }

   public boolean contains(Point2D p) {
      // does the set contain point p?
      if (p == null)
         throw new IllegalArgumentException();

      return contains(root, p);
   }

   public void draw() {
      // draw all points to standard draw
      if (root != null)
         root.draw();
   }

   private SET<Point2D> range(Node root, RectHV rect) {
      SET<Point2D> set = new SET<Point2D>();

      if (root == null)
         return set;
      if (!root.rect.intersects(rect))
         return set;

      if (rect.contains(root.point))
         set.add(root.point);

      for (Point2D p : range(root.lb, rect))
         set.add(p);
      for (Point2D p : range(root.rt, rect))
         set.add(p);

      return set;
   }

   public Iterable<Point2D> range(RectHV rect) {
      // all points that are inside the rectangle (or on the boundary)
      if (rect == null)
         throw new IllegalArgumentException();

      SET<Point2D> set = range(root, rect);
      return set;
   }

   private Point2D nearest(Point2D p, Node root, double bestDistance, Point2D bestPoint) {
      assert root != null;

      double distance = root.point.distanceTo(p);
      if (distance <= bestDistance) {
         bestDistance = distance;
         bestPoint = root.point;
      }

      // TODO organize the recursive method so that when there are two possible
      // subtrees to go down, you always choose the subtree that is on the same side
      // of the splitting line as the query point as the first subtree to explore—the
      // closest point found while exploring the first subtree may enable pruning of
      // the second subtree.

      if (root.lb != null && root.lb.rect.distanceTo(p) < bestDistance) {
         Point2D testPoint = nearest(p, root.lb, bestDistance, bestPoint);
         assert testPoint.distanceTo(p) <= bestDistance;
         bestPoint = testPoint;
         bestDistance = bestPoint.distanceTo(p);
      }

      if (root.rt != null && root.rt.rect.distanceTo(p) < bestDistance) {
         Point2D testPoint = nearest(p, root.rt, bestDistance, bestPoint);
         assert testPoint.distanceTo(p) <= bestDistance;
         bestPoint = testPoint;
         bestDistance = bestPoint.distanceTo(p);
      }

      assert bestPoint != null;
      return bestPoint;
   }

   public Point2D nearest(Point2D p) {
      // a nearest neighbor in the set to point p; null if the set is empty
      if (p == null)
         throw new IllegalArgumentException();

      if (root == null)
         return null;

      return nearest(p, root, root.point.distanceTo(p), root.point);
   }

   public static void main(String[] args) { // unit testing of the methods (optional)
   }
}