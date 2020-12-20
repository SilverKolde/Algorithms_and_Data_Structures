package Varia;

import java.util.Stack;

/*****************************************************************************
 * Algorithms and Data Structures. LTAT.03.005
 * 2020/2021 autumn
 *
 * Subject:
 *      Stack and/or queue.
 *
 * Author:
 *      Silver Kolde
 *
 * Task:
 *  Write a program using stack or queue to transform a formula to
 *  prefix notation according to the following rules:
 *    toPrefix(t) = 1,
 *    toPrefix(f) = 0,
 *    toPrefix(¬t) = not(1),
 *    toPrefix(t&f) = and(1,0),
 *    toPrefix(t∨f) = or(1,0).
 *
 *****************************************************************************/

public class InfixToPrefixUsingStack {
    public static void main(String[] args) {
        String[] formulas = {
                "t&f",
                "(t∨f)&t",
                "¬(t&f)",
                "¬(t∨f)&t",
                "(t∨f)&¬t"
        };
        for (String infix : formulas) {
            String prefix = toPrefix(infix);
            System.out.printf("%11s%25s%1s%1s%n", "Infix form:", infix, " ..and prefix:    ", prefix);
        }
    }

    /**
     * Algorithm's working principle:
     *      Check digits one by one, push to operands or operators stack.
     *
     *      If closing parentheses is detected,
     *      group the contents of stacks to a single sub formula.
     *
     *      If we have detected a sign that has a higher priority than previous,
     *      group the contents of stacks to a single sub formula.
     *
     *      After going through the initial infix formula,
     *      group the contents of stacks to a single sub formula.
     *
     * @param infix formula to be converted.
     * @return formula in prefix notation.
     */
    private static String toPrefix(String infix) {
        Stack<String> operands = new Stack<>(); // sub formulas, e.g. 1, 0, or(1,0), not(and(0,1))
        Stack<String> operators = new Stack<>(); // ¬, ∨, &

        for (int i = 0; i < infix.length(); i++) {
            String current = infix.substring(i, i+1);

            if (current.equals("¬")) {
                operators.push(current);
            }
            else if (current.equals("(")) {
                operators.push(current);
            }
            else if (current.equals(")")) {
                // Convert the contents of parentheses to a single sub formula
                while (!operators.peek().equals("(")) {
                    toSubFormula(operands, operators);
                }
                operators.pop(); // remove "(", the ")" is never added
            }
            // if "t" or "f"
            else if (!isOperator(current)) {
                current = toNumber(current);
                operands.push(current);
            }
            else {
                // Now the current must be an operand (¬, ∨, &)
                // If current operand has higher priority,
                // we must group the previous parts to a single operand.
                while (operators.size() > 0
                        && !operators.peek().equals("(")
                        && !operators.peek().equals("¬")
                        && priority(current) <= priority(operators.peek()))
                {
                    toSubFormula(operands, operators);
                }
                operators.push(current);
            }
        }

        // Having looped through the initial infix formula,
        // form the last operand
        while (operators.size() > 0) {
            toSubFormula(operands, operators);
        }
        // and return it.
        return operands.pop();
    }

    private static String toNumber(String current) {
        if (current.equalsIgnoreCase("t")) return "1";
        if (current.equalsIgnoreCase("f")) return "0";
        throw new RuntimeException("Only \"t\" and \"f\" can be converted to numbers.");
    }

    /**
     * Transforms the contents of stacks to a single sub formula
     * and pushes it to operands.
     */
    private static void toSubFormula(Stack<String> operands, Stack<String> operators) {
        String operator, subFormula1, subFormula2;
        operator = operators.pop();
        operator = operatorToString(operator);
        if (operator.equals("not")) {
            operands.push(operator + "(" + operands.pop() + ")");
        } else {
            subFormula1 = operands.pop();
            subFormula2 = operands.pop();
            operands.push(operator + "(" + subFormula2 + "," + subFormula1 + ")");
        }
    }

    private static String operatorToString(String operator) {
        if (operator.equals("¬")) return "not";
        if (operator.equals("&")) return "and";
        if (operator.equals("∨")) return "or";
        throw new RuntimeException("Operator \"" + operator + "\" is not acceptable.");
    }

    private static int priority(String operator) {
        if (operator.equals("&")) return 1;
        if (operator.equals("∨")) return 0;
        throw new RuntimeException("Operator \"" + operator + "\" is not acceptable.");
    }

    private static boolean isOperator(String current) {
        return current.equals("¬") || current.equals("∨") || current.equals("&");
    }
}