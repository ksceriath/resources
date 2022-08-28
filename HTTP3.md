From [[HTTP2]], 

- the underlying protocol changes from TCP + TLS to UDP + QUIC
- parallel request transmission is supported by QUIC stream, eliminating TCP HoL blocking
- QUIC offers streams and flow-control function
	- *QUIC by design implements separate, per-stream flow control, which solves the problem of head-of-line blocking (HoL) of the entire connection. In HTTP/2, packet-level HoL of the TCP stream could still block all transactions on the connection.*
- Server Push (introduced with HTTP/2) gets modified and is instead sent using QUIC streams

--> reliability stills seems to be an issue here, since the underlying protocol is UDP. How is this dealt with?

#HTTP 

#### References
- https://ably.com/topic/http-2-vs-http-3
- https://ably.com/topic/http3