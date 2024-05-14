package com.example.musicsharing

import android.app.Application

class MusicSharing : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MusicSharing
            private set
    }
}