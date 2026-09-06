# Stage 1 — Custom UI Architecture

## 1. Objective

The objective of this project is to build a customized, modern Spotify-inspired music application based on **MetroList** while preserving continuous, long-term upstream synchronization with the official repository ([https://github.com/MetrolistGroup/Metrolist](https://github.com/MetrolistGroup/Metrolist)).

To achieve this, the architecture must satisfy two core requirements:
1. **Presentation Freedom**: Allow complete visual freedom to craft a dark, sleek, Spotify-inspired interface (sticky navigation, dense rows, pill filters, bold typography, integrated queue, and modern mini-player/player) without breaking application behavior.
2. **Upstream Synchronizability**: Minimize the surface area of merge conflicts so that upstream functional improvements (YouTube Innertube parser updates, ExoPlayer fixes, stream URL caching, database migrations, lyrics providers, Discord RPC, and Listen Together updates) can be pulled cleanly and reliably—both manually and through future automated PR adaptation workflows.

---

## 2. Current MetroList UI Architecture

MetroList is built on modern Android development practices, using **Jetpack Compose** with Material 3:

```
┌─────────────────────────────────────────────────────────────┐
│                       MainActivity                          │
│        (Window setup, Service binding, setContent)          │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                       MetrolistApp                          │
│     (Dynamic theme, BottomSheet player, NavigationHost)     │
└──────────────────────────────┬──────────────────────────────┘
                               │
            ┌──────────────────┴──────────────────┐
            ▼                                     ▼
┌───────────────────────┐             ┌───────────────────────┐
│   NavigationBuilder   │             │   MiniPlayer / Player │
│ (Jetpack Nav Compose) │             │ (Bottom sheet overlay)│
└───────────┬───────────┘             └───────────┬───────────┘
            │                                     │
            ▼                                     ▼
┌───────────────────────┐             ┌───────────────────────┐
│     Screen Layer      │             │   PlayerConnection    │
│  (Home, Search, etc.) │◄───────────►│ (ExoPlayer & Service) │
└───────────┬───────────┘             └───────────────────────┘
            │
            ▼
┌───────────────────────┐
│   Hilt ViewModels     │
│ (StateFlow / Compose) │
└───────────┬───────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────┐
│                Core Subsystems & Services                   │
│   (Innertube API, MusicDatabase Room DAO, MusicService)     │
└─────────────────────────────────────────────────────────────┘
```

### Key Architectural Characteristics:
1. **Single-Activity Architecture**: `MainActivity.kt` hosts the Compose tree (`MetrolistApp`), manages service bindings (`MusicService`), processes system intents (search, deep links, widgets), and controls system window insets.
2. **Navigation**: Uses Jetpack Navigation Compose (`NavHost` inside `NavigationBuilder.kt`) with route strings (`"home"`, `"search/{query}"`, `"album/{albumId}"`, etc.).
3. **State Management**: Uses `@HiltViewModel` classes in `com.metrolist.music.viewmodels.*` exposing `StateFlow` and Compose `mutableStateOf` properties.
4. **Playback Orchestration**: A central `PlayerConnection` wrapper wraps `MusicBinder` and `ExoPlayer`, published to the Compose tree via `LocalPlayerConnection.current`.
5. **Tight Coupling in Composables**: Upstream MetroList frequently combines UI rendering with direct functional calls (`playerConnection.playQueue()`, `databaseDao` queries, lucky song algorithms) directly inside Composable functions.

---

## 3. Proposed Custom UI Architecture

To enable a completely new UI without rewriting MetroList or introducing fragile Clean Architecture boilerplate, we adopt a **Parallel UI Layer with Stable Boundary Interfaces**:

```
┌────────────────────────────────────────────────────────────────────────┐
│                        CUSTOM UI LAYER                                 │
│  (com.metrolist.music.ui.custom.*)                                     │
│  • CustomTheme (Spotify-inspired dark tokens, typography, surfaces)    │
│  • CustomComponents (Pill filters, dense song rows, album cards)       │
│  • CustomScreens (CustomHomeScreen, CustomSearchScreen, etc.)          │
│  • CustomPlayer & CustomMiniPlayer                                     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼ [Stable UI Boundaries]
┌────────────────────────────────────────────────────────────────────────┐
│                   METROLIST FUNCTIONAL LAYER                           │
│  (Preserved strictly aligned with upstream)                            │
│  • Navigation Dispatcher (NavigationBuilder routes to Custom Screens)  │
│  • Existing Hilt ViewModels (HomeViewModel, SearchViewModel, etc.)     │
│  • PlayerConnection & MusicService APIs                                │
│  • MusicDatabase & DatabaseDao (Room)                                  │
│  • :innertube API Client (YouTube Music endpoints)                     │
└────────────────────────────────────────────────────────────────────────┘
```

### Architectural Principles:
1. **Zero Unnecessary Abstractions (YAGNI)**: Do not create redundant repositories, use cases, or duplicate ViewModels. MetroList's existing ViewModels already handle threading, caching, and API calls effectively.
2. **Parallel Screen Namespace**: Custom screens live in `com.metrolist.music.ui.custom.*` instead of overwriting `com.metrolist.music.ui.screens.*` in place.
   - *Why*: Upstream PRs touching `HomeScreen.kt` or `SearchScreen.kt` can be merged cleanly via Git without conflicting with our redesigned screens.
3. **Delegated Navigation**: `NavigationBuilder.kt` routes to our custom screens. Upstream route declarations remain intact.
4. **Event-Driven Presentation**: Custom composables emit clean lambda events (`onSongClick`, `onPlayPause`, `onSeek`, `onLikeToggle`) rather than hardcoding complex service manipulation inside visual widgets.

---

## 4. Custom-Owned Areas

These areas belong completely to our fork and can be freely redesigned without upstream interference:

| Package / File Path | Contents & Custom Role |
| :--- | :--- |
| `app/src/main/kotlin/com/metrolist/music/ui/custom/theme/` | **Custom Design System**: Dark palettes (`#121212`, `#181818`, `#282828`), Spotify-style green accent (`#1DB954`), typography, corner radii (rounded pills and subtle 4dp/8dp corners), surface elevations. |
| `app/src/main/kotlin/com/metrolist/music/ui/custom/components/` | **Custom Atoms & Molecules**: Filter pills ("All", "Music", "Podcasts"), dense song rows, grid album cards with hover/play badges, sticky headers, animated bottom bars. |
| `app/src/main/kotlin/com/metrolist/music/ui/custom/screens/` | **Custom Screen Implementations**: `CustomHomeScreen`, `CustomSearchScreen`, `CustomLibraryScreen`, `CustomArtistScreen`, `CustomAlbumScreen`, `CustomPlaylistScreen`. |
| `app/src/main/kotlin/com/metrolist/music/ui/custom/player/` | **Custom Playback UI**: Modern Spotify-style `CustomMiniPlayer` (floating bar with rounded corners and progress line) and `CustomFullPlayer` (large centered cover, bold title, slider, glowing background). |
| `app/src/main/res/values/metrolist_strings.xml` | **Custom Strings Section**: New localized strings for custom UI elements added exclusively at the bottom of the file. |
| `app/src/main/res/drawable*` | **Custom Assets**: Vector icons, playlist badges, custom placeholders. |

---

## 5. Upstream-Owned Areas

These areas must remain as close to upstream official MetroList as possible. No UI redesign code should ever be placed here:

| Package / Subsystem | Upstream Functionality | Why It Must Remain Upstream-Owned |
| :--- | :--- | :--- |
| **`:innertube/`** | YouTube Music API engine, Ktor client, endpoints, JSON serializers, parsers. | YouTube regularly changes internal endpoints and response shapes. Any drift here breaks search, browsing, and media streaming. |
| **`playback/`** | `MusicService.kt`, `PlayerConnection.kt`, audio normalization, silence skipping, ExoPlayer caching, download service, queues (`playback/queues/*`). | Core playback stability. Upstream actively fixes audio focus issues, lockscreen notification bugs, and stream decryption. |
| **`db/`** | `MusicDatabase.kt`, `DatabaseDao.kt`, entities, Room migrations. | Schema changes violate project rules and risk user data loss or SQLite crash on upgrade. |
| **`lyrics/` & `betterlyrics/`** | Multi-provider lyrics fetching, synchronized TTML parser, translation and romanization. | Bug fixes and new providers (e.g., recent Zemer provider addition) flow from upstream. |
| **`discord/`** | Discord RPC gateway, OAuth, rich presence. | Protocol and reconnection fixes flow directly from upstream. |
| **`listentogether/`** | WebSocket client, room management, server clock sync. | Protocol synchronicity with Listen Together relay servers. |
| **`recognition/`** | Shazam/Vibra acoustic fingerprinting and audio capture. | Fingerprint generator math and audio resampler. |
| **`api/`** | DeepL translation and OpenRouter AI services. | Translation and AI model integrations. |
| **`utils/` & `extensions/`** | Kotlin extensions, `InnerTubeXPlayer`, `SyncUtils`, `Updater`. | Stream extractor rollover and playlist synchronization algorithms. |
| **`di/`** | Hilt dependency injection modules. | Dependency graph configuration. |
| **Build files** | `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`. | Toolchain, plugins, and dependency versions. |

---

## 6. Hybrid Areas

Hybrid files mix presentation code with critical business, service, or system logic. They cannot be blindly replaced, and require careful structural separation:

### 1. `MainActivity.kt`
- **Functional Responsibilities**:
  - Service lifecycle binding (`MusicService` binding and unbinding).
  - System Intent routing (voice search intents, widget action dispatches, deep links).
  - Runtime permissions (Android 13+ `POST_NOTIFICATIONS`).
  - In-app update checking (`Updater.checkForUpdate()`) and notification builder.
  - Display refresh rate settings (switching display modes for 60Hz vs 120Hz).
- **UI Responsibilities**:
  - Sets Compose content (`setContent { MetrolistApp(...) }`).
  - Player sliding bottom sheet scaffold.
  - Dynamic theme palette calculation from current song thumbnail.
- **Handling Strategy**: Keep all Activity lifecycle and Intent handling untouched. In `MetrolistApp`, replace the root theme wrapper and scaffold contents with our custom layout while passing the existing `playerConnection`, `database`, and `downloadUtil`.

### 2. `HomeScreen.kt`
- **Functional Responsibilities**:
  - Instantiates playback queues (`YouTubeQueue(item)`, `YouTubeAlbumRadio`, `LocalAlbumRadio`).
  - Lucky mix roulette algorithm (`luckyItem` randomized selection).
  - Speed dial persistence and item reordering via `databaseDao`.
- **UI Responsibilities**:
  - Multi-browse carousels, speed dial grid, quick picks, mood chips.
- **Handling Strategy**: Extract the queue creation and lucky mix helper functions into a shared controller or extension, so `CustomHomeScreen` can execute the exact same playback actions without copying messy code.

### 3. `Player.kt`
- **Functional Responsibilities**:
  - Playback control dispatch (`togglePlayPause`, `seekTo`, `toggleRepeatMode`, `shuffleModeEnabled`).
  - Media format resolution and audio quality chip metadata.
  - Sleep timer dialog dispatch and timer calculations.
  - Scrobbling trigger updates and Cast button integration.
- **UI Responsibilities**:
  - Over 2,100 lines of vertical pager, lyrics sheet, playback slider, thumbnail layout.
- **Handling Strategy**: Treat upstream `Player.kt` as a reference implementation. In `CustomPlayer`, consume `PlayerConnection` state flows and call its public control methods directly.

### 4. `Queue.kt`
- **Functional Responsibilities**:
  - Timeline queue window resolution (`queueWindows`, `currentWindowIndex`).
  - Queue item reordering (`moveMediaItem`) and shuffle order updates (`setShuffleOrder`).
  - Automix radio integration (`service.automixItems`, `playNextAutomix`, `addToQueueAutomix`).
  - Sleep timer controls.
- **UI Responsibilities**:
  - Reorderable list, swipe-to-dismiss, queue header.
- **Handling Strategy**: The queue logic must be preserved exactly. `CustomQueue` will bind to `playerConnection.queueWindows` and call `playerConnection.player.moveMediaItem(from, to)` and `service.playNextAutomix()`.

### 5. `Wrapped` Feature (`ui/screens/wrapped/`)
- **Note**: `WrappedManager.kt` (database aggregation, stats calculations) and `WrappedAudioService.kt` (dedicated ExoPlayer audio manager) reside inside the `ui/` directory.
- **Handling Strategy**: Treat these two files as **Upstream-Owned Core**, not UI. Only `WrappedScreen.kt` and its sub-pages are UI presentation.

### 6. `Equalizer` Feature (`ui/screens/equalizer/`)
- **Note**: `EQViewModel.kt` and `WizardViewModel.kt` reside in `ui/screens/equalizer/`.
- **Handling Strategy**: Treat equalizer ViewModels and state classes as **Upstream-Owned Core**.

### 7. Menu Dialogs (`ui/menu/`)
- **Note**: Files like `AddToPlaylistDialog.kt`, `ImportPlaylistDialog.kt`, and `SelectionSongsMenu.kt` execute Room database updates, CSV playlist parsing, and sync reconciliation (`SyncUtils`).
- **Handling Strategy**: Restyle dialog visuals (colors, buttons, typography) without altering their internal database insert/sync routines.

### 8. `ui/component/CastButton.kt`
- **Note**: Flavor-differentiated across `foss`, `gms`, and `izzy`.
- **Handling Strategy**: Preserve the multi-source set declarations and method signatures across all flavors.

---

## 7. UI → Functional Boundaries

The boundary between our custom UI and MetroList core is defined through stable, existing channels:

```
┌─────────────────────────────────────────────────────────────┐
│                       CUSTOM UI                             │
└───────────────┬─────────────────────────────┬───────────────┘
                │                             │
       (Playback Controls)            (Data & State)
                │                             │
                ▼                             ▼
┌───────────────────────────────┐   ┌─────────────────────────┐
│       PlayerConnection        │   │    Existing ViewModels  │
│  • playQueue(queue)           │   │  • HomeViewModel        │
│  • togglePlayPause()          │   │  • OnlineSearchViewModel│
│  • seekTo(positionMs)         │   │  • ArtistViewModel      │
│  • skipToNext() / Previous()  │   │  • AlbumViewModel       │
│  • repeat / shuffle modes     │   │  • LibraryViewModels    │
└───────────────┬───────────────┘   └─────────────┬───────────┘
                │                                 │
                ▼                                 ▼
┌───────────────────────────────┐   ┌─────────────────────────┐
│     MusicService / Player     │   │ DatabaseDao / Innertube │
└───────────────────────────────┘   └─────────────────────────┘
```

### Action-to-Interface Mapping:

| User Action in Custom UI | Target Boundary / Existing Mechanism | Implementation in Custom UI |
| :--- | :--- | :--- |
| **Play song / list / album** | `playerConnection.playQueue(queue)` | Instantiate `YouTubeQueue`, `YouTubeAlbumRadio`, or `ListQueue` and pass to `playerConnection`. |
| **Play / Pause toggle** | `playerConnection.togglePlayPause()` | Invoke directly on `PlayerConnection`. |
| **Seek track position** | `playerConnection.player.seekTo(positionMs)` | Pass position from custom slider to `PlayerConnection`. |
| **Skip Next / Previous** | `playerConnection.player.seekToNextMediaItem()` | Invoke existing methods on `playerConnection.player`. |
| **Queue Reorder / Delete** | `playerConnection.player.moveMediaItem(from, to)` / `removeMediaItem(index)` | Invoke existing ExoPlayer queue methods. |
| **Automix / Infinite Radio** | `playerConnection.service.playNextAutomix()` | Triggered on automix item click. |
| **Like / Favorite Song** | `database.query { update(song.toggleLike()) }` | Trigger Room query via `LocalDatabase.current`. |
| **Download Song / Album** | `downloadUtil.download(mediaMetadata)` | Invoke `downloadUtil` methods. |
| **Search (Online/Local)** | `OnlineSearchViewModel` / `LocalSearchViewModel` | Bind query state to existing ViewModel. |
| **Browse Artist / Album** | `navController.navigate("artist/$id")` | Use existing route strings in `NavigationBuilder`. |
| **Fetch Synced Lyrics** | `LyricsHelper` / `playerConnection.currentLyrics` | Observe existing lyrics flows. |
| **Settings Preferences** | `rememberPreference(Key, default)` | Use MetroList's existing DataStore preference delegation. |

---

## 8. Player & Queue Strategy

The player and queue are the most critical user-facing surfaces. Upstream changes to playback must not break when the player UI is restyled.

### Separation of Concerns:
1. **Playback Engine (Upstream-Owned)**:
   - `MusicService.kt` manages the `ExoPlayer` instance, media session callbacks, audio sinks, and cache data sources.
   - `PlayerConnection.kt` provides reactive state streams (`mediaMetadata`, `isEffectivelyPlaying`, `currentPosition`, `queueWindows`, `repeatMode`, `shuffleModeEnabled`).
2. **Player Presentation (Custom-Owned)**:
   - `CustomMiniPlayer`: A sleek, persistent bar with cover art, marquee title, subtle progress indicator, and play/pause/skip buttons.
   - `CustomFullPlayer`: Spotify-inspired presentation featuring a large rounded album cover, bold track titles, clean progress bar, dynamic background glow, lyrics toggle, and integrated queue sheet.
   - `CustomQueue`: Clean reorderable list with drag handles and swipe-to-dismiss, wired directly to `playerConnection.player.moveMediaItem()`.

### Preserving Upstream Playback Enhancements:
Because `CustomPlayer` and `CustomMiniPlayer` only consume `StateFlow`s and invoke standard methods on `PlayerConnection`, any upstream enhancements to `MusicService` (e.g., crossfade fixes, cache optimizations, stream URL decryption, client rollover) will work immediately without requiring player UI modifications.

---

## 9. Navigation Strategy

### Comparison of Navigation Approaches:

1. **Option A: In-Place Modification of `ui/screens/*`**:
   - Directly overwrite existing screen composables.
   - *Risk*: **Extremely High**. Upstream commits that modify `HomeScreen.kt` or `SearchScreen.kt` will produce massive merge conflicts that must be resolved manually every week.
2. **Option B: Forked `NavigationBuilder.kt` with Custom Namespace (Recommended)**:
   - Place custom screens in `com.metrolist.music.ui.custom.screens.*`.
   - Update `NavigationBuilder.kt` so routes point to our custom screens:
     ```kotlin
     composable(Screens.Home.route) {
         CustomHomeScreen(navController = navController, snackbarHostState = snackbarHostState)
     }
     composable(Screens.Search.route) { backStackEntry ->
         CustomSearchScreen(navController = navController, savedStateHandle = backStackEntry.savedStateHandle)
     }
     ```
   - *Advantage*: Upstream screen files (`HomeScreen.kt`, `SearchScreen.kt`) remain unmodified in our branch! Upstream PRs modifying those files apply cleanly without conflicts. If upstream introduces a functional feature to `HomeScreen.kt`, it appears cleanly in git history and can be incorporated into `CustomHomeScreen`.

---

## 10. Theme & Design System Strategy

### Spotify-Inspired Visual Architecture:
MetroList currently uses Material 3 dynamic tonal spot coloring. Our custom design system will implement a Spotify-inspired aesthetic:

1. **Color Palette**:
   - Deep Black Background: `#121212` (pure black `#000000` for OLED option).
   - Elevated Dark Surfaces: Cards `#181818`, elevated dialogs `#282828`, hover states `#2A2A2A`.
   - Signature Green Accent: `#1DB954` (used for primary actions, active play buttons, volume/seek progress).
   - Dynamic Artwork Tinting: Subtle ambient glow behind album artwork derived via existing `Palette` extraction in `ui/theme/Theme.kt`.
2. **Typography**:
   - Bold, clean sans-serif typography with strong hierarchical contrast (Large bold section titles, medium track headers, muted secondary artist/album subtitles).
3. **Geometry & Shapes**:
   - Pill-shaped filter chips (`RoundedCornerShape(50)`).
   - Album artwork: subtle 4dp or 8dp rounded corners.
   - Artist images: fully circular (`CircleShape`).
   - Cards: 8dp rounded corners with zero border stroke and subtle dark elevation.
4. **Component Reuse Strategy**:
   - Reuse: Color extraction algorithms (`extractThemeColor`), image loading (`CoilBitmapLoader`, `AsyncImage`), preference hooks (`rememberPreference`).
   - Custom: Top app bars, bottom navigation bar, song item list rows, filter pill carousels, full player layout.

---

## 11. File Ownership Model

| Category | Description | Repository Examples | Sync Strategy |
| :--- | :--- | :--- | :--- |
| **CUSTOM-OWNED** | Files built specifically for our custom fork. Upstream does not know about them. | `ui/custom/**`<br>`res/values/custom_strings.xml` (or custom section in `metrolist_strings.xml`) | 100% owned by us. Upstream changes never touch these files. Zero conflict risk. |
| **UPSTREAM-OWNED** | Core functional files that should mirror official MetroList. | `innertube/**`<br>`playback/MusicService.kt`<br>`db/**`<br>`lyrics/**`<br>`discord/**`<br>`listentogether/**`<br>`viewmodels/**`<br>`utils/**` | Auto-merge upstream changes directly. Never add custom UI code to these files. |
| **HYBRID** | Files where UI and functional logic connect, requiring careful review during sync. | `MainActivity.kt`<br>`NavigationBuilder.kt`<br>`ui/menu/*`<br>`ui/component/CastButton.kt` | Manual or semantic AI merge. Apply functional upstream diffs while preserving custom UI hook points. |

---

## 12. Upstream Synchronization Strategy

### Git Branch Structure:
- `main`: Our primary branch containing the custom UI and functional code.
- `upstream-tracking`: A branch tracking `upstream/main` (`https://github.com/MetrolistGroup/Metrolist.git`) without local modifications.
- `sync/YYYY-MM-DD`: Ephemeral synchronization branches created to merge or rebase upstream changes before merging into `main`.

### Step-by-Step Synchronization Protocol:
1. **Fetch Latest Upstream**:
   ```bash
   git fetch upstream main
   ```
2. **Inspect Upstream Diff**:
   ```bash
   git log --oneline main..upstream/main
   git diff --stat main..upstream/main
   ```
3. **Classify Upstream Changes**:
   - *Core/Functional only* (`innertube/`, `playback/`, `db/`): Merge automatically.
   - *Upstream UI-only* (changes to upstream `ui/screens/*` that our fork replaced): Accept upstream file changes (keeping upstream files clean) without altering our custom screens in `ui/custom/`.
   - *Hybrid changes* (`MainActivity.kt`, `NavigationBuilder.kt`, `SyncUtils` in menus): Inspect the specific diff lines and integrate functional updates.
4. **Compile and Verify**:
   ```bash
   ./gradlew :app:assembleFossDebug
   ```

---

## 13. Future AI PR Adaptation Strategy

Stage 0 and Stage 1 establish the foundation for a future GitHub Actions automation system that watches merged MetroList PRs and adapts them to this repository.

### How This Architecture Empowers the AI:
1. **Deterministic PR Classification**:
   - When an upstream PR touches only `:innertube`, `MusicService.kt`, or `db/`, the AI knows with 100% confidence that the PR is **functional** and can be merged without touching UI.
   - When an upstream PR touches only upstream `ui/theme/` or upstream composables, the AI knows the PR is **UI cosmetic** and can be cleanly ignored or applied to upstream reference files without impacting `ui/custom/`.
   - When an upstream PR touches **hybrid files**, the AI only needs to analyze the functional portion (e.g. a new Intent action in `MainActivity.kt` or a new parameter in `AddToPlaylistDialog.kt`) and adapt it.
2. **Isolated Failure Domain**:
   - If an upstream functional update breaks compilation, the compiler error will pinpoint the exact interface or ViewModel method that changed, allowing the AI or developer to update only that specific call in `ui/custom/`.

### Real MetroList PR Case Studies:
- **Case 1 (`commit 4158eae4` - Updater Prompt in `MainActivity.kt`)**: Upstream modified 160 lines in `MainActivity.kt` to add KMP updater logic. Because our architecture keeps `MainActivity.kt`'s functional methods intact, this functional PR merges cleanly.
- **Case 2 (`commit 5247e9a3` - Playlist Reconciliation in `SyncUtils` and Menus)**: Upstream updated playlist sync logic across 8 menu files in `ui/menu/`. Because our custom UI reuses existing dialogs and menus for complex database operations, our fork receives this critical bug fix without breaking our custom theme.
- **Case 3 (`commit 649c9f19` - Crossfade Repeat Mode Fix in `MusicService.kt`)**: Upstream fixed repeat/shuffle states during audio crossfade. Because `MusicService.kt` is strictly Upstream-Owned, this fix applies automatically with zero merge friction.

---

## 14. Merge Conflict Risks & Mitigations

| Conflict Vector | Risk Level | Mitigation Strategy |
| :--- | :--- | :--- |
| **`MainActivity.kt` changes** | Medium | Keep `MainActivity.kt` as close to upstream as possible. Limit custom changes to the root composable invocation (`setContent`). |
| **`metrolist_strings.xml` additions** | Low | Always append custom strings to the bottom of the file under a dedicated comment marker (`<!-- Custom UI Strings -->`). Never edit other languages. |
| **`gradle/libs.versions.toml` updates** | Medium | Accept upstream dependency bumps. Only add custom UI dependencies (if any) to a segregated section. |
| **ViewModel method signature changes** | Low-Medium | Since custom screens call ViewModel functions, compiler errors will immediately flag any upstream signature changes during `./gradlew assembleFossDebug`. |
| **Menu and dialog sync logic** | Medium | Keep `ui/menu/` functional logic identical to upstream; only modify visual surface composables within them. |

---

## 15. Recommended Implementation Order

When the project advances to UI implementation, the work should be carried out in the following strictly ordered phases:

```
Phase 1: Design System & Tokens (ui/custom/theme/)
   │
   ▼
Phase 2: Atomic UI Components (Pills, song rows, cards)
   │
   ▼
Phase 3: Playback Bar & Player (CustomMiniPlayer & CustomPlayer)
   │
   ▼
Phase 4: Home Screen (CustomHomeScreen: sticky chips, recents grid, carousels)
   │
   ▼
Phase 5: Search & Browse (CustomSearchScreen: browse categories, live search)
   │
   ▼
Phase 6: Library (CustomLibraryScreen: filter chips, sorting, playlists)
   │
   ▼
Phase 7: Detail Screens (Artist, Album, Playlist layouts)
   │
   ▼
Phase 8: Dialogs, Menus & Settings Polish
```

### Rationale:
1. **Phase 1 & 2 First**: Establishing the color tokens, typography, and card components upfront guarantees visual consistency across all subsequent screens.
2. **Player in Phase 3**: The player and mini-player are visible across all screens. Implementing them early allows continuous testing of playback integration while building screens.
3. **Home & Search Before Details**: Home and Search represent the core day-to-day discovery flow, while detail screens (Artist/Album) naturally reuse the components built for Home and Search.

---

## 16. Important Warnings

1. **Database Schema Immutability**: Strictly obey Rule 5 in `AGENTS.md`. Never modify Room database entities, converters, or schema files. Database migrations are strictly upstream-governed.
2. **English Strings Only**: In accordance with Rule 3 in `AGENTS.md`, edit only `app/src/main/res/values/metrolist_strings.xml`. Do not touch localized string files or `strings.xml`.
3. **Version Number**: In accordance with Rule 7 in `AGENTS.md`, never bump `versionCode` or `versionName` in `app/build.gradle.kts`.
4. **Multi-Flavor Consistency**: Any changes touching `CastButton` must be maintained across all three flavor source sets (`foss`, `gms`, and `izzy`).
5. **Preserve Audio Engine**: Never replace `PlayerConnection` or `MusicService` with ad-hoc media controls. All playback must route through MetroList's established ExoPlayer architecture.

---

## 17. Final Recommendation

The parallel custom UI architecture (`com.metrolist.music.ui.custom.*`) provides the cleanest, lowest-maintenance boundary for our customized fork:
- It eliminates merge conflicts on primary screen files.
- It preserves MetroList's mature playback, networking, caching, and database engines.
- It provides a deterministic, reliable foundation for future automated upstream synchronization.
- It achieves complete visual freedom for a modern, Spotify-inspired user experience without over-engineering or introducing unnecessary architectural layers.
