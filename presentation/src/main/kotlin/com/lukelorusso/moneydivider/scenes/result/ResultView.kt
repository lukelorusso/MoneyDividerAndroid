package com.lukelorusso.moneydivider.scenes.result

import com.lukelorusso.domain.usecases.GetBalance
import com.lukelorusso.moneydivider.scenes.base.view.LoadDataView
import io.reactivex.Observable

interface ResultView : LoadDataView<ResultViewModel> {

    fun intentLoadData(): Observable<GetBalance.Param>

}
