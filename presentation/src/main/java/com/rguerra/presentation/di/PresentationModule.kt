package com.rguerra.presentation.di

import android.content.Context
import android.net.ConnectivityManager
import com.rguerra.domain.usecases.di.MAIN_THREAD_SCHEDULER
import com.rguerra.presentation.NetworkAndroidResourceUtil
import com.rguerra.presentation.viewmodel.di.viewModelModule
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = listOf(module {
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single { NetworkAndroidResourceUtil(get()) }
    single(named(MAIN_THREAD_SCHEDULER)) { AndroidSchedulers.mainThread() }
}, viewModelModule)