### [Click for Live Demo](https://appetize.io/app/b_i23l5equ57sbyz3kc63zoxykha)

## Screenshots
<p float="left">
<img src="https://github.com/adapdesign/newsreader/blob/main/images/app.png" alt="Alt text" width="300" />
<img src="https://github.com/adapdesign/newsreader/blob/main/images/article.png" alt="Alt text" width="300" />
</p>

## Building NewsReader — a developer’s story

It began as a weekend itch: build something useful that demonstrated end‑to‑end engineering — networking, offline, sync, and a little personalization. The idea settled on a compact goal: an offline‑first NewsReader, lightweight personalization, and a clean Android + web surface to show decisions and trade‑offs.

### Day 1 — Project Skeleton

I started by scaffolding the Android app in Kotlin with Jetpack Compose. That choice bought modern UI primitives and a declarative workflow that let me iterate UI quickly. The project used:

- Kotlin + Jetpack Compose for UI
- Navigation Compose for screen flow
- Material3 theme for consistent visuals

The first deliverable was a simple Articles list and a Detail screen wired through a NavHost — placeholders that let me wire future data flows and iterate UX without waiting for backend pieces.

### Day 2 — Fetching Real Feeds

To get content quickly, I built a lightweight aggregator service. For prototyping I used RSS parsing as the primary source and NewsAPI as a fallback. For local testing I also added a tiny Express server that returned a static JSON payload — handy when iterating the Android client without deployment friction.

Tech highlights:

- Server: Node.js + Express (local testing), serverless option with Vercel/Netlify for deployment.
- Networking on Android: Retrofit + Moshi for JSON parsing, OkHttp for low‑level requests.

This let me replace sample data with real headlines and thumbnails in minutes.

### Day 3 — Offline Caching & Sync

Real apps must work when the network is flaky. I added Room as the on‑device source of truth and WorkManager to periodically fetch updates in the background.

- Room (entities, DAO, database) for durable local storage
- WorkManager for periodic sync (Every 30 minutes)
- Repository pattern to mediate between network and DB

The UI was reworked to observe Flow streams from Room so the list showed cached articles instantly and updated when sync finished. This architecture gives a simple but robust offline experience and predictable data flow for tests and demos.

### Day 4 — Reading Experience & Offline Saving

For a good reading experience I added a detail view that either opens the article in a WebView or displays saved HTML extracted from the fetched page.

- AndroidView + WebView inside Compose for rendering HTML
- OkHttp to fetch raw HTML when the user taps “Save offline”
- DAO extensions to save full content and manage read/unread state

I disabled JS for saved content to reduce attack surface, and left remote pages to load with JS enabled if the user opens them live. Storing HTML made offline reading seamless; later iterations could add sanitization or Readability extraction for cleaner saved pages.

### Day 5 — Search and Personalization

To make the feed feel personal, I implemented:

- Local search (simple substring search for prototype; Room FTS is ready if the dataset grows)
- Ability to mark an article as read so it appears grayed out on the UI

### Tooling, libraries and why they were chosen

- Kotlin: concise, null‑safe code and coroutine support.
- Jetpack Compose: modern UI that’s fast to iterate and easy to test.
- Retrofit + Moshi: mature networking + JSON parsing with Kotlin support.
- OkHttp: reliable HTTP client for fetches and downloads.
- RoomDB: local DB with Flow support for reactive UI.
- WorkManager: battery‑friendly periodic background work.
- Coil: simple image loading library with Compose integration.
- Node/Express (or serverless): quick feed aggregation and development server.

### Architecture summary

- Source feeds (RSS / NewsAPI) → Aggregator (serverless or local) → Android client
- Client uses Repository pattern: Network ↔ Room (source of truth) → UI (Compose)
- Background sync via WorkManager; interactions (views/likes) persisted in Room and influence local ranking.

### Tradeoffs and next steps

- Extraction vs raw HTML: saving raw HTML is fast but messy. Using Readability or a server‑side extraction yields cleaner offline articles.
- FTS vs in‑memory search: FTS scales better for many articles
- OpenAI or embedding models could improve personalization but add cost and complexity

### Epilogue
Building NewsReader was deliberately incremental: ship a skeleton, wire real data, make it resilient offline, improve the reading experience, and finally make it personal. Each day focused on one meaningful capability and a minimal API surface so the app remained testable and demoable. The result is a compact, well‑architected prototype that demonstrates senior‑level decisions: maintainable architecture, offline resilience, and pragmatic personalization without unnecessary complexity.
