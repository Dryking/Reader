package com.dryking.reader.core.ao3

/** Builds and recognises AO3 URLs. */
object Ao3Urls {
    const val BASE = "https://archiveofourown.org"

    private val hosts = setOf("archiveofourown.org", "www.archiveofourown.org", "ao3.org", "www.ao3.org")
    private val workUrl = Regex("""^https?://([^/]+)/works/(\d+)(?:/chapters/(\d+))?(?:[/?#].*)?$""")

    fun work(workId: Long, viewAdult: Boolean = false): String =
        "$BASE/works/$workId" + if (viewAdult) "?view_adult=true" else ""

    fun fullWork(workId: Long, viewAdult: Boolean = false): String =
        "$BASE/works/$workId?view_full_work=true" + if (viewAdult) "&view_adult=true" else ""

    fun chapter(workId: Long, chapterId: Long, viewAdult: Boolean = false): String =
        "$BASE/works/$workId/chapters/$chapterId" + if (viewAdult) "?view_adult=true" else ""

    /** Official single-file HTML download of a whole work (one request instead of one per chapter). */
    fun htmlDownload(workId: Long): String = "$BASE/downloads/$workId/work.html"

    /** Parses a work (and optional chapter) out of an AO3 link, or returns null if it isn't one. */
    fun parseWorkLink(url: String): WorkLink? {
        val match = workUrl.matchEntire(url.trim()) ?: return null
        val (host, workId, chapterId) = match.destructured
        if (host.lowercase() !in hosts) return null
        return WorkLink(workId.toLong(), chapterId.toLongOrNull())
    }
}

data class WorkLink(val workId: Long, val chapterId: Long?)
