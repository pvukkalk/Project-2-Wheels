import java.util.function.Predicate;

/**
 * Generic, unordered chain of linked nodes that allows duplicates.
 * New entries go to the back, so the chain keeps insertion order
 * (the oldest entry is always at index 0).
 *
 * @param <T> the type of entry stored in the chain
 */
public class LinkedChain<T> {
    private Node<T> head;  // reference to the first node
    private int numOfEntries;

    private static class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public LinkedChain() {
        this.numOfEntries = 0;
        this.head = null;
    }

    /** Adds an entry to the back of the chain. O(n) */
    public boolean add(T toAdd) {
        if (toAdd == null) {
            return false;
        }
        Node<T> newNode = new Node<>(toAdd);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> currentNode = head;
            while (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
            }
            currentNode.setNext(newNode);
        }
        numOfEntries++;
        return true;
    }

    /** Removes and returns the first entry equal to toRemove, or null. O(n) */
    public T remove(T toRemove) {
        if (head == null || toRemove == null) {
            return null;
        }
        if (head.getData().equals(toRemove)) {
            T data = head.getData();
            head = head.getNext();
            numOfEntries--;
            return data;
        }
        Node<T> currentNode = head;
        while (currentNode.getNext() != null) {
            if (currentNode.getNext().getData().equals(toRemove)) {
                T data = currentNode.getNext().getData();
                currentNode.setNext(currentNode.getNext().getNext());
                numOfEntries--;
                return data;
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    /** Removes all entries. O(1) */
    public void clear() {
        head = null;
        numOfEntries = 0;
    }

    /** Returns the entry at index without removing it, or null if out of bounds. O(n) */
    public T getIndex(int index) {
        if (index < 0 || index >= numOfEntries) {
            return null;
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }
        return currentNode.getData();
    }

    /** O(n) */
    public boolean contains(T anEntry) {
        if (anEntry == null) {
            return false;
        }
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData().equals(anEntry)) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    /** O(1) */
    public int getNumOfEntries() {
        return numOfEntries;
    }

    /** O(1) */
    public boolean isEmpty() {
        return numOfEntries == 0;
    }

    /** Returns how many entries are equal to anEntry. O(n) */
    public int getInstancesOf(T anEntry) {
        if (anEntry == null) {
            return 0;
        }
        int count = 0;
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData().equals(anEntry)) {
                count++;
            }
            currentNode = currentNode.getNext();
        }
        return count;
    }

    /**
     * Returns a new chain holding, in the same order, every entry that
     * passes the test. This chain is not changed. A single pass over the
     * nodes builds the result, so it is O(n) instead of the O(n^2) cost of
     * calling getIndex in a loop.
     */
    public LinkedChain<T> filter(Predicate<? super T> test) {
        LinkedChain<T> result = new LinkedChain<>();
        if (test == null) {
            return result;
        }
        Node<T> resultTail = null;
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (test.test(currentNode.getData())) {
                Node<T> copy = new Node<>(currentNode.getData());
                if (resultTail == null) {
                    result.head = copy;
                } else {
                    resultTail.setNext(copy);
                }
                resultTail = copy;
                result.numOfEntries++;
            }
            currentNode = currentNode.getNext();
        }
        return result;
    }
}
