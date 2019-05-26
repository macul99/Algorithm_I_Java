import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int N, head, tail;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        N = 0;
        head = 0;
        tail = 0;
        q = (Item[]) new Object[1];
    }

    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return N == 0;
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return N;
    }

    private void resize(int capacity)                    // resize the array
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = q[(head++) % q.length];
        q = copy;
        head = 0;
        tail = N;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null)
            throw new IllegalArgumentException("Null input is not allowed!!!");

        if (N == q.length) resize(2 * q.length);

        q[(tail++) % q.length] = item;

        N++;
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty!!!");

        Item item = q[head % q.length];
        q[(head++) % q.length] = null;

        N--;

        if (N > 0 && N == q.length / 4) resize(q.length / 2);

        return item;
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty!!!");

        int rdnIdx = StdRandom.uniform(N);
        return q[(head + rdnIdx) % q.length];
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int[] rndOrder = StdRandom.permutation(N);
        private int idx = 0;

        public boolean hasNext() {
            return idx < N;
        }

        public void remove() {
            throw new UnsupportedOperationException("Unspported!!!");
        }

        public Item next() {
            if (isEmpty())
                throw new NoSuchElementException("Queue is empty!!!");

            return q[(head + rndOrder[idx++]) % q.length];
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {
        return;
    }
}
