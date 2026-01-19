package com.qtglobal.practicaltest.data.services

import android.content.Context
import android.net.Uri
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.proto.EmailMessageProto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import com.qtglobal.practicaltest.util.Result
import kotlin.coroutines.cancellation.CancellationException


//Service responsible for parsing Protocol Buffer files and converting them to Email domain models

interface ProtobufParserService {
    suspend fun parseEmailFromUri(uri: Uri, context: Context): Result<Email>
    suspend fun parseEmailFromBytes(fileBytes: ByteArray): Result<Email>
}

class ProtobufParserServiceImpl : ProtobufParserService {

    override suspend fun parseEmailFromUri(uri: Uri, context: Context): Result<Email> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    return@withContext Result.Error("Failed to open file", Exception("InputStream is null"))
                }

                val fileBytes = inputStream.readBytes()
                inputStream.close()

                parseEmailFromBytes(fileBytes)
            } catch (e: Exception) {
                Result.Error("Failed to read file: ${e.message}", e)
            }
        }
    }

    override suspend fun parseEmailFromBytes(fileBytes: ByteArray): Result<Email> {
        return withContext(Dispatchers.IO) {
            try {
                // Validate file is not empty
                if (fileBytes.isEmpty()) {
                    return@withContext Result.Error("File is empty", Exception("File contains no data"))
                }

                // Validate file starts with valid protobuf tag (should not start with 0x00)
                if (fileBytes.isNotEmpty() && fileBytes[0] == 0x00.toByte()) {
                    return@withContext Result.Error(
                        "Invalid protobuf file: file appears to be corrupted or empty",
                        Exception("File starts with invalid tag (zero)")
                    )
                }

                // Parse Protocol Buffer message
                val emailMessage = EmailMessageProto.EmailMessage.parseFrom(fileBytes)

                // Convert to domain model
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
                if (e is CancellationException) {
                    throw e
                }
                Result.Error("Failed to parse protobuf: ${e.message}", e)
            }
        }
    }
}

