package com.qtglobal.practicaltest.data.mapper


import com.qtglobal.practicaltest.data.local.database.EmailEntity
import com.qtglobal.practicaltest.domain.model.Email


fun EmailEntity.toEmailDomain() = Email(
    senderName = senderName,
    senderEmailAddress = senderEmailAddress,
    subject = subject,
    body = body,
    attachedImage = attachedImage,
    bodyHash = bodyHash,
    imageHash = imageHash
)

fun Email.toEmailEntity() = EmailEntity(
    senderName = senderName,
    senderEmailAddress = senderEmailAddress,
    subject = subject,
    body = body,
    attachedImage = attachedImage,
    bodyHash = bodyHash,
    imageHash = imageHash
)