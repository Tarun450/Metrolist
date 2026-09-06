/**
 * MetroList Custom UI — Stage 2B
 * Custom color tokens for the Modern Obsidian & Emerald design system.
 *
 * These tokens define the visual palette established in STAGE_2A_STITCH_DESIGN_SPEC.md.
 * They are independent of MetroList's dynamic MaterialKolor theme and are used
 * exclusively by the custom UI layer (com.metrolist.music.ui.custom.*).
 *
 * Album artwork, artist imagery, and all content data are NEVER recolored by this system.
 * These tokens govern only the application's chrome (backgrounds, surfaces, text, controls).
 */

package com.metrolist.music.ui.custom.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// Core backgrounds
// ---------------------------------------------------------------------------

/** Primary screen canvas. Deep obsidian charcoal. */
val CustomColorBackground = Color(0xFF0C0D11)

/** Nested section backgrounds; slightly lighter than canvas. */
val CustomColorSurfaceLowest = Color(0xFF121419)

/** Content cards, dialogs, carousels. */
val CustomColorSurface = Color(0xFF16181F)

/** Search bar fill, elevated sheets, floating mini-player surface. */
val CustomColorSurfaceHigh = Color(0xFF1E212B)

/** Active filter pill containers, pressed/hovered list rows. */
val CustomColorSurfaceHighest = Color(0xFF2A2D3A)

// ---------------------------------------------------------------------------
// Brand / accent
// ---------------------------------------------------------------------------

/** Signature emerald accent — active playback, slider, active nav items. */
val CustomColorPrimary = Color(0xFF10B981)

/** Pressed/hover state for primary emerald elements. */
val CustomColorPrimaryPressed = Color(0xFF059669)

/** Secondary indigo — badges, stats, verified labels. */
val CustomColorSecondary = Color(0xFF818CF8)

/** Tertiary amber — offline alerts, explicit tags, warnings. */
val CustomColorTertiary = Color(0xFFF59E0B)

// ---------------------------------------------------------------------------
// Text
// ---------------------------------------------------------------------------

/** Primary text: track titles, screen headers, artist names. WCAG 18.2:1 vs background. */
val CustomColorTextPrimary = Color(0xFFF8FAFC)

/** Secondary text: subtitles, album years, search placeholders. WCAG 7.8:1 vs background. */
val CustomColorTextSecondary = Color(0xFF94A3B8)

/** Captions, durations, timestamps. Passes WCAG AA (4.6:1 vs background). */
val CustomColorTextMetadata = Color(0xFF64748B)

/** Disabled text and icon state. */
val CustomColorTextDisabled = Color(0xFF3D4252)

// ---------------------------------------------------------------------------
// Structural
// ---------------------------------------------------------------------------

/** 1dp dividing lines between sections and list rows. */
val CustomColorDivider = Color(0xFF242836)

// ---------------------------------------------------------------------------
// Semantic states
// ---------------------------------------------------------------------------

/** Error state: streaming failures, download errors. */
val CustomColorError = Color(0xFFEF4444)

/** Warning state: offline mode banner. Matches tertiary amber for cohesion. */
val CustomColorWarning = Color(0xFFF59E0B)

/** Success / confirmation state. */
val CustomColorSuccess = Color(0xFF10B981)
