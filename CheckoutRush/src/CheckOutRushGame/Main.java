package CheckOutRushGame;

import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {
        CalculatorView calculator = new CalculatorView();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter an arithmetic expression:");

        if (input.hasNextLine())
        {
            System.out.println(calculator.calculate(input.nextLine()));
        }

    }
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
