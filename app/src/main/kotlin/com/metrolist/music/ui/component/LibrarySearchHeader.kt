/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metrolist.music.R
import com.metrolist.music.ui.custom.theme.CustomColorPrimary
import com.metrolist.music.ui.custom.theme.CustomColorSurfaceHigh
import com.metrolist.music.ui.custom.theme.CustomColorTextMetadata
import com.metrolist.music.ui.custom.theme.CustomColorTextPrimary
import com.metrolist.music.ui.custom.theme.CustomColorTextSecondary

@Composable
fun LibrarySearchHeader(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
    inactiveContent: @Composable RowScope.() -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isSearchActive) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .weight(1f)
                        .background(
                            color = CustomColorSurfaceHigh,
                            shape = RoundedCornerShape(24.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_library),
                            style = MaterialTheme.typography.titleMedium,
                            color = CustomColorTextSecondary,
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(color = CustomColorTextPrimary),
                    keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = CustomColorTextPrimary,
                        unfocusedTextColor = CustomColorTextPrimary,
                        disabledTextColor = CustomColorTextMetadata,
                        cursorColor = CustomColorPrimary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = CustomColorTextSecondary,
                        unfocusedPlaceholderColor = CustomColorTextSecondary,
                    ),
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                )

                IconButton(
                    onClick = onBack,
                    colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                        contentColor = CustomColorTextSecondary,
                    ),
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close),
                        contentDescription = stringResource(R.string.close),
                    )
                }
            }
        } else {
            inactiveContent()
        }
    }
}

@Composable
fun LibrarySearchEmptyPlaceholder(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.search,
    text: String? = null,
) {
    EmptyPlaceholder(
        icon = icon,
        text = text ?: stringResource(R.string.no_results_found),
        modifier = modifier,
    )
}
