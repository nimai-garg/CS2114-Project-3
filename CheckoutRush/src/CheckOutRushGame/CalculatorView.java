package CheckOutRushGame;

import java.util.ArrayList;
import java.util.List;
import static CheckOutRushGame.ToolClass.*;

public class CalculatorView
{
    public CalculatorView()
    {
    }


    public String calculate(String expression)
    {
        if (locateErrors(expression))
        {
            return "Error";
        }

        try
        {
            expression = expression.replaceAll("\\s+", "");

            while (expression.contains("("))
            {
                int lastOpen = expression.lastIndexOf('(');
                int firstClose = expression.indexOf(')', lastOpen);

                if (firstClose == -1)
                {
                    return "Error";
                }

                String subExpr = expression.substring(lastOpen + 1, firstClose);
                double subResult = evaluateBasic(subExpr);

                expression = expression.substring(0, lastOpen) + subResult
                    + expression.substring(firstClose + 1);
            }

            double total = evaluateBasic(expression);
            return String.format("%.2f", total);
        }
        catch (Exception e)
        {
            return "Error";
        }
    }


    private double evaluateBasic(String expr)
    {
        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        int i = 0;
        while (i < expr.length())
        {
            char c = expr.charAt(i);
            if (isBasicOperator(c))
            {
                if (c == '-' && (i == 0 || isBasicOperator(expr.charAt(i - 1))))
                {
                    int start = i;
                    i++;
                    while (i < expr.length()
                        && (Character.isDigit(expr.charAt(i))
                            || expr.charAt(i) == '.'))
                    {
                        i++;
                    }
                    numbers.add(Double.parseDouble(expr.substring(start, i)));
                    continue;
                }

                operators.add(c);
                i++;
            }
            else
            {
                int start = i;
                while (i < expr.length() && (Character.isDigit(expr.charAt(i))
                    || expr.charAt(i) == '.'))
                {
                    i++;
                }
                numbers.add(Double.parseDouble(expr.substring(start, i)));
            }
        }

        for (int j = 0; j < operators.size(); j++)
        {
            char op = operators.get(j);
            if (op == '*' || op == '/')
            {
                double left = numbers.get(j);
                double right = numbers.get(j + 1);
                double res = (op == '*') ? (left * right) : (left / right);
                numbers.set(j, res);
                numbers.remove(j + 1);
                operators.remove(j);
                j--;
            }
        }

        double result = numbers.get(0);
        for (int j = 0; j < operators.size(); j++)
        {
            char op = operators.get(j);
            double right = numbers.get(j + 1);
            if (op == '+')
            {
                result += right;
            }
            else if (op == '-')
            {
                result -= right;
            }
        }

        return result;
    }


    public boolean locateErrors(String expression)
    {
        if (expression == null || expression.trim().isEmpty())
        {
            println("Error: Expression cannot be empty.");
            return true;
        }

        int parenBalance = 0;

        for (int i = 0; i < expression.length(); i++)
        {
            char c = expression.charAt(i);

            if (!Character.isDigit(c) && !isBasicOperator(c) && c != '('
                && c != ')' && c != '.' && c != ' ')
            {
                println("Error: Invalid character '" + c + "' found.");
                return true;
            }

            if (c == '(')
            {
                parenBalance++;
            }
            else if (c == ')')
            {
                parenBalance--;
                if (parenBalance < 0)
                {
                    println(
                        "Error: Unmatched closing parenthesis ')' found.");
                    return true;
                }
            }

            if (isBasicOperator(c) && i < expression.length() - 1)
            {
                char nextChar = expression.charAt(i + 1);
                if (isBasicOperator(nextChar))
                {
                    println(
                        "Error: Consecutive operators are not allowed.");
                    return true;
                }
            }
        }

        if (parenBalance > 0)
        {
            println("Error: Unclosed opening parenthesis '(' found.");
            return true;
        }

        return false;
    }


    private boolean isBasicOperator(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}
