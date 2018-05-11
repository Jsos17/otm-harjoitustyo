/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorapp.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

/**
 * ExpressionEvaluator is responsible for actually evaluating expressions.
 *
 * @author jpssilve
 */
public class ExpressionEvaluator {

    private CalculatorService calculator;
    private InputParser inputParser;

    /**
     * The constructor gets an instance of CalculatorService class and an
     * instance of InputParser class.
     *
     * @param calculator
     * @param inputParser
     */
    public ExpressionEvaluator(CalculatorService calculator, InputParser inputParser) {
        this.calculator = calculator;
        this.inputParser = inputParser;
    }

    protected ArrayList<String> tokenizeExpression(String expression) {
        ArrayList<String> mathematicalTokens = new ArrayList<>();

        int i = 0;
        while (i < expression.length()) {
            char character = expression.charAt(i);

            if (this.inputParser.isANumber(character)) {
                i = numberProcessor(expression, mathematicalTokens, i);
                continue;

            } else if (this.inputParser.startsAFunction(character)) {
                i = functionProcessor(expression, mathematicalTokens, i);
                continue;

            } else if (this.inputParser.isAMathOperator(character) || character == '(' || character == ')') {
                mathematicalTokens.add(Character.toString(character));
            }

            i++;
        }

        return mathematicalTokens;
    }

    private int numberProcessor(String expression, ArrayList<String> mathematicalTokens, int i) {
        int index = i;
        while (i < expression.length() && (this.inputParser.isANumber(expression.charAt(i)) || expression.charAt(i) == '.')) {
            i++;
        }

        if (this.inputParser.stringIsANumber(expression.substring(index, i))) {
            mathematicalTokens.add(expression.substring(index, i));
        }

        return i;
    }

    private int functionProcessor(String expression, ArrayList<String> mathematicalTokens, int i) {
        int index = i;
        while (i < expression.length() && expression.charAt(i) != '(') {
            i++;
        }

        if (this.inputParser.isAFunction(expression.substring(index, i))) {
            mathematicalTokens.add(expression.substring(index, i));
        }

        return i;
    }

    protected ArrayDeque<String> shuntingYardWithFunctions(ArrayList<String> mathematicalTokens) {
        ArrayDeque<String> postFixOutput = new ArrayDeque();
        Stack<String> operatorStack = new Stack();

        for (int j = 0; j < mathematicalTokens.size(); j++) {
            String mathToken = mathematicalTokens.get(j);

            if (this.inputParser.stringIsANumber(mathToken)) {
                postFixOutput.addLast(mathToken);

            } else if (this.inputParser.isAFunction(mathToken) || mathToken.equals("(")) {
                operatorStack.push(mathToken);

            } else if (stringIsAMathOperator(mathToken)) {
                while (!operatorStack.empty() && !operatorStack.peek().equals("(")
                        && (this.inputParser.isAFunction(operatorStack.peek()) || hasHigherPrecedence(operatorStack.peek().charAt(0), mathToken.charAt(0)))) {
                    postFixOutput.addLast(operatorStack.pop());
                }

                operatorStack.push(mathToken);

            } else if (mathToken.equals(")")) {
                while (!operatorStack.empty()) {
                    String operator = operatorStack.pop();

                    if (operator.equals("(")) {
                        break;
                    }

                    postFixOutput.addLast(operator);
                }
            }
        }

        return helperShuntingYard(operatorStack, postFixOutput);
    }

    private ArrayDeque<String> helperShuntingYard(Stack<String> operatorStack, ArrayDeque<String> postFixOutput) {
        while (!operatorStack.empty()) {
            postFixOutput.addLast(operatorStack.pop());
        }

        return postFixOutput;
    }

