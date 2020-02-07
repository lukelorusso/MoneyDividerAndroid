package com.lukelorusso.data.di.components

import android.content.Context
import com.google.gson.Gson
import com.lukelorusso.data.modules.NetModule
import com.lukelorusso.data.modules.RepositoryModule
import com.lukelorusso.domain.repository.TransactionRepository
import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.repository.BalanceRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetModule::class), (RepositoryModule::class)])
interface DataComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataComponent
    }

    // Exposed to sub-graphs
    fun provideGson(): Gson

    fun provideBalanceRepository(): BalanceRepository

    fun provideHistoryRepository(): HistoryRepository

    fun provideTransactionRepository(): TransactionRepository

}
