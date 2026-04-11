import java.math.BigInteger;

/** This class represents fractions of form n/d where n and d are long integer
 * numbers. Basic operations and arithmetics for fractions are provided.
 */
public class Lfraction implements Comparable<Lfraction> {

   /** Main method. Different tests. */
   public static void main (String[] param) {
      // TODO!!! Your debugging tests here
   }

   // ChatGPT (OpenAI) was used as an assistant for implementation discussion and wording:
	// https://chatgpt.com/

   private final long numerator;
   private final long denominator;

   /** Constructor.
    * @param a numerator
    * @param b denominator > 0
    */
   public Lfraction (long a, long b) {
      if (b == 0L) {
         throw new ArithmeticException("Denominator must not be zero: " + a + "/" + b);
      }

      long num = a;
      long den = b;
      if (den < 0L) {
         num = -num;
         den = -den;
      }

      if (num == 0L) {
         this.numerator = 0L;
         this.denominator = 1L;
         return;
      }

      long g = gcd(num, den);
      this.numerator = num / g;
      this.denominator = den / g;
   }

   /** Public method to access the numerator field.
    * @return numerator
    */
   public long getNumerator() {
      return numerator;
   }

   /** Public method to access the denominator field.
    * @return denominator
    */
   public long getDenominator() {
      return denominator;
   }

   /** Conversion to string.
    * @return string representation of the fraction
    */
   @Override
   public String toString() {
      return numerator + "/" + denominator;
   }

   /** Equality test.
    * @param m second fraction
    * @return true if fractions this and m are equal
    */
   @Override
   public boolean equals (Object m) {
      if (this == m) {
         return true;
      }
      if (!(m instanceof Lfraction)) {
         return false;
      }
      Lfraction other = (Lfraction) m;
      return numerator == other.numerator && denominator == other.denominator;
   }

   /** Hashcode has to be the same for equal fractions and in general, different
    * for different fractions.
    * @return hashcode
    */
   @Override
   public int hashCode() {
      int result = 17;
      result = 31 * result + Long.hashCode(numerator);
      result = 31 * result + Long.hashCode(denominator);
      return result;
   }

