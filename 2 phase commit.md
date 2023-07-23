#DistributedSystems #TransactionManagement 

… is an atomic commit protocol.

(abbreviated as 2PC)

→ Context : there are multiple computers, which need to adhere to atomicity guarantees of a transaction management system

→ Assume : there is one computer that acts as a transaction coordinator (TC)


1. TC sends messages to the participants to make changes (after acquiring locks etc)
2. phase1: Once all the changes are communicated, TC sends a *PREPARE* message to the participants
3. If the participants were able to make the changes and are able to commit/persist, they respond affirmative. If the changes cannot be made due to whatever reason (node restarted, deadlock resolution, etc), they respond in a negative.
4. phase2: If all the partipants send an affirmative to the *PREPARE* request, TC sends a *COMMIT* to the participants, which they subsequently *ACK*. If even a single participant responds in a negative to the *PREPARE* request, TC sends an *ABORT* message to all.
5. Participants release any locks acquired for this transaction once the *COMMIT* or *ABORT* message is received.

![[Excalidraw/2pc happy path]]

### Failure cases
1. If a participant crashes
	- before getting PREPARE, it can’t say yes, and the transaction ABORTS
	- *after sending yes to a PREPARE, but before getting a COMMIT, it still needs to be able to persist the changes when it comes back up*, because other participants would get COMMIT messages and would persist state.
	
	To accomplish the above (second point), a participant generally persists the changes to a [[Write Ahead Log|log]] before sending yes to a PREPARE call.
	
	→ This makes 2pc slower (writing changes to a log).

2. If the TC crashes,
	- before sending any COMMIT messages, it must write the txn id and outcome of PREPARE messages to a log, which can be used be subsequent restarts of the TC.

3. If a participant replies YES to a PREPARE, but does not receive a subsequent COMMIT/ABORT, **it will wait indefinitely** — it’ll block.
    
	Participants can’t unilaterally abort or commit.



### slow
- lot of chit chat
- multiple disk writes at each participant and also at TC

This is slow, which means locks are held for long times, which means other waiting transactions wait long → further slower.

### use cases
- in a single room, e.g. small orgs, like within a bank

- not seen in usage across orgs (e.g. between different banks), or other physically separated scenarios

### RAFT
2PC is similar in construction to [[RAFT]]

but they are different use cases.

Raft enables availability but replicating across multiple participants. It does not need everyone to participate, so it works on a majority quorum. All the participants are doing the same thing.

2pc ensures atomicity across multiple participants, and thus requires everyone to participate – it does not tolerate even a single participant not able to keep up (opposite of availability).


### Raft + 2PC
Raft and 2pc can be combined : replicating TC and the participants individually in a Raft-replicated cluster.