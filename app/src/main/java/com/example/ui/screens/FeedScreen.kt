package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MockPost
import com.example.model.NumberFormatter
import com.example.ui.components.FloatingHeartsOverlay
import com.example.ui.theme.HeartPink
import com.example.ui.theme.TikBlack
import com.example.ui.theme.TikCyan
import com.example.ui.theme.TikGold
import com.example.ui.theme.TikLightText
import com.example.ui.theme.TikMutedText
import com.example.ui.theme.TikRed
import com.example.viewmodel.FloatingHeart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    currentPost: MockPost,
    isSimulatingBoost: Boolean,
    activeBoostMessage: String?,
    floatingHearts: List<FloatingHeart>,
    onToggleLike: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onToggleFollow: (String) -> Unit,
    onDoubleTapLike: (postId: String, normX: Float, normY: Float) -> Unit,
    onOpenBoostDialog: () -> Unit,
    onOpenComments: () -> Unit,
    onNextPost: () -> Unit,
    onPrevPost: () -> Unit,
    onRemoveFloatingHeart: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var bigHeartVisible by remember { mutableStateOf(false) }
    var bigHeartPos by remember { mutableStateOf(Offset(0.5f, 0.5f)) }
    val scope = rememberCoroutineScope()

    // Disc rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TikBlack)
            .pointerInput(currentPost.id) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        bigHeartPos = Offset(offset.x / size.width, offset.y / size.height)
                        bigHeartVisible = true
                        onDoubleTapLike(currentPost.id, bigHeartPos.x, bigHeartPos.y)
                        scope.launch {
                            delay(600)
                            bigHeartVisible = false
                        }
                    }
                )
            }
            .testTag("feed_screen")
    ) {
        // Dynamic Simulated Video Background with animated ambient lighting
        SimulatedVideoCanvas(post = currentPost)

        // Overlay: Top Feed Navigation Bar ("Following" | "For You")
        TopFeedNavigation(
            isSimulating = isSimulatingBoost,
            onOpenBoostDialog = onOpenBoostDialog,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        // Switch post indicators (arrows)
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevPost,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Post",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNextPost,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Post",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Right Sidebar: Interactive Actions
        RightSidebarActions(
            post = currentPost,
            discRotation = discRotation,
            isSimulatingBoost = isSimulatingBoost,
            onToggleLike = { onToggleLike(currentPost.id) },
            onToggleBookmark = { onToggleBookmark(currentPost.id) },
            onToggleFollow = { onToggleFollow(currentPost.id) },
            onOpenBoostDialog = onOpenBoostDialog,
            onOpenComments = onOpenComments,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 28.dp)
        )

        // Bottom Left: Video Metadata, Caption, Music Ticker
        BottomPostMetadata(
            post = currentPost,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 24.dp, end = 90.dp)
        )

        // Live Boost Status Banner (shows when fake likes are actively increasing!)
        AnimatedVisibility(
            visible = activeBoostMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
        ) {
            activeBoostMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(TikRed)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("boost_active_banner")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Floating Hearts Layer (streams up on double-tap or when likes are injected!)
        FloatingHeartsOverlay(
            hearts = floatingHearts,
            onHeartFinished = onRemoveFloatingHeart
        )

        // Double-Tap Big Heart Animation
        if (bigHeartVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BigHeartBurst()
            }
        }
    }
}

@Composable
private fun BigHeartBurst() {
    val scale = remember { Animatable(0.2f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f)
        )
        delay(150)
        alpha.animateTo(0f, animationSpec = tween(250))
    }

    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = null,
        tint = HeartPink,
        modifier = Modifier
            .size(110.dp)
            .scale(scale.value)
            .alpha(alpha.value)
            .shadow(16.dp, CircleShape)
    )
}

