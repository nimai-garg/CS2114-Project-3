
package CheckOutRushGame;

import student.TestCase;

public class GroceryItemTest extends TestCase
{
    private GroceryItem item;

    public void setUp()
    {
        item = new GroceryItem(GroceryLib.PRINGLES);
    }

    public void testConstructor()
    {
        assertNotNull(item);
        assertEquals("Pringles Chips", item.getName());
        assertEquals(2.50, item.getPrice(), 0.001);
    }

    public void testConstructorDifferentItems()
    {
        GroceryItem banana = new GroceryItem(GroceryLib.BANANA);

        assertEquals("Banana", banana.getName());
        assertEquals(0.25, banana.getPrice(), 0.001);

        GroceryItem cola = new GroceryItem(GroceryLib.COCA_COLA);

        assertEquals("Coca-Cola", cola.getName());
        assertEquals(2.00, cola.getPrice(), 0.001);
    }

    public void testConstructorNull()
    {
        try
        {
            new GroceryItem(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testGetName()
    {
        assertEquals("Pringles Chips", item.getName());
    }

    public void testGetPrice()
    {
        assertEquals(2.50, item.getPrice(), 0.001);
    }

    public void testToString()
    {
        assertEquals("Pringles Chips - $2.50", item.toString());
    }

    public void testToStringDifferentPrice()
    {
        GroceryItem banana = new GroceryItem(GroceryLib.BANANA);

        assertEquals("Banana - $0.25", banana.toString());
    }
}
