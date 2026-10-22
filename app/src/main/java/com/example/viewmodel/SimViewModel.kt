package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.GrowthEvent
import com.example.model.MockComment
import com.example.model.MockPost
import com.example.model.NumberFormatter
import com.example.model.SimulationSpeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class FloatingHeart(
    val id: String = java.util.UUID.randomUUID().toString(),
    val startX: Float, // 0f to 1f relative screen width
    val startY: Float, // 0f to 1f relative screen height
    val scale: Float = 1f,
    val colorIndex: Int = Random.nextInt(5),
    val driftX: Float = (Random.nextFloat() - 0.5f) * 60f,
    val durationMs: Int = Random.nextInt(1200, 2000)
)

class SimViewModel : ViewModel() {

    private val initialPosts = listOf(
        MockPost(
            id = "post_1",
            authorUsername = "viral_creator",
            authorDisplayName = "Alex Rivers ✨",
            authorAvatarEmoji = "🎬",
            authorAvatarColor = 0xFF6C5CE7,
            isVerified = true,
            caption = "POV: When the algorithm finally blesses your 3 AM video idea! 🚀 Turn on sounds for this one!",
            hashtags = listOf("fyp", "algorithm", "viraltrend", "contentcreator", "edu"),
            soundTitle = "original sound - Alex Beats ♫",
            likesCount = 1420L,
            commentsCount = 84L,
            bookmarksCount = 312L,
            sharesCount = 195L,
            viewsCount = 18500L,
            gradientStartColor = 0xFF1A1A2E,
            gradientEndColor = 0xFF16213E,
            videoThemeEmoji = "⚡",
            videoTopicTitle = "Algorithm Test Video #1"
        ),
        MockPost(
            id = "post_2",
            authorUsername = "tech_explains",
            authorDisplayName = "Jordan | Tech Lab 🔬",
            authorAvatarEmoji = "💻",
            authorAvatarColor = 0xFF00B4D8,
            isVerified = true,
            caption = "Educational simulation: Watch how buying fake engagement triggers algorithmic anomaly flags 📉 Notice how ER collapses when followers >> real engagement!",
            hashtags = listOf("edutok", "tech", "socialmedia", "cybersecurity", "analytics"),
            soundTitle = "Cyber Wave Synth - Tech Explains ♫",
            likesCount = 12500L,
            commentsCount = 640L,
            bookmarksCount = 1840L,
            sharesCount = 920L,
            viewsCount = 120000L,
            gradientStartColor = 0xFF0F2027,
            gradientEndColor = 0xFF2C5364,
            videoThemeEmoji = "🤖",
            videoTopicTitle = "Bot Anomaly Lab Video"
        ),
        MockPost(
            id = "post_3",
            authorUsername = "dance_queen",
            authorDisplayName = "Mia Choreography 💃",
            authorAvatarEmoji = "💃",
            authorAvatarColor = 0xFFFF7675,
            isVerified = false,
            caption = "Tried the new viral shuffle in 0.5x speed vs 2x speed! Who else struggled with step 3? 😂",
            hashtags = listOf("dancechallenge", "trending", "tutorial", "shuffle"),
            soundTitle = "Upbeat Bass House (Speed Mix) ♫",
            likesCount = 48200L,
            commentsCount = 1290L,
            bookmarksCount = 4500L,
            sharesCount = 3100L,
            viewsCount = 490000L,
            gradientStartColor = 0xFF2D142C,
            gradientEndColor = 0xFF510A32,
            videoThemeEmoji = "✨",
            videoTopicTitle = "Shuffle Dance Trend"
        ),
        MockPost(
            id = "post_4",
            authorUsername = "tokyo_bites",
            authorDisplayName = "Kenji Eats Tokyo 🍜",
            authorAvatarEmoji = "🍜",
            authorAvatarColor = 0xFFF39C12,
            isVerified = true,
            caption = "Hidden 8-seat Ramen shop in Shinjuku that opens at midnight! Rich tonkotsu broth boiled for 36 hours 🔥",
            hashtags = listOf("japanfood", "ramen", "tokyotravel", "foodie"),
            soundTitle = "Lo-Fi Midnight in Tokyo ♫",
            likesCount = 189400L,
            commentsCount = 3840L,
            bookmarksCount = 15200L,
            sharesCount = 8900L,
            viewsCount = 1450000L,
            gradientStartColor = 0xFF141E30,
            gradientEndColor = 0xFF243B55,
            videoThemeEmoji = "🍜",
            videoTopicTitle = "Midnight Ramen Guide"
        )
    )

