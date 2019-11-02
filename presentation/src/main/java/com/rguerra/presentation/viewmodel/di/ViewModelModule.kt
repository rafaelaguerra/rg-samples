package com.rguerra.presentation.viewmodel.di

import com.rguerra.presentation.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { SelectedProfileSharedViewModel() }
    viewModel { ProfileListViewModel(get()) }
    viewModel { PostsViewModel(get(), get(), get()) }
}