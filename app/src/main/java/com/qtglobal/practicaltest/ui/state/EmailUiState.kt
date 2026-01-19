package com.qtglobal.practicaltest.ui.state

import com.qtglobal.practicaltest.domain.model.Email


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