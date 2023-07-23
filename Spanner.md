

![[Excalidraw/Spanner]]




- Spanner was a breakthrough at the time because it was a practical database which provided transactions across datacenters.
- Spanner is notable for giving distributed transactions which are [[externally consistent transactions|externally consistent]]
- Spanner stores data as a temporal key-value pair
	- `(key, timestamp) → value`
- Time Synchronization - clocks used by the different components need to be in sync.
	- spanner uses a TrueTime api implementation
		- highlight of the api : getTime calls return an interval (earliest, latest) between which the actual time definitely lies; i.e., it includes the uncertainty in the clock in the response itself.
- Transactions : any read done in the transaction does not reflect any writes done in that transaction
	- this is claimed to be okay because Spanner stores key-value mappings along with timestamps.
- Transaction
	- Client starts a transaction
	- Client reads any values by connecting to the leaders of corresponding shards (verify : it can read from replicas instead?)
	- Client sends write requests to corresponding shard leaders.
	- Paxos groups choose among themselves one group to be the coordinator. Leader of that group becomes the Coordinator Leader
	- Upon getting the write requests, leaders make a “prepare commit” message and communicate to their replicas
	- Once they are ready, leaders convey the same to the coordinator leader
	- When all the leaders communicate, coordinator leader sends messages to each leader that its okay to commit, and leaders communicate to their replicas with a paxos commit message.
	- This is the typical [[2 phase commit]] implementation
- Transactions take lot of time if happening across replicas (100s of milliseconds). There are a lot of messages across datacenters.
- R/O transactions
	- Correctness constraints
		- **[[SERIALIAZABLE]]**
		- **[[externally consistent transactions|EXTERNAL CONSISTENCY]]** - even r/o transactions should not see stale data
	- read is done from the local replica if possible - can be done in fraction of milliseconds, compared to dozens of milliseconds in case of cross datacenter reads
		- Reading the latest data always does not work : multiple reads in a transaction may have other write transactions getting committed between them. This would make the read transaction not serializable
	- [[Snapshot Isolation]]
		- spanner stores timestamp with each record, and maintains multiple versions of the data in this manner
		- r/o transactions read not latest but at a particular timestamp - this timestamp can be the latest timestamp available. But all the reads in the transaction will adhere to this timestamp (and not their individual latests), and this ensures that the transaction  as a whole gets a consistent view.
			- Why is it okay for the reads to be getting an older version of data?
				- This solution mitigates the races in concurrent transactions - the read can still be the latest data, barring the writes of the transactions concurrent to it - rules for [[Linearizability]] allow for databases to put concurrent transaction in any serial order they want.
- Write transactions use a timestamp to commit.
	- TrueTime gives an interval `(start, end)` for the current time, when asked
	- Transaction chooses `end` as its timestamp
	- Transaction waits for a current-time interval `(start', end')` where `end < start'`.
	- And commits the transaction then. This is called **commit wait** rule.
- The Synchronized Timestamps along with the Commit Wait Rule give spanner external consistency.