# Reader: plan for an AO3 Android app

Status: draft for review. No code yet.

## 1. Goal

A native Android app for reading fanfiction on [Archive of Our Own](https://archiveofourown.org) (AO3). It should feel faster and more comfortable than the mobile website: a clean reader, an offline library, and alerts when a saved work gets a new chapter.

## 2. AO3 constraints that shape the design

| Constraint | Consequence |
|---|---|
| AO3 has **no public API**. | We scrape HTML with Jsoup. All parsing sits in one module with fixture-based tests, so a site redesign breaks one layer and nothing else. |
| AO3 runs on volunteer servers and rate-limits clients (HTTP 429 + `Retry-After`). Its ToS allows personal-use access but not heavy crawling. | One global rate limiter (about 1 request/sec, honours `Retry-After`), aggressive caching, a descriptive `User-Agent`, and no background bulk crawling. |
| AO3 offers official downloads (`/downloads/{id}/{title}.epub` and `.html`). | For offline saving, we fetch the official HTML download: **one request per work** instead of one per chapter. |
| Content warnings: adult works show an interstitial page. | Send `view_adult=true` after the user confirms once in settings (off by default). |
| Whole works can be fetched in one page (`?view_full_work=true`). | Online reading uses this where it's reasonable, then splits the page into chapters locally. |
| Login uses a Rails form with a CSRF `authenticity_token` and session cookies. | Optional login with a persistent cookie jar. The password is never stored; only the session cookie, kept in EncryptedSharedPreferences. |
| Personal use only. | APK built by GitHub Actions and sideloaded; no store listing. |

## 3. Features by phase

### Phase 1: MVP (anonymous, read and save)
- **Browse and search**: work search with AO3's filters (fandom, rating, warnings, category, complete/WIP, word count, language, sort order); tag and fandom pages; paginated results.
- **Work page**: title, author(s), summary, tags, stats (words, chapters, kudos, hits, date updated), series links.
- **Reader**:
  - chapter list and next/previous navigation;
  - adjustable font, size, line spacing, margins and theme (light, sepia, dark, AMOLED black);
  - author notes can be collapsed;
  - reading position is saved per chapter and restored.
- **Library**:
  - save a work for offline reading via the official HTML download;
  - show progress per work;
  - sort and filter (unread, WIP, complete, updated).
- **Update checks**: a periodic WorkManager job (every 6 h by default, Wi-Fi-only option) compares chapter counts of saved WIPs, marks updated works, and sends a notification.
- **Share and open links**: an intent filter for `archiveofourown.org/works/*` links; share a work's URL.

### Phase 2: account features (optional login)
- Log in and log out.
- Leave kudos.
- Bookmarks: view and create them, with notes and tags.
- Marked for Later.
- Subscriptions, with import into the library.
- Reading history sync.
- Read comments, then post them.

### Phase 3: polish
- Export and import the library as JSON.
- EPUB export of a saved work.
- Text-to-speech.
- Paged (book-style) reading mode.
- Tablet two-pane layout.
- Filter out works by tag (e.g. hide crossovers or specific warnings).

## 4. Tech stack

| Area | Choice | Why |
|---|---|---|
| Language/UI | Kotlin, Jetpack Compose, Material 3 | Current Android standard, and theming is simple. |
| Architecture | Single activity, MVVM + unidirectional data flow, Navigation Compose | Standard and easy to test. |
| DI | Hilt | |
| Networking | OkHttp (cookie jar, rate-limit interceptor, cache) | |
| Parsing | Jsoup | Robust HTML parsing. |
| Persistence | Room (library, chapters, progress), DataStore (settings) | |
| Paging | Paging 3 for search and tag listings | |
| Background | WorkManager | Update checks. |
| Images | Coil | For the rare embedded images. |
| Chapter rendering | `WebView` with our own CSS, JavaScript disabled for work content | AO3 chapter HTML includes tables, lists, and author work skins; a WebView renders these faithfully. Scroll position is reported via a small bridge. |
| Build | Gradle Kotlin DSL, version catalog, minSdk 26, targetSdk latest | |
| CI | GitHub Actions: lint, unit tests, debug APK artifact | |

## 5. Module layout

```
:app                 – Activity, navigation graph, theme, DI wiring
:core:model          – plain Kotlin data classes (Work, Chapter, Tag, SearchQuery…)
:core:ao3            – AO3 client: HTTP, rate limiter, Jsoup parsers, login
:core:database       – Room entities/DAOs
:core:data           – repositories combining ao3 + database (offline-first)
:feature:browse      – search, filters, tag pages
:feature:work        – work details
:feature:reader      – chapter reader + reader settings
:feature:library     – saved works, progress, updates
:feature:settings
```

Parsers are pure functions (`Document -> Model`). Their unit tests run against saved AO3 HTML fixtures in `core/ao3/src/test/resources`.

## 6. Data model (core)

- `Work(id, title, authors, fandoms, rating, warnings, categories, relationships, characters, freeforms, summaryHtml, language, words, chapterCount, expectedChapters, kudos, hits, bookmarks, comments, published, updated, isComplete, seriesRefs)`
- `Chapter(workId, chapterId, index, title, notesHtml, summaryHtml, bodyHtml, endNotesHtml)`
- `LibraryEntry(workId, addedAt, lastReadChapter, scrollFraction, lastReadAt, knownChapterCount, hasUpdate, isDownloaded)`

## 7. Milestones

1. **Skeleton**: Gradle project, modules, theme, empty navigation, and CI building a debug APK.
2. **AO3 client**: HTTP layer, rate limiter, and parsers for the work page, chapters and search results, with fixture tests.
3. **Browse, search and work details** screens.
4. **Reader** with settings and saved position.
5. **Library and offline save**.
6. **Update-check worker and notifications**, plus deep links.
7. MVP release (signed APK built by CI, installed on the owner's phone).
8. Phase 2 (login and account features).

## 8. Risks

- **Site changes break parsing.** Mitigations: parsers are isolated, fixture tests catch breakage, and failures show a clear "open in browser" fallback.
- **Rate limiting or blocking.** Mitigations: the global limiter, caching, and no bulk crawling.
- **Cloudflare or challenge pages.** Detect them, then fall back to an in-app WebView where the user completes the challenge, and share its cookies with OkHttp.

## 9. Decisions

1. **Login**: not in the MVP; it comes in Phase 2.
2. **Distribution**: personal use only. GitHub Actions builds the APK, and it is installed directly on the phone (sideloaded). There is no Play Store listing.
3. **Package name**: `com.dryking.reader`.
4. **minSdk**: 26. The owner's phone runs One UI 8.5, which is Android 16, so it is well above that.
5. **UI**: follow the owner's existing Claude Design mockups when building the screens (milestone 3 onward).
