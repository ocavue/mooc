import java.awt.Color;
import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Topological;
// import edu.princeton.cs.algs4.AcyclicSP;

public class SeamCarver {
   private Picture pic;
   private double[] energies;

   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
      if (picture == null)
         throw new IllegalArgumentException();
      pic = new Picture(picture);
      initEnergies();
   }

   private void initEnergies() {
      energies = new double[pic.width() * pic.height()];
      for (int i =0 ; i < energies.length; i ++) {
         energies[i] = -1;
      }
   }

   // current picture
   public Picture picture() {
      return new Picture(pic);
   }

   // width of current picture
   public int width() {
      return pic.width();
   }

   // height of current picture
   public int height() {
      return pic.height();
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {
      if (x < 0 || x >= width() || y < 0 || y >= height())
         throw new IllegalArgumentException();

      int index = (x % width()) + (y % height()) * width();
      if (energies[index] == -1) {
         energies[index] = calEnergy(x, y);
      }
      return energies[index];
   }

   private double calEnergy(int x, int y) {
      if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
         return 1000;

      // @formatter:off
      int rx = pic.get(x - 1, y).getRed()   - pic.get(x + 1, y).getRed()   ;
      int gx = pic.get(x - 1, y).getGreen() - pic.get(x + 1, y).getGreen() ;
      int bx = pic.get(x - 1, y).getBlue()  - pic.get(x + 1, y).getBlue()  ;
      int ry = pic.get(x, y - 1).getRed()   - pic.get(x, y + 1).getRed()   ;
      int gy = pic.get(x, y - 1).getGreen() - pic.get(x, y + 1).getGreen() ;
      int by = pic.get(x, y - 1).getBlue()  - pic.get(x, y + 1).getBlue()  ;
      // @formatter:on

      return Math.pow(rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by, 0.5);
   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {
      double[][] distTo = new double[width()][height()];
      int[][] pathTo = new int[width()][height()];

      // init distTo and pathTo
      for (int y = 0; y < height(); y++) {
         for (int x = 0; x < width(); x++) {
            if (y == 0) {
               distTo[x][y] = 0;
               pathTo[x][y] = -1;
            } else {
               distTo[x][y] = Double.POSITIVE_INFINITY;
               pathTo[x][y] = Integer.MIN_VALUE;
            }
         }
      }

      // build distTo and pathTo
      for (int y = 1; y < height(); y++) {
         for (int x = 0; x < width(); x++) {
            int[] fromXs;
            int fromY = y - 1;

            if (x == 0 && x == width() - 1)
               fromXs = new int[] { x };
            else if (x == 0)
               fromXs = new int[] { x, x + 1 };
            else if (x == width() - 1)
               fromXs = new int[] { x - 1, x };
            else
               fromXs = new int[] { x - 1, x, x + 1 };

            for (int i = 0; i < fromXs.length; i++) {
               int fromX = fromXs[i];
               if (distTo[x][y] >= distTo[fromX][fromY] + energy(x, y)) {
                  distTo[x][y] = distTo[fromX][fromY] + energy(x, y);
                  pathTo[x][y] = fromX;
               }
            }
            assert 0 <= pathTo[x][y] && pathTo[x][y] <= width() - 1;
            int diff = pathTo[x][y] - x;
            assert -1 <= diff && diff <= 1;
         }
      }

      // check distTo and pathTo
      for (int y = 0; y < height(); y++) {
         for (int x = 0; x < width(); x++) {
            assert distTo[x][y] != Double.POSITIVE_INFINITY : String
                  .format("distTo[%d][%d] != Double.POSITIVE_INFINITY", x, y);
            ;
            assert pathTo[x][y] != Integer.MIN_VALUE : String.format("pathTo[%d][%d] != Integer.MIN_VALUE", x, y);
         }
      }

      // find the pixel with smallest dist
      int topY = height() - 1;
      double minDist = Double.POSITIVE_INFINITY;
      int minTopX = -1;
      for (int x = 0; x < width(); x++) {
         if (distTo[x][topY] < minDist) {
            minDist = distTo[x][topY];
            minTopX = x;
         }
      }
      assert minTopX != -1;

      // find the shortest path
      int[] seam = new int[height()];
      int seamX = minTopX;
      for (int y = height() - 1; y >= 0; y--) {
         seam[y] = seamX;
         if (y - 1 >= 0)
            seamX = pathTo[seamX][y];
      }
      validateSeam(seam, height(), width() - 1);
      return seam;
   }

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {
      double[][] distTo = new double[width()][height()];
      int[][] pathTo = new int[width()][height()];

      // init distTo and pathTo
      for (int x = 0; x < width(); x++) {
         for (int y = 0; y < height(); y++) {
            if (x == 0) {
               distTo[x][y] = 0;
               pathTo[x][y] = -1;
            } else {
               distTo[x][y] = Double.POSITIVE_INFINITY;
               pathTo[x][y] = Integer.MIN_VALUE;
            }
         }
      }

      // build distTo and pathTo
      for (int x = 1; x < width(); x++) {
         for (int y = 0; y < height(); y++) {
            int fromX = x - 1;
            int[] fromYs;

            if (y == 0 && y == height() - 1)
               fromYs = new int[] { y };
            else if (y == 0)
               fromYs = new int[] { y, y + 1 };
            else if (y == height() - 1)
               fromYs = new int[] { y - 1, y };
            else
               fromYs = new int[] { y - 1, y, y + 1 };

            for (int i = 0; i < fromYs.length; i++) {
               int fromY = fromYs[i];
               if (distTo[x][y] >= distTo[fromX][fromY] + energy(x, y)) {
                  distTo[x][y] = distTo[fromX][fromY] + energy(x, y);
                  pathTo[x][y] = fromY;
               }
            }
            assert 0 <= pathTo[x][y] && pathTo[x][y] <= height() - 1;
            int diff = pathTo[x][y] - y;
            assert -1 <= diff && diff <= 1;
         }
      }

      // check distTo and pathTo
      for (int x = 0; x < width(); x++) {
         for (int y = 0; y < height(); y++) {
            assert distTo[x][y] != Double.POSITIVE_INFINITY : String
                  .format("distTo[%d][%d] != Double.POSITIVE_INFINITY", x, y);
            ;
            assert pathTo[x][y] != Integer.MIN_VALUE : String.format("pathTo[%d][%d] != Integer.MIN_VALUE", x, y);
         }
      }

      // find the pixel with smallest dist
      int rightX = width() - 1;
      double minDist = Double.POSITIVE_INFINITY;
      int minRightY = -1;
      for (int y = 0; y < height(); y++) {
         if (distTo[rightX][y] < minDist) {
            minDist = distTo[rightX][y];
            minRightY = y;
         }
      }
      assert minRightY != -1;

      // find the shortest path
      int[] seam = new int[width()];
      int seamY = minRightY;
      for (int x = width() - 1; x >= 0; x--) {
         seam[x] = seamY;
         if (x - 1 >= 0)
            seamY = pathTo[x][seamY];
      }
      validateSeam(seam, width(), height() - 1);
      return seam;
   }

   // remove horizontal seam from current picture
   public void removeHorizontalSeam(int[] seam) {
      validateSeam(seam, width(), height() - 1);
      Picture pic = new Picture(width(), height() - 1);
      for (int x = 0; x < width(); x++) {
         int seamY = seam[x];
         for (int y = 0; y < height(); y++) {
            int newY;
            if (y < seamY)
               newY = y;
            else if (y > seamY)
               newY = y - 1;
            else
               continue;
            pic.set(x, newY, this.pic.get(x, y));
         }
      }
      this.pic = pic;
      initEnergies();
   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
      validateSeam(seam, height(), width() - 1);
      Picture pic = new Picture(width() - 1, height());
      for (int y = 0; y < height(); y++) {
         int seamX = seam[y];
         for (int x = 0; x < width(); x++) {
            int newX;
            if (x < seamX)
               newX = x;
            else if (x > seamX)
               newX = x - 1;
            else
               continue;
            pic.set(newX, y, this.pic.get(x, y));
         }
      }
      this.pic = pic;
      initEnergies();
   }

   private boolean validateSeam(int[] seam, int seamLenght, int maxValue) {
      if (seam == null)
         throw new IllegalArgumentException("seam is null");
      if (seam.length != seamLenght)
         throw new IllegalArgumentException("seam.length is wrong");
      if (maxValue < 0)
         throw new IllegalArgumentException("maxValue should not small than 0");

      for (int i = 0; i < seam.length; i++) {
         if (seam[i] < 0 || seam[i] > maxValue)
            throw new IllegalArgumentException(String.format("seam[%d] == %d", i, seam[i]));
         if (i + 1 < seam.length) {
            int diff = seam[i] - seam[i + 1];
            if (diff < -1 || diff > 1)
               throw new IllegalArgumentException(String.format("seam[%d] - seam[%d] == %d", i, i + 1, diff));
         }
      }
      return true;
   }

   // unit testing (optional)
   public static void main(String[] args) {
      Picture p;
      SeamCarver s;

      p = new Picture(1, 1);
      p.set(0, 0, new Color(0, 0, 0));
      s = new SeamCarver(p);
      s.findVerticalSeam();
      s.findHorizontalSeam();

      p = new Picture(5, 5);
      for (int x = 0; x < 5; x++) {
         for (int y = 0; y < 5; y++) {
            if (x == 0 || x == 4 || y == 0 || y == 4) {
               p.set(x, y, new Color(255, 255, 255));
            } else {
               p.set(x, y, new Color(0, 0, 0));
            }
         }
      }
      s = new SeamCarver(p);

      int[] verticalSeam = s.findVerticalSeam();
      StdOut.println(Arrays.toString(verticalSeam));
      assert 1 <= verticalSeam[0] && verticalSeam[0] <= 3;
      assert verticalSeam[1] == 2 : String.format("verticalSeam[%d] == %d", 1, verticalSeam[1]);
      assert verticalSeam[2] == 2;
      assert verticalSeam[3] == 2;
      assert 1 <= verticalSeam[4] && verticalSeam[4] <= 3;

      int[] horizontalSeam = s.findHorizontalSeam();
      StdOut.println(Arrays.toString(horizontalSeam));
      assert 1 <= horizontalSeam[0] && horizontalSeam[0] <= 3;
      assert horizontalSeam[1] == 2 : String.format("horizontalSeam[%d] == %d", 1, horizontalSeam[1]);
      assert horizontalSeam[2] == 2;
      assert horizontalSeam[3] == 2;
      assert 1 <= horizontalSeam[4] && horizontalSeam[4] <= 3;

      p = new Picture("./input.png");
      s = new SeamCarver(p);
      int i = 1;
      while (i <= 150) {
         int[] seam = s.findVerticalSeam();
         s.removeVerticalSeam(seam);
         StdOut.print(String.format("Removing %d seam\r", i));
         i++;
      }
      s.picture().save("./output.png");
   }
}