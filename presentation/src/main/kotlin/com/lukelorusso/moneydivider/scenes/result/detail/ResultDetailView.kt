package com.lukelorusso.moneydivider.scenes.result.detail

import com.lukelorusso.domain.usecases.GetHistory
import com.lukelorusso.moneydivider.scenes.base.view.LoadDataView
import io.reactivex.Observable

interface ResultDetailView : LoadDataView<ResultDetailViewModel> {

    fun intentLoadData(): Observable<GetHistory.Param>

}
