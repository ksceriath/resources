>> grep [options] pattern [file1 [file2 ...]]

grep searches the given input files (or standard input, if files are not specified) and selects any lines matching the specified pattern.

Each line matching the pattern is written to standard output.

options:

-c          : print only the count of the lines that matched
-i          : case insensitive match
-n          : print matched lines and corresponding line numbers
-v          : print lines that do not match the pattern
-e exp      : uses exp as the regex pattern for matching
-f file     : use the file as a list of patterns, one per line
-o          : print only the matched part of each line
-s          : Silent mode. Non-existent/ unreadable files are ignore : no error reported



-r : recurse into directories :: IS THIS REQUIRED IN LINUX? CHECK.