    private val initialComments = mapOf(
        "post_1" to listOf(
            MockComment(
                authorUsername = "sarah_vids",
                authorDisplayName = "Sarah Vids",
                authorAvatarEmoji = "🎨",
                authorAvatarColor = 0xFFE84393,
                text = "The audio transition was so smooth!! 🔥",
                likesCount = 142,
                timeAgo = "1h ago"
            ),
            MockComment(
                authorUsername = "mark_tech",
                authorDisplayName = "Mark Tech",
                authorAvatarEmoji = "🚀",
                authorAvatarColor = 0xFF0984E3,
                text = "Wait, is this video testing the new FYP batching algorithm?",
                likesCount = 88,
                timeAgo = "3h ago"
            ),
            MockComment(
                authorUsername = "bot_account_992",
                authorDisplayName = "User_928174",
                authorAvatarEmoji = "🤖",
                authorAvatarColor = 0xFF636E72,
                text = "Nice video check my page for free gift cards!!! 🎁🎁",
                likesCount = 1,
                timeAgo = "4h ago",
                isSuspectedBot = true
            )
        ),
        "post_2" to listOf(
            MockComment(
                authorUsername = "data_nerd",
                authorDisplayName = "Data Nerd",
                authorAvatarEmoji = "📊",
                authorAvatarColor = 0xFF00CEC9,
                text = "Spot on! In real systems, bot likes lack watch-time completion signals, so the algorithm immediately penalizes the video.",
                likesCount = 420,
                timeAgo = "5h ago",
                isVerified = true
            ),
            MockComment(
                authorUsername = "creator_academy",
                authorDisplayName = "Creator Academy",
                authorAvatarEmoji = "🎓",
                authorAvatarColor = 0xFFFDCB6E,
                text = "Educational simulator is great for showing students how vanity metrics can be hollow.",
                likesCount = 195,
                timeAgo = "6h ago"
            )
        )
    )

    private val _posts = MutableStateFlow(initialPosts)
    val posts: StateFlow<List<MockPost>> = _posts.asStateFlow()

    private val _currentPostIndex = MutableStateFlow(0)
    val currentPostIndex: StateFlow<Int> = _currentPostIndex.asStateFlow()

    private val _comments = MutableStateFlow(initialComments)
    val comments: StateFlow<Map<String, List<MockComment>>> = _comments.asStateFlow()

    // Profile counters
    private val _profileFollowers = MutableStateFlow(24800L)
    val profileFollowers: StateFlow<Long> = _profileFollowers.asStateFlow()

    private val _profileFollowing = MutableStateFlow(182L)
    val profileFollowing: StateFlow<Long> = _profileFollowing.asStateFlow()

    private val _profileLikes = MutableStateFlow(251420L)
    val profileLikes: StateFlow<Long> = _profileLikes.asStateFlow()

    // Boost simulation state
    private val _isSimulatingBoost = MutableStateFlow(false)
    val isSimulatingBoost: StateFlow<Boolean> = _isSimulatingBoost.asStateFlow()

    private val _activeBoostMessage = MutableStateFlow<String?>(null)
    val activeBoostMessage: StateFlow<String?> = _activeBoostMessage.asStateFlow()

    private val _floatingHearts = MutableStateFlow<List<FloatingHeart>>(emptyList())
    val floatingHearts: StateFlow<List<FloatingHeart>> = _floatingHearts.asStateFlow()

    private var boostJob: Job? = null

    fun selectPost(index: Int) {
        if (index in _posts.value.indices) {
            _currentPostIndex.value = index
        }
    }

    fun nextPost() {
        val current = _currentPostIndex.value
        if (current < _posts.value.size - 1) {
            _currentPostIndex.value = current + 1
        } else {
            _currentPostIndex.value = 0 // loop
        }
    }

    fun previousPost() {
        val current = _currentPostIndex.value
        if (current > 0) {
            _currentPostIndex.value = current - 1
        } else {
            _currentPostIndex.value = _posts.value.size - 1
        }
    }