   /** Sum of fractions.
    * @param m second addend
    * @return this+m
    */
   public Lfraction plus (Lfraction m) {
      if (m == null) {
         throw new NullPointerException("Addend must not be null");
      }
      BigInteger left = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(m.denominator));
      BigInteger right = BigInteger.valueOf(m.numerator).multiply(BigInteger.valueOf(denominator));
      BigInteger den = BigInteger.valueOf(denominator).multiply(BigInteger.valueOf(m.denominator));
      BigInteger num = left.add(right);
      return fromBigInteger(num, den, "Addition overflow");
   }

   /** Multiplication of fractions.
    * @param m second factor
    * @return this*m
    */
   public Lfraction times (Lfraction m) {
      if (m == null) {
         throw new NullPointerException("Factor must not be null");
      }
      BigInteger num = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(m.numerator));
      BigInteger den = BigInteger.valueOf(denominator).multiply(BigInteger.valueOf(m.denominator));
      return fromBigInteger(num, den, "Multiplication overflow");
   }

   /** Inverse of the fraction. n/d becomes d/n.
    * @return inverse of this fraction: 1/this
    */
   public Lfraction inverse() {
      if (numerator == 0L) {
         throw new ArithmeticException("Cannot invert zero fraction: " + this);
      }
      return new Lfraction(denominator, numerator);
   }

   /** Opposite of the fraction. n/d becomes -n/d.
    * @return opposite of this fraction: -this
    */
   public Lfraction opposite() {
      return new Lfraction(-numerator, denominator);
   }

   /** Difference of fractions.
    * @param m subtrahend
    * @return this-m
    */
   public Lfraction minus (Lfraction m) {
      if (m == null) {
         throw new NullPointerException("Subtrahend must not be null");
      }
      return plus(m.opposite());
   }

   /** Quotient of fractions.
    * @param m divisor
    * @return this/m
    */
   public Lfraction divideBy (Lfraction m) {
      if (m == null) {
         throw new NullPointerException("Divisor must not be null");
      }
      if (m.numerator == 0L) {
         throw new ArithmeticException("Cannot divide by zero fraction: " + m);
      }
      BigInteger num = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(m.denominator));
      BigInteger den = BigInteger.valueOf(denominator).multiply(BigInteger.valueOf(m.numerator));
      return fromBigInteger(num, den, "Division overflow");
   }

   /** Comparision of fractions.
    * @param m second fraction
    * @return -1 if this < m; 0 if this==m; 1 if this > m
    */
   @Override
   public int compareTo (Lfraction m) {
      if (m == null) {
         throw new NullPointerException("Compared fraction must not be null");
      }
      BigInteger left = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(m.denominator));
      BigInteger right = BigInteger.valueOf(m.numerator).multiply(BigInteger.valueOf(denominator));
      return Integer.signum(left.compareTo(right));
   }

   /** Clone of the fraction.
    * @return new fraction equal to this
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new Lfraction(numerator, denominator);
   }

   /** Integer part of the (improper) fraction.
    * @return integer part of this fraction
    */
   public long integerPart() {
      return numerator / denominator;
   }

   /** Extract fraction part of the (improper) fraction
    * (a proper fraction without the integer part).
    * @return fraction part of this fraction
    */
   public Lfraction fractionPart() {
      long rem = numerator % denominator;
      return new Lfraction(rem, denominator);
   }

   /** Approximate value of the fraction.
    * @return real value of this fraction
    */
   public double toDouble() {
      return (double) numerator / (double) denominator;
   }

   /** Double value f presented as a fraction with denominator d > 0.
    * @param f real number
    * @param d positive denominator for the result
    * @return f as an approximate fraction of form n/d
    */
   public static Lfraction toLfraction (double f, long d) {
      if (d <= 0L) {
         throw new IllegalArgumentException("Denominator must be positive: " + d);
      }
      if (Double.isNaN(f) || Double.isInfinite(f)) {
         throw new IllegalArgumentException("Cannot convert non-finite double to fraction: " + f);
      }
      double scaled = f * d;
      if (scaled > Long.MAX_VALUE || scaled < Long.MIN_VALUE) {
         throw new ArithmeticException("Numerator out of long range after scaling: f=" + f + ", d=" + d);
      }
      long n = Math.round(scaled);
      return new Lfraction(n, d);
   }

   /** Conversion from string to the fraction. Accepts strings of form
    * that is defined by the toString method.
    * @param s string form (as produced by toString) of the fraction
    * @return fraction represented by s
    */
   public static Lfraction valueOf (String s) {
      if (s == null) {
         throw new IllegalArgumentException("Cannot parse fraction, s=null");
      }

      String trimmed = s.trim();
      int slash = trimmed.indexOf('/');
      if (slash < 0 || slash != trimmed.lastIndexOf('/')) {
         throw new IllegalArgumentException("Illegal fraction format in s='" + s + "': expected exactly one '/'");
      }

      String numPart = trimmed.substring(0, slash).trim();
      String denPart = trimmed.substring(slash + 1).trim();
      if (numPart.isEmpty() || denPart.isEmpty()) {
         throw new IllegalArgumentException("Illegal fraction format in s='" + s + "': numerator or denominator is missing");
      }

      long n;
      long d;
      try {
         n = Long.parseLong(numPart);
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException("Illegal numerator in s='" + s + "': '" + numPart + "'", e);
      }
      try {
         d = Long.parseLong(denPart);
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException("Illegal denominator in s='" + s + "': '" + denPart + "'", e);
      }

      if (d == 0L) {
         throw new IllegalArgumentException("Illegal denominator in s='" + s + "': denominator must not be zero");
      }

      return new Lfraction(n, d);
   }

   private static long gcd(long a, long b) {
      BigInteger aa = BigInteger.valueOf(a).abs();
      BigInteger bb = BigInteger.valueOf(b).abs();
      BigInteger g = aa.gcd(bb);
      return g.longValueExact();
   }

   private static Lfraction fromBigInteger(BigInteger num, BigInteger den, String operationName) {
      try {
         return new Lfraction(num.longValueExact(), den.longValueExact());
      } catch (ArithmeticException e) {
         throw new ArithmeticException(operationName + ": result does not fit into long (" + num + "/" + den + ")");
      }
   }
}

