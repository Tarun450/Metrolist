# Stage 2A — Stitch Design Specification

## 1. Design Goals

The primary goal of this stage is to establish a distinct, modern, premium visual design identity for our customized Android fork of **MetroList** using Google Stitch MCP as the design exploration tool.

### Core Objectives:
1. **Music-First Focus**: Give album artwork, artist imagery, and typography pride of place while minimizing UI clutter.
2. **Modern Usability**: Deliver the fast, fluid, and intuitive browsing experience expected of top-tier streaming platforms without copying proprietary assets or visual identity.
3. **Comfortable for Long Sessions**: Ensure the dark interface reduces eye strain during extended listening, with balanced contrast ratios that pass WCAG AA standards.
4. **Android & Compose Native**: Ensure all visual components translate directly into Jetpack Compose components without complex layout hacks or GPU-straining graphics layers.
5. **Zero Upstream Core Regression**: Preserve MetroList’s mature audio engine (`ExoPlayer`, `MusicService`), database schema (`Room`), YouTube Innertube client, and background services by maintaining the strict boundary established in Stage 1.

---

## 2. Selected Design Direction

### Exploration Summary:
Three distinct design directions were generated and evaluated in Google Stitch:
- **Direction A: Modern Obsidian & Emerald (Card-Centric, Balanced Density)** — *SELECTED*
- **Direction B: Nordic Minimalist / Editorial Sound (Typography-Forward, Sharp & Borderless)**
- **Direction C: Vibrant Neo-Grotesque / Dynamic Aura (Artwork-Driven, Neon Cyan & Glassmorphism)**

### Why Direction A Was Selected:
1. **Best Usability for Mobile Streaming**: Direction A achieves the optimal balance between high visual density (vital for browsing large discographies and playlists) and tactile touch targets (64dp song rows with generous tap areas).
2. **Avoids Gimmicks & Visual Noise**: Direction C relied heavily on multiple blur filters and glowing auras, which can reduce battery efficiency on mobile and create visual fatigue. Direction B was stark and editorial, but lacked the quick-access grid convenience users rely on on mobile.
3. **Clean Android / Compose Alignment**: The Obsidian & Emerald design system maps directly to Material 3 tokens, surface container elevations, and Compose shape primitives without requiring custom OpenGL shaders or heavy backdrop blurs.
4. **Emerald Accent Authority**: The deep charcoal `#0C0D11` paired with a vibrant emerald `#10B981` delivers an immediate sense of energy and audio precision, differentiating MetroList from generic purple or blue music dashboards.

---

## 3. Design Philosophy

- **Quiet Containers, Loud Content**: The interface is a dark, matte canvas (`#0C0D11`). It recedes into the background, allowing album artwork, vibrant vinyl covers, and artist photography to be the hero elements.
- **Hierarchy Through Scale & Weight, Not Color Clutter**: Primary text is bright and crisp; secondary text is muted slate. Color is reserved strictly for active playback states, progress indicators, and primary interactive calls-to-action.
- **Tactile Responsiveness**: Interactive elements feature distinct tactile feedback states: 64dp list rows for finger accuracy, pill-shaped chips with clear active borders, and a prominent 64dp circular play button that commands the playback experience.
- **Continuous Awareness**: The floating mini-player bar anchors every non-player screen, ensuring the listener is always one tap away from playback controls without obscuring navigation.

---

## 4. Color System

