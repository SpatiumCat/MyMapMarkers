package ru.netology.mymapmarkers

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class AppApplication: Application() {
    override fun onCreate() {
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        super.onCreate()
    }
}