    protected double postfixEvaluator(ArrayDeque<String> postFixOutput) {
        Stack<Double> values = new Stack();
        while (!postFixOutput.isEmpty()) {
            String mathObject = postFixOutput.pollFirst();

            if (this.inputParser.stringIsANumber(mathObject)) {
                values.push(Double.parseDouble(mathObject));

            } else if (this.inputParser.isAFunction(mathObject)) {
                if (!values.empty()) {
                    values.push(executeTheRightFunction1(mathObject, values.pop()));
                }

            } else if (stringIsAMathOperator(mathObject)) {
                if (values.size() >= 2) {
                    double x1 = values.pop();
                    double x2 = values.pop();
                    values.push(executeTheRightOperation(mathObject.charAt(0), x2, x1));
                }
            }
        }

        return helperPostFixEval(values);
    }

    protected double helperPostFixEval(Stack<Double> values) {
        if (values.size() == 1) {
            return values.pop();
        } else {
            return Double.NaN;
        }
    }

    /**
     * expressionEvaluation first executes a series of checks by checking the
     * length of the expression and using the InputParser given in the
     * constructor to make sure that the expression can be evaluated.
     *
     * Then it calls the function tokenizeExpressions, which puts numbers,
     * mathematical operators, functions and brackets into an ArrayList of
     * Strings i.e. makes mathematical tokens out of them.
     *
     * Then the ArrayList of Strings is passed to the shunting-yard algorithm
     * (by Djikstra, wikipedia pseudocode forms the basis for implementation).
     *
     * The shunting-yard algorithm produces the expression in reverse Polish
     * notation also know as postfix notation. After the shunting-yard algorithm
     * terminates all the brackets have been thrown out and the expression is
     * now in postfix notation as an ArrayDeque of Strings.
     *
     * This ArrayDeque consisting of the numbers, functions and operators is
     * then passed to the postfixEvaluator, which actually evaluates the
     * expression from left to right (in postfix notation) and returns a double
     * value.
     *
     * @param expression
     * @return a double value, or NaN if expression cannot be evaluated or
     * positive infinity if the expression is too long
     */
    public double expressionEvaluation(String expression) {
        if (expression.length() > 1000) {
            return Double.POSITIVE_INFINITY;
        } else if (this.inputParser.bracketingEquals(expression)
                && this.inputParser.numbersAndBracketsCorrect(expression)
                && this.inputParser.correctOperatorAndDotPlacement(expression)) {

            ArrayDeque<String> queue = shuntingYardWithFunctions(tokenizeExpression(expression));
            return postfixEvaluator(queue);
        } else {
            return Double.NaN;
        }
    }

    private boolean hasHigherPrecedence(char c1, char c2) {
        if (c1 == '^' && c2 != '^') {
            return true;
        } else if ((c1 == '*' || c1 == '/') && c2 != '^') {
            return true;
        } else if ((c1 == '+' || c1 == '-') && (c2 == '+' || c2 == '-')) {
            return true;
        } else {
            return false;
        }
    }

    protected double executeTheRightFunction1(String function, double x) {
        switch (function) {
            case "sqrt":
                return this.calculator.squareRoot(x);
            case "sin":
                return this.calculator.sin(x);
            case "cos":
                return this.calculator.cos(x);
            case "tan":
                return this.calculator.tan(x);
            case "ln":
                return this.calculator.naturalLog(x);
            case "log":
                return this.calculator.base10log(x);
            case "abs":
                return this.calculator.abs(x);
            default:
                return executeTheRightFunction2(function, x);
        }
    }

    protected double executeTheRightFunction2(String function, double x) {
        switch (function) {
            case "neg":
                return this.calculator.negate(x);
            case "%":
                return this.calculator.percent(x);
            default:
                return Double.NaN;
        }
    }

    protected double executeTheRightOperation(char operator, double x1, double x2) {
        switch (operator) {
            case '+':
                return this.calculator.addDouble(x1, x2);
            case '-':
                return this.calculator.subtractDouble(x1, x2);
            case '*':
                return this.calculator.multiplyDouble(x1, x2);
            case '/':
                return this.calculator.divideDouble(x1, x2);
            case '^':
                return this.calculator.exponentiation(x1, x2);
            default:
                return Double.NaN;
        }
    }

    protected boolean stringIsAMathOperator(String candidate) {
        if (candidate.length() == 1) {
            return this.inputParser.isAMathOperator(candidate.charAt(0));
        } else {
            return false;
        }
    }
}
