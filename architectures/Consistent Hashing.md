Provides a way to map keys to servers.

In a typical case of a sharded database, the data needs to be mapped to shards or partitions.

Different partitions can then be stored on different servers independently of each other.

Generally :
- A key in the data is defined
- the key is hashed using some well-defined hash function
- the hashed value is mapped to one of the servers
- when reading by the key, the corresponding server is queried for the data
- when writing by the key, the corresponding server is inserted with the data

(One approach for #3 above is to mod the hashed value with the cluster size, giving a number which can be used to identify the respective server. This has potential downsides in cases where a server gets added or removed.)

##### With consistent hashing, the *mapping key to sever* approach works as follows.
- Each server is mapped to several values (these are called *virtual nodes*) in the hash-function's co-domain
- Every key is also directly mapped to a value in this set (by calling the hash function on the key)
- In a clockwise and cyclical manner, the server-value (and thus the corresponding server) directly following (larger) the key hash is mapped to that key

##### In case of a server removal
move anti-clockwise in cyclical manner from the removed server-values, until another server-value is found -- all the keys in this range get mapped to the next server-value (next cyclically clockwise)

##### In case of a server addition
move anti-clockwise in cyclical manner from the added server-values, until another server-value is found -- all the keys in this range get mapped to the new server (and should be moved out of the next - cyclically clockwise - server).

##### Counter-Point
- The amount of keys moved during server addition/removal is on an average (total number of keys) / (total number of virtual nodes). In case of the *mod-with-cluster-size* approach nearly all the keys get remapped.
- The number of virtual nodes typically go in hundreds - this allows for even distribution of data across servers. If, for instance, each server is mapped to just one virtual node then the chances of data being uneven and a single server handling larger partitions increase.
- Though the above discussion is about consistent hashing with respect to some data storage system, it does not talk about how the existing data is moved in case of node addition/removal. The book #DDIA should be used as a reference to update this. #Draft 