package com.lukelorusso.data.repository

import com.google.gson.Gson
import com.lukelorusso.data.functions.MappingExceptionFunction
import com.lukelorusso.data.mapper.AddAppTransactionListMapper
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.repository.TransactionRepository
import com.lukelorusso.domain.usecases.ParseTransactionList
import io.reactivex.Single

class TransactionDataRepository(
    private val gson: Gson,
    private val addAppTransactionListMapper: AddAppTransactionListMapper
) : TransactionRepository {

    override fun parseTransactionList(param: ParseTransactionList.Param): Single<List<Transaction>> =
        Single.fromCallable {
            addAppTransactionListMapper.map(
                param.messageTimestamp,
                param.unParsedInputList
            )
        }
            .onErrorResumeNext(MappingExceptionFunction())
}
