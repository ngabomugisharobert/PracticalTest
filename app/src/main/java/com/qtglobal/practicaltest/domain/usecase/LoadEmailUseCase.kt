package com.qtglobal.practicaltest.domain.usecase

import android.content.Context
import android.net.Uri
import com.qtglobal.practicaltest.data.repository.EmailRepository
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.util.Result

/**
 * LoadEmailUseCase Plan
 *
 * Purpose: Load email from file URI via repository
 * Delegates to repository.loadEmailFromFile(), returns Result<Email>
 */


class LoadEmailUseCase(
    private val repository: EmailRepository
) {
    suspend operator fun invoke(uri: Uri, context: Context): Result<Email> {
        return repository.loadEmailFromFile(uri, context)
    }
}

