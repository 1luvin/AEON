package com.example.aeon.di

import com.example.aeon.data.remote.Api
import com.example.aeon.data.remote.ResponseDeserializer
import com.example.aeon.data.remote.login.LoginResponse
import com.example.aeon.data.repository.AuthRepositoryImpl
import com.example.aeon.data.repository.PaymentRepositoryImpl
import com.example.aeon.domain.model.remote.PaymentDto
import com.example.aeon.domain.repository.AuthRepository
import com.example.aeon.domain.repository.PaymentRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideApi(): Api {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("app-key", "12345")
                    .addHeader("v", "1")
                    .build()
                chain.proceed(request)
            }
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(LoginResponse::class.java, ResponseDeserializer<LoginResponse>())
            .registerTypeAdapter(List::class.java, ResponseDeserializer<List<PaymentDto>>())
            .serializeNulls()
            .create()

        return Retrofit.Builder()
            .baseUrl("https://easypay.world/api-test/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: Api): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(api: Api): PaymentRepository {
        return PaymentRepositoryImpl(api)
    }
}