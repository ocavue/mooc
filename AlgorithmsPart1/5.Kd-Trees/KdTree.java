import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

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

         if (diecrtion == vertical) {
            RectHV left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            RectHV right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            lb = left;
            rt = right;
         } else {
            RectHV bottom = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            RectHV top = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            lb = bottom;
            rt = top;
         }
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
         if (this.lb != null)
            this.lb.draw();
         if (this.rt != null)
            this.rt.draw();
      }

   }

   public boolean isEmpty() {
      // is the set empty?
      return true;
   }

   public int size() {
      // number of points in the set
      return size;
   }

   private Node getParent(Node root, Point p) {
      assert root != null;
      assert root.rect.contains(p);

      if (root.rt.rect != null && root.lb.rect != null && root.rt.rect.contains(p) && root.lb.rect.contains(p))
         return root;

      if (root.rt.rect != null && root.rt.rect.contains(p))
         return getParent(root.rt, p);

      if (root.lb.rect != null && root.lb.rect.contains(p))
         return getParent(root.lb, p);

      return root;
   }

   public void insert(Point2D p) {
      // add the point to the set (if it is not already in the set)
      if (p == null)
         throw new IllegalArgumentException();

      if (size == 0) {
         root = new Node(p);
         root.rect = new RectHV(0, 0, 1, 1);
         root.diecrtion = vertical;
         size = 1;
      } else {
         Node parent = getParent(root, p);
         assert parent.rect.contains(p);

         if (parent.lbRect().contains(p)) {
            Node node = new Node(p, !parent.diecrtion, parent.lbRect());
            parent.lb = node;
         } else {
            Node node = new Node(p, !parent.diecrtion, parent.rtRect());
            parent.rt = node;
         }
      }
   }

   public boolean contains(Point2D p) {
      // does the set contain point p?
      if (p == null)
         throw new IllegalArgumentException();

      if (root == null)
         return false;

      Node parent = getParent(root, p);
      return parent.point.compareTo(p) == 0;
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

   private Point2D nearest(Node root, Point2D p, double bestDistance) {
      double distance = root.point.distanceTo(p);
      if (distance <= bestDistance) {
         bestDistance = distance;
         bestPoint = root.point;
      }

      double rtDistance = Double.MAX_VALUE;
      double lbDistance = Double.MAX_VALUE;

      if (root.rt != null)
         rtDistance = root.rt.rect.distanceTo(p);
      if (root.lb != null)
         lbDistance = root.lb.rect.distanceTo(p);

      Node first;
      Node second;

      if (rtDistance < lbDistance) {
         first = rtDistance;
         second = lbDistance;
      } else {
         first = rtDistance;
         second = lbDistance;
      }

   }

   public Point2D nearest(Point2D p) {
      // a nearest neighbor in the set to point p; null if the set is empty
   }

   public static void main(String[] args) { // unit testing of the methods (optional)
   }
}