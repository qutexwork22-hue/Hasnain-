package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.MockPost
import com.example.model.NumberFormatter
import com.example.model.SimulationSpeed
import com.example.ui.theme.TikCyan
import com.example.ui.theme.TikCyanDark
import com.example.ui.theme.TikDarkBorder
import com.example.ui.theme.TikDarkElevated
import com.example.ui.theme.TikDarkSurface
import com.example.ui.theme.TikLightText
import com.example.ui.theme.TikMutedText
import com.example.ui.theme.TikRed
import com.example.ui.theme.TikSubtleText

private val PRESET_AMOUNTS = listOf(
    100L, 500L, 1_000L, 5_000L, 10_000L, 50_000L, 100_000L, 1_000_000L
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomLikesDialog(
    post: MockPost,
    onDismiss: () -> Unit,
    onInjectLikes: (amount: Long, speed: SimulationSpeed) -> Unit,
    onSetExactLikes: (amount: Long) -> Unit,
    onResetPost: () -> Unit
) {
    var customInputText by remember { mutableStateOf("1000") }
    var selectedSpeed by remember { mutableStateOf(SimulationSpeed.VIRAL_SURGE) }
    var showExactCalibration by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val currentAmount = customInputText.toLongOrNull() ?: 0L
    val projectedLikes = post.likesCount + currentAmount

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, TikDarkBorder, RoundedCornerShape(24.dp))
                .testTag("custom_likes_dialog"),
            color = TikDarkSurface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(TikRed.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = TikRed,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Inject Fake Likes",
                                color = TikLightText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Educational Simulator",
                                color = TikCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TikMutedText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Post Preview Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TikDarkElevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.videoThemeEmoji,
                                fontSize = 28.sp,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Column {
                                Text(
                                    text = "@${post.authorUsername}",
                                    color = TikLightText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Current: ${NumberFormatter.formatFull(post.likesCount)} likes",
                                    color = TikMutedText,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Projected pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TikRed.copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "→ ${NumberFormatter.formatCompact(projectedLikes)}",
                                color = TikRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Preset Buttons
                Text(
                    text = "Quick Presets",
                    color = TikLightText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PRESET_AMOUNTS.forEach { amount ->
                        val isSelected = (currentAmount == amount)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) TikRed else TikDarkElevated)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TikRed else TikDarkBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    customInputText = amount.toString()
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("preset_${amount}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${NumberFormatter.formatCompact(amount)}",
                                color = if (isSelected) Color.White else TikLightText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Input Field
                Text(
                    text = "Custom Number of Likes",
                    color = TikLightText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customInputText,
                    onValueChange = { input ->
                        val filtered = input.filter { it.isDigit() }.take(9)
                        customInputText = filtered
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_likes_input"),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = TikRed,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (currentAmount > 0) {
                            Text(
                                text = "+${NumberFormatter.formatCompact(currentAmount)}",
                                color = TikCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TikRed,
                        unfocusedBorderColor = TikDarkBorder,
                        focusedContainerColor = TikDarkElevated,
                        unfocusedContainerColor = TikDarkElevated,
                        focusedTextColor = TikLightText,
                        unfocusedTextColor = TikLightText
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Speed / Animation selector
                Text(
                    text = "Simulation Animation Speed",
                    color = TikLightText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SimulationSpeed.values().forEach { speed ->
                        val isSelected = selectedSpeed == speed
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) TikDarkElevated else Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TikCyan else TikDarkBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedSpeed = speed }
                                .padding(12.dp)
                                .testTag("speed_${speed.name}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(
                                        2.dp,
                                        if (isSelected) TikCyan else TikSubtleText,
                                        CircleShape
                                    )
                                    .padding(3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(TikCyan)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = speed.label,
                                    color = if (isSelected) TikLightText else TikMutedText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = speed.description,
                                    color = TikSubtleText,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Educational Disclaimer Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(TikDarkElevated)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TikCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Educational simulation: Numbers are simulated locally to demonstrate metric display and algorithmic ratios.",
                        color = TikMutedText,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Inject Button
                Button(
                    onClick = {
                        if (currentAmount > 0) {
                            onInjectLikes(currentAmount, selectedSpeed)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("inject_likes_confirm_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TikRed,
                        disabledContainerColor = TikDarkBorder
                    ),
                    enabled = currentAmount > 0
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Inject +${NumberFormatter.formatCompact(currentAmount)} Likes Now",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Secondary controls: Reset post or Calibrate exact
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            onResetPost()
                            onDismiss()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = null,
                            tint = TikMutedText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Reset Post",
                            color = TikMutedText,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(
                        onClick = { showExactCalibration = !showExactCalibration }
                    ) {
                        Text(
                            text = if (showExactCalibration) "Hide Calibration" else "Set Exact Count",
                            color = TikCyan,
                            fontSize = 12.sp
                        )
                    }
                }

                if (showExactCalibration) {
                    Spacer(modifier = Modifier.height(8.dp))
                    var exactInput by remember { mutableStateOf(post.likesCount.toString()) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = exactInput,
                            onValueChange = { exactInput = it.filter { c -> c.isDigit() }.take(10) },
                            label = { Text("Exact Likes Value") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TikCyan,
                                unfocusedBorderColor = TikDarkBorder
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val value = exactInput.toLongOrNull() ?: 0L
                                onSetExactLikes(value)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TikCyanDark)
                        ) {
                            Text("Set", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
