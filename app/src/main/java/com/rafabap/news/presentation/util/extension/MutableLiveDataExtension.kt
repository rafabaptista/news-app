package com.rafabap.news.presentation.util.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rafabap.news.presentation.view.NetworkState
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun MutableLiveData<NetworkState>.running(detail: String? = null) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.RUNNING, detail))
}

fun MutableLiveData<NetworkState>.success(detail: String? = null) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.SUCCESS, detail))
}

fun MutableLiveData<NetworkState>.empty(detail: String? = null) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.EMPTY, detail))
}

fun MutableLiveData<NetworkState>.error(detail: String) {
    this.postValue(NetworkState(NetworkState.NetworkStateStatus.ERROR, detail))
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}