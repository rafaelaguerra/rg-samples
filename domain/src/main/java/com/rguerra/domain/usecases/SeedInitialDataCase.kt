package com.rguerra.domain.usecases

import com.rguerra.domain.repositories.RepositoryContractor
import io.reactivex.Scheduler

/**
 * Seed data for the first time the
 */
class SeedInitialDataCase(
        private val repositoryContractor: RepositoryContractor,
        subscriberOn: Scheduler,
        observerOn: Scheduler
) : BaseCompletableUseCase<BaseUseCase.EmptyParams>
(
        subscriberOn,
        observerOn,
        useCase = { repositoryContractor.seedUsers() }
)