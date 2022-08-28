### Execution Model
--> Users provide two functions:
	```
	map (k1, v1) -> list(k2,v2)
	reduce (k2, list(v2)) -> list(v2)
	```
--> `k1` is a key from the input data, and `v1` is the corresponding data
--> `k2` is an intermediate key, and `v2` is the corresponding intermediate data
--> intermediate keys and values are drawn from the same domain as the output key and values

--> User program consists of above function definitions + the MapReduce library
--> Upon execution, the library splits the input file into M pieces of 16 to 64 MB
--> Then the program spawns one *Master* and several *Worker* processes on the cluster
--> Each of the M splits of the input becomes a map task
--> Master finds M idle workers on the cluster and assigns them the map-tasks
--> Each worker
	--> reads the respective input split
	--> parses the key-value pairs
	--> passes each pair to the supplied map method
--> The intermediate key-value pairs in workers are buffered in memory
--> Periodically the buffered pairs are written to local disk, partitioned into R regions
--> R is the user-specified partition count
--> User also supplies a partitioning function
--> Intermediate key-value - output of map - are partitioned into R regions using this function.
	--> `(hash(partitionFunction(key)) mod R)`
--> Locations of buffered pairs on local disk are sent back to master
--> Master identifies reduce workers, and passes these locations
--> Reduce worker reads the buffered data from the local disks of Map workers, using remote procedure calls
--> After reading all the data for a partition, a reduce worker
	--> sorts the data by the intedmediate key
	--> Iterates through the data
	--> Puts all the values corresponding to a key into an iterator
	--> Passes each key-iterator pair to the supplied reduce method
--> If the amount of intermediate data is too large to fit into memory, an external sort is used to sort the data
--> Output of reduce function is appended to a final output file for this reduce partition
--> After all map and reduce workers are completed, master process wakes up the user program.
--> The final output is available in the R output files (one for each reduce task)

### Master Data Structures
--> For each map and reduce tasks, master maintains a *state* (idle, in progress, completed)
--> Master also maintains the identity of the worker machine executing each task
--> For each completed map task, master stores the *location*s and *size*s of the R intermediate file regions


### Fault Tolerance
--> Worker Failure : Master pings every worker periodically, and marks the worker as failed after several missed beats
	--> *Completed* *Map* tasks are reset back to initial state : idle
		--> ready for re-execution
		--> this is done because the intermediate output of map tasks are stored on the local disk of the unresponsive machine
	--> *In Progress* *Map* and *Reduce* tasks are also reset back to initial state : idle
		--> ready for re-execution
	--> *Completed* *Reduce* tasks do not need to be re-executed as their output is stored in a global file system
--> Master Failure : periodic checkpoints of the master data structures so that computations can resume when master is available

--> Assuming the map and reduce functions are deterministic, the re-executions can be done and same result can be achieved as if there were no failures.

### Locality
--> Input data is stored on the same cluster on which the tasks are executed
--> GFS divides each file into 64MB blocks and stores 3 copies of each block on different machines
--> Master takes this location information into accout
	--> attempts to schedule a map task on the *same* or *nearby* machine that contains a replica of the corresponding input data

### Task Granularity
--> We want the number of map and reduce tasks to be much larger than the number of workers
--> Each worker doing many tasks improves *dynamic load balancing*
--> Speeds up recovery in case of failure:
	--> The many tasks on the failed worker can be scheduled on many other workers in parallel
--> Limits on the number of tasks are by the Master
	--> master needs to make O(M + R) scheduling decisions
	--> master needs to maintain O(M * R) state in memory for its data structures
--> Practically,
	--> M <== input tasks ~= 16 to 64 MB of input data
	--> R <== small multiple of the total number of worker machines

### Backup Tasks
--> *Straggler* : Machine that takes unusually long time to complete one of the last few map or reduce tasks
	--> machine may have a bad disk, scheduler scheduled other tasks on the machine
--> when a MapReduce is close to completion, master schedules several 'backup' executions of the remaining in-progress tasks
--> takes the result of whichever version of the task finishes first

## Refinements
### Partitioning function
... can be user specified function as described above. If not supplied, default is just take the hash of the key and mod it. (Basically, `partitionFunction(key) = key` from above).

### Ordering Guarantee
.. since the reduce task sorts the partition (than just a *group by*), the output it produces is sorted which can be useful of subsequent operations.

### Combiner Function
User can specify an additional combiner function
	```
	combiner (k2, list(v2)) -> list(v2)
	```
This works the same as the reduce function, but is executed in the map task, on the output of the map function.
This helps in reducing the data to smaller size, before it is written to disk, and subsequently sent out over network.

### Input Output types
several other input output types can be specified, other than the text files.

### Skipping Bad Records
.. master can optionally keey track of records that keep failing tasks repeatedly (on re-executions), and can ask worker to skip them in subsequent retries.

### Local Execution
(for debugging purposes)

### Status Information
(on master which runs a http server)

### Counters
(to count events - another form of output other than the actual output)


## Use Cases
large scale ...
- machine learning problems 
- clustering problems
- extraction of data to produce reports, etc; extraction of properties of web pages
- graph computations

### Indexing rewrite
Google search indexing logic was re-written using MapReduce
- simpler, smaller and easier to understand code
- good performance => keep conceptual parts separate even if it causes multiple passes over data
- easier to operate process as most of the infra problems are automatically handled by MapReduce library



(Paper also references lot of related work - other researches whose results were used as reference/inspiration for design decisions listed above).

#MapReduce

##### References
1. https://www.youtube.com/watch?v=cQP8WApzIQQ&list=PLrw6a1wE39_tb2fErI4-WkMbsvGQk9_UB&index=2
2. http://nil.csail.mit.edu/6.824/2021/papers/mapreduce.pdf