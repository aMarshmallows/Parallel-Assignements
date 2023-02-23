class Showroom {
    private boolean isAvailable = true;

    public synchronized boolean isAvailable() {
        return isAvailable;
    }

    public synchronized void setAvailable(boolean available) {
        isAvailable = available;
        notifyAll(); // notify waiting threads that the state has changed
    }
}

class Guest extends Thread {
    private Showroom showroom;
    private int id;

    public Guest(Showroom showroom, int id) {
        this.showroom = showroom;
        this.id = id;
    }

    public void run() {
        while (true) {
            // Wait until the showroom is available
            synchronized (showroom) {
                while (!showroom.isAvailable()) {
                    try {
                        showroom.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                showroom.setAvailable(false);
            }

            // Enter the showroom
            System.out.println("Guest " + id + " is viewing the vase...");
            // try {
            // Thread.sleep(2000); // simulate viewing the vase for 2 seconds
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }

            // Exit the showroom and notify the next guest in the queue
            synchronized (showroom) {
                showroom.setAvailable(true);
                showroom.notifyAll();
            }
            System.out.println("Guest " + id + " has exited the showroom.");
        }
    }
}

public class MinotaurVase {
    public static boolean[] guestsSawVase;

    public static void main(String[] args) {
        int numGuests = 5;
        Showroom showroom = new Showroom();
        for (int i = 1; i <= numGuests; i++) {
            Guest guest = new Guest(showroom, i);
            guest.start();
        }
    }
}
