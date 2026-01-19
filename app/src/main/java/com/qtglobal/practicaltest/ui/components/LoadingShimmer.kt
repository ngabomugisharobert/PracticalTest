package com.qtglobal.practicaltest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qtglobal.practicaltest.ui.theme.PracticalTestTheme

@Composable
fun LoadingShimmer(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslateAnim = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sender name shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.linearGradient(
                            colors = shimmerColors,
                            start = Offset(shimmerTranslateAnim.value - 300f, shimmerTranslateAnim.value - 300f),
                            end = Offset(shimmerTranslateAnim.value, shimmerTranslateAnim.value)
                        )
                    )
            )

            // Subject shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.linearGradient(
                            colors = shimmerColors,
                            start = Offset(shimmerTranslateAnim.value - 300f, shimmerTranslateAnim.value - 300f),
                            end = Offset(shimmerTranslateAnim.value, shimmerTranslateAnim.value)
                        )
                    )
            )

            // Body lines shimmer
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.linearGradient(
                                colors = shimmerColors,
                                start = Offset(shimmerTranslateAnim.value - 300f, shimmerTranslateAnim.value - 300f),
                                end = Offset(shimmerTranslateAnim.value, shimmerTranslateAnim.value)
                            )
                        )
                )
            }

            // Image placeholder shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = shimmerColors,
                            start = Offset(shimmerTranslateAnim.value - 300f, shimmerTranslateAnim.value - 300f),
                            end = Offset(shimmerTranslateAnim.value, shimmerTranslateAnim.value)
                        )
                    )
            )
        }
    }
}

@Preview(name = "Loading Shimmer", showBackground = true)
@Composable
private fun LoadingShimmerPreview() {
    PracticalTestTheme {
        LoadingShimmer()
    }
}



