package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.GetBalance
import io.reactivex.Maybe
import io.reactivex.Single

interface BalanceRepository {

    fun getBalance(param: GetBalance.Param): Maybe<List<String>>

    fun getParticipantSituationMap(param: List<Transaction>): Single<Map<String, Double>>

}
