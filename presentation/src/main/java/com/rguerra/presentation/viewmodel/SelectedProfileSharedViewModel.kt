package com.rguerra.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rguerra.presentation.models.ProfileUi

class SelectedProfileSharedViewModel : ViewModel() {

    private val _selectedRepo = MutableLiveData<ProfileUi>()
    val selectedRepo: LiveData<ProfileUi> = _selectedRepo

    fun saveSelectedRepo(selectedItem: ProfileUi) {
        with(selectedItem) {
            _selectedRepo.value = this
        }
    }
}