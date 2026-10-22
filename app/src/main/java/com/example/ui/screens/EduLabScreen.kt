package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.TikDarkSurface
import com.example.ui.theme.TikGold
import com.example.ui.theme.TikLightText
import com.example.ui.theme.TikMutedText
import com.example.ui.theme.TikRed
import com.example.ui.theme.TikSubtleText

@Composable
fun EduLabScreen(
    currentPost: MockPost,
    modifier: Modifier = Modifier
) {
    var simulatedSliderLikes by remember { mutableFloatStateOf(currentPost.likesCount.toFloat()) }
    var simulatedSliderViews by remember { mutableFloatStateOf(currentPost.viewsCount.toFloat().coerceAtLeast(100f)) }

    val ratio = if (simulatedSliderViews > 0) (simulatedSliderLikes / simulatedSliderViews) * 100f else 0f
    val isAnomaly = ratio > 60f || (simulatedSliderLikes > 10000 && ratio < 0.2f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TikBlack)
            .padding(horizontal = 16.dp)
            .testTag("edu_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🎓 Educational Lab",
                        color = TikLightText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Understanding vanity metrics & algorithm fraud detection",
                        color = TikMutedText,
                        fontSize = 13.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = TikCyan,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Real-Time Post Diagnostic Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikDarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TikDarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Active Post Metric Analysis",
                        color = TikLightText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricPill(label = "Simulated Likes", value = NumberFormatter.formatCompact(currentPost.likesCount), color = TikRed)
                        MetricPill(label = "Simulated Views", value = NumberFormatter.formatCompact(currentPost.viewsCount), color = TikCyan)
                        MetricPill(label = "Comments", value = NumberFormatter.formatCompact(currentPost.commentsCount), color = TikGold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val actualRatio = if (currentPost.viewsCount > 0) (currentPost.likesCount.toFloat() / currentPost.viewsCount.toFloat()) * 100f else 0f
                    val flagLevel = when {
                        actualRatio > 80f -> "Extreme Anomaly: Likes > Natural Views"
                        actualRatio > 35f -> "Suspicious Spike (Likely Bot Surge)"
                        actualRatio in 4f..20f -> "Natural Organic Engagement Rate"
                        else -> "Low Conversion Rate"
                    }
                    val flagColor = when {
                        actualRatio > 35f -> TikRed
                        actualRatio in 4f..20f -> TikCyan
                        else -> TikGold
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(flagColor.copy(alpha = 0.12f))
                            .border(1.dp, flagColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (actualRatio > 35f) Icons.Default.ReportProblem else Icons.Default.Security,
                                contentDescription = null,
                                tint = flagColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = flagLevel,
                                    color = flagColor,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Like-to-View Ratio: ${String.format(java.util.Locale.US, "%.1f", actualRatio)}%",
                                    color = TikMutedText,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Interactive Fraud Detection Playground Slider
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikDarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TikCyan.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Algorithm Anomaly Simulator",
                        color = TikLightText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Slide values to see how platform AI flags disproportionate engagement:",
                        color = TikSubtleText,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Simulated Likes: ${NumberFormatter.formatCompact(simulatedSliderLikes.toLong())}",
                        color = TikRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = simulatedSliderLikes,
                        onValueChange = { simulatedSliderLikes = it },
                        valueRange = 100f..500_000f,
                        colors = SliderDefaults.colors(
                            thumbColor = TikRed,
                            activeTrackColor = TikRed,
                            inactiveTrackColor = TikDarkBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Simulated Video Views: ${NumberFormatter.formatCompact(simulatedSliderViews.toLong())}",
                        color = TikCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = simulatedSliderViews,
                        onValueChange = { simulatedSliderViews = it },
                        valueRange = 500f..1_000_000f,
                        colors = SliderDefaults.colors(
                            thumbColor = TikCyan,
                            activeTrackColor = TikCyan,
                            inactiveTrackColor = TikDarkBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isAnomaly) TikRed.copy(alpha = 0.15f) else TikCyan.copy(alpha = 0.15f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (isAnomaly) {
                                "🚨 FRAUD ALERT: Real platforms measure Average Watch Duration. If likes spike without corresponding video watch time, the recommendation engine down-ranks or shadow-limits the account."
                            } else {
                                "✅ NATURAL TRAFFIC: Organic posts generally see 4% to 15% like-to-view conversion, accompanied by genuine comments and shares."
                            },
                            color = if (isAnomaly) TikRed else TikCyan,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Educational Facts
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TikDarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TikDarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = TikGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Why Fake Vanity Metrics Don't Work in Real Life",
                            color = TikLightText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FactBullet(
                        title = "1. Watch-Time Completion Trumps Likes",
                        body = "Modern video feeds distribute content based on completion rate and re-watches, not raw like tallies."
                    )
                    FactBullet(
                        title = "2. Ghost Followers Ruin Engagement",
                        body = "Accounts with 100,000 inactive fake followers show a tiny 0.01% engagement rate, causing recommendation models to classify content as uninteresting."
                    )
                    FactBullet(
                        title = "3. Velocity & IP Cluster Signatures",
                        body = "Automated bot scripts add likes in predictable time intervals from proxy clusters, which modern anti-fraud models instantly isolate."
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MetricPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = TikMutedText, fontSize = 11.sp)
    }
}

@Composable
private fun FactBullet(title: String, body: String) {
    Column {
        Text(text = title, color = TikLightText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = body, color = TikMutedText, fontSize = 12.sp, lineHeight = 16.sp)
    }
}
