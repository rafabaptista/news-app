package com.rafabap.news.presentation.view.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class BaseActivity : AppCompatActivity() {
    fun <T> observe(liveData: MutableLiveData<T>, observer: Observer<in T>) {
        liveData.observe(this, observer)
    }

    fun showToast(message: String?) {
        if (message != null && message.isNotEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}