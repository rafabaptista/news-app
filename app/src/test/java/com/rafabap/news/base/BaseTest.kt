package com.rafabap.news.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafabap.news.common.util.RxSchedulersOverrideRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito

open class BaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulersOverrideRule: RxSchedulersOverrideRule = RxSchedulersOverrideRule()

    @Before
    open fun setup() {
        TestSuite.init()
    }

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    @After
    open fun clear() {
        TestSuite.clear()
    }
}