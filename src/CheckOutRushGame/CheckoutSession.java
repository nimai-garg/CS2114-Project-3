package CheckOutRushGame;

public class CheckoutSession
{
    private final Player player;
    private final Queue<Customer> customers = new Queue<>();
    private Customer currentCustomer;
    private long checkoutStart;
    private long penaltySeconds;
    private long totalCents;
    private long cashCents;
    private boolean awaitingChange;
    private boolean finished;
    private double lastRating;

    public CheckoutSession(int currDay)
    {
        this(currDay, new Player());
    }

    CheckoutSession(int currDay, Player player)
    {
        if (currDay < 1 || currDay > 7)
        {
            throw new IllegalArgumentException("Day must be between 1 and 7.");
        }
        this.player = player;
        for (int i = 0; i < currDay + 4; i++)
        {
            customers.enqueue(new Customer(currDay));
        }
    }

    public void startCheckOut()
    {
        if (finished || currentCustomer != null || customers.isEmpty())
        {
            throw new IllegalStateException("Cannot start a checkout now.");
        }
        currentCustomer = customers.dequeue();
        totalCents = 0;
        cashCents = 0;
        penaltySeconds = 0;
        awaitingChange = false;
        checkoutStart = System.nanoTime();
    }

    public GroceryItem scanNextItem()
    {
        requireActiveCheckout();
        GroceryItem item = currentCustomer.getGroceryQueue().dequeue();
        totalCents += Math.round(item.getPrice() * 100);
        return item;
    }

    public boolean submitCheckout(double amount)
    {
        requireActiveCheckout();
        if (awaitingChange || !currentCustomer.getGroceryQueue().isEmpty())
        {
            throw new IllegalStateException("The checkout is not accepting a total.");
        }
        validateAmount(amount);
        if (Math.abs(amount - totalCents / 100.0) > 0.000001)
        {
            penaltySeconds += 5;
            return false;
        }
        if ("credit".equals(currentCustomer.getPayment()))
        {
            completeCheckout();
        }
        else
        {
            cashCents = generateCash();
            awaitingChange = true;
        }
        return true;
    }

    public boolean submitChange(double amount)
    {
        requireActiveCheckout();
        if (!awaitingChange)
        {
            throw new IllegalStateException("Submit the total first.");
        }
        validateAmount(amount);
        if (Math.abs(amount - (cashCents - totalCents) / 100.0) > 0.000001)
        {
            penaltySeconds += 5;
            return false;
        }
        completeCheckout();
        return true;
    }

    private long generateCash()
    {
        int method = ToolClass.randomInt(0, 2);
        if (method == 0)
        {
            return totalCents;
        }
        int[] bills = { 100, 200, 500, 1000, 2000, 5000, 10000 };
        int first = 0;
        if (method == 2)
        {
            first = 4;
        }
        while (first < bills.length && bills[first] < totalCents)
        {
            first++;
        }
        if (first == bills.length)
        {
            return ((totalCents + 9999) / 10000) * 10000;
        }
        if (method == 2)
        {
            first = ToolClass.randomInt(first, bills.length - 1);
        }
        return bills[first];
    }

    private void completeCheckout()
    {
        lastRating = getCurrentRating();
        player.updateReputation(lastRating);
        currentCustomer = null;
        awaitingChange = false;
        if (customers.isEmpty())
        {
            endSession();
        }
    }

    public void endSession()
    {
        if (currentCustomer != null || !customers.isEmpty())
        {
            throw new IllegalStateException("Serve every customer first.");
        }
        finished = true;
    }

    public double getRemainingSeconds()
    {
        requireActiveCheckout();
        double elapsed = (System.nanoTime() - checkoutStart) / 1_000_000_000.0;
        return currentCustomer.getPatience() - elapsed - penaltySeconds;
    }

    public double getCurrentRating()
    {
        double remaining = getRemainingSeconds();
        if (remaining > 0)
        {
            return 5;
        }
        return Math.max(0, 5 - 0.5 * (1 + Math.floor(-remaining / 5)));
    }

    public double getCashGiven()
    {
        if (!awaitingChange)
        {
            throw new IllegalStateException("No cash payment is active.");
        }
        return cashCents / 100.0;
    }

    public boolean isAwaitingChange()
    {
        return awaitingChange;
    }

    public Customer getCurrentCustomer()
    {
        return currentCustomer;
    }

    public int getCustomersRemaining()
    {
        return customers.size();
    }

    public Player getPlayer()
    {
        return player;
    }

    public double getLastRating()
    {
        return lastRating;
    }

    public boolean isFinished()
    {
        return finished;
    }

    private void requireActiveCheckout()
    {
        if (currentCustomer == null)
        {
            throw new IllegalStateException("There is no active checkout.");
        }
    }

    private void validateAmount(double amount)
    {
        if (amount >= Long.MAX_VALUE / 100.0)
        {
            throw new IllegalArgumentException("The amount is too large.");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0)
        {
            throw new IllegalArgumentException("Enter a finite, nonnegative amount.");
        }
    }
}
