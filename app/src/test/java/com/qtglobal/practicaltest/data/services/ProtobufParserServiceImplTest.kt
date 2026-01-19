package com.qtglobal.practicaltest.data.services

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.ByteString
import com.qtglobal.practicaltest.proto.EmailMessageProto
import com.qtglobal.practicaltest.util.Result
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException

@ExperimentalCoroutinesApi
class ProtobufParserServiceImplTest {

    private lateinit var service: ProtobufParserService
    private lateinit var mockContext: Context
    private lateinit var mockContentResolver: ContentResolver
    private lateinit var mockUri: Uri

    @Before
    fun setup() {
        service = ProtobufParserServiceImpl()
        mockContext = mockk(relaxed = true)
        mockContentResolver = mockk(relaxed = true)
        mockUri = mockk(relaxed = true)

        every { mockContext.contentResolver } returns mockContentResolver
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // Helper Methods 

    private fun createValidEmailProto(): EmailMessageProto.EmailMessage {
        return EmailMessageProto.EmailMessage.newBuilder()
            .setSenderName("Robert Ngabo")
            .setSenderEmailAddress("Robert@qtglobal.com")
            .setSubject("Test Subject")
            .setBody("Test email body content")
            .setAttachedImage(ByteString.copyFrom(byteArrayOf(1, 2, 3, 4, 5)))
            .setBodyHash("abc123")
            .setImageHash("def456")
            .build()
    }

    private fun createValidEmailBytes(): ByteArray {
        return createValidEmailProto().toByteArray()
    }

    // Core Functionality Tests

    @Test
    fun `parseEmailFromUri - successfully parses valid protobuf file`() = runTest {
        // Arrange
        val validBytes = createValidEmailBytes()
        val inputStream = ByteArrayInputStream(validBytes)
        every { mockContentResolver.openInputStream(mockUri) } returns inputStream

        // Act
        val result = service.parseEmailFromUri(mockUri, mockContext)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val email = (result as Result.Success).data
        assertThat(email.senderName).isEqualTo("Robert Ngabo")
        assertThat(email.senderEmailAddress).isEqualTo("Robert@qtglobal.com")
        assertThat(email.subject).isEqualTo("Test Subject")
        assertThat(email.body).isEqualTo("Test email body content")
        assertThat(email.bodyHash).isEqualTo("abc123")
        assertThat(email.imageHash).isEqualTo("def456")
        assertThat(email.attachedImage).isEqualTo(byteArrayOf(1, 2, 3, 4, 5))
    }

    @Test
    fun `parseEmailFromUri - returns error when InputStream is null`() = runTest {
        // Arrange
        every { mockContentResolver.openInputStream(mockUri) } returns null

        // Act
        val result = service.parseEmailFromUri(mockUri, mockContext)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = result as Result.Error
        assertThat(error.message).isEqualTo("Failed to open file")
        assertThat(error.exception).isNotNull()
        assertThat(error.exception?.message).isEqualTo("InputStream is null")
    }

    @Test
    fun `parseEmailFromUri - returns error when IOException occurs`() = runTest {
        // Arrange
        every { mockContentResolver.openInputStream(mockUri) } throws IOException("File read error")

        // Act
        val result = service.parseEmailFromUri(mockUri, mockContext)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = result as Result.Error
        assertThat(error.message).contains("Failed to read file")
        assertThat(error.message).contains("File read error")
    }

    @Test
    fun `parseEmailFromBytes - successfully parses valid protobuf bytes`() = runTest {
        // Arrange
        val validBytes = createValidEmailBytes()

        // Act
        val result = service.parseEmailFromBytes(validBytes)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val email = (result as Result.Success).data
        assertThat(email.senderName).isEqualTo("Robert Ngabo")
        assertThat(email.senderEmailAddress).isEqualTo("john@example.com")
        assertThat(email.subject).isEqualTo("Test Subject")
        assertThat(email.body).isEqualTo("Test email body content")
        assertThat(email.attachedImage).isEqualTo(byteArrayOf(1, 2, 3, 4, 5))
    }

    @Test
    fun `parseEmailFromBytes - returns error for empty byte array`() = runTest {
        // Arrange
        val emptyBytes = byteArrayOf()

        // Act
        val result = service.parseEmailFromBytes(emptyBytes)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = result as Result.Error
        assertThat(error.message).isEqualTo("File is empty")
    }

    @Test
    fun `parseEmailFromBytes - returns error for corrupted protobuf file`() = runTest {
        // Arrange
        val corruptedBytes = byteArrayOf(0x00, 0x01, 0x02, 0x03)

        // Act
        val result = service.parseEmailFromBytes(corruptedBytes)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = result as Result.Error
        assertThat(error.message).contains("Invalid protobuf file")
    }

    @Test
    fun `parseEmailFromBytes - returns error for malformed protobuf data`() = runTest {
        // Arrange
        val malformedBytes = byteArrayOf(0x08, 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())

        // Act
        val result = service.parseEmailFromBytes(malformedBytes)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = result as Result.Error
        assertThat(error.message).contains("Failed to parse protobuf")
    }
}