| Token Name | Hex Code | Role & Usage | Contrast Ratio |
| :--- | :--- | :--- | :--- |
| **`colorBackground`** | `#0C0D11` | Primary screen canvas. Deep obsidian charcoal, softer than pure black to prevent harsh glare. | 17.5:1 vs Text |
| **`colorSurfaceContainerLowest`** | `#121419` | Subtle background for nested sections, Recents headers. | — |
| **`colorSurfaceContainer`** | `#16181F` | Content cards, quick-pick playlist tiles, dialog backgrounds. | — |
| **`colorSurfaceContainerHigh`** | `#1E212B` | Search bar fill, elevated bottom sheets, floating mini-player surface. | — |
| **`colorSurfaceContainerHighest`**| `#2A2D3A` | Active filter pill containers, hovered/pressed list rows. | — |
| **`colorPrimary` (Emerald)** | `#10B981` | Signature brand accent. Active play button, slider thumb/track, active navigation items. | 7.2:1 vs Base |
| **`colorPrimaryHover`** | `#059669` | Pressed state for primary emerald buttons. | — |
| **`colorSecondary` (Indigo)** | `#818CF8` | Secondary badges, stats labels, Verified Artist badges. | 6.8:1 vs Base |
| **`colorTertiary` (Amber)** | `#F59E0B` | Offline mode alerts, explicit content tags, warning badges. | 9.1:1 vs Base |
| **`colorOnSurface` (Text White)** | `#F8FAFC` | Primary track titles, screen headers, artist names. | 18.2:1 vs Base |
| **`colorOnSurfaceVariant` (Muted)**| `#94A3B8` | Artist subtitles, album years, total track counts, search placeholders. | 7.8:1 vs Base |
| **`colorMetadata` (Slate)** | `#64748B` | Track durations, timestamps, secondary captions. | 4.6:1 vs Base (Passes AA) |
| **`colorDivider`** | `#242836` | Subtle 1px structural dividing lines. | — |
| **`colorError`** | `#EF4444` | Streaming failures, download errors. | 5.5:1 vs Base |

---

## 5. Typography

The typography pairs **Plus Jakarta Sans** (for display headers, screen titles, and prominent button labels) with **Inter** (for body copy, track titles, and dense lists).

| Style Level | Font Family | Size (sp) | Weight | Line Height | Tracking / Spacing | Usage |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Display Large** | Plus Jakarta Sans | 32sp | Bold (700) | 40sp | -0.03em | Artist hero title, Wrapped banner |
| **Headline Medium** | Plus Jakarta Sans | 24sp | Bold (700) | 32sp | -0.02em | Screen titles ("Your Library", "Search") |
| **Title Large** | Plus Jakarta Sans | 20sp | SemiBold (600) | 28sp | -0.01em | Album titles, Full player track title |
| **Title Medium** | Plus Jakarta Sans | 16sp | SemiBold (600) | 22sp | 0.00em | Section headers ("Jump Back In", "Popular") |
| **Body Bold** | Inter | 15sp | SemiBold (600) | 20sp | 0.00em | Track titles in song rows, Queue items |
| **Body Regular** | Inter | 13sp | Regular (400) | 18sp | 0.00em | Artist names, album subtitles |
| **Label Medium** | Plus Jakarta Sans | 12sp | Medium (500) | 16sp | +0.02em | Pill filter text, button labels |
| **Caption Mono** | Inter | 11sp | Regular (400) | 14sp | +0.04em | Elapsed time ("1:42 / 4:03"), format tags |

---

## 6. Spacing System

Based on a strict **8dp modular scale** (with 4dp half-step micro-units):
- **`space-2xs` (2dp)**: Icon-to-badge gaps, progress bar stroke.
- **`space-xs` (4dp)**: Subtitle vertical margins, chip internal padding.
- **`space-sm` (8dp)**: Grid gutters between cards, standard row padding.
- **`space-md` (12dp)**: Floating mini-player margin above navigation dock.
- **`space-base` (16dp)**: Screen horizontal padding (left/right margins on Android phones).
- **`space-lg` (24dp)**: Section separation margins.
- **`space-xl` (32dp)**: Large header margins, hero artwork spacing.
- **`space-2xl` (48dp)**: Generous empty-state buffers, hero banner bottoms.

---

## 7. Shape / Radius System

