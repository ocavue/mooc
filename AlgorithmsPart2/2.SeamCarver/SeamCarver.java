import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.DijkstraSP;

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
      for (int i = 0; i < energies.length; i++)
         energies[i] = -1;
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

      int index = digraphIndex(x, y);
      if (energies[index] == -1) {
         energies[index] = getEnergy(x, y);
      }
      return energies[index];
   }

   private double getEnergy(int x, int y) {
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

   private int digraphIndex(int x, int y) {
      assert 0 <= x && x < width();
      assert 0 <= y && y < height();
      return (x % width()) + (y % height()) * width();
   }

   private int index2x(int digraphIndex) {
      return digraphIndex % width();
   }

   private int index2y(int digraphIndex) {
      return digraphIndex / width();
   }

   private void addEdge(EdgeWeightedDigraph D, int fromX, int fromY, int toX, int toY) {
      if (
      // @formatter:off
         0 <= fromX && fromX < width()  &&
         0 <= fromY && fromY < height() &&
         0 <= toX   && toX   < width()  &&
         0 <= toY   && toY   < height()
      // @formatter:on
      ) {
         double weight = energy(fromX, fromY) + energy(toX, toY);
         D.addEdge(new DirectedEdge(digraphIndex(fromX, fromY), digraphIndex(toX, toY), weight));
      }
   }

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {
      // Build a EdgeWeightedDigraph
      EdgeWeightedDigraph D = new EdgeWeightedDigraph(height() * width() + 2);
      for (int x = 0; x < width(); x++) {
         for (int y = 0; y <= height(); y++) {
            addEdge(D, x, y, x + 1, y - 1);
            addEdge(D, x, y, x + 1, y);
            addEdge(D, x, y, x + 1, y + 1);
         }
      }
      final int virtualStart = height() * width();
      final int virtualEnd = height() * width() + 1;
      for (int y = 0; y < height(); y++) {
         D.addEdge(new DirectedEdge(virtualStart, digraphIndex(0, y), 0));
         D.addEdge(new DirectedEdge(digraphIndex(width() - 1, y), virtualEnd, 0));
      }

      // Find the shortest path from virtualStart to virtualEnd
      DijkstraSP sp = new DijkstraSP(D, virtualStart);
      int x = -1;
      int[] seam = new int[width()];
      for (DirectedEdge edge : sp.pathTo(virtualEnd)) {
         if (0 <= x && x < width()) {
            int fromY = index2y(edge.from());
            seam[x] = fromY;
         }
         x++;
      }
      assert validateSeam(seam, width(), height() - 1);
      return seam;
   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {
      // Build a EdgeWeightedDigraph
      EdgeWeightedDigraph D = new EdgeWeightedDigraph(height() * width() + 2);
      for (int x = 0; x < width(); x++) {
         for (int y = 0; y <= height(); y++) {
            addEdge(D, x, y, x - 1, y + 1);
            addEdge(D, x, y, x, y + 1);
            addEdge(D, x, y, x + 1, y + 1);
         }
      }
      final int virtualStart = height() * width();
      final int virtualEnd = height() * width() + 1;
      for (int x = 0; x < width(); x++) {
         D.addEdge(new DirectedEdge(virtualStart, digraphIndex(x, 0), 0));
         D.addEdge(new DirectedEdge(digraphIndex(x, height() - 1), virtualEnd, 0));
      }

      // Find the shortest path from virtualStart to virtualEnd
      DijkstraSP sp = new DijkstraSP(D, virtualStart);
      int y = -1;
      int[] seam = new int[height()];
      for (DirectedEdge edge : sp.pathTo(virtualEnd)) {
         if (0 <= y && y < height()) {
            int fromX = index2x(edge.from());
            seam[y] = fromX;
         }
         y++;
      }
      assert validateSeam(seam, height(), width() - 1);
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
      if (maxValue <= 0)
         throw new IllegalArgumentException("maxValue should small than 1");

      for (int i = 0; i < seam.length; i++) {
         if (seam[i] < 0 || seam[i] > maxValue)
            throw new IllegalArgumentException();
         if (i + 1 < seam.length) {
            int diff = seam[i] - seam[i + 1];
            if (diff < -1 || diff > 1)
               throw new IllegalArgumentException();
         }
      }
      return true;
   }

   // unit testing (optional)
   public static void main(String[] args) {
      Picture p = new Picture(3, 2);
      p.set(0, 0, new Color(0, 0, 0));
      p.set(1, 0, new Color(0, 0, 0));
      p.set(2, 0, new Color(0, 0, 0));
      p.set(0, 1, new Color(255, 255, 255));
      p.set(1, 1, new Color(255, 255, 255));
      p.set(2, 1, new Color(255, 255, 255));

      SeamCarver s = new SeamCarver(p);
      assert s.digraphIndex(0, 0) == 0 && s.index2x(0) == 0 && s.index2y(0) == 0;
      assert s.digraphIndex(1, 0) == 1 && s.index2x(1) == 1 && s.index2y(1) == 0;
      assert s.digraphIndex(2, 0) == 2 && s.index2x(2) == 2 && s.index2y(2) == 0;
      assert s.digraphIndex(0, 1) == 3 && s.index2x(3) == 0 && s.index2y(3) == 1;
      assert s.digraphIndex(1, 1) == 4 && s.index2x(4) == 1 && s.index2y(4) == 1;
      assert s.digraphIndex(2, 1) == 5 && s.index2x(5) == 2 && s.index2y(5) == 1;

      int[] horizontalSeam = s.findHorizontalSeam();
      assert horizontalSeam[0] <= 1 || horizontalSeam[0] >= 0;
      assert horizontalSeam[1] == 0;
      assert horizontalSeam[2] <= 1 || horizontalSeam[0] >= 0;

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