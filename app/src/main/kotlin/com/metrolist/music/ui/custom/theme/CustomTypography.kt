/**
 * MetroList Custom UI — Stage 2B
 * Custom typography tokens for the Modern Obsidian & Emerald design system.
 *
 * Reference: STAGE_2A_STITCH_DESIGN_SPEC.md §5.
 *
 * DESIGN INTENT:
 *   - Headings / titles / actions: Plus Jakarta Sans (Bold 700, SemiBold 600)
 *   - Body / metadata / dense lists: Inter (Regular 400, SemiBold 600)
 *
 * BUNDLED FONTS:
 *   The project currently ships only `bbh_bartle_regular.ttf` in res/font/.
 *   To activate the designed typefaces, add the following TTF files to
 *   app/src/main/res/font/ WITHOUT modifying this file's structure:
 *
 *     res/font/plus_jakarta_sans_bold.ttf      (Plus Jakarta Sans Bold 700)
 *     res/font/plus_jakarta_sans_semibold.ttf  (Plus Jakarta Sans SemiBold 600)
 *     res/font/inter_semibold.ttf              (Inter SemiBold 600)
 *     res/font/inter_regular.ttf               (Inter Regular 400)
 *
 *   Then replace FontFamily.Default usages below with:
 *     FontFamily(Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold))
 *     FontFamily(Font(R.font.inter_regular, FontWeight.Normal))
 *
 *   Until those files are added, the system default (Roboto) is used as
 *   a neutral fallback. Layout dimensions, sizes, and weights remain correct.
 *
 * NO new Gradle dependencies are required — this uses only
 * androidx.compose.ui:ui-text which is already in the project.
 */

package com.metrolist.music.ui.custom.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Font families
// ---------------------------------------------------------------------------

/**
 * Display / heading family — intended for Plus Jakarta Sans.
 * Falls back to system default (Roboto) until TTF files are bundled.
 */
private val DisplayFamily = FontFamily.Default

/**
 * Body / metadata family — intended for Inter.
 * Falls back to system default (Roboto) until TTF files are bundled.
 */
private val BodyFamily = FontFamily.Default

// ---------------------------------------------------------------------------
// Custom Typography — maps to Material 3 scale
// ---------------------------------------------------------------------------

/**
 * Custom typography for all screens rendered inside CustomTheme.
 *
 * Size | Weight | Usage
 * -----|--------|------
 * 32sp | Bold   | Display: artist hero titles, Wrapped banners
 * 24sp | Bold   | Headline: screen titles ("Your Library", "Search")
 * 20sp | SemiBold | Title large: album titles, full-player track title
 * 16sp | SemiBold | Title medium: section headers ("Jump Back In")
 * 15sp | SemiBold | Body bold: track titles in song rows, queue items
 * 13sp | Regular  | Body regular: artist names, album subtitles
 * 12sp | Medium   | Label: pill filter text, button labels
 * 11sp | Regular  | Caption: elapsed time, format tags (monospaced style)
 */
val CustomTypography = Typography(
    // 32sp Bold — artist hero banners, Wrapped headers
    displayLarge = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.03).sp,
    ),
    // 24sp Bold — screen titles
    headlineMedium = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.02).sp,
    ),
    // 20sp SemiBold — album titles, full-player track title
    titleLarge = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.01).sp,
    ),
    // 16sp SemiBold — section headers
    titleMedium = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
    ),
    // 14sp Medium — sub-headers, secondary labels
    titleSmall = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // 15sp SemiBold — song row track titles, queue items
    bodyLarge = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // 13sp Regular — artist names, album subtitles
    bodyMedium = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    // 11sp Regular — captions, elapsed time
    bodySmall = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.04.sp,
    ),
    // 12sp Medium — pill filter chips, button labels
    labelMedium = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.02.sp,
    ),
    // 11sp Regular — smallest captions
    labelSmall = TextStyle(
        fontFamily = BodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.04.sp,
    ),
)
