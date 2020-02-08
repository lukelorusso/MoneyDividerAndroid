package com.lukelorusso.moneydivider.scenes.result.list

import com.lukelorusso.domain.usecases.GetHistory
import com.lukelorusso.moneydivider.scenes.base.view.LoadDataView
import io.reactivex.Observable

interface ResultListView : LoadDataView<ResultListViewModel> {

    fun intentLoadData(): Observable<GetHistory.Param>

}
