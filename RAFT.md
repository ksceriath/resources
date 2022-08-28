... is a [[Consensus Algorithm]].


*Raft relies on a majority system which makes it correct. Leader must ensure that an entry is replicated on majority of servers, and is committed on majority of servers. For that reason, the next Leader's majority will overlap with at least one server from the previous Leader's majority.*

==> There are several servers
==> Servers talk to each other through [[RPC]]s
==> implementing a state machine
==> and a replicated log
==> Each server is in one of three states: FOLLOWER, CANDIDATE and LEADER.
==> Log is leader-append-only
==> Only Leader tells Followers to append messages to the log - in a fixed order
==> Leader separately tells Followers to execute the instructions in the log when all are consistent
==> Leader also sends periodic heartbeat messages to the servers
	==> Heartbeats are append-message RPCs, with empty contents

==> Time is divided into *terms* of arbitrary lengths <-- terms get a monotonically increasing sequence
==> Each term has a single leader
==> If a leader is not available, a new term will start

### *Leader Election*
--> Each server starts as a Follower
--> Each server also gets set an *election timeout* <-- This is a randomized value in a certain range.
--> As per definition, Followers get periodic heartbeats from the current Leader
--> If any Follower does not get a heartbeat from Leader for *election timeout* period, it initiates an election
	--> The Follower marks its state as a Candidate
	--> It will increase its current term number <== marking the start of a new term
	--> It will send RequestVote RPCs to each of the other servers
--> Upon receing a RequestVote RPC,
	--> Follower
		-> checks if the term number is larger than its own <- if not, just ignore the message
		-> checks if its own log is more up-to-date than that of the candidate <- if so, ignore the message
			-> more up-to-date = log with the last entry's term later than the other
			-> if both logs end on same term, more up-to-date = longer log
		-> else, increase the term number, and vote for the candidate by replying
		-> also, vote for only one candidate in one term <- on first-come-first-serve basis
	--> Candidate
		-> checks if the term number is large than its own <- if not, just ignore the message
		-> else, marks itself as a Follower, and does what a Follower does (above)
	--> Leader
		-> checks if the term number is large than its own <- if not, just ignore the message ("Wait .. let me send you a heartbeat instead!")
		-> else, marks itself as a Follower, and does what a Follower does (above)
--> If the Candidate receives votes from a majority of the servers, it marks itself as a Leader, and sends heartbeats to others
--> Else, *election timeout* happens. A new term is started and elections begin anew <-- with incremented term number
--> Since election timeouts are randomized, if no consensus was reached because majority of servers were candidates, differing value of timeouts will ensure that not all servers initiate the election for the new term at the same time.

### *Log Replication*
--> Client request contains command to be executed by the replicated state machines
--> Leader appends the command to its log as a new entry
	--> Log is maintained as an indexed list - each entry contains the command + the term number for that entry
--> Then it issues AppendEntries RPCs in parallel to each of the other servers
  --> Leader maintains nextIndex for each Follower <-- this is the log index onwards of which is to be sent to the Follower
  --> Leader also sends the term and index of the previous entry in AppendEntries call
	  --> Follower can reject the call if the previous entry does not match its records
  --> Upon such rejection, Leader decrements the nextIndex for that Follower and tries again
--> Leader waits for a 'majority' of the servers to replicate the entry
--> This marks the entry as commited
--> Leader applies the commited entry to its state machine
--> and returns the result to the client
--> If some Followers crash, run slow or network-issues occur, AppendEntries RPC is tried indefinitely

#Draft 

#DistributedSystems 