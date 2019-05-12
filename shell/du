>> du -ha --apparent-size [ filename | directoryname ]
>> du -ba --apparent-size [ filename | directoryname ]

Summarize disk usage of the set of FILEs, recursively for directories.

h = human readable format (for sizes)
b = show sizes in bytes
a = recurses into subdirectories
L = evaluate symbolic links also


Disk usage is the amount of disk space being used by a file or directory. Apparent size is the actual size of the file or directory based on the contained data.

For several reasons the disk usage could be more than the apparent size, e.g. filesystems would store files as blocks of some size, and an entire block would be allocated to a file and counted towards its size, irrespective of how much that block is full -- its basically rounded out to the block sizes.