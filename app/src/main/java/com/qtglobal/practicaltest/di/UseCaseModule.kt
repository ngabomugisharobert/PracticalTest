package com.qtglobal.practicaltest.di

import com.qtglobal.practicaltest.data.repository.EmailRepository
import com.qtglobal.practicaltest.domain.usecase.LoadEmailUseCase
import com.qtglobal.practicaltest.domain.usecase.VerifyHashUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoadEmailUseCase(repository: EmailRepository): LoadEmailUseCase {
        return LoadEmailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideVerifyHashUseCase(): VerifyHashUseCase {
        return VerifyHashUseCase()
    }
}


