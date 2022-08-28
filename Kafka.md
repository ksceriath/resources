... is a high speed pub-sub message broker, typically used for streaming architecture.


### How Kafka differs from other standard messaging (activeMQ, rabbitMQ) ?
- *Payload* : typically very very small key-value pairs
	- why??
- can give throughput upto 1 mil messages/sec, typically upwards of 100k messages/sec.
	- other systems can give 4k messages/sec for persisted data, 10k messages/sec for non persisted
- good for *operational data* : e.g. metrics, logging, auditing, etc
	- this can be compared with the standard messaging for *transactional data*
		- bigger payloads of transactional data will be difficult to do in kafka : WHY?
- kafka only supports *publish-subscribe* using topics
	- others give several other mechanisms
		- *point-to-point* : using queues - one and only one consumer
		- *pub-sub* : as in kafka
		- *exchange* : using amqp - producer sends to an exchange, which is bounded to several queues, and typically load-balanced consumers pick these up.

#Draft 


#DataStore 
#ArchitecturePatterns 