import java.util.*;

/** Word puzzle.
 * @since 1.8
 */
public class Puzzle {

   private static String word1;
   private static String word2;
   private static String word3;
   private static char[][] reversedWords;
   private static int maxLen;
   private static int[] letterToDigit;
   private static boolean[] usedDigits;
   private static boolean[] leadingLetters;
   private static long solutionCount;
   private static int[] firstSolutionDigits;

   /** Solve the word puzzle.
    * @param args three words (addend1 addend2 sum)
    */
   public static void main (String[] args) {
      if (args == null || args.length != 3) {
         System.out.println ("Expected exactly three words: addend1 addend2 sum");
         return;
      }

      word1 = args[0];
      word2 = args[1];
      word3 = args[2];
      maxLen = Math.max (word1.length(), Math.max (word2.length(), word3.length()));
      reversedWords = new char[][]{
         reverse (word1),
         reverse (word2),
         reverse (word3)
      };
      letterToDigit = new int[26];
      Arrays.fill (letterToDigit, -1);
      usedDigits = new boolean[10];
      leadingLetters = new boolean[26];
      if (word1.length() > 0) leadingLetters[word1.charAt (0) - 'A'] = true;
      if (word2.length() > 0) leadingLetters[word2.charAt (0) - 'A'] = true;
      if (word3.length() > 0) leadingLetters[word3.charAt (0) - 'A'] = true;
      firstSolutionDigits = null;
      solutionCount = 0;

      System.out.println (word1 + " + " + word2 + " = " + word3);
      searchColumn (0, 0);
      if (solutionCount > 0 && firstSolutionDigits != null) {
         System.out.println (formatSolution (firstSolutionDigits));
      }
      System.out.println ("Total solutions: " + solutionCount);
   }

   private static char[] reverse (String word) {
      char[] reversed = new char[word.length()];
      for (int i = 0; i < word.length(); i++) {
         reversed[i] = word.charAt (word.length() - 1 - i);
      }
      return reversed;
   }

   private static void searchColumn (int column, int carryIn) {
      if (column == maxLen) {
         if (carryIn == 0) {
            solutionCount++;
            if (firstSolutionDigits == null) {
               firstSolutionDigits = Arrays.copyOf (letterToDigit, letterToDigit.length);
            }
         }
         return;
      }

      int[] letters = new int[3];
      int[] coefficients = new int[3];
      int distinctCount = collectColumnTerms (column, letters, coefficients);
      int partial = carryIn;
      int unknownCount = 0;
      int[] unknownLetters = new int[3];
      int[] unknownCoefficients = new int[3];

      for (int i = 0; i < distinctCount; i++) {
         int letter = letters[i];
         int digit = letterToDigit[letter];
         if (digit >= 0) {
            partial += coefficients[i] * digit;
         } else {
            unknownLetters[unknownCount] = letter;
            unknownCoefficients[unknownCount] = coefficients[i];
            unknownCount++;
         }
      }

      if (unknownCount == 0) {
         if (partial == 0 || partial == 10) {
            searchColumn (column + 1, partial / 10);
         }
         return;
      }

      assignColumn (column, partial, unknownLetters, unknownCoefficients, unknownCount, 0);
   }

   private static int collectColumnTerms (int column, int[] letters, int[] coefficients) {
      int count = 0;
      count = addTerm (letters, coefficients, count, column, 0, 1);
      count = addTerm (letters, coefficients, count, column, 1, 1);
      count = addTerm (letters, coefficients, count, column, 2, -1);
      return count;
   }

   private static int addTerm (int[] letters, int[] coefficients, int count,
      int column, int wordIndex, int coefficient) {
      char[] word = reversedWords[wordIndex];
      if (column >= word.length) {
         return count;
      }
      int letter = word[column] - 'A';
      for (int i = 0; i < count; i++) {
         if (letters[i] == letter) {
            coefficients[i] += coefficient;
            if (coefficients[i] == 0) {
               letters[i] = letters[count - 1];
               coefficients[i] = coefficients[count - 1];
               return count - 1;
            }
            return count;
         }
      }
      letters[count] = letter;
      coefficients[count] = coefficient;
      return count + 1;
   }

