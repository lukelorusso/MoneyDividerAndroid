package com.lukelorusso.moneydivider.scenes.home

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.ParseTransactionList
import com.lukelorusso.moneydivider.scenes.base.view.LoadDataView
import io.reactivex.Observable

interface HomeView : LoadDataView<HomeViewModel> {

    fun intentParseInput(): Observable<ParseTransactionList.Param>

    fun intentGotoResult(): Observable<List<Transaction>>

}
