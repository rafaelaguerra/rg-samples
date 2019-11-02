package com.rguerra.data.repository.di

import com.rguerra.data.repository.Repository
import com.rguerra.data.repository.Storage
import com.rguerra.domain.repositories.RepositoryContractor
import org.koin.dsl.module

val repositoryModule = module {
    single(definition = { Storage() })

    single<RepositoryContractor>(definition = { Repository(get(), get()) })
}