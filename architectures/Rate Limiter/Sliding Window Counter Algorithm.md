An algorithm for rate limiting.

This algorithm works by taking average of the requests falling in a window.

- Maintains fixed size time windows, with counters
- Each incoming request increments the counter of *this* window
- Approx number of requests in the rolling window is calculated as `= number of requests in this time window + number of requests in previous time window * percentage of overlap of the rolling-window with the previous time window`
- If the number of requests as calculated above exceeds some *threshold*, the request is dropped

This is memory efficient with compared with [[Sliding Window Log Algorithm]].
The approximate nature of the calculation makes this not-very-accurate.


#RateLimiting #RateLimiter #Algorithm
#SystemDesign