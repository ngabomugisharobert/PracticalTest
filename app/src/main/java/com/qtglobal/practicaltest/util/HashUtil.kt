package com.qtglobal.practicaltest.util

import java.security.MessageDigest


/*
 * HashUtil Plan
 *
 * Purpose: SHA-256 hash computation and verification
 *
 * Methods:
 * - computeSHA256
 * - verifyHash
 * - bytesToHex
 */

object HashUtil {

    fun computeSHA256(input: String): String {
        return computeSHA256(input.toByteArray(Charsets.UTF_8))
    }

    fun computeSHA256(input: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input)
        return bytesToHex(hashBytes)
    }

    fun verifyHash(computedHash: String, expectedHash: String): Boolean {
        return computedHash.equals(expectedHash, ignoreCase = true)
    }

    fun verifyHash(data: ByteArray, expectedHash: String): Boolean {
        val computedHash = computeSHA256(data)
        return computedHash.equals(expectedHash, ignoreCase = true)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}