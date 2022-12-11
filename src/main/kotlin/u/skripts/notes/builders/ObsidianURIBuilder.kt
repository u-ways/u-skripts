package u.skripts.notes.builders

import u.skripts.helpers.encode

class ObsidianURIBuilder private constructor() {
    private lateinit var vault: String
    private lateinit var path: String

    fun withVault(vault: String) = apply { this.vault = vault }
    fun withPath(path: String) = apply { this.path = path }

    fun build() = "obsidian://open?vault=$vault&path=${encode(path)}"

    companion object {
        fun obsidianURI() = ObsidianURIBuilder()
    }
}