![[Frangipani.excalidraw]]

## Cache Coherence

- Frangipani servers do not talk to each other
- access to the shared [[PETAL]] server is coherent
- Coherence is maintained through a read-write locking


Frangipani follows the typical multiple readers - single writer locking : At a time, to any resource, either multiple read locks can be taken, or a single write lock.


### READ Lock

When a server wants to read a block, it must:
-> Acquire a read lock.
-> Update the local cache with the disk data. Use the cache for read-use.

On release:
-> Invalidate the local cache

### WRITE Lock

When a server wants to write to a block, it must:
-> Acquire a write lock.
-> Update the local cache with the disk data. Use this local cache to update/write. This makes the cache "dirty" (out of sync with storage).

On downgrade from write lock to a read lock:
-> Flush cache data to disk

On release:
-> Flush cache data to disk
-> Invalidate the local cache

## Atomicity

=> Multi-step operations should appear as if they happened in a instant in time : achieved using *Transactions* 

### Distributed Transactions

Frangipani does transactions using locking, similar to [[#Cache Coherence]].

**Frangipani uses same locking logic to achieve fundamentally opposite requirements : Cache Coherence (my local changes should be always visible to others) vs Transactions (my local changes should not be visible unless I am done with everything.)

-> Acquire all the locks needed for the operations in the transaction
-> Perform all updates to data in cache
-> Update the cache to PETAL
-> Release all the locks


## Crash Recovery

### What to do when a workstation crashes while holding locks?

-> Frangipani uses [[Write Ahead Log]] to help with crash recovery.
-> it logs entries to a location in PETAL
-> Log entries are written before the dirty-cache changes are pushed to PETAL (on REVOKE, DOWNGRADE, time-based cache update, etc)
-> **Most transaction systems maintain a single log. Frangipani maintains separate per-workstation logs**
-> 
```
LogEntry : {
	LogSequenceNumber : ...
	Modifications : [ 
		{
			BlockNumber : ...
			Version# : ...
			Data : ...
		},
		...
	]
}
```
-> The lock server hands out locks to workstations based on *leases* (with a certain TTL)
-> The workstations are supposed to renew leases before expiration, or give up the lock : send a REVOKE message to lock server
-> If a workstation's lease expires, lock server assumes the workstation has crashed
-> in which case, it asks another workstation to replay the logs (stored in petal) of the crashed workstation
-> The crashed workstation might not have written all the log entries before crashing
-> in which case the changes in PETAL would be some prefix of changes in the crashed-workspace's dirty cache
-> The recovery workstation would only replay "complete" log entries : ensuring completeness by using stuff like checksum, etc


=> Every block of data in PETAL has a version number. This version number is part of the LogEntry that Frangipani makes, and so, during recovery, the recovery workstation does not make changes  for entries whose version number does not match the current version number in PETAL.

=> Since the version numbers are maintained as such in the log entries and PETAL, the recovery workstation does not need to worry about locks on the blocks updated in the log-entries.
	-> Either the crashed workstation did not release its lock, in which case the block is still locked
	-> Or it released the lock, which means it had written its changes to PETAL already, and thus the version which recovery workstation will find now, will not match.



# USE Cases

The frangipani paper presents some interesting ideas and interesting approaches to problems.

It however was not very influential. The use case it was trying to target was small working group of people sharing data as files.

It is an old paper, and the action was happening around usecases involving databases.

File systems like GFS are about processing tera bytes of data - and so the ideas of caching are counter productive altogether there.