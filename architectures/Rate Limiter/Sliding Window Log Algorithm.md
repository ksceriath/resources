An algorithm for rate limiting.

- Maintains a log of timestamps of incoming requests
- For each incoming request
	- its timestamp `t` is recorded into the log
	- every timestamp older than `t - WindowSize` is removed from the log
	- if log size is within the existing threshold, the request passes through
	- otherwise, the request is dropped

This consumes high amount of memory because all the request timestamps are stored in memory.

#RateLimiting #RateLimiter #Algorithm
#SystemDesign