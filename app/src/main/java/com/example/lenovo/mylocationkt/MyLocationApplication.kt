package com.example.lenovo.mylocationkt

import android.app.Application
import timber.log.Timber

class MyLocationApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ", " +
                            element.className + ", " +
                            element.methodName + ", " +
                            element.lineNumber
                }
            })
        }
    }
}
