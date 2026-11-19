package CheckOutRushGame;

import java.util.Locale;

public class CheckoutView
{
    public CheckoutView()
    {
    }


    public int chooseLaneAction()
    {
        return ToolClass.askOption(
            new String[] { "Next customer", "Switch lane" }, 1, 2);
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
        displayMessage(customer.toString());
    }


    public void displayScannedItem(GroceryItem item)
    {
        displayMessage(item.toString());
    }


    public void displayStatus(CheckoutSession session)
    {
        displayPlayerInfo(session.getPlayer());
        String lane = session.isExpressLaneActive() ? "Express" : "Regular";
        System.out.printf(
            "Active lane: %s | Regular waiting: %d | Express waiting (3 items or fewer): %d%n",
            lane, session.getRegularQueueSize(), session.getExpressQueueSize());
        Customer customer = session.getCurrentCustomer();
        if (customer == null)
        {
            return;
        }
        displayCustomer(customer);
        displayTimer(session.getRemainingSeconds());
        System.out.printf("Rating: %.2f / 5.00%n", session.getCurrentRating());
        if (customer.getItemCount() > 0)
        {
            displayMessage("Type scan for the next item. Items left: "
                + customer.getItemCount());
        }
        else if (session.isAwaitingChange())
        {
            System.out.printf(Locale.US,
                "Customer pays $%.2f. Enter change: %n",
                session.getCashGiven());
        }
        else
        {
            displayMessage("Enter grocery total:");
        }
        displayMessage("Calculator: type calc followed by an expression.");
    }


    public void displayShiftResult(int day, boolean timedOut)
    {
        if (timedOut)
        {
            displayMessage("Time expired on Day " + day + ". You were fired.");
        }
        else
        {
            displayMessage("Day " + day + " complete. Both lanes are clear.");
        }
    }


    public void displayCheckoutResult(CheckoutSession session)
    {
        System.out.printf(
            "Customer rating: %.2f / 5.00 | Reputation: %.2f / 5.00%n",
            session.getLastRating(),
            session.getPlayer().getReputation());
    }


    public void displayGameResult(CheckoutGame game)
    {
        System.out.printf(
            "Final reputation: %.2f / 5.00%n",
            game.getPlayer().getReputation());
        System.out.printf(
            "Highest reputation: %.2f / 5.00%n",
            game.getHighestReputation());
    }
}
