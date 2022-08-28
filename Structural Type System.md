#ProgrammingLanguageDesign


This is a type system where the **types of objects** are **inferred from their structure/definitions** instead of a type name.

Golang has structural type system.
- you can define a struct with some methods
- you can define an interface with those same methods (or a subset)
- the struct will have an *is a* relationship with the interface

In a [[nominative type system]] (like java), you have to specify the type of the class or interface when creating an object.