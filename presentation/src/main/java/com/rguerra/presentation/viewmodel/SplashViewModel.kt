package com.rguerra.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rguerra.domain.usecases.BaseUseCase
import com.rguerra.domain.usecases.SeedInitialDataCase
import com.rguerra.presentation.*

class SplashViewModel(
        private val seedInitialDataCase: SeedInitialDataCase,
        private val networkResourceUtil: NetworkAndroidResourceUtil
) : ViewModel() {

    val result = MutableLiveData<Resource<Void>>()
    val networkConnection = MutableLiveData<Resource<Boolean>>()

    fun onStart() {
        if (networkResourceUtil.isNetworkConnected()) {
            networkConnection.postValue(Data(false))
            seedInitialDataCase.execute(
                    onComplete = ::onFetchDataSuccess,
                    onError = ::onError,
                    params = BaseUseCase.EmptyParams()
            )
        } else {
            networkConnection.postValue(Data(true))
        }
    }

    private fun onError(ex: Throwable?) {
        result.postValue(Failure(ex?.message ?: GENERIC_ERROR))
    }

    private fun onFetchDataSuccess() {
        result.postValue(EmptyResource)
    }

    fun onStop() {
        seedInitialDataCase.clean()
    }
}