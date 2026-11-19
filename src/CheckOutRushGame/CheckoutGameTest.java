package CheckOutRushGame;

public class CheckoutGameTest extends student.TestCase
{
    private CheckoutGame game;

    public void setUp()
    {
        game = new CheckoutGame();
    }


    public void testConstructor()
    {
        assertEquals(1, game.getCurrDay());
        assertFalse(game.isFinished());

        assertNotNull(game.getPlayer());
        assertNotNull(game.getCheckoutSession());

        assertSame(game.getPlayer(), game.getCheckoutSession().getPlayer());
    }


    public void testNextSessionBeforeCompletion()
    {
        int originalDay = game.getCurrDay();
        CheckoutSession originalSession = game.getCheckoutSession();

        try
        {
            game.nextSession();
            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException e)
        {
            assertEquals(originalDay, game.getCurrDay());
            assertSame(originalSession, game.getCheckoutSession());
        }
    }


    public void testEndGameBeforeCompletion()
    {
        try
        {
            game.endGame();
            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException e)
        {
            assertFalse(game.isFinished());
        }
    }


    public void testNextSession()
    {
        Player originalPlayer = game.getPlayer();
        CheckoutSession originalSession = game.getCheckoutSession();

        finishSession(game.getCheckoutSession());

        game.nextSession();

        assertEquals(2, game.getCurrDay());
        assertFalse(game.isFinished());

        assertNotSame(originalSession, game.getCheckoutSession());

        assertSame(originalPlayer, game.getPlayer());

        assertSame(originalPlayer, game.getCheckoutSession().getPlayer());
    }


    public void testMultipleSessions()
    {
        for (int day = 1; day < 7; day++)
        {
            finishSession(game.getCheckoutSession());

            game.nextSession();

            assertEquals(day + 1, game.getCurrDay());
            assertFalse(game.isFinished());
        }
    }


    public void testCompleteGame()
    {
        for (int day = 1; day <= 7; day++)
        {
            assertEquals(day, game.getCurrDay());

            finishSession(game.getCheckoutSession());

            if (day < 7)
            {
                game.nextSession();
                assertFalse(game.isFinished());
            }
        }

        game.nextSession();

        assertTrue(game.isFinished());
        assertEquals(7, game.getCurrDay());

        game.endGame();

        assertTrue(game.isFinished());
    }


    public void testNextSessionAfterGameFinished()
    {
        for (int day = 1; day <= 7; day++)
        {
            finishSession(game.getCheckoutSession());

            if (day < 7)
            {
                game.nextSession();
            }
        }

        game.nextSession();

        assertTrue(game.isFinished());

        int finalDay = game.getCurrDay();
        CheckoutSession finalSession = game.getCheckoutSession();

        game.nextSession();

        assertEquals(finalDay, game.getCurrDay());
        assertSame(finalSession, game.getCheckoutSession());
        assertTrue(game.isFinished());
    }


    public void testHighestReputation()
    {
        for (int day = 1; day <= 7; day++)
        {
            finishSession(game.getCheckoutSession());

            if (day < 7)
            {
                game.nextSession();
            }
        }

        game.nextSession();

        assertTrue(game.getHighestReputation() >= 0);
        assertTrue(game.getHighestReputation() <= 5);
    }


    public void testTimeoutEndsCareerWithoutAdvancingDay()
    {
        CheckoutSession session = game.getCheckoutSession();
        session.startCheckOut();
        session.getCurrentCustomer().setPatience(0);

        assertTrue(session.expireCheckoutIfNeeded());
        game.nextSession();

        assertTrue(game.isFinished());
        assertEquals(1, game.getCurrDay());
        assertSame(session, game.getCheckoutSession());
        assertEquals(1, game.getPlayer().getRatingCount());
        assertEquals(0.0, game.getPlayer().getReputation(), 0.001);
    }


    /**
     * Completes every customer in a session using the actual checkout methods.
     *
     * @param session
     *            the session being completed
     */
    private void finishSession(CheckoutSession session)
    {
        while (!session.isFinished())
        {
            int activeLaneSize = session.isExpressLaneActive()
                ? session.getExpressQueueSize() : session.getRegularQueueSize();
            if (activeLaneSize == 0)
            {
                session.switchActiveLane();
            }
            session.startCheckOut();

            double total = 0;

            while (!session.getCurrentCustomer().getGroceryQueue().isEmpty())
            {
                GroceryItem item = session.scanNextItem();
                total += item.getPrice();
            }

            assertTrue(session.submitCheckout(total));

            if (session.isAwaitingChange())
            {
                double change = session.getCashGiven() - total;

                assertTrue(session.submitChange(change));
            }
        }

        assertTrue(session.isFinished());
        assertEquals(0, session.getCustomersRemaining());
    }
}
