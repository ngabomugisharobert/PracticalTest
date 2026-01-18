package com.qtglobal.practicaltest.util


import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import androidx.core.content.edit


class KeystoreManager(private val context: Context) {

    /**
     * KeystoreManager Plan
     *
     * Purpose: Manage database encryption key using Android Keystore
     *
     * Operations:
     * 1. getOrCreateEncryptionKey()
     *    - Check EncryptedSharedPreferences for existing key
     *    - If exists: decode from Base64 and return
     *    - If not: generate 32-byte key via SecureRandom, store as Base64, return
     *
     * 2. getEncryptedSharedPreferences()
     *    - Create with MasterKey (AES256_GCM)
     *    - Use AES256_SIV for keys, AES256_GCM for values
     */

    companion object {
        private const val PREFS_NAME = "email_db_keystore_prefs"
        private const val KEY_PREF_NAME = "db_encryption_key"
        private const val KEY_SIZE = 32 // 32 bytes for AES-256
    }

    fun getOrCreateEncryptionKey(): ByteArray {
        val encryptedPrefs = getEncryptedSharedPreferences()
        val keyBase64 = encryptedPrefs.getString(KEY_PREF_NAME, null)

        return if (keyBase64 != null) {
            android.util.Base64.decode(keyBase64, android.util.Base64.DEFAULT)
        } else {
            // Generate and store new key
            val key = ByteArray(KEY_SIZE)
            SecureRandom().nextBytes(key)
            encryptedPrefs.edit {
                putString(
                    KEY_PREF_NAME,
                    android.util.Base64.encodeToString(key, android.util.Base64.DEFAULT)
                )
            }
            key
        }
    }

    private fun getEncryptedSharedPreferences() = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}