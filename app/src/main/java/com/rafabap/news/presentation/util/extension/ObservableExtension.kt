package com.rafabap.news.presentation.util.extension

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Single

@SuppressLint("CheckResult")
fun <T: Any> Single<T>.doSubscribe(
    onSuccess: (response: T) -> Unit,
    onError: ((throwable: Throwable) -> Unit)? = null
) {
    this.subscribe({ response ->
                       onSuccess.invoke(response)
                   }, {
        onError?.invoke(it)
    })
}