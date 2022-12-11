package u.skripts.notes.model

/**
 * Commonly used templates for [u.skripts.notes.model.Category]s
 * and [u.skripts.notes.model.Bucket]s.
 */
enum class CommonTemplate(private val filename: String): Template {
    DEFAULT("Note"),
    DAILY("Daily"),
    ISSUE("Issue"),
    CODE("Code"),
    PEOPLE("People");

    override fun filename(): String = "$filename.md"
}
