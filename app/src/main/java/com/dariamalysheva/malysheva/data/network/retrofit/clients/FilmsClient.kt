package com.dariamalysheva.malysheva.data.network.retrofit.clients

import com.dariamalysheva.malysheva.data.network.retrofit.services.FilmsApiService
import com.dariamalysheva.malysheva.utils.constants.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FilmsClient {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30,TimeUnit.SECONDS)
        .build()

    val apiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FilmsApiService::class.java)
}
