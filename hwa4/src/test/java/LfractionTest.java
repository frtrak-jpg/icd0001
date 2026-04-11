
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

/** Testklass.
 * @author Jaanus
 */
public class LfractionTest {

   @Test (timeout=1000)
   public void testPlus() { 
      Lfraction f1 = new Lfraction (2, 5);
      Lfraction f2 = new Lfraction (4, 15);
      Lfraction sum = f1.plus(f2);
      assertEquals ("Wrong sum: <" + f1 + "> + <" + f2 + ">", 
         new Lfraction (2, 3), sum);
      Lfraction sm2 = sum.plus (sum);
      assertEquals ("Wrong sum.plus(sum): <2/3> + <2/3>",
         new Lfraction (4, 3), sm2);
      assertEquals ("Do not change the arguments of the sum", 
         new Lfraction (2, 3), sum);
      f1 = new Lfraction (-1, 250);
      f2 = new Lfraction (-1, 375);
      sum = f1.plus(f2);
      assertEquals ("Wrong sum: <" + f1 + "> + <" + f2 + ">",
         new Lfraction (-1, 150), sum);
      f1 = new Lfraction (1, 221);
      f2 = new Lfraction (1, 323);
      sum = f1.plus (f2);
      assertEquals ("Wrong sum: <" + f1 + "> + <" + f2 + ">",
         new Lfraction (32, 4199), sum);
      f1 = new Lfraction (1, 39203);
      f2 = new Lfraction (1, 41989);
      sum = f1.plus (f2);
      assertEquals ("Wrong sum: <" + f1 + "> + <" + f2 + ">",
         new Lfraction (408, 8271833), sum);
      f1 = new Lfraction (-2, 5);
      f2 = new Lfraction (2, 5);
      sum = f1.plus(f2);
      assertEquals ("Wrong sum: <" + f1 + "> + <" + f2 + ">",
         new Lfraction (0, 1), sum);
   }

   @Test (timeout=1000)
   public void testTimes() {
      Lfraction f1 = new Lfraction (2, 5);
      Lfraction f2 = new Lfraction (4, 15);
      Lfraction prd = f1.times (f2);
      assertEquals ("Wrong product: <" + f1 + "> * <" + f2 + ">",
         new Lfraction (8, 75), prd);
      f1 = new Lfraction (-3, 5);
      f2 = new Lfraction (-5, 7);
      prd = f1.times (f2);
      assertEquals ("Wrong product: <" + f1 + "> * <" + f2 + ">",
         new Lfraction (3, 7), prd);
      Lfraction pr2 = prd.times (prd);
      assertEquals ("Wrong prd.times(prd): <3/7> * <3/7>",
         new Lfraction (9, 49), pr2);
      assertEquals ("Do not change the arguments of the product",
         new Lfraction (3, 7), prd);
      f1 = new Lfraction (0, 1);
      f2 = new Lfraction (2, 3);
      prd = f1.times (f2);
      assertEquals ("Wrong product: <" + f1 + "> * <" + f2 + ">",
         new Lfraction (0, 1), prd);
      f1 = new Lfraction (3, 2);
      f2 = new Lfraction (2, 3);
      prd = f1.times (f2);
      assertEquals ("Wrong product: <" + f1 + "> * <" + f2 + ">",
         new Lfraction (1, 1), prd);
      f1 = new Lfraction (3, 5);
      f2 = new Lfraction (5, 7);
      prd = f1.times (f2);
      assertTrue ("Result must be reduced: 3/5 * 5/7 -> 3/7",
         prd.getDenominator()==7);
   } 

   @Test (expected=RuntimeException.class)
   public void testCreateZeroDenominator() {
      Lfraction f = new Lfraction (1, 0);
   }

   @Test (expected=RuntimeException.class)
   public void testZeroInverse() {
      Lfraction f = new Lfraction (0, 1);
      f.inverse();
   }

   @Test (timeout=1000)
   public void testClone() {
      Lfraction f1 = new Lfraction (2, 5);
      Lfraction f2 = null;
      try {
         f2 = (Lfraction)f1.clone();
      } catch (CloneNotSupportedException e) {};
      assertNotSame ("clone must differ from original", f2, f1);
      assertEquals ("clone must be equal to original", f1, f2);
      f1 = f2.plus(f1);
      assertEquals ("clone must be independent from original",
         new Lfraction (2, 5), f2);
   }

   @Test (timeout=1000)
   public void testToString() {
      String s = new Lfraction (1, 4).toString();
      assertTrue (s + " must represent quarter", 
         (s.indexOf('1') < s.indexOf('4')) && (s.indexOf('1') >= 0));
      s = new Lfraction (-1, 5).toString();
      assertTrue (s + " does not contain minus", s.indexOf('-') >= 0);
   } 

   @Test (expected=RuntimeException.class)
   public void testDivideByZero() {
      Lfraction f1 = new Lfraction (1, 5);
      Lfraction f2 = new Lfraction (0, 1);
      Lfraction q = f1.divideBy (f2);
   }

