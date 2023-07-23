Also see [[Pessimistic Concurrency Control]].

→ you can read data without locking
→ writes are first buffered at the client
→ at the time of commit, validation is done to check if the data that was read or written still holds w.r.t serializability of transactions
→ if there are no conflicts, then go ahead
→ otherwise abort the transaction