.. is a [[Replicated State Machine]].

#DistributedSystems


Zookeeper uses [[ZAB]] protocol as its [[consensus algorithm]].

(Zookeeper paper mainly discusses the api interface Zookeeper exposes in its role as a *coordination service*.)


=> Zookeeper achieves read performance by having reads served from replica locally, sacrificing [[linearizability]].
- only writes are linearizable
- reads are not linearizable


- ZAB is responsible for Leader Election and [[Atomic Broadcast]] in the zookeeper cluster
- Zookeeper keeps data in a tree-like hierarchical namespace - like a filesystem
	- with only full data reads and writes - effectively a key-value store
	- each node in the tree is called a *znode*
- This data lives entirely in the memory
- A [[write ahead log]] along with checkpoints are persisted to disk
- Each server in the zookeeper cluster keeps a copy of the entire data : [[replicated state machine | replicated database]]
- **Watches** : zookeeper allows clients to watch for updates to any file/znode. This feature is useful for implementing a variety of *primitives*.

#### Working
- A client connects to a zookeeper server, and gets a session created
- Any read/write request is received by a **request processor**
	- Any read request is served from the *local replicated database*
		- This can be out of date from the latest data
	- Write request : the server will forward the request to the current leader
		- Leader executes the write on its local **replicated database** and,
		- broadcasts the change to others
			- using **Atomic Broadcast** of *ZAB*
		- Others apply the changes in the order directed by the leader

=> Reads are fast because of scalability, but not consistent
=> Writes require a majority quorum, so they are not so fast
=> Zookeeper is optimized for *read heavy* workloads


-> ZAB has atleast once semantics in case of failures
-> For this purpose, Zookeeper internally converts each write request into an idempotent transaction
	-> Due to this, same transaction can be executed multiple times in case of faults, without an issue

=> This also allows zookeeper to have [[fuzzy snapshot]] for failure recovery
	=> This is doable since any duplicated writes do not cause an issue


#### Typical Uses
- Test-and-set service like the one required by vmware-ft
- dynamic configuration information
- master election in a cluster
- a master can store its state in zookeeper

Typically one zookeeper cluster can be used in a data-center, and serve several different applications/services.


Zookeeper paper discusses the API and the primitives we can implement using it in more details.
The paper presents itself as how a general-purpose replicated state machine built on top of a consensus algorithm looks like.