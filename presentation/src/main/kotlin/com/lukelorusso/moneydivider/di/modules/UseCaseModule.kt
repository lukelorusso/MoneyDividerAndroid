package com.lukelorusso.moneydivider.di.modules

import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import com.lukelorusso.moneydivider.di.PerApplication
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class UseCaseModule {

    @Provides
    @PerApplication
    internal fun providePostScheduler() = AndroidSchedulers.mainThread()

    @Provides
    @PerApplication
    internal fun provideUseCaseScheduler(postScheduler: Scheduler) = UseCaseScheduler(Schedulers.io(), postScheduler)

    @Provides
    @PerApplication
    internal fun provideLogger(): Logger = object : Logger {
        override fun log(message: () -> String) {
            TimberWrapper.d(message)
        }

        override fun logError(throwable: () -> Throwable) {
            TimberWrapper.e(throwable)
        }
    }

}
