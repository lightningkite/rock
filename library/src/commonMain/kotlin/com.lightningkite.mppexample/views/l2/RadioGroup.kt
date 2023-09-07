package com.lightningkite.mppexample

@ViewDsl
fun <T> ViewContext.radioGroup(
    options: ReactiveScope.() -> List<T>,
    value: Writable<T>,
    getKey: (T) -> String,
    getLabel: (T) -> String,
) {
    val innerProp = Property(getKey(value.once))

    reactiveScope {
        val current = innerProp.current
        val currentGeneric = options().find { getKey(it) == current }
        if (currentGeneric != null)
            value set currentGeneric
    }

    forEach(
        data = {options()},
        render = { option ->
            row {
                gravity = RowGravity.Center
                nativeRadio {
                    bind(
                        prop = innerProp,
                        value = getKey(option)
                    )
                    activeColor = theme.primary.background.closestColor()
                } in margin(right = 4.px)
                text(getLabel(option)) in clickable { innerProp set getKey(option) }
            } in margin(vertical = 4.px)
        }
    ) in margin(vertical = 2.px)
}
