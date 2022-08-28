An algorithm for rate limiting.

- Maintains fixed size non-overlapping time windows in real time.
- Each window gets a request counter (initialized to 0)
- Incoming request increments a counter for the specific window
- If the counter exceeds the threshold, further requests in that window are dropped


Since the window size is fixed, a window overlapping with two adjacent time windows could see allowed request count = double the threshold value set.

#RateLimiting #RateLimiter #Algorithm
#SystemDesign