package com.qtglobal.practicaltest.data.repository

import android.content.Context
import android.net.Uri
import com.qtglobal.practicaltest.data.local.database.EmailDao
import com.qtglobal.practicaltest.data.mapper.toEmailDomain
import com.qtglobal.practicaltest.data.mapper.toEmailEntity
import com.qtglobal.practicaltest.data.services.ProtobufParserService
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.proto.EmailMessageProto
import com.qtglobal.practicaltest.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream


/**
 * Implementation plan of EmailRepository that manages email data operations.
 *
 * Provides functionality to:
 * - Parse and load emails from protobuf (.pb) files via URI
 * - Save emails to local Room database
 * - Query emails
 *
 *      All operations must be execute on IO dispatcher and return Result<T> for error handling.
 */


interface EmailRepository {
    suspend fun loadEmailFromFile(uri: Uri, context: Context): Result<Email>
    suspend fun saveEmail(email: Email): Result<Long>
    //    suspend fun getLatestEmail(): Result<Email?>
    suspend fun getAllEmails(): Result<List<Email>>
}

class EmailRepositoryImpl(
    private val emailDao: EmailDao,
    private val protobufParserService: ProtobufParserService
) : EmailRepository {

    override suspend fun loadEmailFromFile(uri: Uri, context: Context): Result<Email> {
        return protobufParserService.parseEmailFromUri(uri, context)
    }


    override suspend fun saveEmail(email: Email): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = email.toEmailEntity()
                val id = emailDao.insertEmail(entity)
                Result.Success(id)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Result.Error("Failed to save email: ${e.message}", e)
            }
        }
    }


    override suspend fun getAllEmails(): Result<List<Email>> {
        return withContext(Dispatchers.IO) {
            try {
                val entities = emailDao.getAllEmails()
                val emails = entities.map {
                    it.toEmailDomain()
                }
                Result.Success(emails)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Result.Error("Failed to get emails: ${e.message}", e)
            }
        }
    }

}


