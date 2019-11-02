package com.rguerra.data.di

import com.rguerra.data.network.di.networkModule
import com.rguerra.data.network.di.servicesModule
import com.rguerra.data.repository.di.repositoryModule

val dataModule = listOf(
        networkModule,
        servicesModule,
        repositoryModule
)