    fun toggleLikePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLikedByMe
                    val delta = if (newLiked) 1L else -1L
                    if (newLiked) {
                        spawnFloatingHearts(count = 3, startX = 0.88f, startY = 0.65f)
                    }
                    post.copy(
                        isLikedByMe = newLiked,
                        likesCount = (post.likesCount + delta).coerceAtLeast(0L)
                    )
                } else post
            }
        }
    }

    fun toggleBookmark(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newBookmarked = !post.isBookmarkedByMe
                    val delta = if (newBookmarked) 1L else -1L
                    post.copy(
                        isBookmarkedByMe = newBookmarked,
                        bookmarksCount = (post.bookmarksCount + delta).coerceAtLeast(0L)
                    )
                } else post
            }
        }
    }

    fun toggleFollow(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(isFollowedByMe = !post.isFollowedByMe)
                } else post
            }
        }
    }

    fun triggerDoubleTapLike(postId: String, screenXNormalized: Float = 0.5f, screenYNormalized: Float = 0.5f) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val wasLiked = post.isLikedByMe
                    val delta = if (!wasLiked) 1L else 0L
                    post.copy(
                        isLikedByMe = true,
                        likesCount = post.likesCount + delta
                    )
                } else post
            }
        }
        spawnFloatingHearts(count = 7, startX = screenXNormalized, startY = screenYNormalized)
    }

    /**
     * Primary feature requested:
     * User can add a custom number of fake 'likes' to a specific post.
     * The app visually displays these likes increasing on the post.
     */
    fun injectCustomLikes(
        postId: String,
        amountToAdd: Long,
        mode: SimulationSpeed
    ) {
        if (amountToAdd <= 0) return

        boostJob?.cancel()
        boostJob = viewModelScope.launch {
            _isSimulatingBoost.value = true
            _activeBoostMessage.value = "Injecting +${NumberFormatter.formatFull(amountToAdd)} likes (${mode.label})..."

            val targetPost = _posts.value.find { it.id == postId } ?: return@launch
            val initialLikes = targetPost.likesCount

            when (mode) {
                SimulationSpeed.INSTANT -> {
                    // Instantly apply
                    applyLikesIncrement(postId, amountToAdd, initialLikes, mode.label)
                    spawnFloatingHearts(count = 15, startX = 0.88f, startY = 0.65f)
                    _activeBoostMessage.value = "⚡ +${NumberFormatter.formatCompact(amountToAdd)} Likes Injected Instantly!"
                    delay(2000)
                }

                SimulationSpeed.VIRAL_SURGE -> {
                    // Visual counter rolling up smoothly across ~25 increments
                    val steps = 30
                    val stepDelayMs = 60L
                    val incrementPerStep = (amountToAdd / steps).coerceAtLeast(1L)
                    var accumulated = 0L

                    for (i in 1..steps) {
                        val currentStepAdd = if (i == steps) {
                            amountToAdd - accumulated
                        } else {
                            incrementPerStep
                        }
                        accumulated += currentStepAdd

                        applyLikesIncrement(postId, currentStepAdd, initialLikes, mode.label, isFinal = (i == steps), totalAmount = amountToAdd)
                        // Emit continuous floating hearts stream
                        if (i % 2 == 0) {
                            spawnFloatingHearts(count = 2, startX = 0.88f + (Random.nextFloat() - 0.5f) * 0.05f, startY = 0.65f)
                        }
                        delay(stepDelayMs)
                    }

                    _activeBoostMessage.value = "🚀 Viral Surge Finished! +${NumberFormatter.formatCompact(amountToAdd)} Likes Added"
                    delay(2200)
                }

                SimulationSpeed.BOT_DRIP -> {
                    // Continuous drip stream simulating automated bot network
                    val chunkSize = (amountToAdd / 40).coerceIn(10L, 500L)
                    var remaining = amountToAdd

                    _activeBoostMessage.value = "🤖 Bot Farm Drip active: streaming fake likes..."

                    while (remaining > 0) {
                        val currentChunk = minOf(chunkSize, remaining)
                        remaining -= currentChunk

                        applyLikesIncrement(postId, currentChunk, initialLikes, mode.label, isFinal = (remaining == 0L), totalAmount = amountToAdd)
                        spawnFloatingHearts(count = 1, startX = 0.88f, startY = 0.65f)

                        _activeBoostMessage.value = "🤖 Drip: +${NumberFormatter.formatCompact(amountToAdd - remaining)} / ${NumberFormatter.formatCompact(amountToAdd)}"
                        delay(90L)
                    }

                    _activeBoostMessage.value = "🤖 Bot Drip Simulation Complete (+${NumberFormatter.formatCompact(amountToAdd)} likes)"
                    delay(2500)
                }
            }

            _isSimulatingBoost.value = false
            _activeBoostMessage.value = null
        }
    }

    private fun applyLikesIncrement(
        postId: String,
        delta: Long,
        baseLikes: Long,
        source: String,
        isFinal: Boolean = true,
        totalAmount: Long = delta
    ) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newCount = post.likesCount + delta
                    val history = if (isFinal) {
                        val event = GrowthEvent(
                            amount = totalAmount,
                            source = source,
                            previousLikes = baseLikes,
                            newLikes = newCount
                        )
                        listOf(event) + post.growthHistory.take(15)
                    } else post.growthHistory

                    post.copy(
                        likesCount = newCount,
                        growthHistory = history
                    )
                } else post
            }
        }
    }

    fun setExactLikes(postId: String, exactAmount: Long) {
        val nonNegative = exactAmount.coerceAtLeast(0L)
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val event = GrowthEvent(
                        amount = nonNegative - post.likesCount,
                        source = "Direct Calibration",
                        previousLikes = post.likesCount,
                        newLikes = nonNegative
                    )
                    post.copy(
                        likesCount = nonNegative,
                        growthHistory = listOf(event) + post.growthHistory.take(15)
                    )
                } else post
            }
        }
    }

    fun resetPostMetrics(postId: String) {
        val original = initialPosts.find { it.id == postId } ?: return
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(
                        likesCount = original.likesCount,
                        commentsCount = original.commentsCount,
                        bookmarksCount = original.bookmarksCount,
                        sharesCount = original.sharesCount,
                        viewsCount = original.viewsCount,
                        isLikedByMe = false,
                        growthHistory = emptyList()
                    )
                } else post
            }
        }
    }

    fun injectCustomFollowers(amountToAdd: Long) {
        viewModelScope.launch {
            _profileFollowers.update { (it + amountToAdd).coerceAtLeast(0L) }
            _activeBoostMessage.value = "👤 +${NumberFormatter.formatCompact(amountToAdd)} Profile Followers Injected!"
            delay(2000)
            _activeBoostMessage.value = null
        }
    }

    fun addComment(postId: String, text: String, isBot: Boolean = false) {
        if (text.isBlank()) return
        val newComment = MockComment(
            authorUsername = if (isBot) "crypto_bot_77" else "you_creator",
            authorDisplayName = if (isBot) "Fast Likes Service" else "You",
            authorAvatarEmoji = if (isBot) "🤖" else "⭐",
            authorAvatarColor = if (isBot) 0xFF636E72 else 0xFFFE2C55,
            text = text.trim(),
            likesCount = 0,
            timeAgo = "Just now",
            isSuspectedBot = isBot
        )

        _comments.update { map ->
            val existing = map[postId] ?: emptyList()
            map + (postId to (listOf(newComment) + existing))
        }

        // Increment post commentsCount
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(commentsCount = post.commentsCount + 1)
                } else post
            }
        }
    }

    fun toggleLikeComment(postId: String, commentId: String) {
        _comments.update { map ->
            val list = map[postId] ?: return@update map
            val updated = list.map { c ->
                if (c.id == commentId) {
                    val wasLiked = c.isLikedByMe
                    val delta = if (wasLiked) -1L else 1L
                    c.copy(
                        isLikedByMe = !wasLiked,
                        likesCount = (c.likesCount + delta).coerceAtLeast(0L)
                    )
                } else c
            }
            map + (postId to updated)
        }
    }

    private fun spawnFloatingHearts(count: Int, startX: Float, startY: Float) {
        val newHearts = (0 until count).map {
            FloatingHeart(
                startX = startX.coerceIn(0.1f, 0.9f),
                startY = startY.coerceIn(0.1f, 0.9f),
                scale = Random.nextFloat() * 0.6f + 0.8f
            )
        }
        _floatingHearts.update { (it + newHearts).takeLast(40) }
    }

    fun removeFloatingHeart(id: String) {
        _floatingHearts.update { list -> list.filterNot { it.id == id } }
    }

    fun createNewCustomPost(
        username: String,
        displayName: String,
        caption: String,
        hashtags: List<String>,
        soundTitle: String,
        initialLikes: Long
    ) {
        val newPost = MockPost(
            id = "custom_" + System.currentTimeMillis(),
            authorUsername = username.removePrefix("@"),
            authorDisplayName = displayName,
            authorAvatarEmoji = "🌟",
            authorAvatarColor = 0xFF00CEC9,
            isVerified = true,
            caption = caption,
            hashtags = hashtags,
            soundTitle = soundTitle,
            likesCount = initialLikes,
            commentsCount = 12L,
            bookmarksCount = 35L,
            sharesCount = 18L,
            viewsCount = (initialLikes * 4).coerceAtLeast(100L),
            gradientStartColor = 0xFF2C3E50,
            gradientEndColor = 0xFF000000,
            videoThemeEmoji = "🎬",
            videoTopicTitle = "Custom Mock Video"
        )
        _posts.update { listOf(newPost) + it }
        _currentPostIndex.value = 0
    }
}
