# Assignment 2
Both of these programs are written in Java. To compile and run the program, navigate to the folder in which the program is stored and open up a command line terminal. Then write:\
javac Assignment1.java\
java Assignment1

### Important Note!
Please note that problem 2 runs infinitely. I wrote it this way because the question prompt says to allow guests to view the vase as many times as they want. Therefore this program will run until interrupted. It will print which guest is currently viewing the vase. I have included a sample output file for both problems.

## Problem 1
In this program, each guest is a thread, and the main thread represents the Minotaur. The Semaphore class is used to signal each guest thread to enter the labyrinth. The counter variable is a shared counter to keep track of cupcakes eaten.
## Problem 2
### Advantages and Disadvantages of Each Strategy
The first strategy of allowing any guest to check whether the showroom's door is open at any time has the advantage of allowing guests to roam around the castle and enjoy the party while waiting for their turn to see the vase. However, this strategy may lead to large crowds gathering around the door, causing chaos and making it difficult for guests to enter the room. Additionally, there is no guarantee that a guest will be able to see the vase when they check if the door is open.

The second strategy of using a sign on the door indicating when the showroom is available has the advantage of allowing guests to know when it is their turn to see the vase without having to physically go to the door. This reduces crowding and confusion around the door. However, this strategy relies on guests being responsible for setting and resetting the sign, which may not always happen in a timely manner, causing delays and frustration for other guests.

The third strategy of forming a queue allows guests to know exactly when it is their turn to see the vase, reducing confusion and frustration. However, this strategy may lead to guests having to wait in a long queue, which could take a long time depending on the number of guests and the time each guest spends in the showroom.

Overall, the third strategy of forming a queue seems to be the most efficient and fair option, as it ensures that guests know exactly when it is their turn to see the vase and reduces confusion and frustration.
