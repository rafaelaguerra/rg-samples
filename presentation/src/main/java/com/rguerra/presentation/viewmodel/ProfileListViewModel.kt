package com.rguerra.presentation.viewmodel

import com.rguerra.domain.models.ProfileModel
import com.rguerra.domain.usecases.BaseUseCase
import com.rguerra.domain.usecases.GetProfilesCase
import com.rguerra.presentation.Data
import com.rguerra.presentation.Failure
import com.rguerra.presentation.GENERIC_ERROR
import com.rguerra.presentation.models.ProfileUi

class ProfileListViewModel(
        private val getUsersCase: GetProfilesCase
) : BaseViewModel<List<ProfileUi>>() {

    private var list: List<ProfileUi>? = null

    fun loadData(previousSelectedUi: ProfileUi?) {
        _loading.postValue(true)
        getUsersCase.execute(
                onSuccess = { onFetchDataSuccess(list = it, previousSelectedUi = previousSelectedUi) },
                onError = ::onError,
                params = BaseUseCase.EmptyParams()
        )
    }

    private fun onError(ex: Throwable?) {
        _data.postValue(Failure(ex?.message ?: GENERIC_ERROR))
        _loading.postValue(false)
    }

    private fun onFetchDataSuccess(list: List<ProfileModel>, previousSelectedUi: ProfileUi?) {
        _data.postValue(Data(list.map {
            ProfileUi(
                    id = it.id,
                    name = it.name,
                    numberPosts = it.numberOfPosts.toString(),
                    numberAlbums = it.numberOfAlbums.toString(),
                    company = it.company,
                    selected = previousSelectedUi?.id == it.id
            )
        }.also {
            this.list = it
        }))

        _loading.postValue(false)
    }

    fun saveSelectedProfile(profileUi: ProfileUi) {
        list?.let { profilesList ->
            profilesList.forEach {
                it.selected = it.id == profileUi.id
            }
            _data.postValue(Data(profilesList))
        }
    }

    fun onStop() {
        getUsersCase.clean()
    }
}