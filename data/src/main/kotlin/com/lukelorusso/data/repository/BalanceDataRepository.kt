package com.lukelorusso.data.repository

import com.google.gson.Gson
import com.lukelorusso.data.mapper.BalanceMapper
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.repository.BalanceRepository
import com.lukelorusso.domain.usecases.GetBalance
import io.reactivex.Maybe
import io.reactivex.Single

class BalanceDataRepository(
    private val gson: Gson,
    private val balanceMapper: BalanceMapper
) : BalanceRepository {

    override fun getBalance(param: GetBalance.Param): Maybe<List<String>> =
        Maybe.fromCallable { balanceMapper.map(param.transactionList, param.owesMessage) }

    override fun getParticipantSituationMap(param: List<Transaction>): Single<Map<String, Double>> =
        Single.fromCallable { balanceMapper.mapParticipantSituation(param) }

}
