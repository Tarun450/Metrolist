/**
 * MetroList Custom UI — Stage 2B
 * Custom spacing tokens for the Modern Obsidian & Emerald design system.
 *
 * Based on a strict 8dp modular scale with a 4dp half-step micro-unit.
 * Reference: STAGE_2A_STITCH_DESIGN_SPEC.md §6.
 *
 * Usage: access through CustomTheme.spacing.* or import individual constants.
 */

package com.metrolist.music.ui.custom.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class CustomSpacing(
    /** 2dp — icon-to-badge gaps, progress bar stroke width. */
    val xxxs: Dp = 2.dp,
    /** 4dp — subtitle vertical margins, chip internal padding. */
    val xxs: Dp = 4.dp,
    /** 8dp — grid gutters between cards, standard row padding. */
    val xs: Dp = 8.dp,
    /** 12dp — floating mini-player margin above navigation dock. */
    val sm: Dp = 12.dp,
    /** 16dp — screen horizontal padding (left/right margins). */
    val md: Dp = 16.dp,
    /** 20dp — comfortable list row internal horizontal padding. */
    val lg: Dp = 20.dp,
    /** 24dp — section separation margins. */
    val xl: Dp = 24.dp,
    /** 32dp — large header margins, hero artwork spacing. */
    val xxl: Dp = 32.dp,
    /** 40dp — generous section top padding. */
    val xxxl: Dp = 40.dp,
    /** 48dp — empty-state buffers, hero banner bottoms. */
    val hero: Dp = 48.dp,
    /** 64dp — song row / list item touch target height. */
    val touchTarget: Dp = 64.dp,
)

/** CompositionLocal providing spacing tokens to the custom UI layer. */
val LocalCustomSpacing = staticCompositionLocalOf { CustomSpacing() }
