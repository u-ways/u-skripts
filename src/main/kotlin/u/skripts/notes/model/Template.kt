package u.skripts.notes.model

/**
 * A [Template] is a file that is used as a template for a [u.skripts.notes.model.Category]
 * or [u.skripts.notes.model.Bucket].
 *
 * @see [u.skripts.notes.model.CommonTemplate]
 *
 * IDEA:
 *  - Add a decorator method to populate common template placeholders.
 *  - i.e. Given "{{date}} {{title}}" will translate to "2021-01-01 My Title"
 */
interface Template {
    fun filename(): String
}