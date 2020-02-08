package com.lukelorusso.moneydivider.scenes.result.output

import com.lukelorusso.domain.usecases.GetBalance
import com.lukelorusso.moneydivider.scenes.base.view.LoadDataView
import io.reactivex.Observable

interface ResultOutputView : LoadDataView<ResultOutputViewModel> {

    fun intentLoadData(): Observable<GetBalance.Param>

}
