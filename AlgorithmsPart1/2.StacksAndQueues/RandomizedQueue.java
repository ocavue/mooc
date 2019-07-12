import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size = 0;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int len) {
        Item[] newItems = (Item[]) new Object[len];
        for (int i = 0; i < len && i < items.length; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    // add the item
    public void enqueue(Item item) {
        items[size] = item;
        size++;
        if (size >= items.length) {
            resize(2 * items.length);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        size--;

        // swip
        Item last = items[size];
        int randomIndex = StdRandom.uniform(size);
        Item randomItem = items[randomIndex];

        items[randomIndex] = last;

        if (size <= items.length / 4) {
            resize(items.length / 2);
        }
        return randomItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int index = StdRandom.uniform(size);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private int[] indexs;
        private int current = 0;

        public MyIterator() {
            indexs = new int[size];
            for (int i = 0; i < size; i++) {
                indexs[i] = i;
            }
            StdRandom.shuffle(indexs);
        }

        public boolean hasNext() {
            return current < size;
        };

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = items[indexs[current]];
            current++;
            return item;
        };

        public void remove() {
            throw new UnsupportedOperationException();
        };
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

        assert q.size() == 0;
        assert q.isEmpty();

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);

        q.dequeue();
        q.dequeue();

        assert q.size() == 2;
        assert !q.isEmpty();

        for (int i : q) {
            StdOut.println(i);
        }
    }

}