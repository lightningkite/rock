
import kotlin.reflect.KClass

actual fun KClass<*>.approximateSimpleName() = simpleName ?: "???"