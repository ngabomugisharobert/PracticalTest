package com.qtglobal.practicaltest.data.repository


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.google.common.truth.Truth
import com.qtglobal.practicaltest.data.local.database.EmailDao
import com.qtglobal.practicaltest.util.TestUtils
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import com.qtglobal.practicaltest.util.Result



/**
 * EmailRepositoryTest - Essential tests for EmailRepository
 *
 * Tests core functionality: file parsing, DB operations, error handling
 */
class EmailRepositoryTest {

    private lateinit var mockDao: EmailDao
    private lateinit var mockContext: Context
    private lateinit var mockContentResolver: ContentResolver
    private lateinit var repository: EmailRepositoryImpl

    @Before
    fun setup() {
        mockDao = mockk()
        mockContext = mockk()
        mockContentResolver = mockk()
        every { mockContext.contentResolver } returns mockContentResolver
        repository = EmailRepositoryImpl(mockDao, mockk())
    }

    // loadEmailFromFile - 3 tests
    @Test
    fun loadEmailFromFile_withValidProtobuf_returnsSuccess() = runTest {
        val uri = mockk<Uri>()
        val protobufBytes = TestUtils.createValidProtobufBytes()
        val inputStream = ByteArrayInputStream(protobufBytes)
        every { mockContentResolver.openInputStream(uri) } returns inputStream

        val result = repository.loadEmailFromFile(uri, mockContext)

        Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        val email = (result as Result.Success).data
        Truth.assertThat(email.senderName).isEqualTo(TestUtils.TEST_SENDER_NAME)
        Truth.assertThat(email.subject).isEqualTo(TestUtils.TEST_SUBJECT)
    }

    @Test
    fun loadEmailFromFile_withNullInputStream_returnsError() = runTest {
        val uri = mockk<Uri>()
        every { mockContentResolver.openInputStream(uri) } returns null

        val result = repository.loadEmailFromFile(uri, mockContext)

        Truth.assertThat(result).isInstanceOf(Result.Error::class.java)
        Truth.assertThat((result as Result.Error).message).contains("Failed to open file")
    }

    @Test
    fun loadEmailFromFile_withInvalidProtobuf_returnsError() = runTest {
        val uri = mockk<Uri>()
        val invalidBytes = TestUtils.createInvalidProtobufBytes()
        val inputStream = ByteArrayInputStream(invalidBytes)
        every { mockContentResolver.openInputStream(uri) } returns inputStream

        val result = repository.loadEmailFromFile(uri, mockContext)

        Truth.assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    // saveEmail - 1 test
    @Test
    fun saveEmail_withValidEmail_returnsSuccessWithId() = runTest {
        val email = TestUtils.createSampleEmail()
        val expectedId = 1L
        coEvery { mockDao.insertEmail(any()) } returns expectedId

        val result = repository.saveEmail(email)

        Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        Truth.assertThat((result as Result.Success).data).isEqualTo(expectedId)
    }



    // getAllEmails - 2 tests

    @Test
    fun getAllEmails_withExistingEmails_returnsSuccess() = runTest {
        val entities = listOf(
            TestUtils.createSampleEmailEntity(id = 1),
            TestUtils.createSampleEmailEntity(id = 2, subject = "Second Email")
        )
        coEvery { mockDao.getAllEmails() } returns entities

        val result = repository.getAllEmails()

        Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        val emails = (result as Result.Success).data
        Truth.assertThat(emails).hasSize(2)
    }

    @Test
    fun getAllEmails_withDatabaseException_returnsError() = runTest {
        coEvery { mockDao.getAllEmails() } throws Exception("Database error")

        val result = repository.getAllEmails()

        Truth.assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    // getEmailCount - 1 test

//    @Test
//    fun getEmailCount_withExistingEmails_returnsCorrectCount() = runTest {
//        val expectedCount = 5
//        coEvery { mockDao.getEmailCount() } returns expectedCount
//
//        val result = repository.getEmailCount()
//
//        Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
//        Truth.assertThat((result as Result.Success).data).isEqualTo(expectedCount)
//    }
}