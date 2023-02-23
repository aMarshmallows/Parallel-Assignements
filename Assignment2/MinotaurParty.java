import java.util.concurrent.Semaphore;

public class MinotaurParty {
    private final int numGuests;
    private final Semaphore[] semaphores;
    private int counter;

    public MinotaurParty(int numGuests) {
        this.numGuests = numGuests;
        this.semaphores = new Semaphore[numGuests];
        for (int i = 0; i < numGuests; i++) {
            semaphores[i] = new Semaphore(0);
        }
        this.counter = 0;
    }

    public void startParty() {
        for (int i = 0; i < numGuests; i++) {
            int guestID = i;
            new Thread(() -> guestTask(guestID)).start();
        }
    }

    private void guestTask(int guestID) {
        boolean hasEntered = false;
        while (true) {
            try {
                // Wait for signal from the Minotaur to enter the labyrinth
                semaphores[guestID].acquire();

                // Enter the labyrinth and check if the cupcake is still there
                if (!hasEntered) {
                    System.out.println("Guest " + guestID + " enters the labyrinth.");
                    hasEntered = true;
                }
                if (Math.random() < 0.5) {
                    System.out.println("Guest " + guestID + " finds a cupcake and eats it.");
                    synchronized (this) {
                        counter++;
                    }
                } else {
                    System.out.println("Guest " + guestID + " finds an empty plate.");
                }

                // If all guests have entered the labyrinth at least once, inform the main
                // thread
                if (counter == numGuests) {
                    synchronized (this) {
                        notify();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void waitUntilAllGuestsHaveEntered() {
        while (counter < numGuests) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All guests have entered the labyrinth at least once. Party success!");
    }

    public void inviteGuest(int guestID) {
        semaphores[guestID].release();
    }

    public static void main(String[] args) {
        int numGuests = 5;
        MinotaurParty party = new MinotaurParty(numGuests);
        party.startParty();
        for (int i = 0; i < numGuests; i++) {
            party.inviteGuest(i);
        }
        party.waitUntilAllGuestsHaveEntered();
    }
}