@Composable
private fun TopFeedNavigation(
    isSimulating: Boolean,
    onOpenBoostDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Educational Simulation Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .border(1.dp, TikCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isSimulating) TikRed else TikCyan)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (isSimulating) "BOOSTING" else "SIMULATOR",
                    color = if (isSimulating) TikRed else TikCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Center Feed Tabs
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Following",
                color = TikMutedText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "For You",
                    color = TikLightText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 2.dp)
                        .background(Color.White, RoundedCornerShape(1.dp))
                )
            }
        }

        // Fast Boost Likes Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(TikRed)
                .clickable(onClick = onOpenBoostDialog)
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("top_boost_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+ Boost",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RightSidebarActions(
    post: MockPost,
    discRotation: Float,
    isSimulatingBoost: Boolean,
    onToggleLike: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleFollow: () -> Unit,
    onOpenBoostDialog: () -> Unit,
    onOpenComments: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Fast Boost Action Button (Glowing)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = onOpenBoostDialog)
                .testTag("sidebar_boost_button")
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(TikRed)
                    .border(2.dp, TikCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Inject Likes",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Boost",
                color = TikCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Creator Avatar with Follow Button
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(post.authorAvatarColor))
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = post.authorAvatarEmoji, fontSize = 22.sp)
            }

            // Follow Plus Icon
            Box(
                modifier = Modifier
                    .offset(y = 6.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (post.isFollowedByMe) TikCyan else TikRed)
                    .clickable(onClick = onToggleFollow)
                    .testTag("follow_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (post.isFollowedByMe) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = "Follow",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Heart / Likes Action with Live Rolling Counter
        LiveRollingHeartAction(
            post = post,
            isSimulatingBoost = isSimulatingBoost,
            onToggleLike = onToggleLike,
            onLongPress = onOpenBoostDialog
        )

        // Comment Button
        ActionItemWithCounter(
            icon = {
                Icon(
                    imageVector = Icons.Default.Sms,
                    contentDescription = "Comments",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            },
            countText = NumberFormatter.formatCompact(post.commentsCount),
            testTag = "feed_comments_button",
            onClick = onOpenComments
        )

        // Bookmark Button
        ActionItemWithCounter(
            icon = {
                Icon(
                    imageVector = if (post.isBookmarkedByMe) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (post.isBookmarkedByMe) TikGold else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            },
            countText = NumberFormatter.formatCompact(post.bookmarksCount),
            testTag = "feed_bookmark_button",
            onClick = onToggleBookmark
        )

        // Share Button
        ActionItemWithCounter(
            icon = {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            },
            countText = NumberFormatter.formatCompact(post.sharesCount),
            testTag = "feed_share_button",
            onClick = onOpenBoostDialog
        )

        // Spinning Music Record Disc
        Box(
            modifier = Modifier
                .size(46.dp)
                .rotate(discRotation)
                .clip(CircleShape)
                .background(Color(0xFF1E1E24))
                .border(2.dp, Color(0xFF2C2D35), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(post.authorAvatarColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "♫", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

/**
 * Visual display of likes increasing on the post in real-time.
 * As the user adds custom fake likes, the counter animates up smoothly,
 * pulses in scale, and displays a celebration glow.
 */
@Composable
private fun LiveRollingHeartAction(
    post: MockPost,
    isSimulatingBoost: Boolean,
    onToggleLike: () -> Unit,
    onLongPress: () -> Unit
) {
    // Track previous value to animate smoothly
    var previousLikes by remember { mutableLongStateOf(post.likesCount) }
    var displayedLikes by remember { mutableLongStateOf(post.likesCount) }

    // Heart bounce animation on change
    val heartScale = remember { Animatable(1f) }

    LaunchedEffect(post.likesCount) {
        if (post.likesCount != previousLikes) {
            heartScale.animateTo(1.35f, animationSpec = tween(120, easing = FastOutSlowInEasing))
            heartScale.animateTo(1f, animationSpec = spring(dampingRatio = 0.5f))
            previousLikes = post.likesCount
        }
    }

    // Smooth counter roll up
    LaunchedEffect(post.likesCount) {
        val start = displayedLikes
        val target = post.likesCount
        if (start != target) {
            val delta = target - start
            // If delta is huge, jump smoothly in ~20 frames
            val frames = 15
            for (f in 1..frames) {
                displayedLikes = start + (delta * f / frames)
                delay(20)
            }
            displayedLikes = target
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pointerInput(post.id) {
                detectTapGestures(
                    onTap = { onToggleLike() },
                    onLongPress = { onLongPress() }
                )
            }
            .testTag("feed_heart_button")
    ) {
        Box(
            modifier = Modifier.size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Like Post",
                tint = if (post.isLikedByMe) TikRed else Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .scale(heartScale.value)
            )

            // Sparkle ring while boosting
            if (isSimulatingBoost) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .border(1.5.dp, TikCyan, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Formatted Animated Counter Text
        Text(
            text = NumberFormatter.formatCompact(displayedLikes),
            color = if (post.isLikedByMe || isSimulatingBoost) TikRed else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("likes_count_text")
        )
    }
}

@Composable
private fun ActionItemWithCounter(
    icon: @Composable () -> Unit,
    countText: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = countText,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BottomPostMetadata(
    post: MockPost,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Creator Handle + Verified
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Text(
                text = "@${post.authorUsername}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            if (post.isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = TikCyan,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        // Caption
        Text(
            text = post.caption,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Hashtags
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            post.hashtags.take(4).forEach { tag ->
                Text(
                    text = "#$tag",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Audio Marquee Ticker
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = post.soundTitle,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SimulatedVideoCanvas(post: MockPost) {
    // Ambient animated gradient simulation of video motion
    val infiniteTransition = rememberInfiniteTransition(label = "video_ambient")
    val ambientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_anim"
    )

    val startColor = Color(post.gradientStartColor)
    val endColor = Color(post.gradientEndColor)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val brush = Brush.radialGradient(
                colors = listOf(
                    startColor.copy(alpha = 0.85f),
                    endColor,
                    Color.Black
                ),
                center = Offset(
                    size.width * (0.3f + ambientOffset * 0.4f),
                    size.height * (0.4f + ambientOffset * 0.2f)
                ),
                radius = size.width * 1.2f
            )
            drawRect(brush = brush)
        }

        // Mock Video Hero Graphics (Card Center)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(0.6f)
        ) {
            Text(
                text = post.videoThemeEmoji,
                fontSize = 92.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = post.videoTopicTitle,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Double-tap screen to Like • Long-press Heart to Boost",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp
            )
        }
    }
}
