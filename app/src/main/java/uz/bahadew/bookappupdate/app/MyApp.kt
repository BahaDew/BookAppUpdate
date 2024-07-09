package uz.bahadew.bookappupdate.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.bahadew.bookappupdate.BuildConfig

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.BUILD_TYPE == "debug") {
            Timber.plant(Timber.DebugTree())
        }
    }
}