package u.skripts.helpers

/** Get an environment variable, or throw an ISE if it is not set... */
internal fun getMandatoryEnv(key: String): String =
    System.getenv(key) ?: error("The \$$key environment variable is not defined...")

// FIXME: Actually this might not be needed if you encode the WHOLE url using the URI encoder...
/** My own implementation of URLEncoder as I need to encode every reserved character. */
internal fun encode(url: String) = mapOf(
    "%" to "%25", // Must be first
    " " to "%20",
    "!" to "%21",
    "#" to "%23",
    "$" to "%24",
    "&" to "%26",
    "'" to "%27",
    "(" to "%28",
    ")" to "%29",
    "*" to "%2A",
    "+" to "%2B",
    "," to "%2C",
    "/" to "%2F",
    ":" to "%3A",
    ";" to "%3B",
    "=" to "%3D",
    "?" to "%3F",
    "@" to "%40",
    "[" to "%5B",
    "]" to "%5D"
).entries.fold(url) { acc, (key, value) -> acc.replace(key, value) }
