An algorithm for rate limiting.

Two main components:
- Token Bucket
	- contains tokens
	- has fixed size : Bucket Size
- Refiller
	- generates tokens
	- has a fixed rate : Refill Rate

Steps
- Refiller keeps filling the bucket with tokens continuously at RefillRate
- Bucket cannot contain more than BucketSize tokens
- Each incoming request checks token bucket for tokens
	- if token is found : its removed and request passes through
	- if token is not found : request is dropped


There are two parameters to be tuned.
Bucket size allows burst of traffic to pass through, for short periods of time.


##### References
- [Amazon API Gateway uses token bucket for rate limiting](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-request-throttling.html) [[API Gateway]]
- [Stripe uses token bucket rate limiters](https://stripe.com/blog/rate-limiters)

#RateLimiting #RateLimiter #Algorithm
#SystemDesign