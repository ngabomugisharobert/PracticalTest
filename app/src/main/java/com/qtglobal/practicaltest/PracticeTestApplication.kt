package com.qtglobal.practicaltest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PracticeTestApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        // Load SQLCipher native library
        System.loadLibrary("sqlcipher")
    }
}

