package com.rguerra.domain.usecases

import com.rguerra.domain.models.request.ByPostIdRequest
import com.rguerra.domain.repositories.RepositoryContractor
import io.reactivex.Scheduler

class DeletePostCase(
        private val repositoryContractor: RepositoryContractor,
        subscriberOn: Scheduler,
        observerOn: Scheduler
) : BaseCompletableUseCase<DeletePostCase.GetPostsParam>
(
        subscriberOn,
        observerOn,
        useCase = { params -> repositoryContractor.deletePost(ByPostIdRequest(params.postId)) }
) {
    class GetPostsParam(val postId: Int) : BaseUseCase.Params
}