package u.skripts.notes.model

import java.io.File

/**
 * A [Bucket] is a collection of [Category]s in a [u.skripts.notes.service.Vault].
 * - A bucket has a singular file (i.e. [entry]) that describes the bucket.
 * - A bucket cannot contain more than one file.
 * - A bucket can contain up to 9 categories. (i.e. directories)
 *
 * NOTE:
 *   Certain buckets are special and have different structure which allows
 *   them to have more than one entry point. (i.e. more than one file...)
 *   For example: [u.skripts.notes.repository.Buckets.ETC_BUCKET]
 */
internal data class Bucket(
    override val location: String,
    private val entry: String,
    internal val categories: List<Category> = emptyList(),
    internal val fileResolver: FileResolver = FILE_RESOLVER_UNSUPPORTED,
) : JohnnyDecimal {
    override fun entry(root: String) = File("$root/$location/$entry.md")
    override fun resolve(vaultPath: String, filename: String, override: Template?) =
        fileResolver.resolve(vaultPath, filename, override)

    /**
     * We assume the user will give the Category's directory name as address only.
     * (i.e. 11 when they're looking for 10/11)
     */
    internal fun find(location: String): Category? = categories
        .find { category -> category.location == "${this.location}/$location" }

    companion object {
        val FILE_RESOLVER_UNSUPPORTED = FileResolver { _, _, _ -> error("This bucket does not support resolving files as it has a single entry file only.") }
    }
}