
- The same process (state machine) is replicated over several servers.
- Also a [[replicated log]] is present, which contains a series of *commands*.
- The process (on each server) executes the commands *in order*, changing the state, generating same outputs and arriving at the same state in end.
- External clients push commands to the log
- The [[replicated log]] is kept consistent across all the servers, using a [[Consensus Algorithm]]


==> A command can complete as soon as a majority of the cluster has responded to a single round of remote procedure calls.




#DistributedSystems 