package com.qtglobal.practicaltest.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qtglobal.practicaltest.ui.components.EmailCard
import com.qtglobal.practicaltest.ui.components.ErrorView
import com.qtglobal.practicaltest.ui.components.LoadingShimmer
import com.qtglobal.practicaltest.ui.state.EmailUiState
import com.qtglobal.practicaltest.ui.viewmodel.EmailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailViewScreen(
    viewModel: EmailViewModel,
    onPickFile: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Email Viewer",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = onPickFile) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                // Content based on state
                Box(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
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
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Email counter
                                Text(
                                    text = "Email ${state.currentIndex} of ${state.totalCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Email card - scrollable container
                                AnimatedContent(
                                    targetState = state,
                                    transitionSpec = {
                                        fadeIn(animationSpec = tween(300)) +
                                                slideInVertically(initialOffsetY = { it }) togetherWith
                                                fadeOut(animationSpec = tween(300)) +
                                                slideOutVertically(targetOffsetY = { -it })
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) {
                                    EmailCard(
                                        email = it.email,
                                        modifier = Modifier.fillMaxSize()
                                    )
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
    )
}

