package dev.aurakai.auraframefx.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized network client for handling all API communications.
 * Manages OkHttp and Retrofit instances with proper configuration.
 */
@Singleton
class NetworkClient @Inject constructor(
    private val context: Context,
    private val authInterceptor: AuthInterceptor,
    private val connectivityManager: NetworkConnectivityManager
) {
    private val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
    private val cacheDir = File(context.cacheDir, "http_cache")
    private val cache = Cache(cacheDir, cacheSize)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .addInterceptor(authInterceptor)
            .addInterceptor(ConnectivityInterceptor(connectivityManager))
            .addInterceptor(ErrorHandlingInterceptor())

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    /**
     * Creates an API service instance for the specified service interface.
     * @param service The service interface class
     * @return An implementation of the service interface
     */
    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    /**
     * Clears the HTTP cache.
     */
    fun clearCache() {
        cache.evictAll()
    }

    /**
     * Gets the current cache size in bytes.
     */
    fun getCacheSize(): Long {
        return cache.size()
    }
}

/**
 * Interceptor to handle network connectivity checks.
 */
class ConnectivityInterceptor(
    private val connectivityManager: NetworkConnectivityManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityManager.isConnected()) {
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())
    }
}

/**
 * Interceptor for handling common HTTP errors.
 */
class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            in 400..499 -> handleClientError(response)
            in 500..599 -> handleServerError(response)
        }

        return response
    }

    private fun handleClientError(response: Response) {
        // Handle client errors (4xx)
        when (response.code) {
            400 -> throw BadRequestException("Bad request")
            401 -> throw UnauthorizedException("Unauthorized")
            403 -> throw ForbiddenException("Access denied")
            404 -> throw NotFoundException("Resource not found")
            429 -> throw RateLimitExceededException("Rate limit exceeded")
        }
    }

    private fun handleServerError(response: Response) {
        // Handle server errors (5xx)
        throw ServerException("Server error: ${response.code}")
    }
}

// Custom exceptions for network errors
class NoConnectivityException : IOException("No network available")
class BadRequestException(message: String) : IOException(message)
class UnauthorizedException(message: String) : IOException(message)
class ForbiddenException(message: String) : IOException(message)
class NotFoundException(message: String) : IOException(message)
class RateLimitExceededException(message: String) : IOException(message)
class ServerException(message: String) : IOException(message)
