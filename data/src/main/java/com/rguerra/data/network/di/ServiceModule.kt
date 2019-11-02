package com.rguerra.data.network.di

import com.rguerra.data.network.service.Service
import org.koin.dsl.module
import retrofit2.Retrofit

val servicesModule = module {
    single { createService(get()) }
}

private fun createService(retrofit: Retrofit) = retrofit.create(Service::class.java)