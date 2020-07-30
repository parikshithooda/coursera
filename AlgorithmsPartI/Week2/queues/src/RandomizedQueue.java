import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int first;
    private int last;
    private int size;
    private int capacity = 10;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[capacity];
        first = -1;
        last = -1;
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (isEmpty()) {
            first = 0;
            last = 0;
            items[last] = item;

        } else if (size() == capacity) {
            capacity = capacity * 2;
            Item[] newItems = (Item[]) new Object[capacity];
            for (int i = 0; i < size(); i++) {
                newItems[i] = items[i];
            }
            first = 0;
            last = size() - 1;
            items = newItems;
            items[++last] = item;

        } else if (last == capacity - 1) {
            for (int i = 0; i < size(); i++) {
                items[i] = items[i + first];
                items[i + first] = null;
            }
            first = 0;
            last = size() - 1;
            items[++last] = item;
        } else {
            items[++last] = item;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int selected = StdRandom.uniform(first, last + 1);

        Item item = items[selected];
        items[selected] = items[first];
        items[first] = null;

        first++;
        size--;


        if (size() <= capacity / 4) {
            capacity = size() > 10 ? size() : 10;
            Item[] newItems = (Item[]) new Object[capacity];
            for (int i = 0; i < size(); i++) {
                newItems[i] = items[first + i];
            }
            first = 0;
            last = size() - 1;
            items = newItems;
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return items[StdRandom.uniform(first, last + 1)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ItemItertor();
    }

    private class ItemItertor implements Iterator<Item> {
        int current = 0;
        int size = size();
        Item[] iItems;

        ItemItertor() {
            if (!hasNext()) return;

            iItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                iItems[i] = items[first + i];
            }
        }

        @Override
        public boolean hasNext() {
            return current != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            int selected = StdRandom.uniform(current, size);
            Item tempItem = iItems[selected];
            iItems[selected] = iItems[current];
            iItems[current] = tempItem;

            Item item = iItems[current];
            iItems[current] = null;
            current++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(5);
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(4);
        StdOut.println(randomizedQueue.dequeue());
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(4);
        StdOut.println(randomizedQueue.size());
        StdOut.println(randomizedQueue.isEmpty());
        StdOut.println(randomizedQueue.dequeue());
        StdOut.println(randomizedQueue.sample());
        for (Integer i: randomizedQueue) {
            StdOut.println(i);
        }
    }
}