
package CheckOutRushGame;


public class QueueTest extends student.TestCase
{
    private Queue<String> queue;

    public void setUp()
    {
        queue = new Queue<>();
    }

    public void testNewQueue()
    {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    public void testEnqueue()
    {
        queue.enqueue("Apple");

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals("Apple", queue.peek());

        queue.enqueue("Banana");

        assertEquals(2, queue.size());
        assertEquals("Apple", queue.peek());
    }

    public void testEnqueueNull()
    {
        try
        {
            queue.enqueue(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }
    }

    public void testDequeue()
    {
        queue.enqueue("Apple");
        queue.enqueue("Banana");

        assertEquals("Apple", queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals("Banana", queue.peek());

        assertEquals("Banana", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    public void testDequeueEmpty()
    {
        try
        {
            queue.dequeue();
            fail("Expected EmptyQueueException");
        }
        catch (EmptyQueueException e)
        {
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }
    }

    public void testPeek()
    {
        queue.enqueue("Apple");

        assertEquals("Apple", queue.peek());
        assertEquals(1, queue.size());
    }

    public void testPeekEmpty()
    {
        try
        {
            queue.peek();
            fail("Expected EmptyQueueException");
        }
        catch (EmptyQueueException e)
        {
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }
    }

    public void testIsEmpty()
    {
        assertTrue(queue.isEmpty());

        queue.enqueue("Apple");
        assertFalse(queue.isEmpty());

        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    public void testSize()
    {
        assertEquals(0, queue.size());

        queue.enqueue("Apple");
        assertEquals(1, queue.size());

        queue.enqueue("Banana");
        assertEquals(2, queue.size());

        queue.dequeue();
        assertEquals(1, queue.size());
    }

    public void testEnqueueAfterEmpty()
    {
        queue.enqueue("Apple");
        queue.dequeue();

        queue.enqueue("Banana");

        assertEquals(1, queue.size());
        assertEquals("Banana", queue.peek());
    }
}
