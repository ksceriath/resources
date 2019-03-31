Variable definitions
====================

MOVE
When a MOVE happens, the ownership of the underlying heap memory gets transferred. It can happen in three ways:

(a) Assignment
    let x = String::new("hello");
    let y = x;
    // ownership of string memory is transferred from x to y
    // x is a variable which lives on stack and contains pointer to a memory on heap among other things. The copy copies these values directly, and hence the pointer is copied as such.
(b) Function Call
    let x = String::new("hello");
    some_function(x);
    // ownership of the string is transferrd from x to the corresponding parameter in some_function
(c) Function Return
    fn some_function() -> String {
        let x = String::new("hello");
        return x
    }
    // ownership of the string is transferred from x to the variable accepting the return value in the caller of some_function

COPY
A COPY happens during assignment whenever the underlying type implements COPY trait. This mostly involves types which occupy memory only on stack.

REFERENCE
A REFERENCE is like a pointer to the variable on stack. It can be an immutable or a mutable reference, which would define if it is allowed to modify the underlying memory on the heap.




let s = "hello";
>> defines a string literal "hello"
>> defines s as immutable reference to the literal

let s = String::from("hello");
>> allocates a string "hello" on heap
>> defines s as an immutable reference to that memory

let mut s = String::from("hello");
>> allocates a string "hello" on heap
>> defines s as a mutable reference to that memory

let s1 = String::from("hello");
let s2 = s1;
>> allocates a string "hello" on heap
>> defines s1 as immutable reference to that memory
>> moves the value of s1 to s2; s1 no longer owns that memory

let s1 = String::from("hello");
let s2 = s1.clone()
>> this is same as before, but copies the memory on the heap also to new location; both s1 and s2 are owners of their respective heap memories.

let s1 = 5;
let s2 = s1;
>> integer 5 lives in stack memory, so the memory is copied and not moved in this case.

let s1 = String::from("hello");
let s2 = &s1;
>> allocates "hello" on heap
>> s1 owns that memory, is immutable etc
>> s2 borrows that memory immutably

let mut s1 = String::from("hello");
let s2 = &mut s1;
>> allocates "hello" on heap
>> s1 owns that memory, and is mutable
>> s2 borrows that memory mutably


let mut s = ....
>> you can reassign s to something
>> you can modify value assigned to s, when it owns it
