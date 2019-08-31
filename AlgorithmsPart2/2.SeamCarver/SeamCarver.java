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
   }

   // width of current picture
   public int width() {
      return pic.width;
   }

   // height of current picture
   public int height() {
      return pic.height;
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {
      if (x < 0 || x >= width() || y < 0 || y >= height())
         throw new IllegalArgumentException();
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
   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
      validateSeam(seam, height(), width() - 1);
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