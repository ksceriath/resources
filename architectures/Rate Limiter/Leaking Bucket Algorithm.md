An algorithm for rate limiting.

Maintains a queue of requests of fixed size ("queue size").

- Incoming requests are added to the queue
- If queue is full, new requests are dropped
- Requests are picked up from the queue and processed at a fixed rate ("outflow rate")


There are two parameters here that need to be tuned.
Because of a fixed outflow rate, burst traffic fills up the queue with older requests, and causes newer requests to be dropped off.


##### References
- [Shopify uses leaky buckets for rate limiting](https://shopify.dev/api/usage/rate-limits)




#RateLimiting #RateLimiter #Algorithm
#SystemDesign