   private static void assignColumn (int column, int partial,
      int[] letters, int[] coefficients, int unknownCount, int index) {
      if (index == unknownCount) {
         if (partial == 0 || partial == 10) {
            searchColumn (column + 1, partial / 10);
         }
         return;
      }

      int letter = letters[index];
      int coefficient = coefficients[index];
      int remaining = unknownCount - index - 1;

      if (remaining == 0) {
         for (int carryOut = 0; carryOut <= 1; carryOut++) {
            int target = 10 * carryOut - partial;
            int digit = solveDigit (coefficient, target);
            if (digit >= 0 && isDigitAvailable (letter, digit)) {
               assignDigit (letter, digit);
               assignColumn (column, partial + coefficient * digit,
                  letters, coefficients, unknownCount, index + 1);
               unassignDigit (letter, digit);
            }
         }
         return;
      }

      if (remaining == 1) {
         int nextLetter = letters[index + 1];
         int nextCoefficient = coefficients[index + 1];
         for (int carryOut = 0; carryOut <= 1; carryOut++) {
            int target = 10 * carryOut - partial;
            for (int digit = 0; digit <= 9; digit++) {
               if (!isDigitAvailable (letter, digit)) continue;
               assignDigit (letter, digit);
               int rest = target - coefficient * digit;
               int nextDigit = solveDigit (nextCoefficient, rest);
               if (nextDigit >= 0 && isDigitAvailable (nextLetter, nextDigit)) {
                  assignDigit (nextLetter, nextDigit);
                  assignColumn (column, partial + coefficient * digit
                     + nextCoefficient * nextDigit, letters, coefficients,
                     unknownCount, index + 2);
                  unassignDigit (nextLetter, nextDigit);
               }
               unassignDigit (letter, digit);
            }
         }
         return;
      }

      int nextLetter = letters[index + 1];
      int nextCoefficient = coefficients[index + 1];
      int thirdLetter = letters[index + 2];
      int thirdCoefficient = coefficients[index + 2];
      for (int digit = 0; digit <= 9; digit++) {
         if (!isDigitAvailable (letter, digit)) continue;
         assignDigit (letter, digit);
         for (int nextDigit = 0; nextDigit <= 9; nextDigit++) {
            if (!isDigitAvailable (nextLetter, nextDigit)) continue;
            assignDigit (nextLetter, nextDigit);
            int partialAfterTwo = partial + coefficient * digit
               + nextCoefficient * nextDigit;
            for (int carryOut = 0; carryOut <= 1; carryOut++) {
               int target = 10 * carryOut - partialAfterTwo;
               int thirdDigit = solveDigit (thirdCoefficient, target);
               if (thirdDigit >= 0 && isDigitAvailable (thirdLetter, thirdDigit)) {
                  assignDigit (thirdLetter, thirdDigit);
                  searchColumn (column + 1, carryOut);
                  unassignDigit (thirdLetter, thirdDigit);
               }
            }
            unassignDigit (nextLetter, nextDigit);
         }
         unassignDigit (letter, digit);
      }
   }

   private static int solveDigit (int coefficient, int target) {
      if (coefficient == 1) {
         return (target >= 0 && target <= 9) ? target : -1;
      }
      if (coefficient == -1) {
         int digit = -target;
         return (digit >= 0 && digit <= 9) ? digit : -1;
      }
      if (coefficient == 2) {
         if (target % 2 != 0) return -1;
         int digit = target / 2;
         return (digit >= 0 && digit <= 9) ? digit : -1;
      }
      return -1;
   }

   private static boolean isDigitAvailable (int letter, int digit) {
      int assigned = letterToDigit[letter];
      if (assigned >= 0) {
         return assigned == digit;
      }
      if (usedDigits[digit]) {
         return false;
      }
      return !(digit == 0 && leadingLetters[letter]);
   }

   private static void assignDigit (int letter, int digit) {
      if (letterToDigit[letter] < 0) {
         letterToDigit[letter] = digit;
         usedDigits[digit] = true;
      }
   }

   private static void unassignDigit (int letter, int digit) {
      if (letterToDigit[letter] == digit) {
         letterToDigit[letter] = -1;
         usedDigits[digit] = false;
      }
   }

   private static String formatSolution (int[] digits) {
      return substitute (word1, digits) + " + " + substitute (word2, digits)
         + " = " + substitute (word3, digits);
   }

   private static String substitute (String word, int[] digits) {
      StringBuilder builder = new StringBuilder (word.length ());
      for (int i = 0; i < word.length(); i++) {
         builder.append (digits[word.charAt (i) - 'A']);
      }
      return builder.toString();
   }
}

