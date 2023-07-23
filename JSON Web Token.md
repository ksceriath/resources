(JWT)

- This is a token returned by OAuth2 authorization server
- Contains the data for the server to be able to authenticate/authorize the server
- Contains three sections:
	- Header. Contains:
		- type of token (e.g. JWT)
		- signing algorithm
	- Payload: data about the user; no standard format.
	- Signature: electronic signature of the token
- All three sections are base64 encoded, and concatenated with a "."
