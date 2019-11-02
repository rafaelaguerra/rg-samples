package com.rguerra.domain.usecases.di

import com.rguerra.domain.usecases.*
import io.reactivex.schedulers.Schedulers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SCHEDULER = "SCHEDULER"
const val MAIN_THREAD_SCHEDULER = "MAIN_THREAD_SCHEDULER"

val domainModule = module {
    single(named(SCHEDULER)) { Schedulers.newThread() }
    single { GetProfilesCase(get(), get(named(SCHEDULER)), get(named((MAIN_THREAD_SCHEDULER)))) }
    single { SeedInitialDataCase(get(), get(named(SCHEDULER)), get(named(MAIN_THREAD_SCHEDULER))) }
    single { GetPostsCase(get(), get(named(SCHEDULER)), get(named(MAIN_THREAD_SCHEDULER))) }
    single { DeletePostCase(get(), get(named(SCHEDULER)), get(named(MAIN_THREAD_SCHEDULER))) }
    single { AddNewPostCase(get(), get(named(SCHEDULER)), get(named(MAIN_THREAD_SCHEDULER))) }
}