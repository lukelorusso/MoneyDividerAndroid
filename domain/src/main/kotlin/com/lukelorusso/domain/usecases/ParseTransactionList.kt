package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.repository.TransactionRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.SingleUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.Single
import javax.inject.Inject

class ParseTransactionList
@Inject constructor(
    private val repository: TransactionRepository,
    useCaseScheduler: UseCaseScheduler? = null, logger: Logger? = null
) :
    SingleUseCase<List<Transaction>, ParseTransactionList.Param>(useCaseScheduler, logger) {

    override fun build(param: Param): Single<List<Transaction>> =
        repository.parseTransactionList(param)

    data class Param(
        val messageTimestamp: Long,
        val unParsedInputList: List<String>
    )

}
