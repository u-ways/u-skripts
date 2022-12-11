package u.skripts.notes.model

import java.io.File

sealed interface JohnnyDecimal: FileResolver {
    val location: String
    fun entry(root: String): File
}