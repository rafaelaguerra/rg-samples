package com.rguerra.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rguerra.domain.models.Post
import com.rguerra.domain.usecases.AddNewPostCase
import com.rguerra.domain.usecases.DeletePostCase
import com.rguerra.domain.usecases.GetPostsCase
import com.rguerra.presentation.Data
import com.rguerra.presentation.Failure
import com.rguerra.presentation.GENERIC_ERROR
import com.rguerra.presentation.models.PostUi
import com.rguerra.presentation.models.ProfileUi

class PostsViewModel(
        private val getPostsCase: GetPostsCase,
        private val deletePost: DeletePostCase,
        private val addNewPost: AddNewPostCase

) : BaseViewModel<List<PostUi>>() {

    private var list: List<PostUi>? = null
    private var currentProfile: ProfileUi? = null

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun start(profileUi: ProfileUi?) = if (profileUi != null) {
        currentProfile = profileUi
        _loading.postValue(true)
        _title.postValue(profileUi.name)
        getPosts(profileUi)
    } else {
        _title.postValue("")
        _data.postValue(Failure(GENERIC_ERROR))
        _loading.postValue(false)
    }

    private fun getPosts(profileUi: ProfileUi) {
        getPostsCase.execute(
                onSuccess = ::populatePosts,
                onError = ::showError,
                params = GetPostsCase.GetPostsParam(profileUi.id))
    }

    fun onPostRemove(item: PostUi) {
        deletePost.execute(
                onComplete = { notifyPostRemoved(item) },
                onError = ::showError,
                params = DeletePostCase.GetPostsParam(postId = item.id))
    }

    private fun populatePosts(posts: List<Post>) {
        _data.postValue(
                Data(posts.map {
                    PostUi(id = it.id, title = it.title, body = it.body)
                }.also {
                    list = it
                }))

        _loading.postValue(false)
    }

    private fun showError(throwable: Throwable) {
        _data.postValue(Failure(throwable.localizedMessage.toString()))
        _loading.postValue(false)
    }

    private fun notifyPostRemoved(item: PostUi) {
        list?.let {
            val updatedList = it.toMutableList()
            updatedList.remove(item)
            list = updatedList
            _data.postValue(Data(updatedList))
        }
        _loading.postValue(false)
    }

    fun addNewPost(title: String, body: String) {
        _loading.postValue(true)
        currentProfile?.let {
            addNewPost.execute(
                    onComplete = { getPosts(it) },
                    onError = ::showError,
                    params = AddNewPostCase.NewPostParam(title = title, body = body, userId = it.id))
        }
    }

    fun onStop() {
        getPostsCase.clean()
        deletePost.clean()
        addNewPost.clean()
    }
}