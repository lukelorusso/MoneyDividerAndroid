package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.MaybeUseCase
import com.lukelorusso.domain.usecases.base.SingleUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class GetHistory
@Inject constructor(
    private val repository: HistoryRepository,
    useCaseScheduler: UseCaseScheduler? = null,
    logger: Logger? = null
) :
    MaybeUseCase<List<String>, GetHistory.Param>(useCaseScheduler, logger) {

    override fun build(param: Param): Maybe<List<String>> =
        repository.getHistory(param)

    data class Param(
        val messageSender: String,
        val transactionList: List<Transaction>,
        val giveSuffix: String,
        val takeSuffix: String
    )

}
