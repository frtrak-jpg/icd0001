import java.util.*;

/** Tree with two pointers.
 * @since 1.8
 */
public class Tnode {

   private String name;
   private Tnode firstChild;
   private Tnode nextSibling;

   private Tnode (String name) {
      validateNodeName (name);
      this.name = name;
   }

   @Override
   public String toString() {
      StringBuilder b = new StringBuilder();
      appendAsParenthetic (b, this);
      return b.toString();
   }

   /**
    * Builds an expression tree from a whitespace-separated reverse Polish notation string.
    * Supported arithmetic operators are +, -, *, / and supported stack operations are
    * DUP, SWAP and ROT.
    * Throws RuntimeException with an explanatory message when the input is null, empty,
    * contains an illegal token, an operation does not have enough subtrees on the stack,
    * or the expression does not reduce to exactly one root node.
    */

   public static Tnode buildFromRPN (String pol) {
      if (pol == null) {
         throw new RuntimeException ("Invalid RPN: input is null.");
      }

      String trimmed = pol.trim();
      if (trimmed.isEmpty()) {
         throw new RuntimeException (
            "Invalid RPN: expression is empty or whitespace only. Input: '" + pol + "'.");
      }

      Deque<Tnode> stack = new ArrayDeque<>();
      String[] tokens = trimmed.split ("\\s+");
      for (int i = 0; i < tokens.length; i++) {
         String token = tokens[i];
         if (isOperator (token)) {
            if (stack.size() < 2) {
               throw new RuntimeException (
                  "Invalid RPN: operator '" + token + "' at token #" + (i + 1)
                     + " requires two operands, but only " + stack.size()
                     + " available. Expression: '" + pol + "'.");
            }
            Tnode right = stack.pop();
            Tnode left = stack.pop();
            Tnode operatorNode = new Tnode (token);
            operatorNode.firstChild = left;
            left.nextSibling = right;
            stack.push (operatorNode);
         } else if ("DUP".equals (token)) {
            if (stack.isEmpty()) {
               throw new RuntimeException (
                  "Invalid RPN: operation 'DUP' at token #" + (i + 1)
                     + " requires one subtree, but stack is empty. Expression: '"
                     + pol + "'.");
            }
            stack.push (cloneSubtree (stack.peek()));
         } else if ("SWAP".equals (token)) {
            if (stack.size() < 2) {
               throw new RuntimeException (
                  "Invalid RPN: operation 'SWAP' at token #" + (i + 1)
                     + " requires two subtrees, but only " + stack.size()
                     + " available. Expression: '" + pol + "'.");
            }
            Tnode first = stack.pop();
            Tnode second = stack.pop();
            stack.push (first);
            stack.push (second);
         } else if ("ROT".equals (token)) {
            if (stack.size() < 3) {
               throw new RuntimeException (
                  "Invalid RPN: operation 'ROT' at token #" + (i + 1)
                     + " requires three subtrees, but only " + stack.size()
                     + " available. Expression: '" + pol + "'.");
            }
            Tnode first = stack.pop();
            Tnode second = stack.pop();
            Tnode third = stack.pop();
            stack.push (second);
            stack.push (first);
            stack.push (third);
         } else if (isIntegerToken (token)) {
            stack.push (new Tnode (token));
         } else {
            throw new RuntimeException (
               "Invalid RPN: illegal token '" + token + "' at token #" + (i + 1)
                  + ". Only integers and operations +, -, *, /, DUP, SWAP, ROT are allowed."
                  + " Expression: '" + pol + "'.");
         }
      }

      if (stack.size() != 1) {
         throw new RuntimeException (
            "Invalid RPN: expression did not reduce to one root node. Remaining operand count: "
               + stack.size() + ". Expression: '" + pol + "'.");
      }

      return stack.pop();
   }

   /**
    * Algorithm notes and sources for buildFromRPN (postfix to expression tree):
    * 1) Use a stack while scanning tokens left to right.
    * 2) Push operand tokens as leaf nodes.
    * 3) For each operator, pop right then left operand, make operator node,
    *    set left as first child and right as next sibling, then push result back.
    * 4) At the end, exactly one node must remain on stack (the root).
    *
    * Sources:
    * - Wikipedia: Reverse Polish notation (evaluation by stack)
    *   https://en.wikipedia.org/wiki/Reverse_Polish_notation
    * - Wikipedia: Binary expression tree (tree structure for arithmetic expressions)
    *   https://en.wikipedia.org/wiki/Binary_expression_tree
   * - ChatGPT (used for explanation cross-checking)
   *   https://chatgpt.com/
    */

   public static void main (String[] param) {
      String[] samples = {
         "1 2 +",
         "2 1 - 4 * 6 3 / +",
         "5"
      };

      for (String rpn : samples) {
         System.out.println ("RPN:  " + rpn);
         Tnode res = buildFromRPN (rpn);
         System.out.println ("Tree: " + res);
         System.out.println();
      }
   }

   private static void appendAsParenthetic (StringBuilder b, Tnode node) {
      b.append (node.name);
      if (node.firstChild != null) {
         b.append ("(");
         Tnode child = node.firstChild;
         while (child != null) {
            appendAsParenthetic (b, child);
            child = child.nextSibling;
            if (child != null) {
               b.append (",");
            }
         }
         b.append (")");
      }
   }

   private static Tnode cloneSubtree (Tnode node) {
      Tnode clone = new Tnode (node.name);
      if (node.firstChild != null) {
         clone.firstChild = cloneSubtree (node.firstChild);
         Tnode sourceSibling = node.firstChild.nextSibling;
         Tnode cloneSibling = clone.firstChild;
         while (sourceSibling != null) {
            cloneSibling.nextSibling = cloneSubtree (sourceSibling);
            cloneSibling = cloneSibling.nextSibling;
            sourceSibling = sourceSibling.nextSibling;
         }
      }
      return clone;
   }

   private static boolean isOperator (String token) {
      return "+".equals (token) || "-".equals (token)
         || "*".equals (token) || "/".equals (token);
   }

   private static boolean isIntegerToken (String token) {
      if (token == null || token.isEmpty()) {
         return false;
      }
      try {
         Integer.parseInt (token);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }

   private static void validateNodeName (String candidate) {
      if (candidate == null || candidate.isEmpty()) {
         throw new RuntimeException ("Invalid node name: name must be non-empty.");
      }
      for (int i = 0; i < candidate.length(); i++) {
         char c = candidate.charAt (i);
         if (Character.isWhitespace (c) || c == '(' || c == ')' || c == ',') {
            throw new RuntimeException (
               "Invalid node name: '" + candidate
                  + "'. Name must not contain whitespace, parentheses, or commas.");
         }
      }
   }
}

