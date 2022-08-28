#DistributedSystems 

In a locking system, when multiple processes need to acquire a lock, only one succeeds.

If they simply wait, and then try again once the lock is released, they all will try at the same time again, and again only one will succeed.

If the processes are distributed, this will consume a significant network bandwidth, where everytime all n nodes try to acquire lock, and only one succeeds, making the complexity n^2 for n processes trying to take a lock.

This is called Herd Effect.