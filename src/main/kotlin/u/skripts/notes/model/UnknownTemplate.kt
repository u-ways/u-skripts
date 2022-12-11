package u.skripts.notes.model

import java.io.File

/**
 * This data class is used to encapsulate a template that is not known
 * to the application. It is used to allow the user to specify custom templates.
 */
data class CustomTemplate(
    private val root: String,
    private val location: String,
    private val filename: String,
): Template {
    init {
        with(File("$root/$location/${filename()}")) {
            check(exists()) { "Custom Template provided, does not exist: $absolutePath" }
        }
    }
    override fun filename(): String = "$filename.md"
}
