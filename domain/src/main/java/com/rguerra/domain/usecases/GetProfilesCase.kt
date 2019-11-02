package com.rguerra.domain.usecases

import com.rguerra.domain.models.ProfileModel
import com.rguerra.domain.models.request.ByUserIdRequest
import com.rguerra.domain.repositories.RepositoryContractor
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetProfilesCase(
        private val repositoryContractor: RepositoryContractor,
        subscriberOn: Scheduler,
        observerOn: Scheduler
) : BaseSingleUseCase<List<ProfileModel>, BaseUseCase.EmptyParams>
(
        subscriberOn,
        observerOn,
        useCase = { _ ->
            repositoryContractor.getUsers()
                    .flatMapObservable { Observable.fromIterable(it) }
                    .flatMapSingle { user ->
                        repositoryContractor.getPosts(ByUserIdRequest(userId = user.id)).flatMap { listPosts ->
                            repositoryContractor.getAlbums(ByUserIdRequest(userId = user.id))
                                    .map { listAlbums ->
                                        ProfileModel(
                                                id = user.id,
                                                name = user.name,
                                                numberOfPosts = listPosts.size,
                                                numberOfAlbums = listAlbums.size,
                                                company = user.company.companyName
                                        )
                                    }
                        }
                    }.toList()
        }
)
