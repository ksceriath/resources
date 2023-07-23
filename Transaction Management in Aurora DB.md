#DistributedSystems #DataStore #TransactionManagement

[[Amazon Aurora]] builds on the notion *Log is the database*.


Log here refers to a [[Write Ahead Log]].

Since this log records a sequence of changes in order, and these changes are immutable:
	=> This allows to give a unique number to each log record
		=> This is called *Log Sequence Number* (**LSN**)

-> This state of LSN is maintained throughout all the storage nodes, and they [[Gossip Protocol|gossip]] with other nodes in the [[Amazon Aurora#Protection Group|PG]] to fill up the gaps in the logs.

-> As typical:
	=> the database runs several transactions in parallel
	=> transactions may finish in different order than they started
	=> database tracks partially completed transactions, and takes care of undoing them in case of a rollback

-> Despite, the storage layer does its own maintenance and gives the database a coherent picture of the state.
	-> In case of a recovery, the storage layer determines the highest LSN (across all nodes in the PG), upto which all the prior records are available with it: call it VCL (Volume Complete LSN)
		=> And truncates all the records with LSN higher than VCL.

-> Database breaks up transactions into multiple mini-transactions (MTRs)
	-> MTRs are ordered
	-> Each MTR must be performed atomically
	-> MTR is composed of multiple log records
	-> The last log record in an MTR is a consistency point : CPL (Consistency Point LSN).

-> Database tells CPLs to the storage layer
	-> During recovery, the storage layer actually truncates all records with *LSN higher than highest CPL lesser than VCL*.
	-> This CPL is called VDL (Volume Durable LSN)