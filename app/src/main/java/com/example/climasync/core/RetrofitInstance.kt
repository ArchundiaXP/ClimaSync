package com.example.climasync.core

import com.google.firebase.sessions.dagger.Module
import dagger.ModuleAdd
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
object  RetrofitInstance {
    fun getRetrofit(): Retrofit{
        @SingletonAdd
        @Provides
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @SingletonAdd
    @Provides
    fun provideWeatherAPI(retrofit: Retrofit): WeatherAPI {
        return retrofit.create(WeatherAPI::class.java)
    }
}