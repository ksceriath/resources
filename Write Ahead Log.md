#Draft 


--> Most systems implementing crash-recoverable transactions use WAL
--> Systems, before making any changes to the persistent state, append a log-entry to *a log* describing the full set of actions that the system intends to do in that transaction
--> Only after the above log-entry is written safely to the log, the actual changes are done