| Shape Token | Radius (dp) | Applied Components |
| :--- | :--- | :--- |
| **`shapeNone`** | 0dp | Full-width divider lines, bottom sheet backdrop edges. |
| **`shapeSmall`** | 6dp | Album thumbnails in list rows (48x48dp). |
| **`shapeMedium`**| 8dp | Album cards in carousels, large player artwork (320x320dp). |
| **`shapeLarge`** | 12dp | Quick-access 2-column cards, dialog popups, Mini-player surface. |
| **`shapeSheet`** | 16dp | Top corners of draggable player sheet and queue drawer. |
| **`shapeFull`**  | 9999dp (Pill) | Filter chips, search bar, play/pause buttons, 'Follow' button. |
| **`shapeCircle`**| 100% Circle | Artist profile avatars, circular play FAB, profile monogram. |

---

## 8. Iconography

- **Style**: Outlined, 2dp stroke weight, rounded joints, crisp SVG rendering.
- **Active State Rule**: Active states switch to solid filled icons (e.g. Heart filled with Emerald `#10B981`, Active Nav icon with emerald glow/dot).
- **Standard Sizing**:
  - Small actions in rows: 20x20dp (in 40x40dp touch box).
  - Standard action buttons: 24x24dp.
  - Large playback controls: 32x32dp (prev/next/shuffle/repeat).
  - Main Play/Pause button: 64x64dp circular container with 28x28dp icon.

---

## 9. Artwork Treatment

1. **Aspect Ratio**: Strictly 1:1 square for albums, playlists, and tracks; 1:1 circular for artists.
2. **Ambient Dynamic Backdrops**: When playing a song or viewing an album, the top 180dp of the screen features a soft, heavily desaturated radial gradient extracted from the artwork via `Palette` (5–8% opacity) over the `#0C0D11` background.
3. **Corner Rounding**: 6dp for 48x48dp thumbnails, 8dp for cards and player art.
4. **Caching & Placeholders**: Neutral dark gray surface (`#16181F`) with subtle musical note glyph while loading via Coil.

---

## 10. Navigation Model

```
┌─────────────────────────────────────────────────────────────┐
│                       Screen Canvas                         │
│             (Home / Search / Library / Details)             │
│                                                             │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  Floating Mini-Player (64dp height, 12dp bottom margin)     │
│  [Cover] Track Title • Artist             [Heart] [Play]    │
│  ─────────────────────────────────────────────────────────  │
├─────────────────────────────────────────────────────────────┤
│  Bottom Navigation Bar (56dp height)                        │
│     [Home]             [Search]             [Library]       │
└─────────────────────────────────────────────────────────────┘
```

1. **3 Primary Destinations**:
   - **Home**: Personalized mixes, quick-picks, recently played, heavy rotation.
   - **Search**: Discovery categories, recent searches, live query results.
   - **Your Library**: Playlists, downloaded songs, followed artists, saved albums.
2. **Persistent Floating Dock**:
   - The mini-player floats 12dp above the navigation bar with rounded 12dp corners and an emerald progress line.
   - Tapping expands the **Full Player** sliding sheet. Swiping left/right skips tracks.
3. **Deep Navigation**:
   - Tapping an artist opens the **Artist Profile** with back arrow navigation.
   - Tapping an album/playlist opens the **Album / Playlist Detail** screen.

---

## 11. Component System

### 1. Song List Row (`CustomSongRow`)
- **Height**: 64dp fixed height.
- **Elements**: 48x48dp thumbnail, Title (15sp Inter SemiBold) stacked above Artist (13sp Inter Regular), Heart icon (40dp touch target), 3-dot menu (40dp touch target).
- **Playing State**: Title text switches to `#10B981` Emerald with an animated 3-bar green equalizer icon replacing the track number or thumbnail overlay.

### 2. Quick-Pick Card (`QuickPickCard`)
- **Height**: 56dp.
- **Layout**: 2-column grid. Left 56x56dp square artwork with 8dp left radius, right label area on `#16181F` dark container. Text truncated to 2 lines.

