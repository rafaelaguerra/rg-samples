package com.rguerra.domain.usecases

import com.rguerra.domain.models.request.NewPostRequest
import com.rguerra.domain.repositories.RepositoryContractor
import io.reactivex.Scheduler

class AddNewPostCase(
        private val repositoryContractor: RepositoryContractor,
        subscriberOn: Scheduler,
        observerOn: Scheduler
) : BaseCompletableUseCase<AddNewPostCase.NewPostParam>
(
        subscriberOn,
        observerOn,
        useCase = { params -> repositoryContractor.addNewPost(NewPostRequest(params.title, params.body, params.userId)) }
) {
    class NewPostParam(val title: String, val body: String, val userId: Int) : BaseUseCase.Params
}