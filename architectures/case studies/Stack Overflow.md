(excerpts)
- uses [[HAProxy]] [[Load Balancer|load balancers]]
	- running on CentOs machines
	- 2 pairs of 10 Gbps LACP network links
		- one pair for external network
		- another pair for [[DMZ]]
	- 64+ GB RAM (high memory helps with "Efficient SSL negotiation")
	- Listens to multiple sites --> Routes to different backends based on 'host-header'
	- TLS traffic gets terminated here
		- Efficient SSL negotiation: [[TLS#Cache TLS sessions for reuse draft|cache more TLS sessions for reuse]] => less things to compute on subsequent connections to the same client
	- [[Rate Limiter|Rate Limiting]] happens here
	- 'Header-captures' for perf logging happens here
- Load Balancers feed into Web Servers, which is followed by a Service Tier.
	- Web Servers run several different applications
		- One specific application - the primary Q&A application - is multi-tenant : it serves the requests for all the Q&A sites
		- These are overall 9+2 servers (running two different sets of applications)
	- Service Tier runs to support Web Tier applications, and internal applications as well
		- This runs two main applications: Stack Server (runs the tag engine), based on [[http.sys]], and Providence API (ML application)
		- These are 3 servers
		- Lot of heavy lifting happens here : costly actions that will be very costly with 9x servers in web tier, are run here with redudancy of only 3
		- elasticsearch indexing jobs run on these servers as well
- separate specialized backend servers for specialized APIs - tag servers (faster raw processors; used when searching by tags), separate from web servers
- uses [[redis]], [[elasticsearch]] and [[ms sql server]] clusters as data stores
- separate hardware for other things unrelated to actually running website - deployments, monitoring, etc
- *" Network cores have 10 Gb of bandwidth on each port"*
	- what is a **Network Core** ? Most likely a reference to a *network switch*.
- 20Gb (2 ports of 10Gb) is the bandwidth given to SQL servers. Active usage is only about 100-200Mb out of the 20Gb pipe. But, backups, rebuilds (?) completely saturate the 20GB.
- SQL servers write data in SSDs. The average write time on databases is **0ms**.
- Database has **2 levels of cache** in front of it
- Database has 40:60 read-write ratio. That is, 60% of database disk access is for *writes*
- Elasticsearch boxes supposedly perform much better with SSDs. *"we write/re-index very frequently"*
- Hardware also contains a SAN (storage area network) - but not used for hosting/running website
- Monitoring is very important. *"We have high visibility of what goes into our page loads by recording timings for _every single request_ to our network"*
- All of the servers (web, sql) run at a very low CPU, RAM and network utilization. This provides headroom to grow, and buffer to not fail in case something bad happens.
- Utilization is low because of efficient code, and that is because performance is important. **"Anything you’re doing that doesn’t need doing costs more than _not doing it_, that continues to apply if it’s a subset of your code that could be more efficient"**
- uses [[CloudFlare]] as a DNS service
- [[Redis]] is used for caching as well as pub/sub
	- efficient : handles 160 bil ops in a month, with <2% CPU utilisation
	- 2 main servers (master/slave) for running sites : 256GB RAM (90GB in use)
	- 2 additional dedicated servers for machine-learning (Providence platform) : 384GB RAM (125GB in use)
		- recommending questions on home page, matching to jobs, etc
- Caching is modeled on L1/L2 cache system
	- L1 : [[HTTP Cache]] on web servers (local-cache)
	- L2 : Redis
	- A web server hits its HTTP cache for values.
	- If that is a miss, it hits Redis, and updates HTTP cache.
	- If that is also a miss, it hits database/whatever-API to get the data, and also updates both caches
	- Each separate Q&A site has its own L1/L2 caching
		- by key-prefix in L1
		- by database-id in L2
- Pub/Sub
	- One server can publish a message and all other subscribers receive it
	- Used for :
		- L1 cache invalidation (for consistency) on other servers, when one server does a removal
		- WebSockets - (How??)
- WebSockets
	- to push realtime updates to users : notifications, vote counts, new answers, comments, etc
	- at peak : 500k concurrent open websocket connections
	- These are more efficient than **polling** at this scale - push more data to user with fewer resources and *being more instant to the user*
- Elasticsearch (v1.4)
	- for very basic searches like calculating related questions and suggestions when asking a question <-- full text search use case
	- Each data center has one cluster
	- Each cluster has 3 nodes
	- Each site has its own index (careers has more than one)
	- each node : 192GB RAM, dual 10Gbps network, with SSD storage - this is beefier than usual
	- Application domains in Stack Server (which run tag engine) continually index items in ES also
		- index only the items in database which have changed since the last pass (maintain a row version number in SQL Server)
	- Elasticsearch vs SQL full-text search
		- scalability
		- SQL CPUs are comparatively expensive
	- Elasticsearch vs SOLR
		- *we want to search accross the entire network - many indexes - at once.. this was not supported in SOLR at the time*
- SQL Server - the single source of truth database
	- 2 clusters
	- Each cluster:
		- 1 master - takes **ALL** the load
		- 1 replica in NewYork, 1 replica in Colorado
		- Replicas are asynchronous
	- First cluster :
		- 384GB RAM, 4TB SSD, 2x 12 cores
		- hosts stack overflow, sites, prizm, mobile databases
	- Second cluster :
		- 768GB RAM, 6TB SSD, 2x 8 cores
		- runs other Q&A sites, talent, chat, etc
	- Straightforward queries; minimal usage of stored procedures




#### Hardware related checklist (copied as it is from nick craver's blog)
-   Is this a scale up or scale out problem? (Are we buying one bigger machine or a few smaller ones?)
    -   How much redundancy do we need/want? (How much headroom and failover capability?)
-   Storage:
    -   Will this server/application touch disk? (Do we need anything besides the spinny OS drives?)
        -   If so, how much? (How much bandwidth? How many small files? Does it need SSDs?)
        -   If SSDs, what’s the write load? (Are we talking Intel S3500/3700s? P360x? P3700s?)
            -   How much SSD capacity do we need? (And should it be a 2-tier solution with HDDs as well?)
            -   Is this data totally transient? (Are SSDs without capacitors, which are far cheaper, a better fit?)
    -   Will the storage needs likely expand? (Do we get a 1U/10-bay server or a 2U/26-bay server?)
    -   Is this a data warehouse type scenario? (Are we looking at 3.5” drives? If so, in a 12 or 16 drives per 2U chassis?)
        -   Is the storage trade-off for the 3.5” backplane worth the 120W TDP limit on processing?
    -   Do we need to expose the disks directly? (Does the controller need to support pass-through?)
-   Memory:
    -   How much memory does it need? (What _must_ we buy?)
    -   How much memory _could_ it use? (What’s _reasonable_ to buy?)
    -   Do we think it will need more memory later? (What memory channel configuration should we go with?)
    -   Is this a memory-access-heavy application? (Do we want to max out the clock speed?)
        -   Is it highly parallel access? (Do we want to spread the same space across more DIMMs?)
-   CPU:
    -   What kind of processing are we looking at? (Do we need base CPUs or power?)
    -   Is it heavily parallel? (Do we want fewer, faster cores? Or, does it call for more, slower cores?)
        -   In what ways? Will there be heavy L2/L3 cache contention? (Do we need a huge L3 cache for performance?)
    -   Is it mostly single core performance? (Do we want maximum clock?)
        -   If so, how many processes at once? (Which turbo spread do we want here?)
-   Network:
    -   Do we need additional 10Gb network connectivity? (Is this a “through” machine, such as a load balancer?)
    -   How much balance do we need on Tx/Rx buffers? (What CPU core count balances best?)
-   Redundancy:
    -   Do we need servers in the DR data center as well?
        -   Do we need the same number, or is less redundancy acceptable?



#### Monitoring

Types of data
- Logs : detailed text and data - e.g. info messages, errors, traffic data
- Metrics : Tagged numbers of data for telemetry - e.g. cpu utilisation, etc <-- useful for ALERTING
- Health Checks : is a service/server up? <-- useful for ALERTING
- Profiling : performance data - how long things are taking?

##### Logging
==> The application logs go into the SQL server database.
==> Logs for Redis, Elasticsearch and SQL server go in the corresponding local disks, utilising built-in logging and log-rotation mechanisms

##### Metrics
==> Metrics are logged as a 'tagged time series data'
==> Metrics are internally collected by [[Bosun]]; Bosun does alerting as well
==> Bosun is backed by [[OpenTSDB]] for storage
==> Since OpenTSDB is backed up by [[HBase]], it has to be given more hardware than actually needed.
==> (There is a mention of some problem with HBase replication when dealing with smaller amounts of data.)
==> They have tried to get Bosun backed up by SQL Server instead, but there are some issues with the performance of some queries
==> Bosun also feeds into [[Grafana]] for graphs

==> MiniProfiler and OpsServer are additional tools used
	- MiniProfiler logs the timings for the end-to-end load of pages for user
	- OpsServer is a tool to monitor - used for monitoring SQL server, redis, elasticsearch, haproxy and web servers, and also other things like exceptions logged, etc


#### Caching

Trade-offs when caching:
- cache invalidation
- memory used by cache
- latency of access to cache
- additional piece of complexity

==> Caches are maintained at different grains, locally (in memory) on a server, and optionally backed by Redis.
==> Multiple instances of Redis are run on the same set of servers, catering to different applications/business domains.
==> Objects can be stored in local (in mem) cache as it is
==> To be sent over the wire to Redis, objects are serialized in [[Protobuf]] format
==> Pipelining
	-> same connection (long lived) to Redis is used for multiple commands sent to Redis
	-> This works for smaller commands/responses
	-> For larger objects, separate 'bulky' connection-path is used
==> Cache Invalidation -> if a key is updated/removed, cache needs to be invalidated for that key in redis as well as the local cache of the servers
	-> First, purge or update the key in Redis
	-> Then, broadcast the key on a channel
	-> servers subscribe to this channel, and invalidate keys from local cache based on it


##### Redis gotcha
- Another process (vmstat - to gather virtual memory stats) was getting kicked-off, and it was kicking the CPU core the Redis process was running on.
	- The context switching to another core was enough for Redis to start causing timeouts.
	- This was solved by pinning Redis process to specific cores : [[CPU Pinning]] is a thing!
- Redis Cluster is a feature, and its not used at SO for several reasons
	- One reason - it needs 3 servers to run (for consensus, etc). SO requirement is well satisfied by just 1 server
- 'StackOverflow for Teams' caching
	- It is maintained in separate isolated network for each client
- Redis is optional; it speeds things up as a cache, but its not a Source of Truth for anything


#### Resources
- https://nickcraver.com/blog/2013/11/22/what-it-takes-to-run-stack-overflow/
- https://nickcraver.com/blog/2016/02/17/stack-overflow-the-architecture-2016-edition/
- https://nickcraver.com/blog/2016/03/29/stack-overflow-the-hardware-2016-edition/
- https://nickcraver.com/blog/2018/11/29/stack-overflow-how-we-do-monitoring/
- https://nickcraver.com/blog/2019/08/06/stack-overflow-how-we-do-app-caching/