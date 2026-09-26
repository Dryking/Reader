package com.dryking.reader.core.model

/** One chapter of a [Work]. HTML fields hold AO3's markup for that section. */
data class Chapter(
    val workId: Long,
    val chapterId: Long,
    /** 1-based position in the work. */
    val index: Int,
    val title: String?,
    val summaryHtml: String?,
    val notesHtml: String?,
    val bodyHtml: String,
    val endNotesHtml: String?,
)
