Four features come up when looking for changes in HTTP/2.

- Multiplexing - Support of parallel request transmission (elimination of HTTP Head-of-Line Blocking)
	- Effectively, instead of sending one http request on a connection, waiting for a response, and then sending a second request, several request and response can now fly on the wire at the same time. So, clients can now send multiple requests, instead of waiting for a bulky response from a request at the head of the line.
- Binary Protocol - Compared to textual version before it, binary protocols are space efficient, and also efficient to parse (how?).
- Header Compression - this reduces the overhead, and reduces the number of round trips required to transfer the headers
- Server Push - Push allows servers to push stuff to the clients without the client requesting for it. This can be in anticipation of what resources client will need.



#HTTP


#### References
- https://ably.com/topic/http-2-vs-http-3
- https://www.infoq.com/articles/websocket-and-http2-coexist/