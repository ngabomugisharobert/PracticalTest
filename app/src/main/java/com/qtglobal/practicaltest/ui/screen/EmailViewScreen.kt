package com.qtglobal.practicaltest.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.ui.components.EmailCard
import com.qtglobal.practicaltest.ui.components.ErrorView
import com.qtglobal.practicaltest.ui.components.LoadingShimmer
import com.qtglobal.practicaltest.ui.theme.PracticalTestTheme
import com.qtglobal.practicaltest.ui.viewmodel.EmailUiState
import com.qtglobal.practicaltest.ui.viewmodel.EmailViewModel


@Composable
fun EmailViewScreen(
    viewModel: EmailViewModel,
    onPickFile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with file picker button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Email Viewer",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(onClick = onPickFile) {
                Text("Pick File")
            }
        }

        // Content based on state
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is EmailUiState.Idle -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Select a .pb file to view email",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(onClick = onPickFile) {
                            Text("Select File")
                        }
                    }
                }
                is EmailUiState.Loading -> {
                    LoadingShimmer()
                }
                is EmailUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Email counter
                        Text(
                            text = "Email ${state.currentIndex} of ${state.totalCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Email card
                        AnimatedContent(
                            targetState = state,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) + 
                                        slideInVertically(initialOffsetY = { it }) togetherWith
                                        fadeOut(animationSpec = tween(300)) + 
                                        slideOutVertically(targetOffsetY = { -it })
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            EmailCard(email = it.email)
                        }

                        // Navigation buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.navigateToPrevious() },
                                enabled = state.currentIndex > 1
                            ) {
                                Text("Previous")
                            }

                            Text(
                                text = "${state.currentIndex}/${state.totalCount}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            OutlinedButton(
                                onClick = { viewModel.navigateToNext() },
                                enabled = state.currentIndex < state.totalCount
                            ) {
                                Text("Next")
                            }
                        }
                    }
                }
                is EmailUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.resetState() }
                    )
                }
            }
        }
    }
}

// Preview functions for different states
@Preview(name = "Idle State", showBackground = true)
@Composable
private fun EmailViewScreenIdlePreview() {
    PracticalTestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email Viewer",
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = {}) {
                    Text("Pick File")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Select a .pb file to view email",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedButton(onClick = {}) {
                        Text("Select File")
                    }
                }
            }
        }
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
private fun EmailViewScreenLoadingPreview() {
    PracticalTestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email Viewer",
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = {}) {
                    Text("Pick File")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                LoadingShimmer()
            }
        }
    }
}

@Preview(name = "Success State", showBackground = true)
@Composable
private fun EmailViewScreenSuccessPreview() {
    PracticalTestTheme {
        val sampleEmail = Email(
            senderName = "John Doe",
            senderEmailAddress = "john.doe@example.com",
            subject = "Test Email with Image Attachment",
            body = "This is a test email body. It contains some sample text to demonstrate the email viewer functionality.",
            attachedImage = ByteArray(0),
            bodyHash = "abc123",
            imageHash = "def456",
            bodyVerified = true,
            imageVerified = true
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email Viewer",
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = {}) {
                    Text("Pick File")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Email 1 of 3",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    EmailCard(email = sampleEmail)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("Previous")
                        }
                        Text(
                            text = "1/3",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        OutlinedButton(
                            onClick = {},
                            enabled = true
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Error State", showBackground = true)
@Composable
private fun EmailViewScreenErrorPreview() {
    PracticalTestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email Viewer",
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = {}) {
                    Text("Pick File")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ErrorView(
                    message = "Failed to load email file. Please try again.",
                    onRetry = {}
                )
            }
        }
    }
}


