package com.qtglobal.practicaltest.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: EmailEntity): Long

    @Query("SELECT * FROM emails ORDER BY id DESC LIMIT 1")
    suspend fun getLatestEmail(): EmailEntity?

    @Query("SELECT * FROM emails ORDER BY id DESC")
    suspend fun getAllEmails(): List<EmailEntity>

    @Query("SELECT COUNT(*) FROM emails")
    suspend fun getEmailCount(): Int

    @Query("DELETE FROM emails")
    suspend fun deleteAllEmails()
}


