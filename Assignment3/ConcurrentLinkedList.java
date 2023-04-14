import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedList {
    private Node head;
    private final ReentrantLock headLock;

    public ConcurrentLinkedList() {
        head = null;
        headLock = new ReentrantLock();
    }

    // add present to ordered list
    public void addPresent(Present present) {
        Node newNode = new Node(present);
        Node pred = null, curr = null;

        headLock.lock();
        try {
            pred = null;
            curr = head;

            // find correct location for current present
            while (curr != null && curr.present.getTag() < present.getTag()) {
                pred = curr;
                curr = curr.next;
            }

            // if this present is meant to be the first in line, attach rest of list to
            // .next of current present
            // set head to be current present
            if (pred == null) {
                newNode.next = head;
                head = newNode;
            } else {
                pred.next = newNode;
                newNode.next = curr;
            }
        } finally {
            headLock.unlock();
        }

        // if we just added a present to end of list
        if (curr != null) {
            curr.previous = newNode;
            newNode.next = curr;
        }
        if (pred != null) {
            pred.next = newNode;
            newNode.previous = pred;
        }
    }

    // write thank you note and remove present from head of list
    public void removePresent() {
        headLock.lock();
        try {
            if (head != null) {
                head = head.next;
                if (head != null) {
                    head.previous = null;
                }
                // System.out.println("Removed present " + head.present.getTag());
            }
        } finally {
            headLock.unlock();
        }
    }

    // searches for the present with given tag
    public boolean containsPresent(int tag) {
        headLock.lock();
        try {
            Node curr = head;
            while (curr != null && curr.present.getTag() < tag) {
                curr = curr.next;
            }
            return curr != null && curr.present.getTag() == tag;
        } finally {
            headLock.unlock();
        }
    }

    private class Node {
        Present present;
        Node next;
        Node previous;

        Node(Present present) {
            this.present = present;
            this.next = null;
            this.previous = null;
        }
    }

    private static final int NUM_PRESENTS = 500;
    private static final int NUM_THREADS = 4;
    private static final int WORK_CHUNK_SIZE = 100; // number of operations per loop

    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedList linkedList = new ConcurrentLinkedList();
        ConcurrentLinkedQueue<Integer> workQueue = new ConcurrentLinkedQueue<>();
        long startTime = System.currentTimeMillis();
        // add all presents to queue
        for (int i = 0; i < NUM_PRESENTS; i++) {
            workQueue.add(i);
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // while workQueue is not empty, keep assigning chunks of work to threads
        do {
            for (int i = 0; i < NUM_THREADS; i++) {
                executor.submit(() -> {
                    while (!workQueue.isEmpty()) {
                        for (int j = 0; j < WORK_CHUNK_SIZE && !workQueue.isEmpty(); j++) {

                            // create random choice of either adding, removing or searching
                            Random random = new Random();
                            int op = random.nextInt(3) + 1;
                            int presentNum = workQueue.poll();
                            Present present = new Present(presentNum);

                            switch (op) {
                                case 1:
                                    linkedList.addPresent(present);
                                    // System.out.println("Added present " + presentNum);
                                    break;
                                case 2:
                                    linkedList.removePresent();

                                    break;
                                case 3:
                                    linkedList.containsPresent(present.getTag());
                                    // System.out.println("Searched for present " + present.getTag());
                                    break;
                            }
                        }
                    }
                });
            }

        } while (!workQueue.isEmpty());

        System.out.println("Finished writing all thank you notes!");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time took " + executionTime + "milliseconds");
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}

class Present {
    public int tag;

    public Present(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
