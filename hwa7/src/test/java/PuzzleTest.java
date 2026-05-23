import static org.junit.Assert.*;
import org.junit.Test;

/** Testklass.
 * @author jaanus
 */
public class PuzzleTest {

   @Test (timeout=200000)
   public void test1() { 
      Puzzle.main (new String[]{"YKS", "KAKS", "KOLM"}); // 234 solutions
      Puzzle.main (new String[]{"SEND", "MORE", "MONEY"}); // 1 solution
      Puzzle.main (new String[]{"ABCDEFGHIJAB", "ABCDEFGHIJA", "ACEHJBDFGIAC"});  // 2 solutions
      // {A=1, B=2, C=3, D=4, E=5, F=6, G=7, H=8, I=9, J=0},
      // {A=2, B=3, C=5, D=1, E=8, F=4, G=6, H=7, I=9, J=0}
      Puzzle.main (new String[]{"CBEHEIDGEI", "CBEHEIDGEI", "BBBBBBBBBB"}); // no solutions
      assertTrue ("There are no formal tests", true);
   }
}

