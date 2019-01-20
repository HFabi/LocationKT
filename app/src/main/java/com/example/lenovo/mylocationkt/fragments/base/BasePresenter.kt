package com.example.lenovo.mylocationkt.fragments.base

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

interface BasePresenter {
    fun onResume()
    fun onPause()
    fun onDestroy()
}

/**
 * BasePresenterImpl
 */
open class BasePresenterImpl : CoroutineScope {
    private var job = Job()
    private val mainContext = Dispatchers.Main
    protected open val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.d(throwable.message)
    }

    override val coroutineContext: CoroutineContext
        get() = mainContext + job + exceptionHandler

    open fun onResume() {
        job = Job()
    }

    open fun onPause() {
        job.cancel()
    }

    open fun onDestroy() {
        job.cancel()
    }
}