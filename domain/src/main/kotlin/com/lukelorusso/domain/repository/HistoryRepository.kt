package com.lukelorusso.domain.repository

import com.lukelorusso.domain.usecases.GetHistory
import io.reactivex.Maybe

interface HistoryRepository {

    fun getHistory(param: GetHistory.Param): Maybe<List<String>>

}
