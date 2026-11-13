package CheckOutRushGame;

public class Queue<T>
{
    private Node<T> firstNode;
    private Node<T> lastNode;
    private int size;

    public void enqueue(T item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Queue items cannot be null.");
        }

        Node<T> newNode = new Node<>(item);
        if (isEmpty())
        {
            firstNode = newNode;
        }
        else
        {
            lastNode.next = newNode;
        }
        lastNode = newNode;
        size++;
    }

    public T dequeue()
    {
        T item = peek();
        firstNode = firstNode.next;
        size--;
        if (isEmpty())
        {
            lastNode = null;
        }
        return item;
    }

    public T peek()
    {
        if (isEmpty())
        {
            throw new EmptyQueueException("The queue is empty.");
        }
        return firstNode.item;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }

    private static class Node<T>
    {
        private final T item;
        private Node<T> next;

        private Node(T item)
        {
            this.item = item;
        }
    }
}
