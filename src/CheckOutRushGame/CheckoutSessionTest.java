package CheckOutRushGame;

public class CheckoutSessionTest extends student.TestCase
{
    private CheckoutSession session;

    public void setUp()
    {
        session = new CheckoutSession(new Player());
    }

    public void testGeneratedShiftsOfferBothLanes()
    {
        for (int day = 1; day <= 7; day++)
        {
            CheckoutSession shift = new CheckoutSession(day);
            assertEquals(day + 4, shift.getCustomersRemaining());
            assertTrue(shift.getExpressQueueSize() > 0);
            assertTrue(shift.getRegularQueueSize() > 0);
            assertFalse(shift.isExpressLaneActive());
        }
    }

    public void testInvalidDay()
    {
        expect(IllegalArgumentException.class, () -> new CheckoutSession(0));
        expect(IllegalArgumentException.class, () -> new CheckoutSession(8));
    }

    public void testRoutingAndFifoWithinEachLane()
    {
        Customer expressFirst = shopper(3, "credit");
        Customer regularFirst = shopper(4, "credit");
        Customer expressSecond = shopper(1, "credit");
        Customer regularSecond = shopper(5, "credit");
        session.addCustomer(expressFirst);
        session.addCustomer(regularFirst);
        session.addCustomer(expressSecond);
        session.addCustomer(regularSecond);
        assertEquals(2, session.getExpressQueueSize());
        assertEquals(2, session.getRegularQueueSize());

        session.startCheckOut();
        assertSame(regularFirst, session.getCurrentCustomer());
        assertTrue(session.submitCheckout(scanCart()));
        session.switchActiveLane();
        assertTrue(session.isExpressLaneActive());

        session.startCheckOut();
        assertSame(expressFirst, session.getCurrentCustomer());
        assertTrue(session.submitCheckout(scanCart()));
        session.startCheckOut();
        assertSame(expressSecond, session.getCurrentCustomer());
        assertTrue(session.submitCheckout(scanCart()));
        assertEquals(1, session.getCustomersRemaining());
        assertFalse(session.isFinished());

        session.switchActiveLane();
        assertFalse(session.isExpressLaneActive());
        session.startCheckOut();
        assertSame(regularSecond, session.getCurrentCustomer());
        assertTrue(session.submitCheckout(scanCart()));
        assertTrue(session.isFinished());
        assertEquals(0, session.getCustomersRemaining());
        assertEquals(4, session.getPlayer().getRatingCount());
    }

    public void testInvalidCustomerLeavesQueuesEmpty()
    {
        expect(IllegalArgumentException.class, () -> session.addCustomer(null));
        Customer customer = shopper(1, "credit");
        customer.getGroceryQueue().dequeue();
        expect(IllegalArgumentException.class, () -> session.addCustomer(customer));
        assertEquals(0, session.getExpressQueueSize());
        assertEquals(0, session.getRegularQueueSize());
    }

    public void testEmptyExpressLaneDoesNotServeRegularCustomer()
    {
        session.addCustomer(shopper(4, "credit"));
        session.switchActiveLane();
        expect(IllegalStateException.class, session::startCheckOut);
        assertNull(session.getCurrentCustomer());
        assertEquals(1, session.getRegularQueueSize());
        assertEquals(0, session.getExpressQueueSize());
    }

    public void testCannotSwitchOrStartAgainDuringCheckout()
    {
        Customer customer = startExpress("credit");
        expect(IllegalStateException.class, session::switchActiveLane);
        expect(IllegalStateException.class, session::startCheckOut);
        assertTrue(session.isExpressLaneActive());
        assertSame(customer, session.getCurrentCustomer());
    }

    public void testCannotEndWithWaitingOrActiveCustomers()
    {
        session.addCustomer(shopper(1, "credit"));
        expect(IllegalStateException.class, session::endSession);
        session.switchActiveLane();
        session.startCheckOut();
        expect(IllegalStateException.class, session::endSession);
        assertFalse(session.isFinished());
    }

    public void testEndEmptySession()
    {
        session.endSession();

        assertTrue(session.isFinished());
        expect(IllegalStateException.class, session::switchActiveLane);
        expect(IllegalStateException.class, session::startCheckOut);
        expect(IllegalStateException.class,
            () -> session.addCustomer(shopper(1, "credit")));
    }

    public void testScanAndPaymentRejectInvalidSequence()
    {
        expect(IllegalStateException.class, session::scanNextItem);
        expect(IllegalStateException.class, () -> session.submitCheckout(0));
        expect(IllegalStateException.class, () -> session.submitChange(0));

        Customer customer = startExpress("credit");
        expect(IllegalStateException.class, () -> session.submitCheckout(2.50));
        expect(IllegalStateException.class, () -> session.submitChange(0));
        assertEquals(1, customer.getItemCount());
        assertEquals(2.50, session.scanNextItem().getPrice(), 0.001);
        expect(EmptyQueueException.class, session::scanNextItem);
        assertTrue(session.submitCheckout(2.50));
        expect(IllegalStateException.class, () -> session.submitCheckout(2.50));
        assertNull(session.getCurrentCustomer());
        assertEquals(1, session.getPlayer().getRatingCount());
    }

