
package CheckOutRushGame;

import java.util.ArrayList;
import java.util.List;

public class Calculator
{
    public Calculator()
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

                String subExpr = expression.substring(
                    lastOpen + 1, firstClose);

                double subResult = evaluateBasic(subExpr);

                expression = expression.substring(0, lastOpen)
                    + subResult
                    + expression.substring(firstClose + 1);
            }

            double total = evaluateBasic(expression);

            if (!Double.isFinite(total))
            {
                return "Error";
            }

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
                if (c == '-' && (i == 0
                    || isBasicOperator(expr.charAt(i - 1))))
                {
                    int start = i;
                    i++;

                    while (i < expr.length()
                        && (Character.isDigit(expr.charAt(i))
                            || expr.charAt(i) == '.'))
                    {
                        i++;
                    }

                    numbers.add(Double.parseDouble(
                        expr.substring(start, i)));

                    continue;
                }

                operators.add(c);
                i++;
            }
            else
            {
                int start = i;

                while (i < expr.length()
                    && (Character.isDigit(expr.charAt(i))
                        || expr.charAt(i) == '.'))
                {
                    i++;
                }

                numbers.add(Double.parseDouble(
                    expr.substring(start, i)));
            }
        }

        // Handle multiplication and division first
        for (int j = 0; j < operators.size(); j++)
        {
            char op = operators.get(j);

            if (op == '*' || op == '/')
            {
                double left = numbers.get(j);
                double right = numbers.get(j + 1);

                double res = (op == '*')
                    ? left * right
                    : left / right;

                numbers.set(j, res);
                numbers.remove(j + 1);
                operators.remove(j);

                j--;
            }
        }

        // Handle addition and subtraction
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
            return true;
        }

        int parenBalance = 0;

        for (int i = 0; i < expression.length(); i++)
        {
            char c = expression.charAt(i);

            if (!Character.isDigit(c) && !isBasicOperator(c)
                && c != '(' && c != ')' && c != '.' && c != ' ')
            {
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
                    return true;
                }
            }

            if (isBasicOperator(c)
                && i < expression.length() - 1)
            {
                char nextChar = expression.charAt(i + 1);

                if (isBasicOperator(nextChar))
                {
                    return true;
                }
            }
        }

        return parenBalance != 0;
    }

    private boolean isBasicOperator(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}
