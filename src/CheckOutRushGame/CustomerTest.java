
package CheckOutRushGame;

public class CustomerTest
    extends student.TestCase
{
    private Customer customer;

    public void setUp()
    {
        customer = new Customer(2);
    }


    public void testConstructor()
    {
        assertNotNull(customer.getIdentity());
        assertNotNull(customer.getPayment());
        assertNotNull(customer.getGroceryQueue());

        assertTrue(customer.getGroceryQueue().size() > 0);
        assertTrue(customer.getPatience() > 0);
    }


    public void testInvalidConstructor()
    {
        try
        {
            new Customer(-1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        try
        {
            new Customer(8);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }


    public void testSetPayment()
    {
        customer.setPayment("credit");
        assertEquals("credit", customer.getPayment());

        customer.setPayment("cash");
        assertEquals("cash", customer.getPayment());
    }


    public void testSetPaymentInvalid()
    {
        String original = customer.getPayment();

        try
        {
            customer.setPayment(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(original, customer.getPayment());
        }

        try
        {
            customer.setPayment("Bitcoin");
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(original, customer.getPayment());
        }
    }


    public void testSetGroceryQueue()
    {
        Queue<GroceryItem> queue = new Queue<>();
        GroceryItem item = new GroceryItem(GroceryLib.BANANA);

        queue.enqueue(item);
        customer.setGroceryQueue(queue);

        assertSame(queue, customer.getGroceryQueue());
        assertEquals(item, customer.getGroceryQueue().peek());
    }


    public void testSetGroceryQueueInvalid()
    {
        Queue<GroceryItem> original = customer.getGroceryQueue();

        try
        {
            customer.setGroceryQueue(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertSame(original, customer.getGroceryQueue());
        }

        try
        {
            customer.setGroceryQueue(new Queue<GroceryItem>());
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertSame(original, customer.getGroceryQueue());
        }
    }


    public void testSetIdentity()
    {
        customer.setIdentity("Student");
        assertEquals("Student", customer.getIdentity());

        customer.setIdentity("Worker");
        assertEquals("Worker", customer.getIdentity());

        customer.setIdentity("Housewife");
        assertEquals("Housewife", customer.getIdentity());
    }


    public void testSetIdentityInvalid()
    {
        String original = customer.getIdentity();

        try
        {
            customer.setIdentity(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(original, customer.getIdentity());
        }

        try
        {
            customer.setIdentity("Invalid");
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(original, customer.getIdentity());
        }
    }


    public void testSetPatience()
    {
        customer.setPatience(60);
        assertEquals(60, customer.getPatience());

        customer.setPatience(30);
        assertEquals(30, customer.getPatience());

        customer.setPatience(0);
        assertEquals(0, customer.getPatience());
    }


    public void testSetPatienceInvalid()
    {
        int original = customer.getPatience();

        try
        {
            customer.setPatience(-1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(original, customer.getPatience());
        }
    }


    public void testGetItemCount()
    {
        Customer shopper = new Customer(1, 3);

        assertEquals(3, shopper.getItemCount());
        shopper.getGroceryQueue().dequeue();
        assertEquals(2, shopper.getItemCount());
        shopper.getGroceryQueue().dequeue();
        shopper.getGroceryQueue().dequeue();
        assertEquals(0, shopper.getItemCount());
    }

    public void testRejectEmptyGeneratedCart()
    {
        try
        {
            new Customer(1, 0);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception)
        {
            assertEquals("A cart must contain at least one item.",
                exception.getMessage());
        }
    }


    public void testToString()
    {
        customer.setIdentity("Student");
        customer.setPayment("cash");
        customer.setPatience(45);

        String result = customer.toString();

        assertTrue(result.contains("Student"));
        assertTrue(result.contains("cash"));
        assertTrue(result.contains("45"));
    }
}
