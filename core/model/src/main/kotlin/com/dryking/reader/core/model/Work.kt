package com.dryking.reader.core.model

import java.time.LocalDate

/** A work (story) on AO3, as shown on its work page or in a listing. */
data class Work(
    val id: Long,
    val title: String,
    val authors: List<String>,
    val fandoms: List<String>,
    val rating: Rating,
    val warnings: List<String>,
    val categories: List<String>,
    val relationships: List<String>,
    val characters: List<String>,
    val freeforms: List<String>,
    val summaryHtml: String?,
    val language: String,
    val words: Int,
    val chapterCount: Int,
    /** Total chapters the author has announced, or null when AO3 shows "?". */
    val expectedChapters: Int?,
    val kudos: Int,
    val hits: Int,
    val bookmarks: Int,
    val comments: Int,
    val published: LocalDate,
    val updated: LocalDate,
    val series: List<SeriesRef>,
) {
    val isComplete: Boolean get() = expectedChapters != null && chapterCount >= expectedChapters
}

data class SeriesRef(
    val id: Long,
    val title: String,
    val position: Int,
)

enum class Rating(val label: String) {
    NotRated("Not Rated"),
    General("General Audiences"),
    Teen("Teen And Up Audiences"),
    Mature("Mature"),
    Explicit("Explicit"),
}
