package com.example.mysportsapp

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class SportsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("fc94633a-5a13-48c5-b5e7-abb0c5a76ee3")
        MapKitFactory.initialize(this)
    }

}