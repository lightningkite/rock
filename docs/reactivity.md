# Advanced Reactivity

When calling `.await()` in a reactive scope, here's what happens:
- A listener to the changing item is added, but blocked from running
- The value is retrieved
- The block from running is removed

Thus, `await()`ing something will cause the value to be retrieved and only then will subsequent changes to recall the scope.