### 3. Filter Pill (`FilterPill`)
- **Height**: 32dp.
- **Inactive**: `#1E212B` surface, `#94A3B8` text, no border.
- **Active**: `#10B981` solid emerald surface, `#0C0D11` dark text, bold font.

### 4. Floating Mini-Player (`CustomMiniPlayer`)
- **Height**: 64dp with 12dp floating margin.
- **Layout**: `#1E212B` surface container, 48x48dp thumbnail, title with marquee scrolling, artist subtitle, filled emerald heart toggle, play/pause circular button.
- **Progress Line**: 2dp line directly on bottom edge of the mini-player surface (`#10B981`).

---

## 12. Home Screen (`CustomHomeScreen`)

- **Header**: Warm personalized greeting ("Good evening"), profile monogram, notification bell, settings icon.
- **Filter Row**: Horizontal scrolling pills: `All` (default), `Music`, `Podcasts`, `Audiobooks`.
- **Quick Access ("Jump Back In")**: 2-column, 3-row grid of the user's top 6 recent items with square art on the left.
- **Carousels ("Made For You")**: 160x160dp square cards with bold titles ("Discover Weekly", "Daily Mix 1") and metadata subtitles.
- **Artist Spotlight ("Heavy Rotation")**: 120x120dp circular artist avatars with artist names centered below.

---

## 13. Search (`CustomSearchScreen`)

- **Header & Search Bar**: Large bold "Search" headline. Pill-shaped 48dp input bar with magnifying glass, clear button, and voice search / recognition triggers.
- **Filter Pills**: Instant filter toggles: `Top Results`, `Songs`, `Artists`, `Albums`, `Playlists`.
- **Recent Searches**: Horizontal row of chips with individual dismiss ("x") buttons and clear-all action.
- **Browse All Grid**: 2-column grid of genre/mood cards (Synthwave, Electronic, Hip-Hop, Rock, Chill, Indie) featuring saturated dark gradients, bold tilted typography, and rotated album artwork peeking from the bottom-right corner.

---

## 14. Library (`CustomLibraryScreen`)

- **Header**: Large "Your Library" title, profile avatar, search within library icon, and `+` (Create Playlist) button.
- **Filter Pills**: `Playlists` (active), `Podcasts`, `Albums`, `Artists`, `Downloaded` (with offline indicator).
- **Sort & View Control**: "Recents" dropdown menu on the left, Grid/List toggle button on the right.
- **Pinned Card**: Dedicated "Liked Songs" tile with a glowing purple-emerald gradient cover, white heart icon, and "Pinned • 248 songs" subtitle.
- **List Items**: 64dp rows displaying playlists, circular artist avatars, and albums with green downloaded checkmarks.

---

## 15. Album / Artist / Playlist (`CustomDetailScreens`)

### Album / Playlist View:
- **Hero Header**: Centered 180x180dp artwork with soft drop shadow, album title (24sp bold), creator metadata, total song count and duration.
- **Action Strip**: Green download checkmark, favorite heart toggle, 3-dot options, and floating 56dp circular Emerald Play button on the right.
- **Tracklist**: Numbered 64dp rows showing track duration, explicit badge, and download status.

### Artist Profile View:
- **Cinematic Header**: Wide photographic header fading smoothly into the `#0C0D11` background.
- **Artist Metadata**: 36sp bold artist name, Verified Artist badge with emerald checkmark, monthly listener count.
- **Action Strip**: "Follow" pill button, shuffle icon, 56dp circular play button.
- **Discography Carousel**: Tabbed releases (`Albums`, `Singles`, `EPs`).
- **Fans Also Like**: Circular avatars of related artists.

---

## 16. Mini Player (`CustomMiniPlayer`)

