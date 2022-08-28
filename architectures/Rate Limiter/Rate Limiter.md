Throttles the requests to an API.

(Can be useful in cases to prevent DOS attacks, or where limiting is needed for a paid api.)

Can be implemented at:
- client side
- server side
- a separate rate limiting middlware (e.g. [[API Gateway]]). See [[Rate Limiter#Architecture|below]].

##### Algorithms
- [[Token Bucket Algorithm|Token Bucket]]
- [[Leaking Bucket Algorithm|Leaking Bucket]]
- [[Fixed Window Counter Algorithm|Fixed Window Counter]]
- [[Sliding Window Log Algorithm|Sliding Window Log]]
- [[Sliding Window Counter Algorithm|Sliding Window Counter]]

##### Architecture
One valid implementation can be as follows.
- Client connects to a cluster of Rate Limiter servers, through a dedicated Load Balancer
- Rate Limiter servers forward the successful requests to application web-servers through another dedicated Load Balancer.
- Rate Limiter servers connect to a separately running server of an [[Data Stores#In Memory stores|in-memory store]] like [[Redis]] or [[Memcaced]]
- In memory store is used for storing the api-specific data e.g. token-buckets in case of [[Token Bucket Algorithm]], etc.
- Rate Limiter servers keep configuration which tells which APIs are to be rate limited, and with what parameters

##### References
- [RateLimit library (used at Lyft) (golang/grpc)](https://github.com/envoyproxy/ratelimit)
- [Using IPTables to achieve rate limiting at ip address level](https://blog.programster.org/rate-limit-requests-with-iptables)

#RateLimiting #RateLimiter
#SystemDesign