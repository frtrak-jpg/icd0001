import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

/** Test class.
 * @author Jaanus
 */
public class DoubleSortingTest {

   static final double delta = 0.0000001;
   double[] a, b;
   String msg = "";

   /**
    * Check whether an array is ordered.
    * 
    * @param a
    *           sorted (?) array
    * @return false
    *            if an array is not ordered
    */
   static boolean inOrder(double[] a) {
      if (a.length < 2)
         return true;
      for (int i = 0; i < a.length - 1; i++) {
         if (a[i] > a[i + 1])
            return false;
      }
      return true;
   }

   @Test (timeout=1000)
   public void testTrivialArray() {
      a = new double[] {1., 3., 2.};
      b = new double[] {1., 2., 3.};
      msg = Arrays.toString(a);
      DoubleSorting.binaryInsertionSort (a);
      assertArrayEquals (msg, b, a, delta);
   }

   @Test (timeout=1000)
   public void testRandom1000() {
      double[] a = new double[1000];
      Random generaator = new Random();
      for (int i = 0; i < a.length; i++) {
         a[i] = generaator.nextDouble()*100.;
      }
      DoubleSorting.binaryInsertionSort (a);
      msg = " array not sorted!";
      assertTrue (msg, inOrder (a));
   }

}

