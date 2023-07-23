#DistributedSystems #TransactionManagement 

transaction = concurrency control + atomic commit


### ACID

**Atomic** = all or none (writes) despite failures
**Consistent** = ?
**Isolated** = transaction *execution* is [[Distributed Transactions#serializable|serializable]] (two transaction do not see each others changes before completion)
**Durable** = After commit, changes are presistent 

*Atomicity* deals with failure-tolerance.

*Isolation* or *serializability* deals with concurrency-control.


### serializable

*a (concurrent) execution of transactions is said to be serialiazable
- *if, there exists
- *a serial order of execution of transactions
- *that yields the same result

(serial order = not in parallel)


### concurrency control

1. *Pessimistic* = essentially locking
2. *Optimistic* = just go ahead and make changes and check later if there was another conflicting transaction -> if it was, abort the transaction and retry

If conflicts are frequent => Pessismistic concurrency control
If conflicts are rare => Optimistic concurrency control

→ [[2 phase locking]] is a pessimistic concurrency control.


### atomicity

*atomic commit protocol* : either do all the steps in the transaction, or do none (in case of failures)

→ [[2 phase commit]] is an atomic commit protocol.


