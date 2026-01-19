package com.qtglobal.practicaltest.ui.viewmodel


import android.content.Context
import android.net.Uri
import com.qtglobal.practicaltest.data.repository.EmailRepository
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.domain.usecase.LoadEmailUseCase
import com.qtglobal.practicaltest.domain.usecase.VerifyHashUseCase
import com.qtglobal.practicaltest.util.Result
import com.qtglobal.practicaltest.util.TestUtils
import com.google.common.truth.Truth.assertThat
import com.qtglobal.practicaltest.ui.state.EmailUiState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * EmailViewModel Core Functionality Tests
 *
 * Tests the main ViewModel functionality:
 * - Initial state loading (init)
 * - Loading emails from files
 * - Hash verification
 * - State management
 * - Error handling
 */

@OptIn(ExperimentalCoroutinesApi::class)
class EmailViewModelCoreTest {

    private lateinit var mockLoadEmailUseCase: LoadEmailUseCase
    private lateinit var mockVerifyHashUseCase: VerifyHashUseCase
    private lateinit var mockRepository: EmailRepository
    private lateinit var mockContext: Context
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockLoadEmailUseCase = mockk()
        mockVerifyHashUseCase = mockk()
        mockRepository = mockk()
        mockContext = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_withNoSavedEmails_setsIdleState() = runTest(testDispatcher) {
        // Arrange
        coEvery { mockRepository.getAllEmails() } returns Result.Success(emptyList())

        // Act
        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value).isInstanceOf(EmailUiState.Idle::class.java)
    }

    @Test
    fun init_withSavedEmails_showsFirstEmail() = runTest(testDispatcher) {
        // Arrange
        val email1 = TestUtils.createSampleEmail(subject = "Email 1")
        val email2 = TestUtils.createSampleEmail(subject = "Email 2")
        val verifiedEmail1 = email1.copy(bodyVerified = true, imageVerified = true)
        val verifiedEmail2 = email2.copy(bodyVerified = true, imageVerified = true)

        coEvery { mockRepository.getAllEmails() } returns Result.Success(listOf(email1, email2))
        every { mockVerifyHashUseCase(email1) } returns verifiedEmail1
        every { mockVerifyHashUseCase(email2) } returns verifiedEmail2

        // Act
        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as EmailUiState.Success
        assertThat(state.email).isEqualTo(verifiedEmail1)
        assertThat(state.currentIndex).isEqualTo(1)
        assertThat(state.totalCount).isEqualTo(2)
        verify(exactly = 1) { mockVerifyHashUseCase(email1) }
        verify(exactly = 1) { mockVerifyHashUseCase(email2) }
    }

    @Test
    fun loadEmail_success_savesAndReloads() = runTest(testDispatcher) {
        // Arrange
        val uri = mockk<Uri>()
        val email = TestUtils.createSampleEmail()
        val verifiedEmail = email.copy(bodyVerified = true, imageVerified = true)

        // Setup mocks: getAllEmails called twice (init + reload after save)
        coEvery { mockRepository.getAllEmails() } returnsMany listOf(
            Result.Success(emptyList()), // First call (init)
            Result.Success(listOf(email)) // Second call (reload after save - returns unverified email)
        )
        coEvery { mockLoadEmailUseCase(uri, mockContext) } returns Result.Success(email)
        // Mock verifyHashUseCase to handle both the initial email and the reloaded email
        every { mockVerifyHashUseCase(any()) } answers {
            val input = firstArg<Email>()
            input.copy(bodyVerified = true, imageVerified = true)
        }
        coEvery { mockRepository.saveEmail(verifiedEmail) } returns Result.Success(1L)

        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        // Act
        viewModel.loadEmail(uri)
        // Wait for loadEmail() coroutine to complete (includes save and reload)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(EmailUiState.Success::class.java)
        val successState = state as EmailUiState.Success
        assertThat(successState.email.subject).isEqualTo(verifiedEmail.subject)
        assertThat(successState.currentIndex).isEqualTo(1)
        assertThat(successState.totalCount).isEqualTo(1)
        coVerify(exactly = 1) { mockRepository.saveEmail(any()) }
        coVerify(exactly = 2) { mockRepository.getAllEmails() } // init + reload
    }

    @Test
    fun loadEmail_error_showsErrorState() = runTest(testDispatcher) {
        // Arrange
        coEvery { mockRepository.getAllEmails() } returns Result.Success(emptyList())
        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        val uri = mockk<Uri>()
        val errorMessage = "Failed to parse email"
        coEvery { mockLoadEmailUseCase(uri, mockContext) } returns Result.Error(errorMessage, Exception())

        // Act
        viewModel.loadEmail(uri)
        // Wait for loadEmail() coroutine to complete and set error state
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as EmailUiState.Error
        assertThat(state.message).isEqualTo(errorMessage)
    }

    @Test
    fun loadEmail_saveFailure_displaysEmailTemporarily() = runTest(testDispatcher) {
        // Arrange
        coEvery { mockRepository.getAllEmails() } returns Result.Success(emptyList())
        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        val uri = mockk<Uri>()
        val email = TestUtils.createSampleEmail()
        val verifiedEmail = email.copy(bodyVerified = true, imageVerified = true)

        coEvery { mockLoadEmailUseCase(uri, mockContext) } returns Result.Success(email)
        every { mockVerifyHashUseCase(email) } returns verifiedEmail
        coEvery { mockRepository.saveEmail(verifiedEmail) } returns Result.Error("Save failed", Exception())

        // Act
        viewModel.loadEmail(uri)
        // Wait for loadEmail() coroutine to complete (save fails, but email is still displayed)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(EmailUiState.Success::class.java)
        val successState = state as EmailUiState.Success
        assertThat(successState.email).isEqualTo(verifiedEmail)
        assertThat(successState.currentIndex).isEqualTo(1)
        assertThat(successState.totalCount).isEqualTo(1)
        coVerify(exactly = 1) { mockRepository.saveEmail(verifiedEmail) }
    }

    @Test
    fun loadEmail_verifiesHashBeforeSaving() = runTest(testDispatcher) {
        // Arrange
        val uri = mockk<Uri>()
        val email = TestUtils.createSampleEmail()
        val verifiedEmail = email.copy(bodyVerified = true, imageVerified = true)

        coEvery { mockRepository.getAllEmails() } returnsMany listOf(
            Result.Success(emptyList()), // init
            Result.Success(listOf(email)) // reload after save - returns unverified email
        )
        coEvery { mockLoadEmailUseCase(uri, mockContext) } returns Result.Success(email)
        // Mock verifyHashUseCase to handle both calls
        every { mockVerifyHashUseCase(any()) } answers {
            val input = firstArg<Email>()
            input.copy(bodyVerified = true, imageVerified = true)
        }
        coEvery { mockRepository.saveEmail(verifiedEmail) } returns Result.Success(1L)

        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        // Act
        viewModel.loadEmail(uri)
        // Wait for loadEmail() coroutine to complete (includes verification, save, and reload)
        advanceUntilIdle()

        // Assert - verify all methods were called
        coVerify(exactly = 1) { mockLoadEmailUseCase(uri, mockContext) }
        verify(atLeast = 1) { mockVerifyHashUseCase(any()) } // Called for load + reload
        coVerify(exactly = 1) { mockRepository.saveEmail(any()) }

        // Verify order for suspend functions: load -> save
        coVerifyOrder {
            mockLoadEmailUseCase(uri, mockContext)
            mockRepository.saveEmail(any())
        }
    }

    @Test
    fun resetState_fromSuccessState_returnsToIdle() = runTest(testDispatcher) {
        // Arrange
        val email = TestUtils.createSampleEmail()
        val verifiedEmail = email.copy(bodyVerified = true, imageVerified = true)
        coEvery { mockRepository.getAllEmails() } returns Result.Success(listOf(email))
        every { mockVerifyHashUseCase(email) } returns verifiedEmail

        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        // Act
        viewModel.resetState()
        // resetState() is synchronous, no need to wait

        // Assert
        assertThat(viewModel.uiState.value).isInstanceOf(EmailUiState.Idle::class.java)
    }

    @Test
    fun resetState_fromErrorState_returnsToIdle() = runTest(testDispatcher) {
        // Arrange
        coEvery { mockRepository.getAllEmails() } returns Result.Success(emptyList())
        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        advanceUntilIdle()

        val uri = mockk<Uri>()
        coEvery { mockLoadEmailUseCase(uri, mockContext) } returns Result.Error("Error", Exception())

        viewModel.loadEmail(uri)
        // Wait for loadEmail() coroutine to complete and set error state
        advanceUntilIdle()

        // Act
        viewModel.resetState()
        // resetState() is synchronous, no need to wait

        // Assert
        assertThat(viewModel.uiState.value).isInstanceOf(EmailUiState.Idle::class.java)
    }

    @Test
    fun loadSavedEmails_verifiesAllEmailHashes() = runTest(testDispatcher) {
        // Arrange
        val email1 = TestUtils.createSampleEmail(subject = "Email 1")
        val email2 = TestUtils.createSampleEmail(subject = "Email 2")
        val email3 = TestUtils.createSampleEmail(subject = "Email 3")

        coEvery { mockRepository.getAllEmails() } returns Result.Success(listOf(email1, email2, email3))
        every { mockVerifyHashUseCase(any()) } answers { firstArg<Email>() }

        // Act
        val viewModel = createViewModel()
        // Wait for init block's loadSavedEmails() coroutine to complete
        // This verifies that all emails are processed through verifyHashUseCase
        advanceUntilIdle()

        // Assert
        verify(exactly = 1) { mockVerifyHashUseCase(email1) }
        verify(exactly = 1) { mockVerifyHashUseCase(email2) }
        verify(exactly = 1) { mockVerifyHashUseCase(email3) }
    }

    private fun createViewModel(): EmailViewModel {
        return EmailViewModel(
            mockLoadEmailUseCase,
            mockVerifyHashUseCase,
            mockRepository,
            mockContext
        )
    }
}