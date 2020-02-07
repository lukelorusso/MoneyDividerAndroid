package com.lukelorusso.moneydivider

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.lukelorusso.data.di.components.DaggerDataComponent
import com.lukelorusso.data.di.components.DataComponent
import com.lukelorusso.moneydivider.di.components.ApplicationComponent
import com.lukelorusso.moneydivider.di.components.DaggerApplicationComponent
import timber.log.Timber

class AndroidApplication : Application() {

    @set:VisibleForTesting
    lateinit var appComponent: ApplicationComponent

    @VisibleForTesting
    val dataComponent: DataComponent by lazy { DaggerDataComponent.factory().create(baseContext) }

    override fun onCreate() {
        super.onCreate()

        // Init Debug log
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Create App Component
        appComponent = createAppComponent()
    }

    @VisibleForTesting
    fun createAppComponent(): ApplicationComponent =
        DaggerApplicationComponent.factory()
            .create(this, dataComponent)

}
