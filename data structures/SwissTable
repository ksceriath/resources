Swiss-Table (C++) or HashBrown (Rust)
======================================

Introduction
-------------
SwissTable is a hash table implementation that was implemented in C++, at Google. HashBrown is a Rust variant of the same with changes/improvements.

HashTable is data structure which aims to store data as an array, providing random access, but with non-integer keys (compared to integer indices in an array).

Typically, a hash function is used to map a key to an integer. That integer is then used as an index into the array. This process is used for all the operations on the hash table, e.g. find, insert, and delete. Depending upon the choice of the hash function, many times more than one objects from the key-space could get mapped to the same integer. In such cases, we need to do Collission Resolution:

CHAINING - One way is to have each array element point to a linked list. The hash table is maintained as an array of buckets. Whenever a key resolves to an integer, we insert the object into corresponding bucket. When more than one keys map to the same integer, the corresponding bucket size grows. Memory is wasted in this approach for storing pointers.

OPEN ADDRESSING - In this case we maintain the hash table as an array of objects. While inserting, if the integer index the hash of the key maps to, is occupied in the array, then we iterate over the array onwards from that index (this is called probing), and insert the object into the next empty slot. In case of deletion using such an implementation, simply removing an objects in middle of a run, could render the objects inaccesible. We have to reinsert all the elements in that run, following the hole. We might also choose to insert an tombstone in place of the deleted object so that the run isn't broken, and then take care of tombstones at sometime in future to get some sort of amortized efficiency.

There is another interesting variation on the Open Addressing scheme, called ROBIN HOOD HASHING. The basic idea is to maintain another number with every inserted element, signifying its distance from its actual index in the array. During an insert, while traversing the run, we don't just run to the end and insert. Instead, we insert as soon as the distance of the element we want to insert is more than that of the element at the current location. Then we proceed onwards with the element at the current location. This is similar to the Insertion Sort mechanism.


Swiss Table
-----------
The Swiss Table takes the Open Addressing scheme and seeks to improve its performance. The optimizations made for this data structure are pretty low level. The aim is to utilize the read from the L1 cache. L1 cache sits very close to the CPU, and this is the fastest to read from. (Its faster to read from L1 cache, then L2 cache, then L3 cache, and then the RAM -- this is depended on the physical distances of these memories from the CPU -- also check the latency.txt elsewhere on this repo).

For every position in the array (slot), we keep a control byte. First (or last) bit of this control byte is 1 (if the slot is empty or deleted) or 0 (if the slot is filled). The rest of the seven bits of the control byte contain the most (or least) significant seven bits of the hash of the key. We further divide the slots into groups of 16. So each group contains 16 bytes as control bytes. Thus, the control bytes for an entire group amouting to 128 bits can fit on one L1 cache line.

We can:
1. fetch an entire group worth of control bytes into L1 cache,
2. build the bytes to compare using the hash of the key we want to find,
3. compare the two values above,
4. Mask the result to a true/false value.

The above steps have been listed as such to emphasize that it takes a total of 4 CPU instructions to check if a key exists in a group consisting of 16 elements.

This is the a very basic gist of how the SwissTable hash table is supposed to work. Also the HashBrown implementation works like so.


[ References ]
1. Blog on hashbrown crate: https://blog.waffles.space/2018/12/07/deep-dive-into-hashbrown/
2. Talk on the Swiss Table: https://www.youtube.com/watch?v=ncHmEUmJZf4