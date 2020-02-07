package com.lukelorusso.moneydivider.di.components

import android.app.Activity
import com.lukelorusso.moneydivider.di.PerActivity
import com.lukelorusso.moneydivider.di.modules.ActivityModule
import com.lukelorusso.moneydivider.scenes.home.HomeActivity
import dagger.BindsInstance
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): ActivityComponent
    }

    fun inject(activity: HomeActivity)

}