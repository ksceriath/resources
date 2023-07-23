![[Network - data transfer|Traditional data transfer over network]]

![[FaRM - Network - data transfer|Kernel Bypass in FaRM]]


- Performance bottlenecks generally come during:
	- write data to disk
	- cpu cycles
	- network
- stores data in [[NVRAM]] which makes it fast.
	- any failures other than power failures will not get handled by an NVRAM solution - thus replicas etc are required.
- makes use of 
	- RDMA (Remote Direct Memory Access) : access the memory locations of remote machines directly from the [[NIC]] of that machine, without involving its CPU
	- also Kernel Bypass : application code directly talks to the NIC without getting the kernel involved
- assumes all replicas are in the same datacenter
	- is not trying to solve the problem as [[Spanner]] - what if an entire datacenter goes down?
- RDMA by itself is not enough to give transaction support along with shards/replica.
- RDMA restricts the design severely - forces to use [[Optimistic Concurrency Control]]
- FaRM’s performance on the other hand is extremely impressive
- [[Spanner]] tries to solve performance problems like speed of light and network delays, etc
- FaRM tries to solve performance problems of CPU time on the servers
	- Glosses over the performance problems of network delays by talking about only a single datacenter
- Data is shard-ed by a key into Primary-Backup pairs
- Always read from the primary.
- Always update all the replicas. Paxos is not used.
- Fault tolerance - if at least one replica of a shard is available, it works; doesn’t require a majority of replicas to be available. For tolerating up to `f` failures, `f+1` replicas will be required
- Each server contains message queues for each of the other servers - these are non volatile logs, to which the other servers do RDMA-writes to.
- Each server contains a separate set of volatile message queues (for each of the other servers, written to using the RDMA-writes) which store the RPC-like messages.