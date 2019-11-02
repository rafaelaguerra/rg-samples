package com.rguerra.profiles

import android.app.Application
import com.rguerra.data.di.dataModule
import com.rguerra.domain.usecases.di.domainModule
import com.rguerra.presentation.di.presentationModule
import leakcanary.LeakSentry
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ProfilesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = false)

        startKoin {
            androidContext(this@ProfilesApplication)
            modules(presentationModule + listOf(domainModule) + dataModule)
        }
    }
}