package fr.guddy.kandroidmodular

import android.app.Application
import org.koin.android.ext.android.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(
                this,
                allModules
        )
    }
}