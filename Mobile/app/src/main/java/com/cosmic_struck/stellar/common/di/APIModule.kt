package com.cosmic_struck.stellar.common.di

import com.cosmic_struck.stellar.stellar.scantext.data.remote.ScanService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // Increase timeouts for heavy AI operations
            .connectTimeout(60, TimeUnit.SECONDS) // Time to establish connection
            .readTimeout(60, TimeUnit.SECONDS)    // Time waiting for data (Server processing)
            .writeTimeout(60, TimeUnit.SECONDS)   // Time sending data (Uploading image)
            .build()
    }
    @Provides
    @Singleton
    fun provideScanService(okHttpClient: OkHttpClient): ScanService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.3:5000")
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScanService::class.java)
    }
}