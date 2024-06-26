package com.example.mvvm.di

import com.example.mvvm.ApplicationConfig
import com.example.mvvm.data.remote.Api
import com.example.mvvm.data.remote.ResponseDeserializer
import com.example.mvvm.data.remote.login.LoginResponse
import com.example.mvvm.data.repository.AuthRepositoryImpl
import com.example.mvvm.data.repository.PaymentRepositoryImpl
import com.example.mvvm.domain.model.remote.PaymentDto
import com.example.mvvm.domain.repository.AuthRepository
import com.example.mvvm.domain.repository.PaymentRepository
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
                val builder = chain.request().newBuilder()
                ApplicationConfig.apiHeaders.forEach { (name, value) ->
                    builder.addHeader(name, value)
                }
                chain.proceed(builder.build())
            }
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(LoginResponse::class.java, ResponseDeserializer<LoginResponse>())
            .registerTypeAdapter(List::class.java, ResponseDeserializer<List<PaymentDto>>())
            .serializeNulls()
            .create()

        return Retrofit.Builder()
            .baseUrl(ApplicationConfig.baseUrl)
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