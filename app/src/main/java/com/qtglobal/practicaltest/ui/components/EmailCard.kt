package com.qtglobal.practicaltest.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.ui.theme.PracticalTestTheme

@Composable
fun EmailCard(
    email: Email,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sender Information
            Column {
                Text(
                    text = email.senderName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = email.senderEmailAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider()

            // Subject
            Text(
                text = email.subject,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Divider()

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

            Divider()

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
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(email.attachedImage)
                            .build(),
                        contentDescription = "Attached image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
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
                senderName = "John Doe",
                senderEmailAddress = "john.doe@example.com",
                subject = "Test Email with Image Attachment",
                body = "This is a test email body. It contains some sample text to demonstrate the email viewer functionality. The email includes verification hashes for both the body text and the attached image.",
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
                senderName = "Jane Smith",
                senderEmailAddress = "jane.smith@example.com",
                subject = "Important Update",
                body = "This email has failed verification. The content may have been tampered with.",
                attachedImage = ByteArray(0), // Empty for preview
                bodyHash = "abc123",
                imageHash = "def456",
                bodyVerified = false,
                imageVerified = false
            )
        )
    }
}

