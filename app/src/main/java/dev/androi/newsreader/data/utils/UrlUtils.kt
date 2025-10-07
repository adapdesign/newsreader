package dev.androi.newsreader.data.utils
import java.net.URI

fun String.addDefaultHttpsScheme(): String {
    val trimmed = trim()
    if (trimmed.isEmpty()) return trimmed

    // Fast check for common schemes
    val lower = trimmed.lowercase()
    val hasScheme = lower.startsWith("http://")
            || lower.startsWith("https://")
            || lower.startsWith("ftp://")
            || lower.startsWith("file://")
            || lower.startsWith("mailto:")
            || lower.startsWith("ws://")
            || lower.startsWith("wss://")

    if (hasScheme) return trimmed

    // If it already parses as a URI with a scheme, keep it
    return try {
        val uri = URI(trimmed)
        if (uri.scheme != null) trimmed else "https://$trimmed"
    } catch (e: Exception) {
        // If parsing fails, still prepend https to give a usable URL
        "https://$trimmed"
    }
}