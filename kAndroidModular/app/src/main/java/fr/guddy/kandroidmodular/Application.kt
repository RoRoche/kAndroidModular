package fr.guddy.kandroidmodular

import android.support.multidex.MultiDexApplication
import fr.guddy.kandroidmodular.di.allModules
import org.koin.android.ext.android.startKoin

class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        startKoin(
                this,
                allModules
        )
    }
}