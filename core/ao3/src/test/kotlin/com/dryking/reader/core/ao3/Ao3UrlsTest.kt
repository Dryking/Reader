package com.dryking.reader.core.ao3

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class Ao3UrlsTest {

    @Test
    fun parsesWorkLink() {
        assertEquals(WorkLink(123456, null), Ao3Urls.parseWorkLink("https://archiveofourown.org/works/123456"))
    }

    @Test
    fun parsesChapterLinkWithQueryAndFragment() {
        assertEquals(
            WorkLink(123456, 789),
            Ao3Urls.parseWorkLink("https://archiveofourown.org/works/123456/chapters/789?view_adult=true#workskin"),
        )
    }

    @Test
    fun acceptsAlternateHosts() {
        assertEquals(WorkLink(42, null), Ao3Urls.parseWorkLink("http://www.ao3.org/works/42/"))
    }

    @Test
    fun rejectsOtherSitesAndPages() {
        assertNull(Ao3Urls.parseWorkLink("https://example.com/works/123"))
        assertNull(Ao3Urls.parseWorkLink("https://archiveofourown.org/tags/Fluff/works"))
        assertNull(Ao3Urls.parseWorkLink("https://archiveofourown.org/works/abc"))
    }

    @Test
    fun buildsUrls() {
        assertEquals("https://archiveofourown.org/works/1?view_full_work=true&view_adult=true", Ao3Urls.fullWork(1, viewAdult = true))
        assertEquals("https://archiveofourown.org/works/1/chapters/2", Ao3Urls.chapter(1, 2))
    }
}