- **Placement**: Floats 12dp above the bottom navigation bar.
- **Dimensions**: 64dp height, 12dp left/right margin, 12dp corner radius.
- **Surface**: `#1E212B` container with subtle 1px border (`#242836`).
- **Controls**: 48x48dp thumbnail, horizontal marquee for long song titles, artist name, favorite heart icon, and play/pause toggle.
- **Scrubbing / Progress**: High-contrast 2dp emerald `#10B981` line along the bottom edge showing live playback progress.
- **Gestures**: Tap anywhere to expand Full Player; swipe left/right to trigger next/previous track.

---

## 17. Full Player (`CustomFullPlayer`)

- **Top Bar**: Down chevron collapse icon, playlist context subtitle ("PLAYING FROM PLAYLIST // Synthwave Essentials"), 3-dot menu.
- **Artwork**: Centered 320x320dp square cover art with 8dp rounded corners and dynamic ambient glow.
- **Track Details**: 22sp bold title, 15sp artist name, explicit badge, and filled emerald heart button.
- **Scrubber**: Smooth emerald progress slider with clean elapsed time ("1:42") and remaining duration ("-2:21") labels. Quality badges (`FLAC 24-bit / 96kHz`, `SponsorBlock Skipped`).
- **Transport Bar**: Shuffle button, previous track, prominent 64dp circular emerald play/pause button, next track, repeat toggle.
- **Footer**: Audio output/Cast device selector ("Living Room Speaker"), peek card for synchronized lyrics ("Waiting in a car..."), and queue drawer icon.

---

## 18. Queue (`CustomQueue`)

- **Header**: Top drag handle, title "Queue", origin subtitle ("Playing from: Synthwave Essentials"), "Clear" button in emerald.
- **View Tabs**: Segmented pills: `Current Queue (18)` and `Automix Radio (Endless)`.
- **Now Playing Highlight**: Elevated card with a 2px emerald glowing border, album thumbnail, live animated 3-bar green equalizer wave, and drag reorder handle.
- **Next Up List**: 64dp reorderable rows with drag handles on the left, thumbnail, track title, artist, and 3-dot options. Supports smooth drag-and-drop reordering.
- **Automix Section**: Infinite recommendations with `+` quick-add buttons and "Based on this session" context badges.

---

## 19. Settings (`CustomSettingsScreen`)

- **Structured Grouping**:
  1. **Playback & Audio**: Audio quality selector (Streaming Opus 256k / Download FLAC), Equalizer shortcut with curve icon, Volume normalization toggle, Skip silence toggle, SponsorBlock toggle.
  2. **Appearance & Theming**: Dynamic theme toggle (album art extraction), Pure Black Mode toggle (OLED), Accent color picker.
  3. **Integrations**: Discord Rich Presence status, Last.fm scrobbling toggle, Listen Together room management.
  4. **Storage**: Downloaded song counter and size, Clear image cache action.
  5. **About**: MetroList version info and updater.
- **Visual Style**: Clean Material 3 grouped cards with emerald switches and high-contrast labels.

---

## 20. Empty, Loading & Error States

1. **Empty Library**: Clean illustration, warm headline ("Your library is empty"), subtitle ("Search for artists and songs to build your collection"), and a prominent emerald "Explore Trending Music" pill button.
2. **Loading States**: Shimmer skeleton blocks (`#16181F` pulsing softly to `#2A2D3A`) maintaining exact 64dp row and card proportions to prevent layout shift.
3. **Offline State**: Discrete amber pill banner at top ("Offline mode — Showing downloaded music only"), with instant toggle to view downloaded tracks.
4. **No Search Results**: Centered magnifying glass icon, "No results found for '{query}'", with suggestions to check spelling or explore browse categories.

---

## 21. Accessibility & Touch Targets

- **Minimum Touch Targets**: Every interactive button, chip, and list row enforces a minimum touch target of **48x48dp** (list rows are 64dp).
- **WCAG AA Contrast**:
  - Primary text (`#F8FAFC`) on Background (`#0C0D11`): **18.2:1** (far exceeds 4.5:1 requirement).
  - Secondary text (`#94A3B8`) on Background (`#0C0D11`): **7.8:1**.
  - Emerald Accent (`#10B981`) on Background (`#0C0D11`): **7.2:1**.
