#Draft 

Can be assumed synonymous with [[Strong Consistency]].


=> If a client sees two writes in a particular order, then any other client should also see the writes in that particular order.
=> Any read should necessarily see the most recent write


*A linearlizable system cannot have reads served locally from a replica - a more recent committed value might be visible to the replica and hence its not strong consistency.*


