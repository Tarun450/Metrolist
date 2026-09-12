package com.metrolist.music.ui.custom.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metrolist.music.ui.custom.theme.CustomColorBackground
import com.metrolist.music.ui.custom.theme.CustomColorPrimary
import com.metrolist.music.ui.custom.theme.CustomColorSurfaceHigh
import com.metrolist.music.ui.custom.theme.CustomColorTextSecondary

@Composable
fun CustomFilterPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        enabled = enabled,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        shape = RoundedCornerShape(percent = 50),
        border = null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = CustomColorSurfaceHigh,
            labelColor = CustomColorTextSecondary,
            selectedContainerColor = CustomColorPrimary,
            selectedLabelColor = CustomColorBackground,
            disabledContainerColor = CustomColorSurfaceHigh,
            disabledLabelColor = CustomColorTextSecondary,
        ),
        modifier = modifier.height(32.dp),
    )
}