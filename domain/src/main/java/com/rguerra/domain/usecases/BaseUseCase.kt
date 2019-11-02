package com.rguerra.domain.usecases

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

sealed class BaseUseCase(
        protected val subscriberOn: Scheduler,
        protected val observerOn: Scheduler
) {
    interface Params

    class EmptyParams : Params
}

open class BaseSingleUseCase<T, in P : BaseUseCase.Params>(
        subscriberOn: Scheduler,
        observerOn: Scheduler,
        private val useCase: (P) -> Single<T>
) : BaseUseCase(subscriberOn, observerOn) {

    private val subscriptions = CompositeDisposable()

    fun execute(
            onSuccess: (T) -> Unit,
            onError: (Throwable) -> Unit,
            params: P
    ) {
        subscriptions.add(useCase(params)
                .subscribeOn(subscriberOn)
                .observeOn(observerOn)
                .subscribe(onSuccess, onError))
    }

    fun subscribe(disposable: Disposable): Disposable {
        subscriptions.add(disposable)
        return disposable
    }

    fun clean() {
        subscriptions.clear()
    }
}

open class BaseCompletableUseCase<in P : BaseUseCase.Params>(
        subscriberOn: Scheduler,
        observerOn: Scheduler,
        private val useCase: (P) -> Completable
) : BaseUseCase(subscriberOn, observerOn) {

    private val subscriptions = CompositeDisposable()

    fun execute(
            onComplete: () -> Unit,
            onError: (Throwable) -> Unit,
            params: P
    ) {
        subscriptions.add(useCase(params)
                .subscribeOn(subscriberOn)
                .observeOn(observerOn)
                .subscribe(onComplete, onError))
    }

    fun subscribe(disposable: Disposable): Disposable {
        subscriptions.add(disposable)
        return disposable
    }

    fun clean() {
        subscriptions.clear()
    }
}