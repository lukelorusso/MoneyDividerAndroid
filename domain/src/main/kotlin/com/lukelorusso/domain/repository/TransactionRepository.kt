package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.ParseTransactionList
import io.reactivex.Single

interface TransactionRepository {

    fun parseTransactionList(param: ParseTransactionList.Param): Single<List<Transaction>>

}
