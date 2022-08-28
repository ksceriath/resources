... is a toolset of low level language-agnostic technologies : assemblers, compilers, debuggers.

![[Excalidraw/LLVM|10000]]

#### Frontend
--> Parses the source code
--> build *language-specific* Abstract Syntax Tree
--> Responsible for validating and diagnosing errors in the input code
--> Parsed code is translated into [[LLVM#LLVM IR|LLVM IR]]

#### Optimizer
--> takes the LLVM IR, and runs a series of optimizations
--> optimizations are:
	1. Look for a pattern to be transformed
	2. Verify the transformation is safe for the matched instance
	3. Do the transformation, updating the LLVM IR code
--> optimizations are generally aimed at : improving code runtime, memory footprint, stroage size, etc

#### Backend
--> also known as *code generator*
--> transforms LLVM IR into target specific machine code
--> responsible for generating "good" code for the *target* instruction set
--> takes advantage of specific features of the supported architecture
--> splits code generation into multiple passes
	- instruction selection
	- register allocation
	- scheduling
	- code layout optimization
	- assembly emission
	- ...

==> Multiple components of this architecture can be reused, modified, dropped, or enhanced for specific use-cases


#### LLVM IR
*LLVM Intermediate Representation*

This is a complete code representation.
It is the only input to the optimizer.
This is a low-level RISC-like virtual instruction set.
(looks like some form of assembly)


#LLVM #Compiler

##### References
1. http://aosabook.org/en/llvm.html