
# Flow Log Processor

The Flow Log Processor is a Java-based application designed to process flow log data and map each entry to a tag based on a lookup table. It processes log data in parallel, aggregates the results, and writes the output to CSV files. The application adheres to the SOLID principles, making it modular, extensible, and maintainable.

# Project Structure

src/main/java/\
├── processor/\
│   ├── FlowLogProcessor.java       // Main entry point of the application\
│   ├── chunk/\
│   │   ├── ChunkProcessor.java     // Handles processing of log chunks\
│   ├── data/\
│   │   ├── PortProtocolKey.java    // Represents the key for lookup table\
│   │   ├── ProcessingResult.java   // Encapsulates thread-safe aggregation logic\
│   ├── io/\
│   │   ├── LookupLoader.java       // Loads the lookup table from a file\
│   │   ├── OutputWriter.java       // Writes aggregated results to output files\
│   ├── util/\
│       ├── ProtocolMapper.java     // Maps protocol numbers to names\
│   ├── resources/\
│       ├── inputFiles\
│   │   │      ├── flow_logs_test.txt\
│   │   │      ├── lookUpTable.txt\
│       ├── outFiles\
│   │   │      ├── port_protocol_counts.csv\
│   │   │      ├── tag_counts.csv\
│   │   ├── properties\
│   │   │      ├── configuration.properties


src/test/java/\
├── processor/\
│   ├── chunk/\
│   │   ├── ChunkProcessorTest.java // Unit tests for ChunkProcessor\
│   ├── data/\
│   │   ├── PortProtocolKeyTest.java // Unit tests for PortProtocolKey\
│   │   ├── ProcessingResultTest.java // Unit tests for ProcessingResult\
│   ├── io/\
│   │   ├── LookupLoaderTest.java   // Unit tests for LookupLoader\
│   │   ├── OutputWriterTest.java   // Unit tests for OutputWriter\
│   ├── util/


# Why These Design Choices?

## 1. Why ConcurrentHashMap?

### 1. Thread Safety:

ConcurrentHashMap provides thread-safe operations, enabling multiple threads to update the map without requiring explicit synchronization. This is critical for concurrent tasks such as aggregating results from multiple chunks of logs.

### 2. High Performance:

Operations like put, get, and merge are implemented with minimal contention using bucket-level locks and CAS (Compare-And-Swap), ensuring high throughput even under heavy load.

### 3. Scalability:

ConcurrentHashMap supports concurrent reads and writes, making it ideal for highly parallel tasks such as log aggregation. Different threads can operate on different buckets simultaneously without blocking each other.

### 4. Time Complexity:

In the average case, the time complexity of operations like get, put, and remove is O(1) because the keys are distributed across buckets uniformly.
In the worst case, when many keys are hashed to the same bucket (high collision rate), the bucket’s structure changes from a linked list to a red-black tree. This reduces the worst-case time complexity for bucket operations (like get or put) from O(n) to O(log n), where n is the number of entries in that bucket.

## 2. Why CompletableFuture?

### 1. Asynchronous Processing:

CompletableFuture allows us to process log chunks asynchronously and efficiently manage parallelism. Each chunk of logs is processed independently in a non-blocking manner.

### 2. Simplified Concurrency:

Provides a clean and declarative way to execute tasks and wait for their completion using methods like allOf().

### 3. Error Handling:

Built-in methods for exception handling make it robust for handling errors in asynchronous tasks.

## 3. Why ExecutorService?

### 1. Thread Pool Management:

Using ExecutorService, we create a fixed-size thread pool (THREAD_COUNT) to control the number of threads and prevent resource overloading.

### 2. Reusability:

Threads in the pool are reused across tasks, reducing the overhead of creating and destroying threads.

### 3. Tunable Performance:

The thread pool size is dynamically configurable to optimize resource utilization based on the system's capabilities.

# Classes and Their Responsibilities

## 1. FlowLogProcessor

### Role:
#### 1. The main entry point of the application.

### Responsibilities: 
1. Loads the lookup table from the file.
2. Splits the flow logs into manageable chunks.
 3. Orchestrates parallel processing of chunks using CompletableFuture and ExecutorService.
 4. Waits for all tasks to complete and writes the aggregated results to output files.


## 2. LookupLoader
### Role:
 1. Loads the lookup table data from a CSV file.

### Responsibilities:
 1. Reads the destination port, protocol, and tag mappings.
 2. Populates a HashMap for efficient lookups during log processing.

## 3. ChunkProcessor
### Role:
 1. Processes each chunk of flow log data.
### Responsibilities:
 1. Maps protocol numbers to protocol names.
 2. Aggregates tag counts and port-protocol combination counts in a thread-safe manner.
 3. Handles errors gracefully for malformed log lines.

## 4. ProtocolMapper
### Role:
 1. Maps protocol numbers to their respective protocol names.

### Responsibilities:
 1. Supports predefined mappings (e.g., "6" to "TCP", "17" to "UDP").
 2. Extensible for additional protocol mappings as needed.

## 5. ProcessingResult
### Role:
 1. Encapsulates thread-safe aggregation logic.

### Responsibilities:
 1. Maintains a ConcurrentHashMap for tag counts and port-protocol counts.
 2. Provides methods for safely incrementing counts and merging results from multiple threads.

## 6. OutputWriter
### Role:
 1. Writes aggregated results to output files.

### Responsibilities:
 1. Writes tag counts to tag_counts.csv.
 2. Writes port-protocol combination counts to port_protocol_counts.csv.

# How It Works

## Input Files

### 1. Flow logs

#### A text file containing flow log data with fields like destination port, protocol number, etc.
 Example: 2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK

### Lookup Table

#### A CSV file mapping destination ports and protocols to tags.
 Example:
 dstport,protocol,tag \
 443,tcp,web \
 80,tcp,web \
 22,tcp,ssh \

## Output Files

### Tag Counts (tag_counts.csv)
1. Aggregates the count of logs for each tag.
2. Example:
   Tag,Count\
   web,10\
   ssh,5\

### Port-Protocol Counts (port_protocol_counts.csv)
1. Aggregates the count of logs for each port-protocol combination.
2. Port,Protocol,Count
   443,tcp,6
   80,tcp,4

# How to Run

## Prerequisites
1. Java Version : Java 8 or higher
2. Build Tool : Maven

## Build the Project
mvn clean install

## Run the Application

 mvn exec:java

## Configure the Input and Output Files
Open the config.properties file located in src/main/resources/properties/config.properties and modify the following properties:

Path to the flow log file flowLogFile=src/main/resources/inputFiles/flow_logs_test.txt

Path to the lookup table file lookupFile=src/main/resources/inputFiles/lookUpTable.txt

Path to the output directory outputDir=src/main/resources/outputFiles

Maximum number of threads to use maxThreads=10

Average line size in bytes (used for dynamic chunk size calculation) averageLineSize=200

# Key Design Principles

### Single Responsibility Principle (SRP):

Each class has a clearly defined single responsibility (e.g., LookupLoader handles lookup loading, OutputWriter handles writing results).

### Open/Closed Principle (OCP):

Classes like ProtocolMapper are open for extension (e.g., adding more protocol mappings) but closed for modification.

### Liskov Substitution Principle (LSP):

Components can be replaced with new implementations without affecting the system's behavior.
### Interface Segregation Principle (ISP):

Classes expose only the methods required for their specific use cases (e.g., ProcessingResult provides only aggregation-related methods).
### Dependency Inversion Principle (DIP):

High-level modules (e.g., FlowLogProcessor) depend on abstractions like ProcessingResult, not on concrete implementations.
