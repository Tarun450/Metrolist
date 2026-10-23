/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.metrolist.music.ui.custom.theme.CustomColorDivider
import com.metrolist.music.ui.custom.theme.CustomColorSurfaceHigh
import com.metrolist.music.ui.custom.theme.CustomColorTextMetadata
import com.metrolist.music.ui.custom.theme.CustomColorTextPrimary
import com.metrolist.music.ui.custom.theme.CustomShapeLarge
import com.metrolist.music.ui.custom.theme.CustomShapeMedium
import com.metrolist.music.ui.custom.theme.LocalCustomSpacing

@Composable
fun Material3MenuGroup(
    items: List<Material3MenuItemData>
) {
    val spacing = LocalCustomSpacing.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.xxs)
    ) {
        items.forEachIndexed { index, item ->
            val shape = when {
                items.size == 1 -> CustomShapeLarge
                index == 0 -> CustomShapeLarge
                index == items.size - 1 -> CustomShapeLarge
                else -> CustomShapeMedium
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = shape,
                colors = item.cardColors ?: CardDefaults.cardColors(
                    containerColor = CustomColorSurfaceHigh
                ),
                border = CardDefaults.outlinedCardBorder(enabled = true).copy(
                    brush = SolidColor(CustomColorDivider.copy(alpha = 0.45f)),
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Material3MenuItemRow(item = item)
            }
        }
    }
}

@Composable
private fun Material3MenuItemRow(
    item: Material3MenuItemData
) {
    val spacing = LocalCustomSpacing.current
    val enabled = item.onClick != null
    val titleColor = if (enabled) CustomColorTextPrimary else CustomColorTextMetadata
    val descriptionColor = if (enabled) CustomColorTextMetadata else CustomColorTextMetadata.copy(alpha = 0.75f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = { item.onClick?.invoke() }
            )
            .padding(horizontal = spacing.lg, vertical = spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item.icon?.let { icon ->
            androidx.compose.runtime.CompositionLocalProvider(
                LocalContentColor provides
                    if (enabled) CustomColorTextMetadata else CustomColorTextMetadata.copy(alpha = 0.75f)
            ) {
                icon()
            }
            Spacer(modifier = Modifier.width(spacing.md))
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            ProvideTextStyle(
                MaterialTheme.typography.titleMedium.copy(
                    color = titleColor,
                )
            ) {
                item.title()
            }

            item.description?.let { desc ->
                Spacer(modifier = Modifier.height(spacing.xxxs))
                ProvideTextStyle(
                    MaterialTheme.typography.bodyMedium.copy(
                        color = descriptionColor
                    )
                ) {
                    desc()
                }
            }
        }
        item.trailingContent?.let { trailing ->
            Spacer(modifier = Modifier.width(spacing.xs))
            trailing()
        }
    }
}

data class Material3MenuItemData(
    val icon: (@Composable () -> Unit)? = null,
    val title: @Composable () -> Unit,
    val description: (@Composable () -> Unit)? = null,
    val onClick: (() -> Unit)? = null,
    val cardColors: CardColors? = null,
    val trailingContent: (@Composable () -> Unit)? = null
)
