# Assignment 3

## Report for problem 2
In this implementation, there are two classes: TemperatureSensor and ReportGenerator.
TemperatureSensor is responsible for collecting temperature readings at regular intervals and storing them in shared memory space. It does this by generating a random temperature and storing it in the temperatures array at the current minute index.
ReportGenerator is responsible for compiling a report at the end of every hour. It does this by iterating through each minute in the temperatures array and finding the top 5 highest and lowest temperatures, as well as the largest temperature difference observed over a 10-minute interval. The getTopN function is a helper function used to find the top N values in an array.
To ensure correctness, all accesses to the temperatures array are synchronized to avoid race conditions. The program guarantees progress by using separate threads for each temperature sensor and for the report generator, ensuring that neither process blocks the other. The program is also efficient, as it uses shared memory to avoid the overhead of interprocess communication and only performs a single pass over the temperatures array to compile the report.

## Notes
### Question1
In the file named ConcurrentLinkedList the program runs without printing anything to console except once the program is done. If you would like to see some print statements, uncomment the ones on lines 67, 131 and 139.\
### Question 2
In the file MarsRoverTemperature, an hour is simulated to be 3.6 seconds. This can be changed by changing the value of TIME_COMPRESSION on line 6. Setting this value to 1000 will mean an hour to actually take an hour long. 
