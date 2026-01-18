package com.qtglobal.practicaltest.domain.model

import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode
import kotlin.jvm.javaClass

data class Email(
    val senderName: String,
    val senderEmailAddress: String,
    val subject: String,
    val body: String,
    val attachedImage: ByteArray,
    val bodyHash: String,
    val imageHash: String,
    val bodyVerified: Boolean = false,
    val imageVerified: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Email

        if (senderName != other.senderName) return false
        if (senderEmailAddress != other.senderEmailAddress) return false
        if (subject != other.subject) return false
        if (body != other.body) return false
        if (!attachedImage.contentEquals(other.attachedImage)) return false
        if (bodyHash != other.bodyHash) return false
        if (imageHash != other.imageHash) return false
        if (bodyVerified != other.bodyVerified) return false
        if (imageVerified != other.imageVerified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = senderName.hashCode()
        result = 31 * result + senderEmailAddress.hashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + attachedImage.contentHashCode()
        result = 31 * result + bodyHash.hashCode()
        result = 31 * result + imageHash.hashCode()
        result = 31 * result + bodyVerified.hashCode()
        result = 31 * result + imageVerified.hashCode()
        return result
    }
}


