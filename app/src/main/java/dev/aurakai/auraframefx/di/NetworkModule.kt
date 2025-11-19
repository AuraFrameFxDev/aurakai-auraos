package dev.aurakai.auraframefx.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.aurakai.auraframefx.BuildConfig
import dev.aurakai.auraframefx.di.qualifiers.BaseUrl
import dev.aurakai.auraframefx.network.AuraApiService
import dev.aurakai.auraframefx.network.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides network-related dependencies.
 * TEMPORARY FIX: Removed circular dependency with AuthInterceptor
 */
@Module
@InstallIn(/* ...value = */ SingletonComponent::class)
object NetworkModule {

    @Provides
    @BaseUrl
    @Singleton
    fun provideBaseUrl(): String {
        // Environment-based URL configuration
        // Debug builds use local development server
        // Release builds use production API
        return if (BuildConfig.DEBUG) {
            // For local development, you can override this with:
            // BuildConfig field or environment variable
            System.getenv("API_BASE_URL") ?: "http://10.0.2.2:5000/api/v1/"
        } else {
            "https://api.auraframefx.com/v1/"
        }
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            // TEMPORARY: Removed AuthInterceptor to fix circular dependency
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        @BaseUrl baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuraApiService(retrofit: Retrofit): AuraApiService {
        return retrofit.create(AuraApiService::class.java)
    }
}
