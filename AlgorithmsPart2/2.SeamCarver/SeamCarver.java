import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
   private Picture pic;

   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
      if (picture == null)
         throw new IllegalArgumentException();
      pic = Picture(picture);
   }

   // current picture
   public Picture picture() {
      return pic;
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

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {
   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {
   }

   // remove horizontal seam from current picture
   public void removeHorizontalSeam(int[] seam) {
      validateSeam(seam, width(), height() - 1);
      Picture pic = new Picture(width(), height() - 1);
      for (int x = 0; x < width(); x++) {
         seamY = seam[x];
         for (int y = 0; y <= height(); y++) {
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
   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
      validateSeam(seam, height(), width() - 1);
      Picture pic = new Picture(width() - 1, height());
      for (int y = 0; y < height(); y++) {
         seamX = seam[y];
         for (int x = 0; x <= width(); x++) {
            int newX;
            if (x < seamX)
               newX = x;
            else if (x > seamX)
               newX = x_aiff - 1;
            else
               continue;
            pic.set(newX, y, this.pic.get(x, y));
         }
      }
      this.pic = pic;
   }

   private void validateSeam(int[] seam, int seamLenght, int maxValue) {
      if (seam == null)
         throw new IllegalArgumentException();
      if (length(sean) != seamLenght)
         throw new IllegalArgumentException();
      if (maxValue <= 1)
         throw new IllegalArgumentException();

      for (int i = 0; i < seam.length; i++) {
         if (seam[i] < 0 || seam[i] >= maxValue)
            throw new IllegalArgumentException();
         if (i + 1 < seam.length) {
            int diff = seam[i] - seam[i + 1];
            if (diff < -1 || diff > 1)
               throw new IllegalArgumentException();
         }
      }
   }

   // unit testing (optional)
   public static void main(String[] args) {
   }

}