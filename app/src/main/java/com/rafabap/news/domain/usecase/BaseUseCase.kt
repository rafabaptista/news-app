package com.rafabap.news.domain.usecase

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseUseCase {
    protected val subscribeScheduler: Scheduler = Schedulers.io()
    protected val observeScheduler: Scheduler = AndroidSchedulers.mainThread()
}