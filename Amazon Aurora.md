#DistributedSystems #DataStore 


![[Excalidraw/Amazon Aurora]]


------------------------
Aurora is basically built on top of MySQL, with some variations in its InnoDB engine.


-> Aurora attempts to improve performance by decoupling database engine from the storage.
-> writes come to the database compute nodes
-> and these writes are persisted (over the network) to a replicated storage
-> Further the engine only sends the [[Write Ahead Log|WAL]] over the network - and not the actual data; this considerably reduces the network IOPS
	=> Log contains the change to be applied to data
	=> (initial data) ----> log1 ----> log2 ----> (final data)
-> Storage is then not a general purpose data storage, but an aurora-specialized storage
-> Storage may just store the logs as is, and only compute the 'final data' when it is asked to be read. In doing so it will save that data, an equivalent of checkpointing.
	-> Storage may choose to checkpoint only those pages which have a long chain of modifications.
-> Further, reads can seperately be served from separate ReadOnly database instances, which talk directly to the storage layer.

====================

Paper argues that typically used 2/3 quorums do not work in case of data center failures.
--> 2/3 quorum means read quorum (n=3, w=3, r=2)
----> You have a total of 3 nodes, you write to all 3, you read from any 2
=> If one data center goes down, then any random failures in other data centers can cost availability
=> Paper argues that the random failures are always present as a constant noise

==> Aurora chooses 4/6 write quorum and 3/6 read quorum. 6 nodes are spread uniformly across 3 availability zones (data centers).
==> they refer to this as *AZ+1* durability

This replication is done via [[Chain Replication]].

###### Protection Group
The above makes it a replication of 6.
-> Then data is further partitioned into multiple 10GB blocks.
-> Per above scheme, each this block is replicated 6-ways
	-> 2 blocks per AZ
		-> These live on separate nodes in an AZ
	-> across 3 AZs
-> These copies for each block, across 6 nodes, in 3 DCs is called as a *Protection Group*



Next thing Aurora targets is reducing network IOPS by just persisting the LOG. This is called redo-log in the paper, and also known as WriteAheadLog.


The paper also delves deeper into how the db handles transactions: [[Transaction Management in Aurora DB]]

