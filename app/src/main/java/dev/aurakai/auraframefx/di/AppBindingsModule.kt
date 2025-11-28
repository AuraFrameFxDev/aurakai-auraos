package dev.aurakai.auraframefx.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.aurakai.auraframefx.ai.error.ErrorHandler
import dev.aurakai.auraframefx.ai.error.ErrorStats
import dev.aurakai.auraframefx.ai.services.AuraAIServiceImpl
import dev.aurakai.auraframefx.oracledrive.EncryptionManager
import dev.aurakai.auraframefx.oracledrive.genesis.ai.AuraAIService
import dev.aurakai.auraframefx.system.monitor.DefaultSystemMonitor
import dev.aurakai.auraframefx.system.monitor.SystemMonitor
import javax.inject.Singleton

/**
 * Application-level DI bindings to resolve missing Hilt bindings reported during annotation processing.
 * Keep this module conservative: only bind concrete implementations that exist in the codebase.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingsModule {

    @Binds
    @Singleton
    abstract fun bindAuraAIService(impl: AuraAIServiceImpl): AuraAIService

    @Binds
    @Singleton
    abstract fun bindSystemMonitor(impl: DefaultSystemMonitor): SystemMonitor

    @Binds
    @Singleton
    abstract fun bindEncryptionManager(impl: DefaultEncryptionManager): EncryptionManager

    companion object {
        // Provide a default ErrorHandler implementation (not the UI ErrorHandler object)
        @Provides
        @Singleton
        fun provideErrorHandler(): ErrorHandler = DefaultErrorHandler()

        // Provide Legacy TaskScheduler if some modules still expect it; keep as lightweight shim
        // Note: replace or remove when all modules migrated.
        @Provides
        @Singleton
        fun provideLegacyTaskScheduler(): Any = Any()
    }
}

class DefaultErrorHandler : ErrorHandler {
    override fun handleError(error: Throwable, operation: String) {
        TODO("Not yet implemented")
    }

    override fun reportCriticalError(error: Throwable, context: String) {
        TODO("Not yet implemented")
    }

    override fun getRecoverySuggestions(error: Throwable): List<String> {
        TODO("Not yet implemented")
    }

    override fun isRecoverable(error: Throwable): Boolean {
        TODO("Not yet implemented")
    }

    override fun getErrorStats(): ErrorStats {
        TODO("Not yet implemented")
    }

}

class DefaultEncryptionManager

enum class DefaultTaskExecutionManager
