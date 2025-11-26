package dev.aurakai.auraframefx.api.client.infrastructure


import com.squareup.moshi.Moshi
import dev.aurakai.auraframefx.api.client.models.AgentStatus
import dev.aurakai.auraframefx.infrastructure.Serializer
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Request


open class ApiClient(
    protected var baseUrl: String = defaultBasePath,
    private val okHttpClientBuilder: OkHttpClient.Builder? = null,
    private val serializerBuilder: Moshi.Builder = Serializer.moshiBuilder,
    private val callFactory: Call.Factory? = null,
    private val callAdapterFactories: List<CallAdapter.Factory> = listOf(
    ),
    private val converterFactories: List<Converter.Factory> = listOf(
        ScalarsConverterFactory.create(),
        MoshiConverterFactory.create(serializerBuilder.build()),
    )
) {
    protected val apiAuthorizations = mutableMapOf<String, Interceptor>()
    var logger: ((String) -> Unit)? = null

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .apply {
                callAdapterFactories.forEach {
                    addCallAdapterFactory(it)
                }
            }
            .apply {
                converterFactories.forEach {
                    addConverterFactory(it)
                }
            }
    }

    protected val clientBuilder: OkHttpClient.Builder by lazy {
        okHttpClientBuilder ?: defaultClientBuilder
    }

    private val defaultClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor { message -> logger?.invoke(message) }
                .apply { level = HttpLoggingInterceptor.Level.BODY }
            )
    }

    init {
        normalizeBaseUrl()
    }

    /**
     * Adds an authorization to be used by the client
     * @param authName Authentication name
     * @param authorization Authorization interceptor
     * @return ApiClient
     */
    fun addAuthorization(authName: String, authorization: Interceptor): ApiClient {
        if (apiAuthorizations.containsKey(authName)) {
            throw RuntimeException("auth name $authName already in api authorizations")
        }
        apiAuthorizations[authName] = authorization
        clientBuilder.addInterceptor(authorization)
        return this
    }

    fun setLogger(logger: (String) -> Unit): ApiClient {
        this.logger = logger
        return this
    }

    fun <S> createService(serviceClass: Class<S>): S {
        val usedCallFactory = this.callFactory ?: clientBuilder.build()
        return retrofitBuilder.callFactory(usedCallFactory).build().create(serviceClass)
    }

    private fun normalizeBaseUrl() {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
        }
    }



    @Throws(IllegalStateException::class, IOException::class)
    inline fun <reified T, reified U> request(requestConfig: RequestConfig<T>): ApiResponse<U> {
        val client = clientBuilder.build()

        var builder = okhttp3.Request.Builder()
            .url(baseUrl + requestConfig.path.removePrefix("/"))

        // Add headers
        requestConfig.headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }

        // Body
        if (requestConfig.body != null) {
            val contentType = requestConfig.headers["Content-Type"] ?: "application/json"
            val bodyString = if (contentType.contains("json", ignoreCase = true)) {
                 Serializer.moshi.adapter(T::class.java).toJson(requestConfig.body)
            } else {
                 requestConfig.body.toString()
            }
            val requestBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse(contentType), bodyString)
            builder.method(requestConfig.method.name, requestBody)
        } else {
            if (requestConfig.method == RequestMethod.POST || requestConfig.method == RequestMethod.PUT) {
                 builder.method(requestConfig.method.name, okhttp3.RequestBody.create(null, ByteArray(0)))
            } else {
                 builder.method(requestConfig.method.name, null)
            }
        }

        val request = builder.build()
        val response = client.newCall(request).execute()

        val statusCode = response.code
        val headers = response.headers.toMultimap()

        return when (statusCode) {
            in 200..299 -> {
                val responseBody = response.body?.string()
                val data = if (U::class == Unit::class) {
                    Unit as U
                } else if (responseBody != null) {
                     Serializer.moshi.adapter(U::class.java).fromJson(responseBody)!!
                } else {
                     throw IOException("Response body is null")
                }
                Success(data, statusCode, headers)
            }
            in 400..499 -> ClientError(response.message, response.body?.string(), statusCode, headers) as ApiResponse<U>
            in 500..599 -> ServerError(response.message, response.body?.string(), statusCode, headers) as ApiResponse<U>
            else -> Informational(response.message, statusCode, headers) as ApiResponse<U>
        }
    }


    companion object {
        @JvmStatic
        protected val baseUrlKey: String = "org.openapitools.client.baseUrl"

        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(baseUrlKey, "https://api.aurafx.com/v1")
        }

        @JvmStatic
        val defaultClient: Call.Factory by lazy {
            OkHttpClient.Builder().build()
        }
    }
}
