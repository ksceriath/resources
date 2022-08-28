>> file filename

Determine the type of file.

It looks at the file and tries to determine if that is :

text : the file contains only printing characters
     and a few common control characters and is probably safe to read on an ASCII terminal
executable : the
     file contains the result of compiling a program in a form understandable to some UNIX kernel or another
data : usually “binary” or non-printable

