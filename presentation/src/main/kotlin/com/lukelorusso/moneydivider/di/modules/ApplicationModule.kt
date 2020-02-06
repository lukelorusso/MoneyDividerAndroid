package com.lukelorusso.moneydivider.di.modules

import android.app.Application
import com.lukelorusso.moneydivider.di.PerApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @PerApplication
    internal fun provideContext(application: Application) = application.baseContext

}
