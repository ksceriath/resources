This architecture was created to simplify the two-tiered [[Data Lake#Architecture]].

This uses the [[Delta Lake]] technology, and adds a metadata layer on top of the existing data lake.

This metadata, along with indexing, caching, etc, gives the data lake the ability to serve the structured query workloads which would have required a [[data warehouse]].

#Draft 