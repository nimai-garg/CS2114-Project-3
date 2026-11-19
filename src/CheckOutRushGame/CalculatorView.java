
package CheckOutRushGame;

import static CheckOutRushGame.ToolClass.*;

public class CalculatorView
{
    private Calculator calculator;

    public CalculatorView()
    {
        calculator = new Calculator();
    }

    public String calculate(String expression)
    {
        return calculator.calculate(expression);
    }

    public void displayResult(String result)
    {
        println("Calculator: " + result);
    }
}
