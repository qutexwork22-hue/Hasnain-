package com.example.model

enum class SimulationSpeed(val label: String, val description: String) {
    INSTANT("Instant Burst", "All likes added immediately with particle burst"),
    VIRAL_SURGE("Viral Surge", "Smooth counter ticker rolling up over a few seconds with floating hearts"),
    BOT_DRIP("Bot Drip Farm", "Simulates continuous automated trickle stream of likes")
}

data class GrowthEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val amount: Long,
    val source: String,
    val previousLikes: Long,
    val newLikes: Long
)

data class MockComment(
    val id: String = java.util.UUID.randomUUID().toString(),
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarEmoji: String,
    val authorAvatarColor: Long,
    val text: String,
    val likesCount: Long,
    val isLikedByMe: Boolean = false,
    val timeAgo: String = "2h ago",
    val isCreator: Boolean = false,
    val isVerified: Boolean = false,
    val isSuspectedBot: Boolean = false
)

data class MockPost(
    val id: String,
    val authorUsername: String,
    val authorDisplayName: String,
    val authorAvatarEmoji: String,
    val authorAvatarColor: Long,
    val isVerified: Boolean = true,
    val caption: String,
    val hashtags: List<String>,
    val soundTitle: String,
    val likesCount: Long,
    val commentsCount: Long,
    val bookmarksCount: Long,
    val sharesCount: Long,
    val viewsCount: Long,
    val isLikedByMe: Boolean = false,
    val isBookmarkedByMe: Boolean = false,
    val isFollowedByMe: Boolean = false,
    val gradientStartColor: Long,
    val gradientEndColor: Long,
    val videoThemeEmoji: String,
    val videoTopicTitle: String,
    val growthHistory: List<GrowthEvent> = emptyList()
)

object NumberFormatter {
    fun formatCompact(count: Long): String {
        return when {
            count >= 1_000_000_000 -> {
                val formatted = count.toDouble() / 1_000_000_000.0
                if (formatted >= 100) "${count / 1_000_000_000}B" else String.format(java.util.Locale.US, "%.1fB", formatted)
            }
            count >= 1_000_000 -> {
                val formatted = count.toDouble() / 1_000_000.0
                if (formatted >= 100) "${count / 1_000_000}M" else String.format(java.util.Locale.US, "%.1fM", formatted)
            }
            count >= 10_000 -> {
                val formatted = count.toDouble() / 1_000.0
                if (formatted >= 100) "${count / 1_000}K" else String.format(java.util.Locale.US, "%.1fK", formatted)
            }
            count >= 1_000 -> {
                String.format(java.util.Locale.US, "%,d", count)
            }
            else -> count.toString()
        }
    }

    fun formatFull(count: Long): String {
        return String.format(java.util.Locale.US, "%,d", count)
    }
}
