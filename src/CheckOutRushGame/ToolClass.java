package CheckOutRushGame;

import java.util.Scanner;

public final class ToolClass
{
    private static final Scanner input = new Scanner(System.in);

    public static void print(String txt)
    {
        System.out.print(txt);
    }

    public static void println(String txt)
    {
        System.out.println(txt);
    }

    public static String readInput() {
        return input.nextLine().trim();
    }

    public static int randomInt(int min, int max) {
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    public static int askOption(String prompt, int min, int max)
    {
        int option = -1;
        while (true)
        {
            println(prompt);
            print("> Enter choice: ");
            try
            {
                option = Integer.parseInt(readInput());
            }
            catch (NullPointerException e)
            {
                println("Please enter a number.");
                continue;
            }
            catch (NumberFormatException e)
            {
                println("Please enter a valid integer.");
                continue;
            }
            if (option < min)
            {
                println("Please enter an integer no less than " + min + ".");
            }
            else if (option > max)
            {
                println("Please enter an integer no more than " + max + ".");
            }
            else {
                break;
            }
        }
        return option;
    }

    public static int askOption(String[] options, int min, int max)
    {
        int option = -1;
        while (true)
        {
            println("");
            println("=== MENU ===");
            for (int i = 0; i < options.length; i++) {
                println(" [" + (i + 1) + "] " + options[i]);
            }
            print("\n> Enter choice: ");
            try
            {
                option = Integer.parseInt(readInput());
            }
            catch (NullPointerException e)
            {
                println("Please enter a number.");
                continue;
            }
            catch (NumberFormatException e)
            {
                println("Please enter a valid integer.");
                continue;
            }
            if (option < min)
            {
                println("Please enter an integer no less than " + min + ".");
            }
            else if (option > max)
            {
                println("Please enter an integer no more than " + max + ".");
            }
            else {
                break;
            }
        }
        return option;
    }
}
