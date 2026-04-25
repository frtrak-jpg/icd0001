
import static org.junit.Assert.*;
import org.junit.Test;

/** Testklass.
 * @author Jaanus
 */
public class TnodeTest {

   @Test (timeout=1000)
   public void testBuildFromRPN() { 
      String s = "1 2 +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(1,2)", r);
      s = "2 1 - 4 * 6 3 / +";
      t = Tnode.buildFromRPN (s);
      r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(*(-(2,1),4),/(6,3))", r);
   }

   @Test (timeout=1000)
   public void testBuild2() {
      String s = "512 1 - 4 * -61 3 / +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(*(-(512,1),4),/(-61,3))", r);
      s = "5";
      t = Tnode.buildFromRPN (s);
      r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "5", r);
   }

   @Test (expected=RuntimeException.class)
   public void testEmpty1() {
      Tnode t = Tnode.buildFromRPN ("\t\t");
   }

   @Test (expected=RuntimeException.class)
   public void testEmpty2() {
      Tnode t = Tnode.buildFromRPN ("\t \t ");
   }

   @Test (expected=RuntimeException.class)
   public void testIllegalSymbol() {
      Tnode t = Tnode.buildFromRPN ("2 xx");
   }

   @Test (expected=RuntimeException.class)
   public void testIllegalSymbol2() {
      Tnode t = Tnode.buildFromRPN ("x");
   }

   @Test (expected=RuntimeException.class)
   public void testIllegalSymbol3() {
      Tnode t = Tnode.buildFromRPN ("2 1 + xx");
   }

   @Test (expected=RuntimeException.class)
   public void testTooManyNumbers() {
      Tnode root = Tnode.buildFromRPN ("2 3");
   }

   @Test (expected=RuntimeException.class)
   public void testTooManyNumbers2() {
      Tnode root = Tnode.buildFromRPN ("2 3 + 5");
   }

   @Test (expected=RuntimeException.class)
   public void testTooFewNumbers() {
      Tnode t = Tnode.buildFromRPN ("2 -");
   }

   @Test (expected=RuntimeException.class)
   public void testTooFewNumbers2() {
      Tnode t = Tnode.buildFromRPN ("2 5 + -");
   }

   @Test (expected=RuntimeException.class)
   public void testTooFewNumbers3() {
      Tnode t = Tnode.buildFromRPN ("+");
   }

   // ---- SWAP tests ----

   /** "2 5 SWAP -" must produce "-(5,2)" */
   @Test (timeout=1000)
   public void testSwapBasic() {
      String s = "2 5 SWAP -";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "-(5,2)", r);
   }

   /** SWAP preserves tree structure of the swapped subtrees */
   @Test (timeout=1000)
   public void testSwapWithSubtrees() {
      String s = "2 5 9 ROT + SWAP -";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "-(+(9,2),5)", r);
   }

   // ---- DUP tests ----

   /** "3 DUP *" must produce "*(3,3)" */
   @Test (timeout=1000)
   public void testDupBasic() {
      String s = "3 DUP *";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "*(3,3)", r);
   }

   /** DUP must produce a structurally independent copy */
   @Test (timeout=1000)
   public void testDupIndependentCopy() {
      String s = "2 5 DUP ROT - + DUP *";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "*(+(5,-(5,2)),+(5,-(5,2)))", r);
   }

   // ---- ROT tests ----

   /** "2 5 9 ROT - +" must produce "+(5,-(9,2))" */
   @Test (timeout=1000)
   public void testRotBasic() {
      String s = "2 5 9 ROT - +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(5,-(9,2))", r);
   }

   /** Combined ROT + SWAP example */
   @Test (timeout=1000)
   public void testRotAndSwap() {
      String s = "2 5 9 ROT + SWAP -";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "-(+(9,2),5)", r);
   }

   /** Complex combination: DUP, ROT, SWAP on negative numbers */
   @Test (timeout=1000)
   public void testRotSwapDupNegative() {
      String s = "-3 -5 -7 ROT - SWAP DUP * +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(-(-7,-3),*(-5,-5))", r);
   }

   // ---- Underflow (negative) tests ----

   /** DUP on empty stack must throw */
   @Test (expected=RuntimeException.class)
   public void testDupUnderflow() {
      Tnode t = Tnode.buildFromRPN ("DUP");
   }

   /** SWAP with only one element must throw */
   @Test (expected=RuntimeException.class)
   public void testSwapUnderflow() {
      Tnode t = Tnode.buildFromRPN ("5 SWAP");
   }

   /** ROT with only two elements must throw */
   @Test (expected=RuntimeException.class)
   public void testRotUnderflow() {
      Tnode t = Tnode.buildFromRPN ("5 3 ROT");
   }

   @Test (timeout=1000)
   public void testDupUnderflowMessage() {
      try {
         Tnode.buildFromRPN ("DUP");
         fail ("DUP on empty stack must throw RuntimeException");
      } catch (RuntimeException e) {
         assertTrue (e.getMessage().contains ("operation 'DUP'"));
         assertTrue (e.getMessage().contains ("requires one subtree"));
      }
   }

   @Test (timeout=1000)
   public void testSwapUnderflowMessage() {
      try {
         Tnode.buildFromRPN ("5 SWAP");
         fail ("SWAP with one subtree must throw RuntimeException");
      } catch (RuntimeException e) {
         assertTrue (e.getMessage().contains ("operation 'SWAP'"));
         assertTrue (e.getMessage().contains ("requires two subtrees"));
      }
   }

   @Test (timeout=1000)
   public void testRotUnderflowMessage() {
      try {
         Tnode.buildFromRPN ("5 3 ROT");
         fail ("ROT with two subtrees must throw RuntimeException");
      } catch (RuntimeException e) {
         assertTrue (e.getMessage().contains ("operation 'ROT'"));
         assertTrue (e.getMessage().contains ("requires three subtrees"));
      }
   }
}
