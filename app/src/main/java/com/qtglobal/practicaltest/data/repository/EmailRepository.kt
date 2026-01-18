package com.qtglobal.practicaltest.data.repository

import android.content.Context
import android.net.Uri
import com.qtglobal.practicaltest.data.local.database.EmailDao
import com.qtglobal.practicaltest.data.local.database.EmailEntity
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.proto.EmailMessageProto
import com.qtglobal.practicaltest.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream


/**
 * Implementation plan of EmailRepository that manages email data operations.
 *
 * Provides functionality to:
 * - Parse and load emails from protobuf (.pb) files via URI
 * - Save emails to local Room database
 * - Query emails (latest, all, count)
 *
 *      All operations must be execute on IO dispatcher and return Result<T> for error handling.
 */


interface EmailRepository {
    suspend fun loadEmailFromFile(uri: Uri, context: Context): Result<Email>
    suspend fun saveEmail(email: Email): Result<Long>
    suspend fun getLatestEmail(): Result<Email?>
    suspend fun getAllEmails(): Result<List<Email>>
}

class EmailRepositoryImpl(
    private val emailDao: EmailDao
) : EmailRepository {

    override suspend fun loadEmailFromFile(uri: Uri, context: Context): Result<Email> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    return@withContext Result.Error("Failed to open file", Exception("InputStream is null"))
                }

                val fileBytes = inputStream.readBytes()
                inputStream.close()

                val emailMessage = EmailMessageProto.EmailMessage.parseFrom(fileBytes)

                val email = Email(
                    senderName = emailMessage.senderName,
                    senderEmailAddress = emailMessage.senderEmailAddress,
                    subject = emailMessage.subject,
                    body = emailMessage.body,
                    attachedImage = emailMessage.attachedImage.toByteArray(),
                    bodyHash = emailMessage.bodyHash,
                    imageHash = emailMessage.imageHash
                )

                Result.Success(email)
            } catch (e: Exception) {
                Result.Error("Failed to load email: ${e.message}", e)
            }
        }
    }

    override suspend fun saveEmail(email: Email): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = EmailEntity(
                    senderName = email.senderName,
                    senderEmailAddress = email.senderEmailAddress,
                    subject = email.subject,
                    body = email.body,
                    attachedImage = email.attachedImage,
                    bodyHash = email.bodyHash,
                    imageHash = email.imageHash
                )
                val id = emailDao.insertEmail(entity)
                Result.Success(id)
            } catch (e: Exception) {
                Result.Error("Failed to save email: ${e.message}", e)
            }
        }
    }

    override suspend fun getLatestEmail(): Result<Email?> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = emailDao.getLatestEmail()
                val email = entity?.let {
                    Email(
                        senderName = it.senderName,
                        senderEmailAddress = it.senderEmailAddress,
                        subject = it.subject,
                        body = it.body,
                        attachedImage = it.attachedImage,
                        bodyHash = it.bodyHash,
                        imageHash = it.imageHash
                    )
                }
                Result.Success(email)
            } catch (e: Exception) {
                Result.Error("Failed to get email: ${e.message}", e)
            }
        }
    }

    override suspend fun getAllEmails(): Result<List<Email>> {
        return withContext(Dispatchers.IO) {
            try {
                val entities = emailDao.getAllEmails()
                val emails = entities.map {
                    Email(
                        senderName = it.senderName,
                        senderEmailAddress = it.senderEmailAddress,
                        subject = it.subject,
                        body = it.body,
                        attachedImage = it.attachedImage,
                        bodyHash = it.bodyHash,
                        imageHash = it.imageHash
                    )
                }
                Result.Success(emails)
            } catch (e: Exception) {
                Result.Error("Failed to get emails: ${e.message}", e)
            }
        }
    }

}


