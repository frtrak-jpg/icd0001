/** Sorting of balls.
 * @since 1.8
 */
public class ColorSort {

   enum Color {red, green, blue};
   
   public static void main (String[] param) {
      // for debugging
   }

   /**
    * Sort balls red to left, green in the middle, blue to the right with the DNF method in a single pass
    * @param balls
    */
   public static void sortBalls (Color[] balls) {

      int low = 0;
      int mid = 0;
      int high = balls.length - 1;

      while (mid <= high) {
         Color value = balls[mid];

         if (value == Color.red) {
            Color temp = balls[low];
            balls[low] = balls[mid];
            balls[mid] = temp;
            low++;
            mid++;

         } else if (value == Color.green) {
            mid++;

         } else {

            Color temp = balls[high];
            balls[high] = balls[mid];
            balls[mid] = temp;
            high--;
         }
      }
   }
}

