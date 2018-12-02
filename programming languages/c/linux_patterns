[ Source : https://lwn.net/Articles/444910/ ]

Method dispatch
===============
Method dispatch is associated with object oriented programming paradigm. Specifically, Method Dispatch refers to the algorithm that is used to determine which method should be invoked during runtime based on the call.

In linux kernal written in C, we see several places where OOP paradigms are utilised. The C programming language has no intrinsic support for objects, thus it is left for the developers to work out the basic frameworks that are required for OOP.

The above article talks about Method Dispatch in particular as it used in the linux kernal code.

An object is basically some state (data) and some behaviour (methods). In OOP languages, classes provide these features in the form of member variables and methods.

In C, the same can be implemented using structs. However, structs are just data. C also provides function pointers, which are basically pointers to a function definition. Structs with function pointers can mimic to some extent the behaviour provided by OOP objects.

A function pointer is defined as such:

[ Source: https://www.geeksforgeeks.org/function-pointer-in-c/ ]

[function definition]   :   void fun (int a) { printf("%d", a); }

[function pointer]      :   void (*fun_ptr)(int) = &fun;

[usage]                 :   (*fun_ptr)(10)

Thus, a typical struct can be defined as follows :

struct Point {
    int a;
    int b;
    void (*fun_ptr)(int);
}

The fun_ptr provides functionality that we would put inside an equivalent class method.

It is customary that the function pointer accepts the corresponding struct type as the first argument. This is in effect same as what some other languages provide as method receivers (e.g. golang), or pointers to the self objects (e.g. this in java, etc)

---------------------------

There are three different patterns which are interesting, in the linux kernal, as mentioned in the above article.

1. structs containing the direct functions

This is similar to the example above, and is the simplest case. In practice, this is used at places when there are very few method pointers, or when method definition needs to be individually tailored to the object definition.

Using this, would have every instance of the structure contain these functions.

Example:

struct Point {
    void (*scale)(*Point, int);
    int x;
    int y;
}

2. using vtables

It is more common for the structures to contain reference to a separate operations struct, which defines a bunch of method for the structure.

This way, multiple instances of the same struct could point to the same functions definitions.

Example:

struct Point {
    Point_operations *operations;
    int x;
    int y;
}

struct Point_operations {
    void (*scale)(*Point, int);
    void (*invert)(*Point);
}

These operations structures are typically called vtables, or virtual tables, or virtual function tables.

Sometimes the methods can appear in a separate vtable (call it : mixin vtable) [dig up on mixins], which are not present in the base type of the object. These are used to collect the functaionality which operates on the object, but may not be directly linked to the object so as to be placed with other methods.

As example, we can have:

struct Point_extended_operations {
    void (*someRandomTransformation)(*Point);
}

As a practical example from the article:
"both tcp_congestion_ops and inet_connection_sock_af_ops operate (primarily) on a struct sock, which itself has already got a small set of dedicated operations."


3. vtable of the parent

Sometimes the functionality corresponding to an object may be defined inside the vtable of its parent structure.

Doing so primarily helps reduce the size of the structure which might already be overpopulous.

[The parent might also want to provide an abstract functionality which could act on its multiple children. However, I have not really thought this hard, and I'm not really sure how this would be done.]

An example just for the sake of completion:

struct Rectangle {
    RectangleOperations *operations;
    Point* lowerLeft;
    Point* lowerRight;
    Point* topLeft;
    Point* topRight;
}

struct RectangleOperations {
    int (*area)(*Rectangle);
    void (*doSomethingToAPoint)(*Point);
}

--------------------------------
 
Additionaly, the vtables can rarely contain additional fields needed to "register the object class"... can read up more on this at a later point when some related work is picked up seriously.