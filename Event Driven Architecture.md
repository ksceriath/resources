#ArchitecturePatterns

-> Asynchronous request/reply pattern
-> **Uses queues**

- Use a correlation id to track responses

```

 ______________                                                      ______________
|              |  --------- Request Queue [{cid : xxx}] ----------> |              |
|   Service    |                                                    |   Service    |
|              |  <------- Response Queue [{cid : xxx}] ----------- |              |
|______________|                                                    |______________|


```


- Use temporary queues
	- Once the receiver reads the message, the broker removes the queue

```

 ______________                                                      ______________
|              |  --------- Request Queue [{id : xxx}] -----------> |              |
|   Service    |                                                    |   Service    |
|              |  <----------- Temporary Queue XXX ---------------- |              |
|______________|                                                    |______________|


```
