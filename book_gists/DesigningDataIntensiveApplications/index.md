Reliability : System should perform correct function at desired performance even in case of faults (hardward, software, human).
Scalability : There should be reasonable ways of dealing with data volume/ traffic volume/ complexity growth.
Maintainability : People should be able to work on the system productively over time.
Fault = one component of a system deviating from its spec
Fault-Tolerant/ Resilient = systems that can anticipate and cope with faults
MTTF = Mean Time To Failure . Hard disks are reported to have mttf of 10 to 50 years.
Load Parameters = Parameters to define load on the system; depending on particular architecture can be different things: requests per second, ratio of reads to writes, number of simultaneously active users, hit rate on a cache, etc.
Throughput = Amount of data a system can process in a fixed time.
Response time = Time spent between a client sending a request and receiving a response.
Latency = Duration that a request is 'waiting' to be handled, when its awaiting service.
Median/ p50/ 50th Percentile = Median of the response times; half of the requests are served in median response time, and half take longer.
p95 = 95th Percentile
p99 = 99th Percentile
p999 = 99.9th Percentile
SLO/ SLA = Service Level Objective/Agreements - the expected performance and availability of a service.
Queueing Delay = Requests get queued at a server with limited parallelism, leading to delays.
Head-of-line blocking = Small number of slow requests can hold up the queue slowing everything else.
Tail latency amplification = When slow backend services are called multiple times for one user request, even if a small percentage of services are slow, a higher percentage of user requests become slow.
Vertical Scaling = Moving to a more powerful machine.
Horizontal Scaling = Distributing load across multiple smaller machines.
Shared-Nothing architecture = Horizontal scaling.
Relational Model = Data is organized into Relations (tables) where each relation is an unordered collection of Tuples (rows). These enforce schema-on-write.
CODASYL = Conference on Data Systems Languages
Hierarchical Model = Represents all data as a tree of records nested within records, similar to json structure.
Network Model/ CODASYL Model = Generalization of hierarchical model, where each record can have multiple parents.
Access Path = In network model; the path from root to a record, following the links (stored as pointers); application code had to decide the access paths which reduced the maintainability.
Query Optimizer = A component in relational databases, decides which parts of query to execute in what order and using which indexes. This is effectively 'access path' but is made automatically by the database.
Object Databases = 
XML Databases = 
Document Databases = Store data as json documents. Similar to hierarchical model in that they store nested records (1-to-many relationships) within parent record, but differ in that storing many-to-one and many-to-many relationships they store a separate document and refer it using 'document-reference' (similar to foreign key in relational model). These enforce scehema-on-read. Offer data-locality which gives good perf.
Polyglot Persistence = Usage of relational datastores alongside a broad variety of nonrelational datastores within an application/ architecture.
NoSQL = Offer greater scalability (large datasets + high write throughput) + free/open source + specialized query operations (which are not well supported by relational databases) + dynamic/expressive data mode, over relational databases.
Normalization = Removing duplicates in data which can be stored in just one place.
Object Relational Mismatch = The "impedance mismatch" representing the disconnect between the object model in the application code and database model, which warrants the use of ORM tools.
Shredding = Relational technique of splitting a document like structure into multiple tables.
Graph Model = Most natural choice of database model for highly-interconnected data. Relational model is acceptable and document model is awkward for such use cases. Graph DB also does not enforce a schema.
Property graph model = Consists of vertex (with an id, set of incoming and outgoing edges and properties) and edge (with an id, start and end vertex, label and properties).
Triple store model = Store all information in triplets (subject, predicate, object). Subject is equivalent to a vertex. Predicate and Object are equivalent to either a property (key, value) or an edge and an (end) vertex).
MapReduce querying = A limited form of Google's MapReduce offered by NoSQL databases MongoDB and CouchDB for performing read-only queries across many documents.


Memcached = caching layer
Elasticsearch/Solr = full text search server
Netflix Chaos Monkey = triggers faults deliberately to test fault tolerance
Forward decay, t-digest, HdrHistogram = Algorithms for calculating percentiles.
RethinkDB = Document oriented NoSQL DB which supports joins
CouchDB = Document oriented NoSQL DB which supports joins only in predeclared views
MongoDB = Document oriented NoSQL DB which does not support joins
Espresso = Document oriented NoSQL DB
IBM IMS = Information Management System, database based on hierarchical model
Google Spanner = Relational database, which offers option if data locality (like document model) by allowing schema to declare that a table's rows should be interleaved/nested within a parent table.
Oracle RDBMS = relational db, offers multi-table index cluster tables which is same as Spanner's nesting for data locality.
Cassandra = Column oriented databases based on Bigtable data model
HBase = Column oriented databases based on Bigtable data model
Neo4j = Graph database based on property-graph model
Titan = Graph database based on property-graph model
InfiniteGraph = Graph database based on property-graph model
Datomic = Graph Database based on triple-store model
AllegroGraph = Graph Database based on triple-store model
RDF = Resource Description Framework; intended as a mechanism for different websites to publish data in a consistent format : semantic web.
Cypher = Declarative graph query language (borrowed ideas from SPARQL)
SPARQL = Declarative graph query language (sparkle : SPARQL Protocol And RDF Query Language)
Datalog = Declarative graph query language
Gremlin = Imperative graph query language
Pregel = Graph processing framework
Cascalog = Datalog implementation for querying large datasets in Hadoop
