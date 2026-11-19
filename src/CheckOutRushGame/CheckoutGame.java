package CheckOutRushGame;

public class CheckoutGame
{
    private static double highestReputation;
    private final Player player = new Player();
    private int currDay = 1;
    private CheckoutSession checkoutSession = new CheckoutSession(currDay, player);
    private boolean finished;

    public void endGame()
    {
        if (finished)
        {
            return;
        }
        if (checkoutSession.hasTimedOut())
        {
            finished = true;
            return;
        }
        if (currDay != 7 || !checkoutSession.isFinished())
        {
            throw new IllegalStateException("Complete all seven days first.");
        }
        highestReputation = Math.max(highestReputation, player.getReputation());
        finished = true;
    }

    public void nextSession()
    {
        if (finished)
        {
            return;
        }
        if (!checkoutSession.isFinished())
        {
            throw new IllegalStateException("Serve every customer first.");
        }
        if (checkoutSession.hasTimedOut() || currDay == 7)
        {
            endGame();
        }
        else
        {
            currDay++;
            checkoutSession = new CheckoutSession(currDay, player);
        }
    }

    public int getCurrDay()
    {
        return currDay;
    }

    public CheckoutSession getCheckoutSession()
    {
        return checkoutSession;
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public double getHighestReputation()
    {
        return highestReputation;
    }
}