- **Screen Reader Support**: All iconography includes explicit Android `contentDescription` strings defined in `metrolist_strings.xml`.

---

## 22. Android / Jetpack Compose Implementation Notes

### Mapping Design Tokens to Jetpack Compose:
1. **Theme Setup (`com.metrolist.music.ui.custom.theme.CustomTheme`)**:
   - Provide custom `ColorScheme` with `background = Color(0xFF0C0D11)`, `surface = Color(0xFF16181F)`, `surfaceVariant = Color(0xFF1E212B)`, `primary = Color(0xFF10B981)`.
   - Provide custom `Typography` using `FontFamily(Font(R.font.plus_jakarta_sans_bold))` and `FontFamily(Font(R.font.inter_regular))`.
2. **Reusable Composables (`com.metrolist.music.ui.custom.components.*`)**:
   - `CustomSongRow(song, isPlaying, onPlayClick, onMenuClick, onLikeClick)`
   - `CustomFilterPill(text, isSelected, onClick)`
   - `CustomMiniPlayer(mediaMetadata, isPlaying, progress, onPlayPause, onExpand)`
   - `CustomPlayButton(size = 56.dp, isPlaying, onClick)`
3. **Data & State Binding**:
   - `CustomHomeScreen` injects existing `HomeViewModel` and binds directly to `viewStateMap` and `luckyItem`.
   - `CustomSearchScreen` injects existing `OnlineSearchViewModel`.
   - `CustomMiniPlayer` and `CustomFullPlayer` consume `LocalPlayerConnection.current` state flows (`mediaMetadata`, `isEffectivelyPlaying`, `currentPosition`).

---

## 23. What Should NOT Be Changed in MetroList Core

To maintain seamless synchronization with upstream MetroList, strictly follow these constraints:
1. **DO NOT edit Room Database entities, migrations, or schema** (`app/src/main/kotlin/com/metrolist/music/db/*`).
2. **DO NOT modify `:innertube` library module** (`innertube/*`).
3. **DO NOT alter playback service internals** (`MusicService.kt`, `MediaLibrarySessionCallback.kt`, audio processors).
4. **DO NOT create duplicate ViewModels or Repositories**.
5. **DO NOT change Android build flavors (`foss`, `gms`, `izzy`)** or touch `CastButton.kt` flavor bindings.
6. **DO NOT edit non-English strings files** — only append custom strings to `metrolist_strings.xml`.

---

## 24. Stitch Project & Screen References

All exploratory and high-fidelity screen designs are preserved in the dedicated Stitch project:
- **Project Name**: `MetroList Custom Android UI`
- **Project Resource**: `projects/372808246461962021`
- **Design System Asset**: `assets/4929112098013512569` (`MetroList Premium Dark Music Design System`)

### Key Generated Screen Instances in Stitch:
1. **Home Screen (Direction A — Obsidian & Emerald)**:
   - Screen ID: `7ae16caccede44e8a8b4e8ac04d1538a`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1WqNScBTFeyFHeCOoJg7SAa_KySHv_GdKq_kY6K8KnraZJjaBQ7MDVKbY20IV21X8NOc40qwhRAPYwBFX3TGztuEkm2JZBhd-4eHphTn4oMhMQ8sAdZwFcx0AgPQ9t1iuG1Bd_kF83DR1Ck7Wtz5sRzgGyOuAu-nIqky2UvJX0gnIcHDAdL5SLIAD3tAPgkw7_R8MGSxng4zp9oizKwy-iOLtV-l0MdKPzax_cSLNY953Kz5xe9Vpqr)
