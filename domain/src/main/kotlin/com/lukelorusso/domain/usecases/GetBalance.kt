package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.repository.BalanceRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.MaybeUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.Maybe
import javax.inject.Inject

class GetBalance
@Inject constructor(
    private val repository: BalanceRepository,
    useCaseScheduler: UseCaseScheduler? = null,
    logger: Logger? = null
) :
    MaybeUseCase<List<String>, GetBalance.Param>(useCaseScheduler, logger) {

    override fun build(param: Param): Maybe<List<String>> =
        repository.getBalance(param)

    data class Param(
        val transactionList: List<Transaction>,
        val owesMessage: String
    )

}
