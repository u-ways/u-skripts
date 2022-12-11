package u.skripts.notes.repository

import java.io.File
import java.time.Instant
import u.skripts.notes.model.Bucket
import u.skripts.notes.model.Category
import u.skripts.notes.model.CommonTemplate.CODE
import u.skripts.notes.model.CommonTemplate.DAILY
import u.skripts.notes.model.CommonTemplate.ISSUE
import u.skripts.notes.model.CommonTemplate.PEOPLE
import u.skripts.notes.model.JohnnyDecimal

/** Buckets repository with cached map of buckets and categories */
internal object Buckets {
    private const val INDEX_ID = ""
    private const val ETC_ID = "_"
    private const val TEMPLATE_ID = "00/02"

    /** The Vault's entry point */
    private val INDEX_BUCKET = Bucket(
        INDEX_ID, "Index"
    )

    /** The Vault's scratch, daily, and etc notes  */
    private val ETC_BUCKET = Bucket(
        ETC_ID,
        // Entry is the current daily note, e.g. "2023-01-01"
        "${Instant.now()}".substringBefore("T"),
    ) { vaultPath, filename, _ ->
        filename
            .removeSuffix(".md")
            .run { File("$vaultPath/$ETC_ID/$this.md") }
            .let { file ->
                if (file.exists()) file
                else file.apply {
                    this.runCatching { createNewFile() }
                        .getOrElse { error("Failed to create file: $absolutePath") }
                    this.writeText(readTemplate(vaultPath, DAILY.filename()))
                }
            }
    }

    /** The Vault's user-defined template's location */
    private val TEMPLATE_CATEGORY = Category(
        TEMPLATE_ID,
        "Templates"
    ) { vaultPath, filename, _ ->
        filename
            .removeSuffix(".md")
            .run { File("$vaultPath/$TEMPLATE_ID/$this.md") }
            .let { file -> if (file.exists()) file else file.apply { check(file.createNewFile()) } }
    }

    // FIXME: Move to a private sub-module and make it a singleton so this won't be publicly accessible...
    /** Currently this repository only contain cached buckets until further complexity is needed. */
    private val CACHED_BUCKETS = mapOf(
        INDEX_ID to INDEX_BUCKET,
        ETC_ID to ETC_BUCKET,
        "00" to Bucket(
            "00", "00 - System Meta", listOf(
                Category("00/01", "Obsidian"),
                TEMPLATE_CATEGORY,
                Category("00/03", "Scripts"),
            )
        ),
        "10" to Bucket(
            "10", "10 - Life Quarters", listOf(
                Category("10/11", "Strategies"),
                Category("10/12", "Kanban Boards", ISSUE),
                Category("10/13", "People", PEOPLE),
            )
        ),
        "20" to Bucket(
            "20", "20 - Computer Science", listOf(
                Category("20/21", "Discrete & Statistical Mathematics"),
                Category("20/22", "Theory of computation & Compilers"),
                Category("20/23", "Algorithm Data Structures & Computational Complexity"),
                Category("20/24", "Operating Systems & architecture"),
                Category("20/25", "Artificial Intelligence & Machine Learning"),
                Category("20/26", "Computer Networks & Security"),
            )
        ),
        "30" to Bucket(
            "30", "30 - Software Engineering", listOf(
                Category("30/31", "Theory"),
                Category("30/32", "Tools"),
                Category("30/33", "10x", CODE),
            )
        ),
        "40" to Bucket(
            "40", "40 - Personal Work", listOf(
                Category("40/41", "Management"),
                Category("40/42", "Domain"),
                Category("40/43", "Projects"),
            )
        ),
        "50" to Bucket(
            "50", "50 - Contracted Work", listOf(
                Category("50/51", "Management"),
                Category("50/52", "Domain"),
                Category("50/53", "Projects"),
            )
        ),
    )

    /**
     * Finds a Bucket or Category (depending on the type supplied) by its location.
     *  - Bucket locations are a multiple of 10 that are smaller than 100, e.g. "00", "10", "20", etc.
     *  - Category locations are a 2-digit number that aren't a multiple of 10, e.g. "01", "02", "03", etc.
     *
     * @param location The location of the Bucket or Category to find.
     * @return The Bucket or Category found.
     */
    @JvmStatic
    internal inline fun <reified T : JohnnyDecimal> find(location: String) = when {
        INDEX_BUCKET.location == location -> INDEX_BUCKET
        ETC_BUCKET.location == location -> ETC_BUCKET
        else -> when (location.toIntOrNull()) {
            null -> error("Invalid location: $location")
            else -> when (T::class) {
                Bucket::class -> CACHED_BUCKETS[location]
                    ?: error("Bucket not found: $location")
                Category::class -> CACHED_BUCKETS[location.first().plus("0")]
                    ?.find(location)
                    ?: error("Category not found: $location")
                else -> error("Invalid type: ${T::class}")
            }
        }
    }

    /** Reads the template file from the vault if it exists. */
    @JvmStatic
    internal fun readTemplate(vaultPath: String, filename: String) =
        File("$vaultPath/${template().location}/$filename")
            .apply { check(exists()) { "Template not found: $absolutePath" } }
            .readText()

    @JvmStatic internal fun index() =
        INDEX_BUCKET

    @JvmStatic internal fun etc() =
        ETC_BUCKET

    @JvmStatic internal fun template() =
        TEMPLATE_CATEGORY
}