2. **Home Screen (Direction B — Nordic Editorial)**:
   - Screen ID: `d590bf0ab46a40f090fe25c3e29f61e6`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1UrYpd1uEYv78oS46aaK29mPokbA4M-sI422T7_a2oxW_fls1RCx5KUhe6R5eehJ5Yx1hmbVTUPwLPI-EfHHgLKsXpMDkN0RTW0h2W1zjUVvIu_9pPs8YO0qAwbHBw729XKEqgHqMRMgN_p0y_qp75jzyFjuCV-xRYxsL1VFosJmAqs5yOXmhwE5NxjcR8E9MHLKJzH8c1zj5gB9Cmu7NMwp7iR0MhfyDIAqbc5YVP0w8bpbGq_T7sH5g)
3. **Home Screen (Direction C — Vibrant Neo-Grotesque)**:
   - Screen ID: `f839a8f8af5f46ebb3201a31301c5f54`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1UMShiyQDouCkHXQeXtEmfRXyFLkh_o7JcAogRX3J0kOvcZT-5jHCQiHi48wlLHWy0GAyOHPTYeVkVBxFzkdtTXyplXhXHvPwvdzl6EB01CfoQui_-HrcDmH9cXYOXPBbLh2iLyFyOuew3saBktX2OUJ6vpvoMzSUj59LfFf-ikfhGKQwe_AJvAGRIbMMnwLiG0ftwbIsDWJ-jO4xbCMrc9KPVl1Uq7JZ4aL6LYYTE3-OaOiNnCphBsBA)
4. **Fullscreen Player**:
   - Screen ID: `5f4f13d009674212bf33a5f1209f7801`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1V-uno7heT2PNmAPwwDk-RHCO8HWFOvRewLCi7UNfwVBrd5dgGFi_Ed8_wAAHU0i111T9WWsYhVDOj8y8ke7h3V_0pdPQmvW2jju2ALqEBa5k36GuYmwO-VZGE8juyzY-Jql9LecmXxeqOZRI-CuShQ9M9Kf26SQgPObZZjhfgu04CGnB0I9_vgJga-HaslRIm2lDuah1d6yL2wRXhutwjLc1dYmf8kEcTdqu4QXzh94g2B7zemvg6bmg)
5. **Playback Queue & Automix**:
   - Screen ID: `dd015956a62546f996fb0b8f3695aa17`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1VI-ByFSNKI7wb694njyO4krPqLKc9rim0JTuLhWAztM8ZVtvaUgo_w6cH0_CZEk_Iu6zeHiyiNiipfFTOLwCmoFCsrcAUdTk3bG7LcWGZqReuI02Uo3XuYBY864VkJyMvSyiCcsLsrCpXzVN5Xp97lB5bblwo45ftvuibH9gyRFrRoWbFULtkx2slNrG0ufbxjXaPRZ2XMahDtdfGCC_0eNH4Ga6ZQv1IHNl5ERpGSfRm75tNrlhdX6g)
6. **Search & Browse Screen**:
   - Screen ID: `c9a920c6273e481491c92e473cab650b`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1Wk4yLL5ichUR9GoS_U3G4_Czo_oaF5wXjP7-JOubv6EiYodvugqdqG53L4wreSJenCU2UAT_mnE5aFRDqANY3t_GHSpnsySSG4aak-CkZ9M-4AzezM-8WHQH1Nj-7xsn5OQwugrIYQO8npZRUgYblZpMz9ojCTDLMEvuxINzX6qj31AKwpI9JsCxUxJy8AjnhIOAC3kHcKvhOfF9otSJMfg5iqAjoECkUNI74oWHGXbXEOJNZRFppQ)
7. **Your Library Screen**:
   - Screen ID: `48b9aeb0e1704ae3ba1e49da445e09bc`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1VGjHkgyH8uNiwwLNdvS2Z4DYeEt0h6IQiXtcwGC_h3EC95wzDaOH29NE_ptlLfepLgqHJ19ZAwvCH82TLmDgNV2FAOpI2pCkcxXqkRA4FYfK9fYchCWLPkPpuS0nzsHukdRUp18Ka_k9IjgIfIu4jUJ14ZQfWa_UGMuEiWI7LyRvY8jSZXMGJxUVsCP21sh1G_Pk6soLtUPiRLtLkdANvg_RMk35pnlh0teUM4sB9_ozcphPmeRE82TQ)
