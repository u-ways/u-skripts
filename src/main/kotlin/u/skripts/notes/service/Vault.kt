package u.skripts.notes.service

import java.io.File
import java.time.LocalDate
import u.skripts.notes.model.Bucket
import u.skripts.notes.model.Category
import u.skripts.notes.model.CustomTemplate
import u.skripts.notes.repository.Buckets

/** File processing service for the vault's notes. */
internal class Vault(private val root: String) {
    fun file(location: String?, filename: String?, template: String?): File = when (location) {
        Buckets.index().location, null -> Buckets.index().entry(root)
        Buckets.etc().location -> etcBucketProcessor(filename)
        else -> johnnyDecimalBucketProcessor(location, filename, template)
    }

    // The daily's bucket only accepts current and past dates as filenames
    private fun etcBucketProcessor(filename: String?): File {
        return when (filename) {
            null -> Buckets.etc().entry(root)
            else -> filename
                .runCatching {
                    LocalDate
                        .parse(this)
                        .isBefore(LocalDate.now().plusDays(1))
                }.getOrElse {
                    error("Filename provided for ${Buckets.etc().location} is not a date: $filename")
                }
                .let { notAFutureDate ->
                    if (notAFutureDate) Buckets.etc().resolve(root, filename)
                    else error("Only past or today's daily note can be opened/created in ${Buckets.etc().location}...")
                }
        }
    }

    // Derives whether it's opening a Bucket or a Category and then behaves accordingly
    private fun johnnyDecimalBucketProcessor(location: String, filename: String?, template: String?): File {
        val johnnyDecimal = location
            .runCatching(String::toInt)
            .getOrElse { error("Location is not a JohnnyDecimal address: $location") }
            .apply { check(this < 100) { "Address is larger than 100, which is an invalid JohnnyDecimal value: $location" } }

        return when {
            johnnyDecimal % 10 == 0 -> Buckets.find<Bucket>(location).entry(root)
            else -> Buckets.find<Category>(location).let { category ->
                when (filename) {
                    "", null -> category.entry(root)
                    else -> when (template) {
                        "", null -> category.resolve(root, filename)
                        else -> category.resolve(root, filename, CustomTemplate(root, Buckets.template().location, template))
                    }
                }
            }
        }
    }
}