/**
 * MetroList Custom UI — Stage 2B
 * Central theme provider for the Modern Obsidian & Emerald design system.
 *
 * Architecture:
 *   CustomTheme wraps future custom screens (CustomHomeScreen, CustomSearchScreen, etc.)
 *   without touching MetroList's existing MetrolistTheme or any functional layer.
 *
 * Usage:
 *   CustomTheme {
 *       CustomHomeScreen(...)
 *   }
 *
 * Design constraints preserved:
 *   - Album artwork colors are NEVER overridden — Coil loads artwork as-is.
 *   - MetroList's dynamic palette extraction (extractThemeColor) is untouched.
 *   - MetroList's existing MetrolistTheme continues to wrap the entire app;
 *     CustomTheme is an inner wrapper applied only to custom screen composables.
 *   - No new dependencies are introduced.
 *
 * Reference: STAGE_2A_STITCH_DESIGN_SPEC.md §4 Color System, §22 Implementation Notes.
 */

package com.metrolist.music.ui.custom.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// Custom color scheme — dark-first Obsidian & Emerald palette
// ---------------------------------------------------------------------------

/**
 * A Material 3 [ColorScheme] populated with the Obsidian & Emerald tokens.
 *
 * Mapping strategy:
 *   Material 3 role          → Custom token
 *   background               → CustomColorBackground      (#0C0D11)
 *   surface                  → CustomColorSurface          (#16181F)
 *   surfaceVariant           → CustomColorSurfaceHigh      (#1E212B)
 *   surfaceContainer         → CustomColorSurfaceHighest   (#2A2D3A)
 *   primary                  → CustomColorPrimary          (#10B981 Emerald)
 *   onPrimary                → CustomColorBackground (contrast text on emerald)
 *   secondary                → CustomColorSecondary        (#818CF8 Indigo)
 *   tertiary                 → CustomColorTertiary         (#F59E0B Amber)
 *   onSurface                → CustomColorTextPrimary      (#F8FAFC)
 *   onSurfaceVariant         → CustomColorTextSecondary    (#94A3B8)
 *   outline                  → CustomColorDivider          (#242836)
 *   error                    → CustomColorError            (#EF4444)
 */
private val CustomDarkColorScheme = ColorScheme(
    // Core backgrounds
    background = CustomColorBackground,
    onBackground = CustomColorTextPrimary,

    // Surfaces
    surface = CustomColorSurface,
    onSurface = CustomColorTextPrimary,
    surfaceVariant = CustomColorSurfaceHigh,
    onSurfaceVariant = CustomColorTextSecondary,
    surfaceTint = CustomColorPrimary,
    inverseSurface = CustomColorTextPrimary,
    inverseOnSurface = CustomColorBackground,

    // Containers (used by MaterialTheme.colorScheme.surfaceContainer*)
    surfaceContainerLowest = CustomColorSurfaceLowest,
    surfaceContainerLow = CustomColorSurface,
    surfaceContainer = CustomColorSurfaceHigh,
    surfaceContainerHigh = CustomColorSurfaceHighest,
    surfaceContainerHighest = CustomColorSurfaceHighest,

    // Primary (Emerald)
    primary = CustomColorPrimary,
    onPrimary = CustomColorBackground,
    primaryContainer = Color(0xFF064E3B),      // deep emerald container
    onPrimaryContainer = Color(0xFFD1FAE5),    // light mint for text on container
    inversePrimary = Color(0xFF059669),

    // Secondary (Indigo)
    secondary = CustomColorSecondary,
    onSecondary = CustomColorBackground,
    secondaryContainer = Color(0xFF1E1B4B),    // deep indigo container
    onSecondaryContainer = Color(0xFFE0E7FF),

    // Tertiary (Amber)
    tertiary = CustomColorTertiary,
    onTertiary = CustomColorBackground,
    tertiaryContainer = Color(0xFF451A03),     // deep amber container
    onTertiaryContainer = Color(0xFFFEF3C7),

    // Structural
    outline = CustomColorDivider,
    outlineVariant = Color(0xFF1A1D26),

    // Error
    error = CustomColorError,
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2),

    // Scrim (dialogs, bottom sheets)
    scrim = Color(0xCC000000),
)

// ---------------------------------------------------------------------------
// CustomTheme composable
// ---------------------------------------------------------------------------

/**
 * Theme wrapper for all custom MetroList UI screens.
 *
 * Provides:
 *   - Obsidian & Emerald [ColorScheme] via [MaterialTheme]
 *   - [CustomTypography] via [MaterialTheme]
 *   - [CustomShapes] via [MaterialTheme]
 *   - [CustomSpacing] via [LocalCustomSpacing]
 *
 * Intentionally does NOT:
 *   - Replace MetrolistTheme (the existing app-level theme).
 *   - Modify album artwork loading, colors, or data.
 *   - Touch any playback, database, or service layer.
 *
 * Nesting: MetrolistTheme { ... CustomTheme { CustomHomeScreen() } ... }
 */
@Composable
fun CustomTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCustomSpacing provides CustomSpacing(),
    ) {
        MaterialTheme(
            colorScheme = CustomDarkColorScheme,
            typography = CustomTypography,
            shapes = CustomShapes,
            content = content,
        )
    }
}
