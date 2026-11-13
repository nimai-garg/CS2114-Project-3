package collections;

public class CheckoutView
{
    public CheckoutView()
    {
    }


    public void displayCheckoutLine(Queue<Customer> customersInLine)
    {
        if (customersInLine.isEmpty())
        {
            System.out.println("No customers in line");
            return;
        }

        System.out.println(customersInLine.peek().toString());
        System.out.println("  Current  ");
        System.out.println("     O     ");
        System.out.println(" ___/|\\   ");
        System.out.println("|__| |     ");
        System.out.println("O  O/ \\   ");
        System.out.println("___________");

        for (int i = 1; i < customersInLine.size(); i++)
        {
            System.out.println("     O     ");
            System.out.println(" ___/|\\   ");
            System.out.println("|__| |     ");
            System.out.println("O  O/ \\   ");
            System.out.println("___________");
        }
        System.out.println(
            "You have " + customersInLine.size() + " customers in" + "line");

    }


    public void displayGroceries(Queue<GroceryItem> groceries)
    {
        if (groceries.isEmpty())
        {
            System.out.println(
                "You have completed scanning this customer's items, "
                    + "please begin calculating his cost!");
        }

        System.out.println(groceries.peek().toString());
        System.out.println(
            "This customer has " + groceries.size() + "groceries left!");
    }


    public void displayTimer(double time, int day)
    {
        System.out.println("============================================");
        System.out.println("Day: " + day + " time passed: " + time);
        System.out.println("============================================");
    }


    public void displayPlayerInfo(PlayerClass player)
    {
        System.out.println("==========================================");
        System.out.println(
            " SCORE: " + player.getScore() + "  |  REPUTATION: "
                + player.getReputation());
        System.out.println("==========================================");
    }

}
