import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
   WordNet wordnet;

   // constructor takes a WordNet object
   public Outcast(WordNet wordnet) {
      this.wordnet = wordnet;
   }

   // given an array of WordNet nouns, return an outcast
   public String outcast(String[] nouns) {
      Integer maxDistance = -1;
      String maxString = null;
      for (int i = 0; i < nouns.length; i++) {
         Integer distance = 0;
         for (int j = 0; j < nouns.length; j++) {
            distance += wordnet.distance(nouns[i], nouns[j]);
         }
         if (distance > maxDistance) {
            maxDistance = distance;
            maxString = nouns[i];
         }
      }
      return maxString;
   }

   // see test client below
   public static void main(String[] args) {
      WordNet wordnet = new WordNet(args[0], args[1]);
      Outcast outcast = new Outcast(wordnet);
      for (int t = 2; t < args.length; t++) {
         In in = new In(args[t]);
         String[] nouns = in.readAllStrings();
         String out = outcast.outcast(nouns);
         StdOut.println(args[t] + ": " + out);
         if (t == 2)
            assert out.equals("table");
         if (t == 3)
            assert out.equals("bed");
         if (t == 4)
            assert out.equals("potato");
      }
   }
}