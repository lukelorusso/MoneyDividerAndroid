package com.lukelorusso.data.repository

import com.google.gson.Gson
import com.lukelorusso.data.mapper.HistoryMapper
import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.usecases.GetHistory
import io.reactivex.Maybe

class HistoryDataRepository(
    private val gson: Gson,
    private val historyMapper: HistoryMapper
) : HistoryRepository {

    override fun getHistory(param: GetHistory.Param): Maybe<List<String>> =
        Maybe.fromCallable {
            historyMapper.map(
                param.messageSender,
                param.transactionList,
                param.giveSuffix,
                param.takeSuffix
            )
        }

}
