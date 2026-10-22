package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.CustomLikesDialog
import com.example.ui.components.FollowerBoostDialog
import com.example.ui.screens.BoostHubScreen
import com.example.ui.screens.EduLabScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TikBlack
import com.example.ui.theme.TikCyan
import com.example.ui.theme.TikDarkSurface
import com.example.ui.theme.TikLightText
import com.example.ui.theme.TikMutedText
import com.example.ui.theme.TikRed
import com.example.viewmodel.SimViewModel

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    FEED("Feed", Icons.Filled.Home, Icons.Outlined.Home, "tab_feed"),
    BOOST("Boost Lab", Icons.Filled.FlashOn, Icons.Outlined.FlashOn, "tab_boost"),
    EDU("Edu Lab", Icons.Filled.School, Icons.Outlined.School, "tab_edu"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(viewModel: SimViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(MainTab.FEED) }
    var showLikesDialog by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }
    var showFollowerDialog by remember { mutableStateOf(false) }

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val currentPostIndex by viewModel.currentPostIndex.collectAsStateWithLifecycle()
    val commentsMap by viewModel.comments.collectAsStateWithLifecycle()
    val profileFollowers by viewModel.profileFollowers.collectAsStateWithLifecycle()
    val profileFollowing by viewModel.profileFollowing.collectAsStateWithLifecycle()
    val profileLikes by viewModel.profileLikes.collectAsStateWithLifecycle()
    val isSimulatingBoost by viewModel.isSimulatingBoost.collectAsStateWithLifecycle()
    val activeBoostMessage by viewModel.activeBoostMessage.collectAsStateWithLifecycle()
    val floatingHearts by viewModel.floatingHearts.collectAsStateWithLifecycle()

    val currentPost = posts.getOrNull(currentPostIndex) ?: posts.first()
    val currentComments = commentsMap[currentPost.id] ?: emptyList()

    // Handle back press
    if (currentTab != MainTab.FEED) {
        BackHandler {
            currentTab = MainTab.FEED
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(TikBlack),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = TikDarkSurface,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (tab == MainTab.BOOST) TikRed else TikCyan,
                            selectedTextColor = TikLightText,
                            indicatorColor = if (tab == MainTab.BOOST) TikRed.copy(alpha = 0.2f) else TikCyan.copy(alpha = 0.2f),
                            unselectedIconColor = TikMutedText,
                            unselectedTextColor = TikMutedText
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.FEED -> {
                    FeedScreen(
                        currentPost = currentPost,
                        isSimulatingBoost = isSimulatingBoost,
                        activeBoostMessage = activeBoostMessage,
                        floatingHearts = floatingHearts,
                        onToggleLike = { viewModel.toggleLikePost(it) },
                        onToggleBookmark = { viewModel.toggleBookmark(it) },
                        onToggleFollow = { viewModel.toggleFollow(it) },
                        onDoubleTapLike = { id, x, y -> viewModel.triggerDoubleTapLike(id, x, y) },
                        onOpenBoostDialog = { showLikesDialog = true },
                        onOpenComments = { showCommentsSheet = true },
                        onNextPost = { viewModel.nextPost() },
                        onPrevPost = { viewModel.previousPost() },
                        onRemoveFloatingHeart = { viewModel.removeFloatingHeart(it) }
                    )
                }

                MainTab.BOOST -> {
                    BoostHubScreen(
                        posts = posts,
                        currentPostIndex = currentPostIndex,
                        profileFollowers = profileFollowers,
                        isSimulating = isSimulatingBoost,
                        onSelectPost = { viewModel.selectPost(it) },
                        onInjectLikes = { postId, count, speed ->
                            viewModel.injectCustomLikes(postId, count, speed)
                        },
                        onInjectFollowers = { viewModel.injectCustomFollowers(it) }
                    )
                }

                MainTab.EDU -> {
                    EduLabScreen(currentPost = currentPost)
                }

                MainTab.PROFILE -> {
                    ProfileScreen(
                        followers = profileFollowers,
                        following = profileFollowing,
                        totalLikes = profileLikes,
                        posts = posts,
                        onSelectPost = { index ->
                            viewModel.selectPost(index)
                            currentTab = MainTab.FEED
                        },
                        onOpenFollowerBoost = { showFollowerDialog = true }
                    )
                }
            }
        }

        // Custom Likes Injection Dialog (Primary Requested Feature)
        if (showLikesDialog) {
            CustomLikesDialog(
                post = currentPost,
                onDismiss = { showLikesDialog = false },
                onInjectLikes = { amount, speed ->
                    viewModel.injectCustomLikes(currentPost.id, amount, speed)
                },
                onSetExactLikes = { exact ->
                    viewModel.setExactLikes(currentPost.id, exact)
                },
                onResetPost = {
                    viewModel.resetPostMetrics(currentPost.id)
                }
            )
        }

        // Comments Bottom Sheet
        if (showCommentsSheet) {
            CommentsBottomSheet(
                comments = currentComments,
                commentsCount = currentPost.commentsCount,
                onDismiss = { showCommentsSheet = false },
                onAddComment = { text, isBot ->
                    viewModel.addComment(currentPost.id, text, isBot)
                },
                onToggleLikeComment = { commentId ->
                    viewModel.toggleLikeComment(currentPost.id, commentId)
                }
            )
        }

        // Follower Boost Dialog
        if (showFollowerDialog) {
            FollowerBoostDialog(
                currentFollowers = profileFollowers,
                onDismiss = { showFollowerDialog = false },
                onInjectFollowers = { count ->
                    viewModel.injectCustomFollowers(count)
                }
            )
        }
    }
}