    public void testInvalidAmountsAllowRetryForTotalAndChange()
    {
        startExpress("cash");
        double total = scanCart();
        double[] invalid = { -1, Double.NaN, Double.POSITIVE_INFINITY, 1e20 };
        for (double amount : invalid)
        {
            expect(IllegalArgumentException.class, () -> session.submitCheckout(amount));
        }
        assertFalse(session.isAwaitingChange());
        assertTrue(session.submitCheckout(total));
        assertTrue(session.isAwaitingChange());

        double change = session.getCashGiven() - total;
        for (double amount : invalid)
        {
            expect(IllegalArgumentException.class, () -> session.submitChange(amount));
        }
        assertTrue(session.isAwaitingChange());
        assertTrue(session.submitChange(change));
        expect(IllegalStateException.class, () -> session.submitChange(change));
        assertEquals(1, session.getPlayer().getRatingCount());
        assertTrue(session.isFinished());
    }

    public void testWrongTotalAndChangeDeductTimeThenAllowCorrection()
    {
        startExpress("cash");
        double total = scanCart();
        double beforeTotal = session.getRemainingSeconds();
        assertFalse(session.submitCheckout(total + 1));
        assertTrue(session.getRemainingSeconds() <= beforeTotal - 5);
        assertTrue(session.submitCheckout(total));

        double change = session.getCashGiven() - total;
        double beforeChange = session.getRemainingSeconds();
        assertFalse(session.submitChange(change + 1));
        assertTrue(session.getRemainingSeconds() <= beforeChange - 5);
        assertTrue(session.isAwaitingChange());
        assertTrue(session.submitChange(change));
    }

    public void testTimerAndCashAccessRequireAppropriateState()
    {
        expect(IllegalStateException.class, session::getRemainingSeconds);
        expect(IllegalStateException.class, session::getCurrentRating);
        expect(IllegalStateException.class, session::getCashGiven);
        assertFalse(session.expireCheckoutIfNeeded());

        Customer customer = startExpress("cash");
        assertTrue(session.getRemainingSeconds() > 0);
        assertEquals(5.0, session.getCurrentRating(), 0.001);
        assertFalse(session.expireCheckoutIfNeeded());
        expect(IllegalStateException.class, session::getCashGiven);
        double total = scanCart();
        assertTrue(session.submitCheckout(total));
        assertTrue(session.getCashGiven() >= total);
        expect(IllegalStateException.class, () -> session.submitCheckout(total));

        customer.setPatience(0);
        assertTrue(session.expireCheckoutIfNeeded());
        assertTrue(session.hasTimedOut());
        assertTrue(session.isFinished());
        assertFalse(session.isAwaitingChange());
        assertFalse(session.expireCheckoutIfNeeded());
        assertEquals(0.0, session.getLastRating(), 0.001);
        assertEquals(1, session.getPlayer().getRatingCount());
    }

    public void testReputationRejectsInvalidRatings()
    {
        Player player = session.getPlayer();
        assertEquals(0, player.getRatingCount());
        assertEquals(0.0, player.getReputation(), 0.001);
        player.updateReputation(3);
        player.updateReputation(5);
        expect(IllegalArgumentException.class, () -> player.updateReputation(-1));
        expect(IllegalArgumentException.class, () -> player.updateReputation(6));
        expect(IllegalArgumentException.class,
            () -> player.updateReputation(Double.NaN));

        assertEquals(2, player.getRatingCount());
        assertEquals(4.0, player.getReputation(), 0.001);
    }

    private Customer startExpress(String payment)
    {
        Customer customer = shopper(1, payment);
        session.addCustomer(customer);
        session.switchActiveLane();
        session.startCheckOut();
        return customer;
    }

    private Customer shopper(int items, String payment)
    {
        Customer customer = new Customer(1, items);
        Queue<GroceryItem> cart = new Queue<>();
        for (int i = 0; i < items; i++)
        {
            cart.enqueue(new GroceryItem(GroceryLib.PRINGLES));
        }
        customer.setGroceryQueue(cart);
        customer.setPayment(payment);
        customer.setPatience(120);
        return customer;
    }

    private double scanCart()
    {
        double total = 0;
        while (session.getCurrentCustomer().getItemCount() > 0)
        {
            total += session.scanNextItem().getPrice();
        }
        return total;
    }

    private void expect(Class<? extends RuntimeException> type, Runnable action)
    {
        try
        {
            action.run();
            fail("Expected " + type.getSimpleName());
        }
        catch (RuntimeException exception)
        {
            assertEquals(type, exception.getClass());
        }
    }
}
