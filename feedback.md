# Feedback

- Spacing seems to apply at the whole containerview layer.  A default spacing is sane; but individual containers have their own spacing rules.
- Labels should have feedback spaces
- A feedback theme makes sense

----

## Layouts and Themes

- Nesting layout sizing?  Need to find a specific instant where "nested weights are required"
- An individual element's theme twist (for highlighting purposes)
- "staleTheme"
- Exceptions to the theme kinda suck to implement
- Expanding views don't fully expand
- *** Looks like you can use view creation/writing in reactive scopes, but you can't.
- *** Text sizes in theme or titles with out title font?
- Gravity shortcut
- Weight by prefix instead of suffix?
- `weight(1f)` shortcut

## Reactivity

- Sockets are kinda funky
- `::{}` syntax is stupid about importing
- Performance hit from async?
- Just freaking use KotlinX Coroutines

## Navigation

- Making the top navigator static/public is basically inevitable

## Web Style

- CSS issues with size
- Optimize view reuse