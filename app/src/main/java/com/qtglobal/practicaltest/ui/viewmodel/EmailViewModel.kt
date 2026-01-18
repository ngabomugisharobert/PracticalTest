package com.qtglobal.practicaltest.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qtglobal.practicaltest.data.repository.EmailRepository
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.domain.usecase.LoadEmailUseCase
import com.qtglobal.practicaltest.domain.usecase.VerifyHashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import com.qtglobal.practicaltest.util.Result
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * EmailViewModel Plan
 *
 * Purpose: Manage email loading, hash verification, and navigation
 *
 * States: Idle, Loading, Success(email, index, total), Error
 * Dependencies: LoadEmailUseCase, VerifyHashUseCase, EmailRepository
 *
 * Operations:
 * 1. loadSavedEmails() - Fetch all from DB, verify hashes, show first
 * 2. loadEmail(uri) - Load from file, verify, save to DB, reload all
 * 3. navigateToPrevious/Next() - Update currentIndex, emit new Success state
 * 4. resetState() - Set to Idle
 */


sealed class EmailUiState {
    object Idle : EmailUiState()
    object Loading : EmailUiState()
    data class Success(
        val email: Email,
        val currentIndex: Int,
        val totalCount: Int
    ) : EmailUiState()
    data class Error(val message: String) : EmailUiState()
}

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val loadEmailUseCase: LoadEmailUseCase,
    private val verifyHashUseCase: VerifyHashUseCase,
    private val emailRepository: EmailRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<EmailUiState>(EmailUiState.Idle)
    val uiState: StateFlow<EmailUiState> = _uiState.asStateFlow()

    private var emails: List<Email> = emptyList()
    private var currentIndex: Int = 0

    init {
        loadSavedEmails()
    }

    private fun loadSavedEmails() {
        viewModelScope.launch {
            _uiState.value = EmailUiState.Loading
            when (val result = emailRepository.getAllEmails()) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) {
                        emails = result.data.map { verifyHashUseCase(it) }
                        currentIndex = 0
                        _uiState.value = EmailUiState.Success(
                            email = emails[currentIndex],
                            currentIndex = currentIndex + 1,
                            totalCount = emails.size
                        )
                    } else {
                        _uiState.value = EmailUiState.Idle
                    }
                }
                is Result.Error -> {
                    // show idle
                    _uiState.value = EmailUiState.Idle
                }
                is Result.Loading -> {
                    // show loading
                    _uiState.value = EmailUiState.Loading
                }
            }
        }
    }

    fun loadEmail(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = EmailUiState.Loading
            when (val result = loadEmailUseCase(uri, context)) {
                is Result.Success -> {
                    val verifiedEmail = verifyHashUseCase(result.data)
                    
                    // Save email to encrypted database
                    when (val saveResult = emailRepository.saveEmail(verifiedEmail)) {
                        is Result.Success -> {
                            // Reload all emails to include the new one
                            loadSavedEmails()
                        }
                        is Result.Error -> {
                            emails = listOf(verifiedEmail) + emails
                            currentIndex = 0
                            _uiState.value = EmailUiState.Success(
                                email = verifiedEmail,
                                currentIndex = 1,
                                totalCount = emails.size
                            )
                        }
                        is Result.Loading -> {
                            // show loading
                            _uiState.value = EmailUiState.Loading
                        }
                    }
                }
                is Result.Error -> {
                    _uiState.value = EmailUiState.Error(
                        result.message
                    )
                }
                is Result.Loading -> {
                    // show loading
                    _uiState.value = EmailUiState.Loading
                }
            }
        }
    }

    fun navigateToPrevious() {
        if (emails.isNotEmpty() && currentIndex > 0) {
            currentIndex--
            val currentState = _uiState.value
            if (currentState is EmailUiState.Success) {
                _uiState.value = EmailUiState.Success(
                    email = emails[currentIndex],
                    currentIndex = currentIndex + 1,
                    totalCount = emails.size
                )
            }
        }
    }

    fun navigateToNext() {
        if (emails.isNotEmpty() && currentIndex < emails.size - 1) {
            currentIndex++
            val currentState = _uiState.value
            if (currentState is EmailUiState.Success) {
                _uiState.value = EmailUiState.Success(
                    email = emails[currentIndex],
                    currentIndex = currentIndex + 1,
                    totalCount = emails.size
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = EmailUiState.Idle
    }
}

