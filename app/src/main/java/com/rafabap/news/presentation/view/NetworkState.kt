package com.rafabap.news.presentation.view

data class NetworkState(val status: NetworkStateStatus, val detail: String?) {
    enum class NetworkStateStatus {
        RUNNING,
        SUCCESS,
        EMPTY,
        ERROR
    }
}