This provides an ACID table storage over cloud data stores.

In a nutshell, it adds a metadata layer on top of the existing data-object storage, which give it better performance.

The existing object storages are already immutable objects. Typically these are used to store data in open-data formats like parquet, and can be accessed like a table using several compute interfaces - e.g. Dataframe api in Apache Spark, etc.

A Delta Lake table additionally maintains metadata about the table on the same data store: transaction logs and checkpoints, which are versioned.

Transaction logs contain the history of add, remove, etc operations on the table objects (here, object = parquet files, which contain multiple records), and also metadata about the object, e.g. min/max values of the columns in the object according to the change, etc.

These logs enable features like, improved read performance by data-skipping (skipping reading objects 
based on the min-max values), concurrency control and transaction management for writes, rollbacks, etc.

#Draft 