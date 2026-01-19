package com.qtglobal.practicaltest.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qtglobal.practicaltest.util.KeystoreManager
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory


@Database(
    entities = [EmailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EmailDatabase : RoomDatabase() {
    abstract fun emailDao(): EmailDao

    companion object {
        @Volatile
        private var INSTANCE: EmailDatabase? = null
        private const val DATABASE_NAME = "email_database"

        fun create(context: Context): EmailDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): EmailDatabase {
            val builder = Room.databaseBuilder(
                context,
                EmailDatabase::class.java,
                DATABASE_NAME
            )

            // Get encryption key from Android Keystore
            val encryptionKey = KeystoreManager(context).getOrCreateEncryptionKey()

            // Set up SQLCipher encryption
            builder.openHelperFactory(SupportOpenHelperFactory(encryptionKey))

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

