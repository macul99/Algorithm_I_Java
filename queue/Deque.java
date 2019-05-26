import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int queSize;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque()                           // construct an empty deque
    {
        first = null;
        last = null;
        queSize = 0;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return queSize == 0;
    }

    public int size()                        // return the number of items on the deque
    {
        return queSize;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new IllegalArgumentException("Null input is not allowed!!!");

        if (isEmpty()) {
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            first.prev = null;
            oldFirst.prev = first;
        }

        queSize++;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new IllegalArgumentException("Null input is not allowed!!!");

        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.prev = oldLast;
            last.next = null;
            oldLast.next = last;
        }

        queSize++;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty!!!");

        Item item = first.item;

        if (size() == 1) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }

        queSize--;

        return item;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty!!!");

        Item item = last.item;

        if (size() == 1) {
            first = null;
            last = null;
        }
        else {
            last = last.prev;
            last.next = null;
        }

        queSize--;

        return item;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Unspported!!!");
        }

        public Item next() {
            if (isEmpty())
                throw new NoSuchElementException("Deque is empty!!!");

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {
        return;
    }
}
