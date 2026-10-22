package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HeartCyan
import com.example.ui.theme.HeartGold
import com.example.ui.theme.HeartPink
import com.example.ui.theme.HeartPurple
import com.example.ui.theme.HeartRed
import com.example.viewmodel.FloatingHeart
import kotlin.math.roundToInt

private val HeartColors = listOf(HeartPink, HeartRed, HeartCyan, HeartPurple, HeartGold)

@Composable
fun FloatingHeartsOverlay(
    hearts: List<FloatingHeart>,
    onHeartFinished: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    Box(modifier = modifier.fillMaxSize()) {
        hearts.forEach { heart ->
            SingleFloatingHeart(
                heart = heart,
                screenWidthPx = screenWidthPx,
                screenHeightPx = screenHeightPx,
                onFinished = { onHeartFinished(heart.id) }
            )
        }
    }
}

@Composable
private fun SingleFloatingHeart(
    heart: FloatingHeart,
    screenWidthPx: Float,
    screenHeightPx: Float,
    onFinished: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    val color = remember(heart.colorIndex) {
        HeartColors[heart.colorIndex.coerceIn(HeartColors.indices)]
    }

    LaunchedEffect(heart.id) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = heart.durationMs,
                easing = LinearEasing
            )
        )
        onFinished()
    }

    val currentProgress = progress.value
    val startX = heart.startX * screenWidthPx
    val startY = heart.startY * screenHeightPx

    // Rises up ~350px while gently drifting horizontally
    val currentY = startY - (currentProgress * 450f)
    val currentX = startX + (currentProgress * heart.driftX * 2f)
    val scale = (1f - (currentProgress * 0.3f)) * heart.scale
    val alpha = (1f - (currentProgress * currentProgress)).coerceIn(0f, 1f)
    val rotation = (currentProgress * heart.driftX * 1.5f).coerceIn(-45f, 45f)

    Icon(
        imageVector = Icons.Filled.Favorite,
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .offset { IntOffset(currentX.roundToInt(), currentY.roundToInt()) }
            .size(34.dp)
            .scale(scale)
            .rotate(rotation)
            .alpha(alpha)
    )
}
