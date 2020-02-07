package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.repository.BalanceRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.SingleUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.Single
import javax.inject.Inject

class GetParticipantSituationMap
@Inject constructor(
    private val repository: BalanceRepository,
    useCaseScheduler: UseCaseScheduler? = null,
    logger: Logger? = null
) :
    SingleUseCase<Map<String, Double>, List<Transaction>>(useCaseScheduler, logger) {

    override fun build(param: List<Transaction>): Single<Map<String, Double>> =
        repository.getParticipantSituationMap(param)

}
