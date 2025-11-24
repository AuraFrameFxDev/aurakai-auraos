package dev.aurakai.auraframefx.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.aurakai.auraframefx.network.model.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindUserApi(impl: UserApiImpl): UserApi

    @Binds
    @Singleton
    abstract fun bindAIAgentApi(impl: AIAgentApiImpl): AIAgentApi

    @Binds
    @Singleton
    abstract fun bindThemeApi(impl: ThemeApiImpl): ThemeApi
}