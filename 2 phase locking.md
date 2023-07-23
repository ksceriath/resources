
#TransactionManagement #DistributedSystems 
#DDIA #Draft 

1. acquire a lock before using record
2. hold the locks until done

*Locking systems generally have locks on per record basis - i.e. separate lock for each record.*

*Locks are required at the time the record is read first - and **not** all at the 
beginning* -- needed for [[Distributed Transactions#serializable|serializability]] 