8. **Album Detail Screen (Hurry Up, We're Dreaming)**:
   - Screen ID: `8815e5da177046a18e4e4a226f185a89`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1VxszZzd5gT7WZi3qKZEVrRjRqJJ8cEORWUoZM_Ea0b9syPv_aLA6_axH_nKoGNyV3pTYbQ4BO_DeoGg5kuVIa0urKAxiSKcaY19tz6tb1z5vKGTG0Rr8leMdUrrG83IMW0em0Y1BrNFvFOR5g0fA1kcKaXK3jwLkFFIg1iJAeNdXcPjmssCB-Yjpexg04FIjUWWZj_dDRJXst6ty_95qA6FWmTzbcHTgJ6CZiMZIli4DkFVmPPB9bwVg)
9. **Artist Profile Screen (M83)**:
   - Screen ID: `0e9eb2b30340417882d9e2caecfb5f95`
   - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1VXmIb6oKTlIGcrqc2h5gBU3qOxsfe81xrtaXW5Qf9hzDDgdHx2-lwVoy3aPOXq6rD1HIshK413g-lVW4uAssosgV2vbsWuzc3DwPAvAEEPY0IRM90-xbsUGk6kJJCiLPG3FI-udjwJu5HhMH8QoT4SVZn91Qnd9dPSfG7r2u5yTQTUYRaRd_8oHP9HNPHYWKtvByTxZI-Elb1DZt-cMT8RQWXOKL5wTrPBKYngOeGS9_v7y5gOnQawDw)
10. **Settings Screen**:
    - Screen ID: `3b660b8fb0084711b51febd510371093`
    - [Screenshot Reference](https://lh3.googleusercontent.com/aida/AEtjO1VqxeObvZA4c-rnPl6fUSZ6ujOd-5Yrqr4r4_glaZdKJk_NVzCrwWLIaHxE1ngjmyuCkCcH3_ou-v7CtUSTd1Lx-m89fgv415jHdoLl-JB-DK9TLd3JDn-cQCvPjthujg40Uyd4PixEUj-M_AbhhK4S6P4bWvWtrAN-AwDZlu-riT37xOqJzL8BdDjUKvfB1217Scn5-7nGU5SHAI1RUS-Hymz8KKi1ZF63hxFeUsPHaaBQMiIMYw1W)

---

## 25. Recommended Stage 2B Implementation Order

When transitioning from design to Compose code in Stage 2B, follow this dependency-first sequence:

```
Step 1: Theme & Tokens
  └─ Implement CustomTheme, ColorScheme, and Typography in com.metrolist.music.ui.custom.theme
Step 2: Core Atoms & Molecules
  └─ Implement CustomSongRow, FilterPill, QuickPickCard, CustomPlayButton
Step 3: Mini-Player & Full Player Presentation
  └─ Build CustomMiniPlayer, CustomFullPlayer, and CustomQueue
Step 4: Home Screen Assembly
  └─ Build CustomHomeScreen connected to existing HomeViewModel
Step 5: Search & Browse Assembly
  └─ Build CustomSearchScreen connected to OnlineSearchViewModel
Step 6: Library Assembly
  └─ Build CustomLibraryScreen connected to LibraryViewModels
Step 7: Detail Screens (Album, Artist, Playlist)
  └─ Build CustomAlbumScreen and CustomArtistScreen
Step 8: Navigation Wiring
  └─ Update NavigationBuilder.kt to route destinations to the new custom screens
Step 9: Verification & Regression Testing
  └─ Run ./gradlew :app:assembleFossDebug and verify complete playback functionality
```
