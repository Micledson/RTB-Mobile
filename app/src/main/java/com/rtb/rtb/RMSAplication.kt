package com.rtb.rtb

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RMSApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}