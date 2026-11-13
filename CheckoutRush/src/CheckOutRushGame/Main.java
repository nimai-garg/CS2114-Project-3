package CheckOutRushGame;

import java.util.Scanner;
import static CheckOutRushGame.ToolClass.*;

public class Main
{

    public static void main(String[] args)
    {
        CalculatorView calculator = new CalculatorView();
        Scanner input = new Scanner(System.in);
        println("Enter an arithmetic expression:");

        if (input.hasNextLine())
        {
            println(calculator.calculate(input.nextLine()));
        }

    }
}
