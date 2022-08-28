#DistributedSystems 

This method achieves strong consistency with replication.

#### Working
- Data is replicated on multiple systems and these form a chain (like a linked-list).
- *Head* of the list gets all the **writes**
- Each node in the chain gets a write request, updates the data locally, and forwards to the next node
- Once the *Tail* of the list gets the write, data is considered *committed* and a response is sent back to the client
	- Based on implementation, either *Tail* can send the reply back to client, or an acknowledgment can be sent back from *Tail* all the way back to *Head*, and back to client
- *Tail* of the list gets all the **reads**
	- And data on tail is considered to be in commited state (all replica have it), and so its okay to return it locally

#### CRAQ
*Chain Replication with Apportioned Queries* is a extension of above chain replication.
- Any node is eligible to get a **read** request
- During **writes**, each nodes writes the data to a *new version*
	- Once the write-acknowledgement for a certain version comes back from the next node in the chain, this node can remove the older versions, and mark this version as committed
- When serving a read request, if the latest version the node has is commited, that is sent as reply
	- If there are uncommitted fresh versions available, the node will talk to the *tail* and get the last commited version, and send that as reply

#### Gotchas
- Since this is a strong consistency model, writes are going to be comparatively slower than eventual consistency models
	- This has to ensure the write has taken place on all the nodes, where as latter systems mostly reply on only a majority of writes to succeed.
- Furthermore, in other systems the Leader can send out concurrent writes to each replica concurrently, which update the data in parallel
	- Here, instead, each node writes its data and then forwards the request to the next
	- the complexity here increases linearly with the length of the chain
- On the contrary, since the Head only has to talk to the next node, the load on the leader/head is significantly reduced compared to other systems

#### Network Partitions
CR does not have a good defence for network partitions : suggested fault tolerance methods would result in [[split brain]]
- This implies CR cannot be used practically just by itself.
- It can use a component : a *Configuration Manager* that manages the membership (who is alive, who is not, who is leader, what are different chains made of, etc)
	* This can be something built on [[PAXOS]], [[RAFT]] or something like [[Zookeeper]]
