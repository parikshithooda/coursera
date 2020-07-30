import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;
        Node(Item item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node newFirst = new Node(item);
        newFirst.next = first;
        if (first != null)
            first.prev = newFirst;
        first = newFirst;

        if (first.next == null) last = first;

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node newLast = new Node(item);
        if (size == 0) {
            first = newLast;
        } else {
            last.next = newLast;
            newLast.prev = last;
        }
        last = newLast;

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException();

        Item item = first.item;
        first = first.next;

        if (first == null) last = null;
        else first.prev = null;

        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException();

        Item item = last.item;
        last = last.prev;

        if (last == null) first = null;
        else last.next = null;

        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<Item> {

        Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        Deque<Integer> deque = new Deque<>();
        deque.addLast(4);
        StdOut.println(deque.removeFirst());
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addFirst(5);
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.removeLast());
        for (Integer i: deque) {
            StdOut.println(i);
        }
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
    }
}
