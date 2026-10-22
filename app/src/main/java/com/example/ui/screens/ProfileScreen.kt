package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MockPost
import com.example.model.NumberFormatter
import com.example.ui.theme.TikBlack
import com.example.ui.theme.TikCyan
import com.example.ui.theme.TikDarkBorder
import com.example.ui.theme.TikDarkElevated
import com.example.ui.theme.TikLightText
import com.example.ui.theme.TikMutedText
import com.example.ui.theme.TikRed

@Composable
fun ProfileScreen(
    followers: Long,
    following: Long,
    totalLikes: Long,
    posts: List<MockPost>,
    onSelectPost: (Int) -> Unit,
    onOpenFollowerBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TikBlack)
            .padding(horizontal = 16.dp)
            .testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Studio Profile",
                color = TikLightText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onOpenFollowerBoost) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TikMutedText
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(TikCyan.copy(alpha = 0.2f))
                .border(2.dp, TikCyan, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "👑", fontSize = 42.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Handle & Verified Badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "@creator_lab",
                color = TikLightText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = TikCyan,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Three Metrics Row (Following, Followers, Likes)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileStatItem(
                value = NumberFormatter.formatCompact(following),
                label = "Following"
            )

            ProfileStatItem(
                value = NumberFormatter.formatCompact(followers),
                label = "Followers",
                isHighlighted = true,
                highlightColor = TikCyan,
                onClick = onOpenFollowerBoost
            )

            // Calculate aggregate likes of all posts + profile base
            val totalAllLikes = posts.sumOf { it.likesCount } + totalLikes
            ProfileStatItem(
                value = NumberFormatter.formatCompact(totalAllLikes),
                label = "Likes",
                isHighlighted = true,
                highlightColor = TikRed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Boost Followers Button
        Button(
            onClick = onOpenFollowerBoost,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(40.dp)
                .testTag("profile_boost_followers_button"),
            colors = ButtonDefaults.buttonColors(containerColor = TikDarkElevated),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TikDarkBorder)
        ) {
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = null,
                tint = TikCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "+ Boost Followers",
                color = TikLightText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bio
        Text(
            text = "Educational Social Media Growth Lab 🎬\nTesting how fake likes & followers distort platform metrics.",
            color = TikMutedText,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Video Grid
        Text(
            text = "Simulated Videos (${posts.size})",
            color = TikLightText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(posts) { index, post ->
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(post.gradientStartColor))
                        .clickable { onSelectPost(index) }
                        .testTag("profile_video_item_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = post.videoThemeEmoji, fontSize = 34.sp)

                    // Likes overlay badge at bottom
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = TikRed,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = NumberFormatter.formatCompact(post.likesCount),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Views at bottom right
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = NumberFormatter.formatCompact(post.viewsCount),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(
    value: String,
    label: String,
    isHighlighted: Boolean = false,
    highlightColor: Color = TikLightText,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = value,
            color = if (isHighlighted) highlightColor else TikLightText,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = TikMutedText,
            fontSize = 12.sp
        )
    }
}
