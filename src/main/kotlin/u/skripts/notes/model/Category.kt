package u.skripts.notes.model

import java.io.File
import u.skripts.notes.model.CommonTemplate.DEFAULT
import u.skripts.notes.repository.Buckets

/**
 * A [Category] is a collection of notes in a [u.skripts.notes.service.Vault].
 *
 * - A category has a singular file (i.e. [entry]) that describes the category.
 * - A category can have multiple files (i.e. user notes...)
 * - A category can have a custom [template] in which is used to create new notes.
 */
internal data class Category(
    override val location: String,
    private val entry: String,
    private val template: CommonTemplate = DEFAULT,
    private val fileResolver: FileResolver = FileResolver { vaultPath, filename, override ->
        filename
            .removeSuffix(".md")
            .run { File("$vaultPath/$location/$this.md") }
            .let { file ->
                if (file.exists()) file
                else file.apply {
                    this.runCatching { createNewFile() }
                        .getOrElse { error("Failed to create file: $absolutePath") }
                    this.writeText(Buckets.readTemplate(vaultPath, (override ?: template).filename()))
                }
            }
    },
): JohnnyDecimal {
    override fun entry(root: String) = File("$root/$location/$entry.md")
    override fun resolve(vaultPath: String, filename: String, override: Template?) =
        fileResolver.resolve(vaultPath, filename, override)
}