import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Assignment1 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        int numThreads = 8;
        int max = 100000000;

        // Create a boolean array to store whether each number is prime or not
        boolean[] isPrime = new boolean[max + 1];
        Arrays.fill(isPrime, true);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Create a shared counter to keep track of the next number to check
        AtomicInteger next = new AtomicInteger(2);

        // assign task to each of the 8 threads
        for (int i = 0; i < numThreads; i++) {
            PrimeChecker task = new PrimeChecker(next, isPrime, max);
            executor.execute(task);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        int totalPrimes = 0;
        long sumOfPrimes = 0;
        int[] largest10Primes = new int[10];
        int index = 9;

        // loop through final boolean array and find values to output
        for (int i = max; i >= 2; i--) {
            if (isPrime[i]) {
                totalPrimes++;
                sumOfPrimes += i;
                if (index >= 0) {
                    largest10Primes[index] = i;
                    index--;
                }

            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // attempt to write output to new file
        try {
            FileWriter fileWriter = new FileWriter("primes.txt");

            PrintWriter writer;
            try {
                writer = new PrintWriter("primes.txt", "UTF-8");
                writer.println("Total primes found: " + totalPrimes);
                writer.println("Sum of primes: " + sumOfPrimes);
                writer.println("Execution time: " + executionTime + " milliseconds");
                writer.println("Top 10 maximum primes:");
                for (int i = 0; i < 10; i++) {
                    writer.println(largest10Primes[i]);
                }
                writer.close();
            } catch (Exception e) {
                System.out.println("An exception occurred whilst writing ot the file");
                e.printStackTrace();
            }
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("File could not be created");
            e.printStackTrace();
        }
    }
}

class PrimeChecker implements Runnable {
    private AtomicInteger next;
    private boolean[] isPrime;
    private int max;

    public PrimeChecker(AtomicInteger next, boolean[] isPrime, int max) {
        this.next = next;
        this.isPrime = isPrime;
        this.max = max;
    }

    @Override
    public void run() {
        // Sieve of Eratosthenes algorithm
        while (true) {
            int i = next.getAndIncrement();
            if (i > max) {
                break;
            }
            if (isPrime[i]) {
                for (int j = 2 * i; j <= max; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    }
}