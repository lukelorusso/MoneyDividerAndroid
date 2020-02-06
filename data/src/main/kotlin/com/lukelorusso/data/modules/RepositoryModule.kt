package com.lukelorusso.data.modules

import com.google.gson.Gson
import com.lukelorusso.data.mapper.AddAppTransactionListMapper
import com.lukelorusso.data.repository.TransactionDataRepository
import com.lukelorusso.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideTransactionRepository(
        gson: Gson,
        addAppTransactionListMapper: AddAppTransactionListMapper
    ): TransactionRepository =
        TransactionDataRepository(
            gson,
            addAppTransactionListMapper
        )

}
