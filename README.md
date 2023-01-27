# Assignment 1
This program is written in Java and uses 8 threads to compute the prime numbers up to 10^8. To compile and run the program, navigate to the folder in which the program is stored and open up a command line terminal. Then write:\
javac Assignment1.java\
java Assignment1\
\
The program will create a file in the same folder called primes.txt and print the output of the program to the file. If an exception occurs during file I/O an error will be printed to the console.\
\
An example output file is also included in this repository

## A brief summary of my approach and an informal statement reasoning about the correctness and efficiency of my design.
This program uses the ExecutorService class from the java.util.concurrent package to create and manage the threads. It creates 8 threads which all create and set a new task - an instance of the PrimeChecker class - for the thread to compute. The PrimeChecker class implements the Runnable interface, which means that it can be executed by a thread. It contains a run() method that performs the Sieve of Eratosthenes algorithm on whichever number is next to evaluate. This number is kept track of using a shared counter which uses the Atomic Integer class in Java. I believe this solution is correct and efficient because it correctly identifies that there are 561455 prime numbers from 2 to 10^8 and does so within 4 seconds. It uses an efficient algorithm to find the prime numbers and does so whilst utilizing dynamic loading.
