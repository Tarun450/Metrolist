# MetroList Custom Version: Upstream Synchronization & Architecture Boundaries

This document defines the architectural structure, component boundaries, and synchronization strategy for maintaining a custom-designed fork of **MetroList** while keeping core functionality in sync with upstream ([https://github.com/MetrolistGroup/Metrolist](https://github.com/MetrolistGroup/Metrolist)).

---

## 1. Project Overview & Architectural Goals

- **Objective**: Develop and maintain a completely customized user interface (UI) and user experience (UX) for MetroList, while seamlessly pulling upstream bug fixes, feature additions, YouTube/Innertube API adaptions, playback enhancements, and database migrations.
- **Core Philosophy**:
  1. **Strict Separation of Concerns**: Isolate presentation/rendering components from core business logic, playback services, and data models.
  2. **Minimal Upstream Core Divergence**: Keep non-UI modules and functional classes as close to upstream MetroList as possible.
  3. **Conflict Surface Minimization**: Confine custom UI changes to designated directories and use clean presentation abstractions (ViewModels, UI state holders) to interact with core services.

---

## 2. Module & Gradle Structure

The project is structured as a multi-module Gradle project built with:
- **Build System**: Gradle 9.7.0 with Kotlin DSL (`*.gradle.kts`), Android Gradle Plugin (AGP) 9.3.1, and Java/JVM Toolchain 21.
- **Dependency Management**: Gradle Version Catalog via `gradle/libs.versions.toml`.

### Included Modules:
1. **`:innertube`** (`innertube/`)
   - **Type**: Android Library (`com.android.library`), Namespace: `com.metrolist.innertube`
   - **Responsibility**: Pure API/network abstraction client for YouTube Music (Innertube).
   - **Key Technologies**: Ktor HTTP client (`ktor-client-okhttp`), Kotlinx Serialization, Timber, InnertubeX.
   - **Upstream Sync Rule**: **100% Upstream Mirror**. Do not introduce UI or custom app dependencies here. Keep in sync with upstream commits.

2. **`:app`** (`app/`)
   - **Type**: Android Application (`com.android.application`), Namespace: `com.metrolist.music`
   - **Responsibility**: Houses playback engine, Room database, Discord RPC, lyrics providers, Listen Together, widgets, ViewModels, and Jetpack Compose UI.
   - **Flavors**:
     - `foss` (Default): Open-source variant (includes updater, excludes proprietary Google Cast SDK).
     - `gms`: Google Mobile Services variant (includes Google Cast SDK and updater).
     - `izzy`: F-Droid / IzzyOnDroid variant (excludes Google Cast and in-app updater).

---

## 3. Component Categorization & Boundaries

| Category | Repository Locations | Upstream Sync Policy | Responsibility |
| :--- | :--- | :--- | :--- |
| **API & Networking** | `innertube/`, `app/.../api/` | **Mirror Upstream** | Innertube endpoints, parsers, DeepL, OpenRouter streaming. |
| **Playback & Audio** | `app/.../playback/`, `app/.../eq/` | **Mirror Upstream** | `MusicService`, ExoPlayer, `PlayerConnection`, queues, volume normalization, silence detector, downloads. |
| **Data & Persistence** | `app/.../db/`, `app/.../constants/` | **Mirror Upstream** | Room database (`MusicDatabase`, `DatabaseDao`), entities, migrations, DataStore preferences. |
| **Features / Services**| `app/.../lyrics/`, `betterlyrics/`, `discord/`, `listentogether/`, `recognition/`, `widget/` | **Mirror Upstream** | Lyrics providers, synced TTML, Discord RPC, Listen Together WebSockets, Shazam recognition, widgets. |
| **Dependency Injection** | `app/.../di/` | **Mirror Upstream** | Hilt modules (`AppModule`, `NetworkModule`, `WrappedModule`). |
| **Presentation Logic** | `app/.../viewmodels/` | **Moderate Sync Care** | `@HiltViewModel` classes feeding state to Compose screens. |
| **UI & Presentation** | `app/.../ui/`, `app/src/main/res/` | **Custom Owned** | Composables, themes, typography, custom layouts, menus, player UI, resources. |

---

## 4. Areas Designated for Custom UI

All custom UI redesign work should be concentrated in the following paths:

1. **Design System & Theme**:
   - `app/src/main/kotlin/com/metrolist/music/ui/theme/`
   - Custom typography, color schemes, elevation, motion, shapes, and dynamic material styling.
2. **Components & Building Blocks**:
   - `app/src/main/kotlin/com/metrolist/music/ui/component/`
   - Custom buttons, chips, song item cards, carousels, bottom sheets, sliders, and loading indicators.
3. **Screen Layouts**:
   - `app/src/main/kotlin/com/metrolist/music/ui/screens/`
   - Redesigned screens: `HomeScreen`, `LibraryScreen`, `SearchScreen`, `ArtistScreen`, `AlbumScreen`, `PlaylistScreen`, `SettingsScreen`.
4. **Playback & Queue Interface**:
   - `app/src/main/kotlin/com/metrolist/music/ui/player/`
   - Full-screen player (`Player.kt`), Mini-player (`MiniPlayer.kt`), Queue sheet (`Queue.kt`), thumbnail animations (`Thumbnail.kt`).
5. **Menus & Dialogs**:
   - `app/src/main/kotlin/com/metrolist/music/ui/menu/`
   - Action sheets, song options menus, playlist creation/import dialogs.
6. **String Resources**:
   - `app/src/main/res/values/metrolist_strings.xml` (**ONLY** edit this file for strings, never `strings.xml` or localized files, per project guidelines).

---

## 5. High-Risk Hybrid Files (Special Care Required During Upstream Sync)

Several files in MetroList mix presentation code with critical business/system logic. During upstream synchronization, these files require careful manual inspection or refactoring:

### 1. `MainActivity.kt` (`app/src/main/kotlin/com/metrolist/music/MainActivity.kt`)
- **Functional Logic**:
  - Service binding lifecycle (`MusicService`, `PlayerConnection`, `ListenTogetherManager`).
  - System intent routing (search intents, recognition intents, app widget actions, deep links).
  - Runtime permissions (Android 13+ POST_NOTIFICATIONS).
  - In-app update checks (`Updater.checkForUpdate()`) and system notifications.
  - Display refresh rate management (60Hz lock vs high refresh rate).
- **UI Logic**:
  - Root `setContent { MetrolistApp(...) }`.
  - Navigation host and bottom sheet sliding layout for player.
  - Dynamic theme palette derivation from song cover art.
- **Sync Strategy**: Keep functional methods (`onStart`, `onNewIntent`, `bindService`, update checks) synchronized with upstream; isolate UI composables into standalone presentation composables.

### 2. `HomeScreen.kt` (`app/src/main/kotlin/com/metrolist/music/ui/screens/HomeScreen.kt`)
- **Functional Logic**:
  - Direct queue instantiation (`YouTubeQueue`, `YouTubeAlbumRadio`, `LocalAlbumRadio`).
  - Speed dial data operations and lucky song roulette generation logic.
  - Direct interaction with `LocalPlayerConnection`.
- **Sync Strategy**: Preserve upstream queue generation and radio dispatch calls when rebuilding the home screen visual presentation.

### 3. `Player.kt` (`app/src/main/kotlin/com/metrolist/music/ui/player/Player.kt`)
- **Functional Logic**:
  - Playback commands (`play`, `pause`, `seek`, `repeat`, `shuffle`, `speed`).
  - Format tag resolution, audio quality indicator logic.
  - Sleep timer dialog and trigger dispatch.
  - Scrobble state updates and cast button integration.
- **Sync Strategy**: Retain player command bindings while redesigning visual surfaces, sliders, and gestures.

### 4. Wrapped Feature (`app/src/main/kotlin/com/metrolist/music/ui/screens/wrapped/`)
- **Note**: `WrappedManager.kt` and `WrappedAudioService.kt` contain non-UI logic (database aggregation, disk caching of bitmaps, dedicated ExoPlayer audio instance) residing inside the `ui/` directory.
- **Sync Strategy**: Treat `WrappedManager.kt` and `WrappedAudioService.kt` as upstream core code, not UI code.

### 5. Equalizer ViewModels (`app/src/main/kotlin/com/metrolist/music/ui/screens/equalizer/`)
- **Note**: `EQViewModel.kt` and `wizard/WizardViewModel.kt` reside inside the `ui/` folder rather than `com.metrolist.music.viewmodels`.
- **Sync Strategy**: Treat equalizer ViewModels and states as functional core code.

### 6. Flavor-Specific Composables (`CastButton.kt`)
- **Note**: Split across `app/src/foss/`, `app/src/gms/`, and `app/src/izzy/`.
- **Sync Strategy**: Keep flavor-specific method signatures intact so all build flavors (`assembleFossDebug`, `assembleGmsDebug`, `assembleIzzyDebug`) compile seamlessly.

---

## 6. Recommended Boundaries Between Custom UI and Upstream Code

To prevent upstream PRs from repeatedly breaking your custom UI:

1. **Use UI Adapters / Extension Wrappers**:
   - Rather than embedding raw `playerConnection` calls deep inside UI widgets, route UI actions through well-defined callbacks or ViewModel functions (e.g., `onPlayPauseClick`, `onSeek(position)`).
2. **Dedicated Custom UI Package**:
   - Place major custom screen designs in a subpackage (e.g., `com.metrolist.music.ui.custom.*` or component-specific packages) and hook them in `NavigationBuilder.kt`. This minimizes merge conflicts when upstream updates their composables in `ui/screens/*`.
3. **Database Schema Rule**:
   - **NEVER** edit Room database schema or migration files unless upstream introduces a schema change. Treat `db/` as strictly read-only for your custom UI.
4. **Strings Hygiene**:
   - Always append custom string resources to the bottom of `metrolist_strings.xml` under a clearly commented section (e.g., `<!-- Custom UI Strings -->`) to minimize line-based merge conflicts with upstream string additions.

---

## 7. Git Remote Setup & Synchronization Protocol

### Configured Remotes
- `origin`: `https://github.com/Tarun450/Metrolist.git` (Personal customized repository)
- `upstream`: `https://github.com/MetrolistGroup/Metrolist.git` (Official upstream MetroList)

### Basic Synchronization Workflow
1. Fetch latest upstream updates:
   ```bash
   git fetch upstream
   ```
2. Inspect upstream changes against main:
   ```bash
   git log --oneline main..upstream/main
   git diff --stat main..upstream/main
   ```
3. For changes touching core files (`innertube/`, `playback/`, `db/`, `lyrics/`), merge/cherry-pick directly.
4. For changes touching high-risk hybrid files (`MainActivity.kt`, `HomeScreen.kt`, `Player.kt`), review functional diffs and adapt them without overwriting custom UI structure.
5. Verify build integrity:
   ```bash
   ./gradlew :app:assembleFossDebug
   ```
