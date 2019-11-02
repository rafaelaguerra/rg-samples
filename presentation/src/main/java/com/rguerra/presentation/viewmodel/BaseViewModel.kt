package com.rguerra.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rguerra.presentation.Resource

abstract class BaseViewModel<T> : ViewModel() {

    protected val _data = MutableLiveData<Resource<T>>()
    val data: LiveData<Resource<T>> = _data

    protected val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

}