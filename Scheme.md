
# Scheme syntax

#### define
```scheme
; function
(define (f arg) (body))

; identifier
(define identifier (value))
```

#### nil
```scheme
(define nil '())

; `nil` may not be defined in some Lisp languages, so an empty list is more generally accepted to represent the value.
```
 


#### cons
```scheme
(cons item lizt)
```

#### car
```scheme
(car lizt)
```

#### cdr
```scheme
(cdr lizt)
```

#### list
```scheme
(list item item item)
```

#### if
```scheme
(if (condition)
    (then-expr)
    (else-expr)
)
```

#### cond
```scheme
(cond ((condition1) condition1-expr)
	  ((condition2) condition2-expr)
	  (else (else-expr))
)
```

#### lambda
```scheme
(lambda (arg) (definition))
```

#### append
```scheme
(append lizt1 lizt2)
```

#### let
```scheme
(let ((identifier value)) (body)))
```
