package u.skripts.notes

import java.io.File
import u.skripts.helpers.getMandatoryEnv
import u.skripts.notes.builders.ObsidianURIBuilder.Companion.obsidianURI
import u.skripts.notes.service.Vault

/**
 * DESCRIPTION:
 *   A script to open/create a new note in Obsidian
 *   based on my personal note-taking system, u-notes...
 *
 * USAGE:
 *   kotlinc -script homeFiles/scripts/u-notes.kts -- {{LOCATION}} {{FILENAME}} {{TEMPLATE}}
 *
 *   LOCATION: The Johnny.Decimal location of the note to open/create
 *   FILENAME: The name of the file to create
 *   TEMPLATE: The template to use when creating the file
 *
 * EXAMPLE:
 *   kotlinc -script homeFiles/scripts/u-notes.kts -- 52 HIRO-5766 issue
 *
 * TIPS:
 *   It would be desirable to set an alias for this script, e.g.:
 *   alias u-notes="kotlinc -script homeFiles/scripts/u-notes.kts --"
 *
 * See:
 * - https://obsidian.md/
 * - https://johnnydecimal.com/
 */
@Suppress(
    "ReplaceSizeCheckWithIsNotEmpty",
    "LocalVariableName",
)
object Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        // If enabled, the script will print out the file path only instead of opening Obsidian
        var PATH_ONLY = false

        val VAULT_NAME = getMandatoryEnv("DEFAULT_OBSIDIAN_VAULT_NAME")
        val VAULT_PATH = getMandatoryEnv("DEFAULT_OBSIDIAN_VAULT_PATH")

        var LOCATION: String? = null
        var FILENAME: String? = null
        var TEMPLATE: String? = null

        // Collect vault arguments whilst enabling/dropping the path flag if provided
        val options = args
            .toList()
            .takeUnless { it.isNotEmpty() && it.first().lowercase() == "-p" }
            ?: args.toList().drop(1).apply { PATH_ONLY = true }

        if (options.size >= 1) options[0].apply { LOCATION = this }
        if (options.size >= 2) options[1].apply { FILENAME = this }
        if (options.size >= 3) options[2].apply { TEMPLATE = this }

        Vault(VAULT_PATH)
            .file(LOCATION, FILENAME, TEMPLATE)
            .also { file: File ->
                if (PATH_ONLY)
                    print(file.absolutePath)
                else obsidianURI()
                    .withVault(VAULT_NAME)
                    .withPath(file.absolutePath)
                    .build()
                    .run(::print)
            }
    }
}
