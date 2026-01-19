package com.qtglobal.practicaltest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.ui.theme.PracticalTestTheme
import com.qtglobal.practicaltest.util.ImageUtil

@Composable
fun EmailCard(
    email: Email,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sender Information
            Column {
                Row(
                    modifier = Modifier
                ) {
                    Text(
                        text = "Sent : ",
                        modifier
                            .padding(5.dp)
                            .height(IntrinsicSize.Min)
                            .weight(.3f),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = email.senderName,
                        modifier
                            .padding(5.dp)
                            .height(IntrinsicSize.Min)
                            .weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Email : ",
                        modifier
                            .padding(5.dp)
                            .height(IntrinsicSize.Min)
                            .weight(.3f),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = email.senderEmailAddress,
                        modifier
                            .padding(5.dp)
                            .height(IntrinsicSize.Min)
                            .weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            HorizontalDivider()

            // Subject
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Subject : ",
                    modifier
                        .padding(5.dp)
                        .height(IntrinsicSize.Min)
                        .weight(.3f),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = email.subject,
                    modifier
                        .padding(5.dp)
                        .height(IntrinsicSize.Min)
                        .weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider()

            // Body with verification badge
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Body:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    VerificationBadge(
                        isVerified = email.bodyVerified,
                        label = ""
                    )
                }
                Text(
                    text = email.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider()

            // Attached Image with verification badge
            if (email.attachedImage.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Attached Image:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        VerificationBadge(
                            isVerified = email.imageVerified,
                            label = ""
                        )
                    }

                    val imageBitmap = remember(email.attachedImage) {
                        ImageUtil.byteArrayToImageBitmap(email.attachedImage)
                    }

                    imageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Attached image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    } ?: run {
                        // Fallback if image decoding fails
                        Text(
                            text = "Unable to display image",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Email Card - Verified", showBackground = true)
@Composable
private fun EmailCardVerifiedPreview() {
    PracticalTestTheme {
        EmailCard(
            email = Email(
                senderName = "Robert Ngabo",
                senderEmailAddress = "robert@qtglobal.com",
                subject = "Test Email with Image Attachment",
                body = "This is a test email body. with verified badge",
                attachedImage = ByteArray(0), // Empty for preview
                bodyHash = "abc123",
                imageHash = "def456",
                bodyVerified = true,
                imageVerified = true
            )
        )
    }
}

@Preview(name = "Email Card - Failed Verification", showBackground = true)
@Composable
private fun EmailCardFailedPreview() {
    PracticalTestTheme {
        EmailCard(
            email = Email(
                senderName = "Robert Ngabo",
                senderEmailAddress = "robert@qtglobal.com2",
                subject = "Test Email with Image Attachment",
                body = "This is a test email body. wih not verified badge",
                attachedImage = ByteArray(0), // Empty for preview
                bodyHash = "abc123",
                imageHash = "def456",
                bodyVerified = false,
                imageVerified = false
            )
        )
    }
}

