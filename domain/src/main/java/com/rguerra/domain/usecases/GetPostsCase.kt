package com.rguerra.domain.usecases

import com.rguerra.domain.models.Post
import com.rguerra.domain.models.request.ByUserIdRequest
import com.rguerra.domain.repositories.RepositoryContractor
import io.reactivex.Scheduler

class GetPostsCase(
        private val repositoryContractor: RepositoryContractor,
        subscriberOn: Scheduler,
        observerOn: Scheduler
) : BaseSingleUseCase<List<Post>, GetPostsCase.GetPostsParam>
(
        subscriberOn,
        observerOn,
        useCase = { params -> repositoryContractor.getPosts(ByUserIdRequest(params.userId)) }
) {
    class GetPostsParam(val userId: Int) : BaseUseCase.Params
}