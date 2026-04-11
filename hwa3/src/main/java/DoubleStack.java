import java.util.*;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack {

   private LinkedList<Double> stack;

   public static void main (String[] argum) {
      // Intentional no-op.
   }

   DoubleStack() {
      stack = new LinkedList<Double>();
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      DoubleStack copy = new DoubleStack();
      copy.stack = new LinkedList<Double>(stack);
      return copy;
   }

   public boolean stEmpty() {
      return stack.isEmpty();
   }

   public void push (double a) {
      stack.addLast (Double.valueOf (a));
   }

   public double pop() {
      if (stack.isEmpty()) {
         throw new RuntimeException ("pop: stack is empty");
      }
      return stack.removeLast().doubleValue();
   } // pop

   public void op (String s) {
      String op = String.valueOf (s);
      if (s == null || s.length() != 1 || "+-*/".indexOf (s.charAt (0)) < 0) {
         throw new RuntimeException ("op: illegal operation '" + op + "'");
      }
      if (stack.size() < 2) {
         throw new RuntimeException (
            "op: not enough operands for operation '" + op + "'");
      }
      double right = pop();
      double left = pop();
      double result;
      switch (s.charAt (0)) {
         case '+': result = left + right; break;
         case '-': result = left - right; break;
         case '*': result = left * right; break;
         case '/': result = left / right; break;
         default:
            throw new RuntimeException ("op: illegal operation '" + op + "'");
      }
      push (result);
   }

   public void swap() {
      if (stack.size() < 2) {
         throw new RuntimeException ("swap: not enough operands");
      }
      double b = pop();
      double a = pop();
      push (b);
      push (a);
   }

   public void rot() {
      if (stack.size() < 3) {
         throw new RuntimeException ("rot: not enough operands");
      }
      double c = pop();
      double b = pop();
      double a = pop();
      push (b);
      push (c);
      push (a);
   }

   public void dup() {
      if (stack.isEmpty()) {
         throw new RuntimeException ("dup: stack is empty");
      }
      push (tos());
   }

   public void drop() {
      if (stack.isEmpty()) {
         throw new RuntimeException ("drop: stack is empty");
      }
      pop();
   }
  
   public double tos() {
      if (stack.isEmpty()) {
         throw new RuntimeException ("tos: stack is empty");
      }
      return stack.getLast().doubleValue();
   }

   @Override
   public boolean equals (Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof DoubleStack)) {
         return false;
      }
      DoubleStack other = (DoubleStack)o;
      return stack.equals (other.stack);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < stack.size(); i++) {
         if (i > 0) {
            sb.append (' ');
         }
         sb.append (String.valueOf (stack.get (i)));
      }
      return sb.toString();
   }

   public static double interpret (String pol) {
      String expression = String.valueOf (pol);
      if (pol == null || pol.trim().length() == 0) {
         throw new RuntimeException (
            "interpret: expression is missing: '" + expression + "'");
      }
      DoubleStack stack = new DoubleStack();
      StringTokenizer tokenizer = new StringTokenizer (pol);
      while (tokenizer.hasMoreTokens()) {
         String token = tokenizer.nextToken();
         if (isOperatorToken (token)) {
            if (stack.stack.size() < 2) {
               throw new RuntimeException (
                  "interpret: not enough operands for operation '" + token +
                  "' in expression: '" + expression + "'");
            }
            stack.op (token);
         } else if (isStackWordToken (token)) {
            applyStackWord (stack, token, expression);
         } else {
            try {
               double value = Double.parseDouble (token);
               stack.push (value);
            } catch (NumberFormatException e) {
               throw new RuntimeException (
                  "interpret: illegal symbol '" + token +
                  "' in expression: '" + expression + "'");
            }
         }
      }
      if (stack.stack.size() != 1) {
         if (stack.stack.isEmpty()) {
            throw new RuntimeException (
               "interpret: expression leaves no result in expression: '" +
               expression + "'");
         }
         throw new RuntimeException (
            "interpret: too many numbers remain in expression: '" +
            expression + "'");
      }
      return stack.pop();
   }

   private static boolean isOperatorToken (String token) {
      return token != null && token.length() == 1 && "+-*/".indexOf (token.charAt (0)) >= 0;
   }

   private static boolean isStackWordToken (String token) {
      return "SWAP".equals (token) || "ROT".equals (token)
         || "DUP".equals (token) || "DROP".equals (token);
   }

   private static void applyStackWord (DoubleStack stack, String token,
      String expression) {
      try {
         if ("SWAP".equals (token)) {
            stack.swap();
         } else if ("ROT".equals (token)) {
            stack.rot();
         } else if ("DUP".equals (token)) {
            stack.dup();
         } else if ("DROP".equals (token)) {
            stack.drop();
         }
      } catch (RuntimeException e) {
         throw new RuntimeException (
            "interpret: " + e.getMessage() + " for operation '" + token +
            "' in expression: '" + expression + "'");
      }
   }

}

