package com.metrolist.music.ui.custom.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.metrolist.innertube.models.SongItem
import com.metrolist.music.R
import com.metrolist.music.db.entities.Song
import com.metrolist.music.models.MediaMetadata
import com.metrolist.music.ui.component.PlayingIndicator
import com.metrolist.music.ui.custom.theme.CustomColorPrimary
import com.metrolist.music.ui.custom.theme.CustomColorTextPrimary
import com.metrolist.music.ui.custom.theme.CustomColorTextSecondary
import com.metrolist.music.ui.custom.theme.CustomShapeSmall
import com.metrolist.music.ui.utils.resize
import com.metrolist.music.utils.joinByBullet
import com.metrolist.music.utils.makeTimeString

@Composable
fun CustomSongRow(
    title: String,
    subtitle: String,
    thumbnailUrl: String?,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    showLikeButton: Boolean = true,
    onLikeClick: (() -> Unit)? = null,
) {
    val titleColor = if (isPlaying) CustomColorPrimary else CustomColorTextPrimary
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(
                role = Role.Button,
                onClickLabel = title,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onPlayClick,
            )
            .padding(horizontal = 16.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailUrl?.resize(200, 200))
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CustomShapeSmall),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPlaying) {
                    PlayingIndicator(
                        color = CustomColorPrimary,
                        modifier = Modifier
                            .size(width = 18.dp, height = 16.dp)
                            .padding(end = 6.dp),
                        barWidth = 3.dp,
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = CustomColorTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (showLikeButton && onLikeClick != null) {
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    painter = painterResource(
                        if (isLiked) R.drawable.favorite else R.drawable.favorite_border
                    ),
                    contentDescription = stringResource(R.string.like),
                    tint = if (isLiked) CustomColorPrimary else CustomColorTextSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.more_vert),
                contentDescription = stringResource(R.string.show_more),
                tint = CustomColorTextSecondary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
fun CustomSongRow(
    song: Song,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLikeButton: Boolean = true,
) = CustomSongRow(
    title = song.song.title,
    subtitle = joinByBullet(
        song.orderedArtists.joinToString(", ") { it.name },
        makeTimeString(song.song.duration * 1000L),
    ),
    thumbnailUrl = song.song.thumbnailUrl,
    isPlaying = isPlaying,
    isLiked = song.song.liked,
    showLikeButton = showLikeButton,
    onPlayClick = onPlayClick,
    onLikeClick = onLikeClick,
    onMenuClick = onMenuClick,
    modifier = modifier,
)

@Composable
fun CustomSongRow(
    song: SongItem,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    showLikeButton: Boolean = true,
) = CustomSongRow(
    title = song.title,
    subtitle = joinByBullet(
        song.artists.joinToString(", ") { it.name },
        song.duration?.let { makeTimeString(it * 1000L) },
    ),
    thumbnailUrl = song.thumbnail,
    isPlaying = isPlaying,
    isLiked = isLiked,
    showLikeButton = showLikeButton,
    onPlayClick = onPlayClick,
    onLikeClick = onLikeClick,
    onMenuClick = onMenuClick,
    modifier = modifier,
)

@Composable
fun CustomSongRow(
    mediaMetadata: MediaMetadata,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLikeButton: Boolean = true,
) = CustomSongRow(
    title = mediaMetadata.title,
    subtitle = joinByBullet(
        mediaMetadata.artists.joinToString(", ") { it.name },
        makeTimeString(mediaMetadata.duration * 1000L),
    ),
    thumbnailUrl = mediaMetadata.thumbnailUrl,
    isPlaying = isPlaying,
    isLiked = mediaMetadata.liked,
    showLikeButton = showLikeButton,
    onPlayClick = onPlayClick,
    onLikeClick = onLikeClick,
    onMenuClick = onMenuClick,
    modifier = modifier,
)