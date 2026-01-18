package com.qtglobal.practicaltest.domain.usecase

import com.qtglobal.practicaltest.domain.model.Email
import com.qtglobal.practicaltest.util.HashUtil

/**
 * VerifyHashUseCase Plan
 *
 * Purpose: Verify integrity of email body and attached image using SHA-256 hashes
 *
 * Steps:
 * 1. Compute SHA-256 hash of email body
 * 2. Compute SHA-256 hash of attached image
 * 3. Compare computed hashes against stored hashes
 * 4. Return email copy with verification results (bodyVerified, imageVerified)
 *
 * Dependencies: HashUtil for hash computation and verification
 */

class VerifyHashUseCase {
    operator fun invoke(email: Email): Email {
        val bodyComputedHash = HashUtil.computeSHA256(email.body)
        val imageComputedHash = HashUtil.computeSHA256(email.attachedImage)

        val bodyVerified = HashUtil.verifyHash(bodyComputedHash, email.bodyHash)
        val imageVerified = HashUtil.verifyHash(imageComputedHash, email.imageHash)

        return email.copy(
            bodyVerified = bodyVerified,
            imageVerified = imageVerified
        )
    }
}


