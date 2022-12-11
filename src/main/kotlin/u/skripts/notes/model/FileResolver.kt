package u.skripts.notes.model

import java.io.File

/**
 * A [FileResolver] is an interface to allow [Category]s and [Bucket]s to
 * resolve their children files within a [u.skripts.notes.service.Vault]
 * based on their own structure and rules.
 */
fun interface FileResolver {
    fun resolve(vaultPath: String, filename: String): File = resolve(vaultPath, filename, null)
    fun resolve(vaultPath: String, filename: String, override: Template?): File
}