package com.qtglobal.practicaltest.di

import android.content.Context
import com.qtglobal.practicaltest.data.local.database.EmailDatabase
import com.qtglobal.practicaltest.data.local.database.EmailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEmailDatabase(@ApplicationContext context: Context): EmailDatabase {
        return EmailDatabase.create(context)
    }

    @Provides
    fun provideEmailDao(database: EmailDatabase): EmailDao {
        return database.emailDao()
    }
}




