package com.lukelorusso.data.modules

import com.google.gson.Gson
import com.lukelorusso.data.mapper.AddAppTransactionListMapper
import com.lukelorusso.data.mapper.BalanceMapper
import com.lukelorusso.data.mapper.HistoryMapper
import com.lukelorusso.data.repository.BalanceDataRepository
import com.lukelorusso.data.repository.HistoryDataRepository
import com.lukelorusso.data.repository.TransactionDataRepository
import com.lukelorusso.domain.repository.BalanceRepository
import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideBalanceRepository(
        gson: Gson,
        balanceMapper: BalanceMapper
    ): BalanceRepository =
        BalanceDataRepository(
            gson,
            balanceMapper
        )

    @Provides
    @Singleton
    internal fun provideHistoryRepository(
        gson: Gson,
        historyMapper: HistoryMapper
    ): HistoryRepository =
        HistoryDataRepository(
            gson,
            historyMapper
        )

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
