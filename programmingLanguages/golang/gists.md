Doug McIlroy, the eventual inventor of Unix pipes, wrote in 1964 (!):
"We should have some ways of coupling programs like garden hose--screw in another segment when it becomes necessary to massage data in another way. This is the way of IO also."

(Go) is a language of composition and coupling.
1. interfaces give us the composition of components. It doesn't matter what that thing is, if it implements method M I can just drop it in here.
2. concurrency gives us the composition of independently executing computations.
3. there's even an unusual (and very simple) form of type composition: embedding.


"Programming in the large"
Go was designed to help write big programs, written and maintained by big teams.
Big software needs methodology to be sure, but not nearly as much as it needs strong dependency management and clean interface abstraction and superb documentation tools.


Go and C++ are profoundly different philosophically.
C++ is about having it all there at your fingertips.
"The range of abstractions that C++ can express elegantly, flexibly, and at zero costs compared to hand-crafted specialized code has greatly increased."
That way of thinking just isn't the way Go operates. Zero cost isn't a goal, at least not zero CPU cost. Go's claim is that minimizing programmer effort is a more important consideration.
Go gives a set of powerful but easy to understand, easy to use building blocks from which you can assemble—compose—a solution to your problem. It might not end up quite as fast or as sophisticated or as ideologically motivated as the solution you'd write in some of those other languages, but it'll almost certainly be easier to write, easier to read, easier to understand, easier to maintain, and maybe safer.

Go provides much of the expressiveness of languages like Python and Ruby with some of the performance and concurrency of languages like C++.

Go doesn't give the 'precise control over every nuance of execution' like C++.



##### Resources
1. https://commandcenter.blogspot.com/2012/06/less-is-exponentially-more.html 