GDB is the GNU Debugger.


GDB can
- start a program and specify anything that might affect its behaviour
- make programs stop on a specified condition
- examine program's state when it's stopped
- allow changing things in program in-flight


GDB has two main sides to it:
1. Symbol Side
	- symbolic information about the program - variables, function names, line numbers, etc
	- reads the binary executable using BFD library $^2$
	- extracts any symbolic information, and builds a symbol table
	- initially, just extracts the globally visible symbols into a partial symbol table
	- complete symbolic info for a method is filled if user stops inside it
2. Target Side
	- manipulation of program execution and raw data; it can start/stop program, read/modify memory/registers, etc
	- this is a machine-level debugger; can just step through the instructions and inspect raw memory dump


Step command - makes the debugger step into the next line, and exercute it.

- Target side is asked to execute a single instruction and then stop
- GDB then looks at the program-counter (through Target side) and compares it with..
- the range of addresses the symbol side is associated with the current line
- if in range -> execute next target side instruction
	- else -> leave the program stopped, and point the next line in code to the user


(more details in aosa book reference)

#Draft 
#Debugger #GDB #GNU

##### References
1. http://aosabook.org/en/gdb.html
2. https://en.wikipedia.org/wiki/Binary_File_Descriptor_library