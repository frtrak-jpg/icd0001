import static org.junit.Assert.*;
import org.junit.Test;

public class ColorSortTest {

   /** Number of milliseconds allowed to spend for sorting 1 million elements */
   public static int threshold = 25;

   /** Test data */
   static ColorSort.Color[] balls = null;

   static int rCount = 0;
   static int gCount = 0;
   static int bCount = 0;

   /** Correctness check for the result */
   static boolean check (ColorSort.Color[] balls, int r, int g, int b) {
      if (balls == null)
         return false;
      int len = balls.length;
      if (r<0 || g<0 || b<0 || len != r+g+b)
         return false; 
      if (len == 0)
         return true;
      for (int i=0; i < r; i++)
         if (balls[i] != ColorSort.Color.red)
            return false;
      for (int i=r; i < r+g; i++)
         if (balls[i] != ColorSort.Color.green)
            return false;
      for (int i=r+g; i < len; i++)
         if (balls[i] != ColorSort.Color.blue)
            return false;
      return true;
   } // check

    @Test (timeout=1000)
    public void testFunctionality() {
      balls = new ColorSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = ColorSort.Color.blue;
            bCount++;
         } else {
            balls[i] = ColorSort.Color.green;
            gCount++;
         }  
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testShort() {
      balls = new ColorSort.Color [1];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      double rnd = Math.random();
      if (rnd < 1./3.) {
         balls[0] = ColorSort.Color.red;
         rCount++;
      } else  if (rnd > 2./3.) {
         balls[0] = ColorSort.Color.blue;
         bCount++;
      } else {
         balls[0] = ColorSort.Color.green;
         gCount++;
      }
      ColorSort.reorder (balls);
      assertTrue ("One element array not working", 
         check (balls, rCount, gCount, bCount));
      balls = new ColorSort.Color [0];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      ColorSort.reorder (balls);
      assertTrue ("Zero element array not working", 
         check (balls, rCount, gCount, bCount));
      balls = new ColorSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = ColorSort.Color.blue;
            bCount++;
         } else {
            balls[i] = ColorSort.Color.green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllRed() {
      balls = new ColorSort.Color [100000];
      int len = balls.length;
      rCount = len;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < len; i++) {
            balls[i] = ColorSort.Color.red;
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect for all red", 
         check (balls, rCount, gCount, bCount));
      balls = new ColorSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = ColorSort.Color.blue;
            bCount++;
         } else {
            balls[i] = ColorSort.Color.green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllGreen() {
      balls = new ColorSort.Color [100000];
      int len = balls.length;
      rCount = 0;
      gCount = len;
      bCount = 0;
      for (int i=0; i < len; i++) {
            balls[i] = ColorSort.Color.green;
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect for all green", 
         check (balls, rCount, gCount, bCount));
      balls = new ColorSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = ColorSort.Color.blue;
            bCount++;
         } else {
            balls[i] = ColorSort.Color.green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testAllBlue() {
      balls = new ColorSort.Color [100000];
      int len = balls.length;
      rCount = 0;
      gCount = 0;
      bCount = len;
      for (int i=0; i < len; i++) {
            balls[i] = ColorSort.Color.blue;
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect for all blue",
         check (balls, rCount, gCount, bCount));
      balls = new ColorSort.Color [100000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = ColorSort.Color.blue;
            bCount++;
         } else {
            balls[i] = ColorSort.Color.green;
            gCount++;
         }
      }
      ColorSort.reorder (balls);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
    }

    @Test (timeout=1000)
    public void testSpeed() {
      balls = new ColorSort.Color [1000000];
      rCount = 0;
      gCount = 0;
      bCount = 0;
      for (int i=0; i < balls.length; i++) {
         double rnd = Math.random();
         if (rnd < 1./3.) {
            balls[i] = ColorSort.Color.red;
            rCount++;
         } else  if (rnd > 2./3.) {
            balls[i] = ColorSort.Color.blue;
            bCount++;
         } else {
            balls[i] = ColorSort.Color.green;
            gCount++;
         }
      }
      long t0 = System.currentTimeMillis();
      ColorSort.reorder (balls);
      long t1 = System.currentTimeMillis();
      int delta = (int)(t1-t0);
      assertTrue ("Result incorrect", check (balls, rCount, gCount, bCount));
      assertTrue ("Too slow: "+ delta, delta < threshold);
      System.out.println ("Time spent: " + delta + " ms");
    }

/*
   @Test (expected=RuntimeException.class)
   public void testNullArray() {
      ColorSort.reorder (null);
   }
*/
}