   @Test (expected=RuntimeException.class)
   public void testValueOfErr1() {
      Lfraction.valueOf("2/4/6");
   }

   @Test (expected=RuntimeException.class)
   public void testValueOfErr2() {
      Lfraction.valueOf("2/4/");
   }

   @Test (timeout=1000)
   public void testMinus() {
      Lfraction f1 = new Lfraction (2, 5);
      Lfraction f2 = new Lfraction (4, 15);
      Lfraction dif = f1.minus (f2);
      assertEquals ("Wrong difference: <" + f1 + "> - <" + f2 + ">", 
         new Lfraction (2, 15), dif);
      Lfraction df2 = dif.minus (dif);
      assertEquals ("Wrong difference: <2/15> - <2/15>",
         new Lfraction (0, 1), df2);
      assertEquals ("Do not change the arguments of the difference", 
         new Lfraction (2, 15), dif);
      f1 = new Lfraction (-2, 5);
      f2 = new Lfraction (-4, 15);
      dif = f1.minus (f2);
      assertEquals ("Wrong difference: <" + f1 + "> - <" + f2 + ">",
         new Lfraction (-2, 15), dif);
   }

   @Test (timeout=1000)
   public void testDivideBy() {
      Lfraction f1 = new Lfraction (-2, 7);
      Lfraction f2 = new Lfraction (-1, 14);
      Lfraction f = f1.divideBy (f2);
      assertEquals ("Wrong quotient: <" + f1 + "> / <" + f2 + ">",
         new Lfraction (4, 1), f);
      f = f2.divideBy (f1);
      assertEquals ("Wrong quotient: <" + f1 + "> / <" + f2 + ">",
         new Lfraction (1, 4), f);
      Lfraction f3 = f.divideBy (f);
      assertEquals (f.toString() + " divided by itself", 
         new Lfraction (1, 1), f3);
      assertEquals ("Do not change the arguments of the quotient",
         new Lfraction (1, 4), f);
   }

   @Test (timeout=1000)
   public void testOpposite() {
      Lfraction f1 = new Lfraction (1, 6);
      Lfraction f2 = f1.opposite();
      assertEquals ("Wrong opposite", new Lfraction (-1, 6), f2);
      assertEquals ("Do not change the argument of opposite",
         new Lfraction (1, 6), f1);
      f1 = new Lfraction (-4, 75);
      f2 = f1.opposite();
      assertEquals ("Wrong opposite", new Lfraction (4, 75), f2);
      f1 = new Lfraction (0, 1);
      f2 = f1.opposite();
      assertEquals ("zero must be neutral to opposite", f1, f2);
   }

   @Test (timeout=1000)
   public void testInverse() {
      Lfraction f1 = new Lfraction (2, 3);
      Lfraction f2 = f1.inverse();
      assertEquals ("Wrong inverse ", new Lfraction (3, 2), f2);
      assertEquals ("Do not change the argument of inverse",
         new Lfraction (2, 3), f1);
      f1 = new Lfraction (-4, 75);
      f2 = f1.inverse();
      assertEquals ("Wrong inverse", new Lfraction (-75, 4), f2);
      assertTrue ("Denominator must always be positive", 
         f2.getDenominator() > 0);
      f1 = new Lfraction (1, 1);
      f2 = f1.inverse();
      assertEquals ("1 must be neutral to inverse", f1, f2);
   }

   @Test (timeout=1000)
   public void testGetters() {
      Lfraction f1 = new Lfraction (2, 3);
      long num = f1.getNumerator();
      assertEquals ("wrong numerator ", 2, num);
      f1 = new Lfraction (-4, 75);
      num = f1.getNumerator();
      assertEquals ("Wrong numerator", -4, num);
      f1 = new Lfraction (0, 7);
      num = f1.getNumerator();
      assertEquals ("Wrong numerator", 0, num);
      f1 = new Lfraction (2, 3);
      long den = f1.getDenominator();
      assertEquals ("wrong denominator ", 3, den);
      f1 = new Lfraction (-4, 75);
      den = f1.getDenominator();
      assertEquals ("Wrong denominator", 75, den);
   }

   @Test (timeout=1000)
   public void testIntegerPart() {
      Lfraction f1 = new Lfraction (2, 3);
      long i = f1.integerPart();
      assertEquals ("wrong integer part ", 0, i);
      f1 = new Lfraction (3, 2);
      i = f1.integerPart();
      assertEquals ("wrong integer part ", 1, i);
      f1 = new Lfraction (32, 3);
      i = f1.integerPart();
      assertEquals ("wrong integer part ", 10, i);
      f1 = new Lfraction (33, 3);
      i = f1.integerPart();
      assertEquals ("wrong integer part ", 11, i);
      f1 = new Lfraction (-33, 3);
      i = f1.integerPart();
      assertEquals ("wrong integer part ", -11, i);
   }

