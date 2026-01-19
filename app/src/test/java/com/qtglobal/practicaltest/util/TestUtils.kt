package com.qtglobal.practicaltest.util


import com.google.protobuf.ByteString
import com.qtglobal.practicaltest.data.local.database.EmailEntity
import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.proto.EmailMessageProto

object TestUtils {
    // Test data constants
    const val TEST_SENDER_NAME = "Robert Ngabo"
    const val TEST_SENDER_EMAIL = "robert@qtglobal.com"
    const val TEST_SUBJECT = "Test Email Subject"
    const val TEST_BODY = "This is a test email body content."
    val TEST_IMAGE_BYTES = byteArrayOf(0x50, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
    const val TEST_BODY_HASH = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3"
    const val TEST_IMAGE_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"


    //Creates a sample Email with test data
    fun createSampleEmail(
        senderName: String = TEST_SENDER_NAME,
        senderEmailAddress: String = TEST_SENDER_EMAIL,
        subject: String = TEST_SUBJECT,
        body: String = TEST_BODY,
        attachedImage: ByteArray = TEST_IMAGE_BYTES,
        bodyHash: String = TEST_BODY_HASH,
        imageHash: String = TEST_IMAGE_HASH,
        bodyVerified: Boolean = false,
        imageVerified: Boolean = false
    ): Email {
        return Email(
            senderName = senderName,
            senderEmailAddress = senderEmailAddress,
            subject = subject,
            body = body,
            attachedImage = attachedImage,
            bodyHash = bodyHash,
            imageHash = imageHash,
            bodyVerified = bodyVerified,
            imageVerified = imageVerified
        )
    }


     //Creates a sample EmailEntity with test data
    fun createSampleEmailEntity(
        id: Long = 0,
        senderName: String = TEST_SENDER_NAME,
        senderEmailAddress: String = TEST_SENDER_EMAIL,
        subject: String = TEST_SUBJECT,
        body: String = TEST_BODY,
        attachedImage: ByteArray = TEST_IMAGE_BYTES,
        bodyHash: String = TEST_BODY_HASH,
        imageHash: String = TEST_IMAGE_HASH
    ): EmailEntity {
        return EmailEntity(
            id = id,
            senderName = senderName,
            senderEmailAddress = senderEmailAddress,
            subject = subject,
            body = body,
            attachedImage = attachedImage,
            bodyHash = bodyHash,
            imageHash = imageHash
        )
    }


    //Creates valid protobuf bytes for testing
    fun createValidProtobufBytes(): ByteArray {
        val emailMessage = EmailMessageProto.EmailMessage.newBuilder()
            .setSenderName(TEST_SENDER_NAME)
            .setSenderEmailAddress(TEST_SENDER_EMAIL)
            .setSubject(TEST_SUBJECT)
            .setBody(TEST_BODY)
            .setAttachedImage(ByteString.copyFrom(TEST_IMAGE_BYTES))
            .setBodyHash(TEST_BODY_HASH)
            .setImageHash(TEST_IMAGE_HASH)
            .build()
        return emailMessage.toByteArray()
    }


     // Creates invalid protobuf bytes for testing error scenarios
    fun createInvalidProtobufBytes(): ByteArray {
        return byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05) // Invalid protobuf data
    }
}

