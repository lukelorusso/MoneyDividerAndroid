package com.lukelorusso.moneydivider.di.components

import android.app.Application
import com.lukelorusso.data.di.components.DataComponent
import com.lukelorusso.moneydivider.di.PerApplication
import com.lukelorusso.moneydivider.di.modules.ApplicationModule
import com.lukelorusso.moneydivider.di.modules.UseCaseModule
import dagger.BindsInstance
import dagger.Component

@PerApplication // Constraints this component to one-per-application or unscoped bindings.
@Component(dependencies = [(DataComponent::class)], modules = [(ApplicationModule::class), (UseCaseModule::class)])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
                @BindsInstance application: Application,
                dataComponent: DataComponent
        ): ApplicationComponent
    }

    fun activityComponent(): ActivityComponent.Factory

}
