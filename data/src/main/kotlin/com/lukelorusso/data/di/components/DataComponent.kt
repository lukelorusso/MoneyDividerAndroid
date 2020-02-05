package com.lukelorusso.data.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component/*(modules = [(NetModule::class),
    (PersistenceModule::class),
    (RepositoryModule::class),
    (SessionModule::class)])*/
interface DataComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataComponent
    }

}