   @Test (timeout=1000)
   public void testLfractionPart() {
      Lfraction f1 = new Lfraction (2, 3);
      Lfraction i = f1.fractionPart();
      assertEquals ("wrong fraction part ", new Lfraction (2, 3), i);
      f1 = new Lfraction (3, 2);
      i = f1.fractionPart();
      assertEquals ("wrong fraction part ", new Lfraction (1, 2), i);
      f1 = new Lfraction (32, 3);
      i = f1.fractionPart();
      assertEquals ("wrong fraction part ", new Lfraction (2, 3), i);
      f1 = new Lfraction (33, 3);
      i = f1.fractionPart();
      assertEquals ("wrong fraction part ", new Lfraction (0, 1), i);
      f1 = new Lfraction (-33, 3);
      i = f1.fractionPart();
      assertEquals ("wrong fraction part ", new Lfraction (0, 1), i);
      f1 = new Lfraction (-5, 4);
      i = f1.fractionPart();
      assertTrue ("wrong fraction part " + i.toString() 
         + " for " + f1.toString(), 
         i.equals (new Lfraction (-1, 4)) || i.equals (new Lfraction (3, 4)));
   }

   @Test (timeout=1000)
   public void testEquals() {
      Lfraction f1 = new Lfraction (2, 5);
      Lfraction f2 = new Lfraction (4, 10);
      assertTrue ("2/5 must be equal to 4/10", f1.equals (f2));
      assertFalse ("2/5 is not 3/5", f1.equals (new Lfraction (3, 5)));
      f1 = new Lfraction (12345678901234567L, 1L);
      f2 = new Lfraction (12345678901234568L, 1L);
      assertFalse ("12345678901234567/1 is not 12345678901234568/1",
         f1.equals (f2));
   }

   @Test (timeout=1000)
   public void testCompareTo() {
      Lfraction f1 = new Lfraction (2, 5);
      Lfraction f2 = new Lfraction (4, 7);
      assertTrue ("2/5 must be less than 4/7", f1.compareTo (f2) < 0);
      assertTrue ("2/5 must be equal to 4/10", 
         f1.compareTo (new Lfraction (4, 10)) == 0);
      assertTrue ("4/7 must be greater than 2/5", f2.compareTo (f1) > 0);
      f1 = new Lfraction (-2, 5);
      f2 = new Lfraction (-4, 7);
      assertTrue ("-2/5 must be greater than -4/7", f1.compareTo (f2) > 0);
      assertTrue ("-2/5 must be equal to -4/10", 
         f1.compareTo (new Lfraction (-4, 10)) == 0);
      assertTrue ("-4/7 must be less than -2/5", f2.compareTo (f1) < 0);
      f1 = new Lfraction (12345678901234567L, 1L);
      f2 = new Lfraction (12345678901234568L, 1L);
      assertFalse ("12345678901234567/1 is not 12345678901234568/1",
         f1.compareTo (f2) == 0);
   }

   @Test (timeout=1000)
   public void testToLfraction() {
      Lfraction f1 = Lfraction.toLfraction (Math.PI, 7);
      Lfraction f2 = new Lfraction (22, 7);
      assertTrue ("Math.PI must be nearly 22/7", f1.equals (f2));
      f1 = Lfraction.toLfraction (-10., 2);
      f2 = new Lfraction (-20, 2);
      assertTrue ("-10. must be -20/2", f1.equals (f2));
   }

   @Test (timeout=1000)
   public void testToDouble() {
      Lfraction f = new Lfraction (2, 5);
      assertEquals ("2/5 must be nearly 0.4", 0.4, f.toDouble(), 0.00001);
      f = new Lfraction (-17, 100);
      assertEquals ("-17/100 must be nearly -0.17", -0.17, 
         f.toDouble(), 0.00001);
   }

   @Test (timeout=1000)
   public void testValueOf() {
      Lfraction f = new Lfraction (2, 5);
      assertEquals ("valueOf must read back what toString outputs. ", 
         f, Lfraction.valueOf (f.toString()));
      f = new Lfraction (-17, 100);
      assertEquals ("valueOf must read back what toString outputs. ", 
         f, Lfraction.valueOf (f.toString()));
   }

   @Test (timeout=1000)
   public void testHashCode() {
      Lfraction q1 = new Lfraction (1L, 2L);
      int h1 = q1.hashCode();
      Lfraction q2 = new Lfraction (1L, 2L);
      int h2 = q2.hashCode();
      Lfraction q3 = null;
      try {
         q3 = (Lfraction)q1.clone();
      } catch (CloneNotSupportedException e) {};
      int h3 = q3.hashCode();
      assertTrue ("hashCode has to be same for equal objects", h1 == h2);
      assertTrue ("hashCode has to be same for clone objects", h1 == h3);
      assertTrue ("hashCode has to be same for the same object", 
         h1 == q1.hashCode());
      q2 = new Lfraction (0L, 2L);
      h2 = q2.hashCode();
      q3 = new Lfraction (1L, 3L);
      h3 = q3.hashCode();
      Lfraction q4 = new Lfraction (3L, 1L);
      int h4 = q4.hashCode();
      assertFalse ("hashcode must not be symmetrical", h3 == h4);
      assertFalse ("hashCode does not depend on numerator", h1 == h2);
      assertFalse ("hashCode does not depend on denominator", h1 == h3);
   }

}

