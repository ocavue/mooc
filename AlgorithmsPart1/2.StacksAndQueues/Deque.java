import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        public Item item;
        public Node prev;
        public Node next;

        public Node(Item item) {
            this.item = item;
        }
    }

    private int size;
    private Node first;
    private Node last;

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
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size++;
        if (first == null) {
            first = new Node(item);
            last = first;
            return;
        }
        Node newFirst = new Node(item);
        newFirst.next = first;
        first.prev = newFirst;
        first = newFirst;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size++;
        if (first == null) {
            first = new Node(item);
            last = first;
            return;
        }
        Node newLast = new Node(item);
        newLast.prev = last;
        last.next = newLast;
        last = newLast;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        size--;
        Node newFirst = first.next;
        Node oldFirst = first;
        if (newFirst != null)
            newFirst.prev = null;
        first = newFirst;
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        size--;
        Node newLast = last.prev;
        Node oldLast = last;
        if (newLast != null)
            newLast.next = null;
        last = newLast;
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private Node nextNode = first;

        public boolean hasNext() {
            return nextNode != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = nextNode.item;
            nextNode = nextNode.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();

        assert d.isEmpty();
        assert d.size() == 0;

        d.addFirst(3);
        d.addFirst(2);
        d.addFirst(1);
        d.addFirst(0);
        d.addLast(4);
        d.addLast(5);
        d.addLast(6);

        for (int item : d) {
            StdOut.print(item); // Should print 0123456
        }
        assert !d.isEmpty();
        assert d.size() == 7;

        assert d.removeFirst() == 0;
        assert d.removeLast() == 6;
        assert d.removeFirst() == 1;
        assert d.removeLast() == 5;
        assert d.removeFirst() == 2;
        assert d.removeLast() == 4;
        assert d.removeFirst() == 3;

        assert d.isEmpty();
        assert d.size() == 0;
    }

}