package com.qtglobal.practicaltest.di

import com.qtglobal.practicaltest.data.local.database.EmailDao
import com.qtglobal.practicaltest.data.repository.EmailRepository
import com.qtglobal.practicaltest.data.repository.EmailRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEmailRepository(emailDao: EmailDao): EmailRepository {
        return EmailRepositoryImpl(emailDao)
    }
}


