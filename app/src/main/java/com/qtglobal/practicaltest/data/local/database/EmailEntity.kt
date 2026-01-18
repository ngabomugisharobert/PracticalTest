package com.qtglobal.practicaltest.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emails")
data class EmailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderName: String,
    val senderEmailAddress: String,
    val subject: String,
    val body: String,
    val attachedImage: ByteArray,
    val bodyHash: String,
    val imageHash: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailEntity

        if (id != other.id) return false
        if (senderName != other.senderName) return false
        if (senderEmailAddress != other.senderEmailAddress) return false
        if (subject != other.subject) return false
        if (body != other.body) return false
        if (!attachedImage.contentEquals(other.attachedImage)) return false
        if (bodyHash != other.bodyHash) return false
        if (imageHash != other.imageHash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + senderName.hashCode()
        result = 31 * result + senderEmailAddress.hashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + attachedImage.contentHashCode()
        result = 31 * result + bodyHash.hashCode()
        result = 31 * result + imageHash.hashCode()
        return result
    }
}


