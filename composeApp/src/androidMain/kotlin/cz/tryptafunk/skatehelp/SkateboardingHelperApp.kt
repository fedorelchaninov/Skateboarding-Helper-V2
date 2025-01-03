package cz.tryptafunk.skatehelp

import android.app.Application
import cz.tryptafunk.skatehelp.di.initKoin

class SkateboardingHelperApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
