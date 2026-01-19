package com.qtglobal.practicaltest.di

import com.qtglobal.practicaltest.data.services.ProtobufParserService
import com.qtglobal.practicaltest.data.services.ProtobufParserServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideProtobufParserService(): ProtobufParserService {
        return ProtobufParserServiceImpl()
    }
}
