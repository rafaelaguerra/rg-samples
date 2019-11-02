package com.rguerra.data.network.di

import com.rguerra.data.network.api.Api
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val SERVER_URL = "https://jsonplaceholder.typicode.com/"

val networkModule = module {
    factory { provideOkHttpClient() }
    single { GsonConverterFactory.create() }
    single { RxJava2CallAdapterFactory.create() }
    single { createWebService(get(), get(), get(), SERVER_URL) }
    single { Api(get()) }
}

fun provideOkHttpClient() = OkHttpClient()

fun createWebService(okHttpClient: OkHttpClient,
                     converterFactory: GsonConverterFactory,
                     adapterFactory: RxJava2CallAdapterFactory,
                     url: String): Retrofit =
        Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(adapterFactory)
                .addConverterFactory(converterFactory)
                .build()

