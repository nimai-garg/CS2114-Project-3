package CheckOutRushGame;

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

        if (groceries.isEmpty())
        {
            return;
        }
        System.out.println(groceries.peek().toString());
        System.out.println(
            "This customer has " + groceries.size() + "groceries left!");
    }


    public void displayTimer(double time)
    {
        System.out.println("============================================");
        System.out.printf("Time remaining: %.0f seconds%n", Math.ceil(time));
        System.out.println("============================================");
    }


    public void displayPlayerInfo(Player player)
    {
        System.out.println("==========================================");
        System.out.printf("Reputation: %.2f / 5.00%n", player.getReputation());
        System.out.println("==========================================");
    }


    public void displayMessage(String message)
    {
        ToolClass.println(message);
    }

    public void displayDay(int day)
    {
        displayMessage("\nDay " + day);
    }

    public void displayCustomer(Customer customer)
    {
        displayMessage(customer.getIdentity() + " - " + customer.getPayment());
    }

    public void displayScannedItem(GroceryItem item)
    {
        System.out.printf("%s  $%.2f%n", item.getName(), item.getPrice());
    }

    public void displayStatus(CheckoutSession session)
    {
        displayPlayerInfo(session.getPlayer());
        Customer customer = session.getCurrentCustomer();
        if (customer == null)
        {
            displayMessage("Customers waiting: " + session.getCustomersRemaining());
            return;
        }
        displayCustomer(customer);
        displayTimer(session.getRemainingSeconds());
        System.out.printf("Rating: %.2f / 5.00%n", session.getCurrentRating());
        if (session.isAwaitingChange())
        {
            System.out.printf("Customer pays $%.2f. Enter change: %n",
                session.getCashGiven());
        }
        else
        {
            displayMessage("Enter grocery total:");
        }
    }

    public void displayCheckoutResult(CheckoutSession session)
    {
        System.out.printf("Customer rating: %.2f / 5.00 | Reputation: %.2f / 5.00%n",
            session.getLastRating(), session.getPlayer().getReputation());
    }

    public void displayGameResult(CheckoutGame game)
    {
        System.out.printf("Final reputation: %.2f / 5.00%n",
            game.getPlayer().getReputation());
        System.out.printf("Highest reputation: %.2f / 5.00%n",
            game.getHighestReputation());
    }
}
