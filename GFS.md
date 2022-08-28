Google file system

... is a 'scalable' 'distributed' file system
... for large 'distributed' 'data-intensive' applications... provides fault-tolerance, running on inexpensive commodity hardware.


#### Goals
- performance
- scalability
- reliability
- availability


#### Problem/Assumptions
- component failures are the norm; system is built from inexpensive commodity components
	- component = machines, disks, etc
- files are huge; support for millions of multi-GB files
- **Most files are mutated by appending new data, rather than overwriting existing data
- Reads are predominantly of two kinds
	- large streaming reads
	- small random reads
- Multiple clients concurrently append to same file
- Data is processed in bulk
	-> high sustained bandwidth is more important than low latency

#### Interface
... not [[POSIX]] compliant.
Supports:
- create
- delete
- open
- close
- read
- write
- snapshot
- record-append

#### Architecture
Following are the main components:
- a single *Master*
- multiple *Chunkservers*
- multiple *Clients*

--> Files are divided in fixed sized chunks
	--> each chunk has an globally unique 64 bit identifier : *chunk handle*
--> chunks are replicated
--> and distributed among chunk servers
--> One of the replica is given a *lease* by the master
	--> This one is called a primary

##### READ
Client wants to read at some offset in a file
--> Client sends filename, offset to the master
--> Master replies the chunk handle, and the list of chunk servers
--> Client caches this information
--> Client sends the chunk handle to the chunk server
--> Chunk server sends the chunk data to the client

##### WRITE
Client wants to write data to *some* chunk.
1. Client asks master for chunkservers for this chunk
2. If none of the chunk-replicas have a lease currently, master chooses a replica and grants it the lease
3. Master replies to client with the locations of the replicas, and the identity of the primary
4. Client caches this information - no need to talk to master as long the primary holds the lease
5. Client then pushes the data to all the replica chunkservers.
6. Chunkservers store this data in an internal LRU buffer cache
7. Chunkservers acknowledge the receiving of the data to the client
8. Once acknowledged, client issues a write request to the primary
9. Primary issues a consecutive serial number to the request
	1. it does this for all the other requests it gets from other clients
10. Primary applies the mutation(s) in this serial order
	1. if this fails, nothing happens, and client can retry
	2. if the write crosses the chunk-boundary
		1. client can break it down into multiple writes
		2. these writes may be interleaved with writes from other clients
			1. the shared file region can be in *undefined state*
			2. but all replicas will have the same data, so its *consistent state*
11. Primary forwards the write request(s) to all the secondaries
12. Secondaries apply the mutation in the order specified by the serial number
	1. if this fails, request is deemed failed and modified region is left in *inconsisted* state
	2. client can try again
13. Secondaries acknowledge to Primary once the operation is completed
14. Primary replies to client


##### RECORD APPEND
client only specifies the data to be written (and not the chunk)
- compared to write above, this guarantees atomic write
	- the data will be written as one record, and not interleaved with other clients' writes
	- the data however may be written multiple times
	- the data may not be consistent across all the replicas
		- the same file region in the replicas may conta different data
		- which can be duplicates of the same records wholly or in part
	- so all replicas are not bytewise identical
	- but the final successful write is written at exactly the same offset on all replicas of some chunk
	- the regions with successful appends are in consistent state
	- the interleaving regions are inconsistent


##### SNAPSHOT
- master revokes any outstanding leases on the relevant chunks
- after the lease has been revoked or expired, master logs the operation to the disk
- it then duplicates the metadata for the source file/directory tree - which is to be snapshotted
- the newly created/duplicated files in the metadata still point to the same chunks as the source file
- the Snapshotting request is basically done at this point
- Next time a client wants to write to any of the above chunks
	- master sees the reference count on this chunk is greater than 1
	- it creates a new chunk handle
	- tells each chunkserver to create a new chunk for this handle
	- chunk servers copy data locally from old chunk to new
	- master then replies to the client with the newly created chunk handle



Master handles several other operations in the backgroud - including chunk allocation, garbage collection.
It mainly talks to chunkservers during periodic Heartbeats and during chunkserver startup,
where chunkservers tell it what chunks they contain, and it tells chunkservers about orphaned chunks, etc.

Master maintains several in-memory datastructures to store the metadata to do all that (these are file and chunk namespaces, and file-to-chunk mapping).
It also keeps a persistent log of all the operation requests it receives (minus the data - master never receives the data, only control operations) <-- this is called operation log. This is also replicated on remote machines.
Master maintains the chunk locations only in memory. In case of a crash, each chunkserver tells new master the chunks it holds, so this data is created at startup, on the fly.


##### Master Data
Map:
		filename   -->   array of chunk handles (non volatile)
Map:
		chunk handle   -->   list of chunk servers (volatile)
										version number (non volatile)
										primary chunk (volatile)
										lease expiration (volatile)
Log (non volatile)
Checkpoint (non volatile)

->  (volatile = in memory, non volatile = on disk)

##### References
1. GFS paper