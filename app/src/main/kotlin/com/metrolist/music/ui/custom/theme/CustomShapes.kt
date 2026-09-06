/**
 * MetroList Custom UI — Stage 2B
 * Custom shape tokens for the Modern Obsidian & Emerald design system.
 *
 * Reference: STAGE_2A_STITCH_DESIGN_SPEC.md §7.
 *
 * Shapes are provided both as individual constants (for direct use in Modifier.clip())
 * and as a Material 3 Shapes object (for MaterialTheme integration inside CustomTheme).
 */

package com.metrolist.music.ui.custom.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// Individual shape constants
// ---------------------------------------------------------------------------

/** 0dp — full-width divider lines, bottom sheet backdrop edges. */
val CustomShapeNone = RoundedCornerShape(0.dp)

/** 6dp — album thumbnails in 48x48dp list rows. */
val CustomShapeSmall = RoundedCornerShape(6.dp)

/** 8dp — album cards in carousels, large player artwork. */
val CustomShapeMedium = RoundedCornerShape(8.dp)

/** 12dp — quick-access grid cards, dialogs, mini-player surface. */
val CustomShapeLarge = RoundedCornerShape(12.dp)

/** 16dp — top corners of draggable player sheet and queue drawer. */
val CustomShapeSheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

/** Pill shape — filter chips, search bar, play/pause buttons, follow button. */
val CustomShapePill = RoundedCornerShape(percent = 50)

/** Full circle — artist avatars, circular play FAB. */
val CustomShapeCircle = CircleShape

// ---------------------------------------------------------------------------
// Material 3 Shapes integration for use inside CustomTheme
// ---------------------------------------------------------------------------

/**
 * Maps custom shape tokens to the Material 3 Shapes scale so that
 * Material 3 components (buttons, cards, dialogs) adopt our radius system
 * when rendered inside CustomTheme.
 */
val CustomShapes = Shapes(
    extraSmall = CustomShapeSmall,   // 6dp — thumbnails
    small = CustomShapeMedium,       // 8dp — cards
    medium = CustomShapeLarge,       // 12dp — dialogs, mini-player
    large = CustomShapeSheet,        // 16dp — bottom sheets
    extraLarge = CustomShapePill,    // pill — chips, FABs
)
