package com.lukelorusso.moneydivider.scenes.result.list

import com.lukelorusso.domain.usecases.GetHistory
import com.lukelorusso.domain.usecases.GetParticipantSituationMap
import com.lukelorusso.moneydivider.exception.ErrorMessageFactory
import com.lukelorusso.moneydivider.scenes.base.presenter.APresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ResultListPresenter
@Inject constructor(
    private val getHistory: GetHistory,
    private val getParticipantSituationMap: GetParticipantSituationMap,
    private val router: ResultListRouter,
    private val scheduler: Scheduler,
    errorMessageFactory: ErrorMessageFactory
) :
    APresenter<ResultListView, ResultListViewModel>(errorMessageFactory) {

    override fun attach(view: ResultListView) {
        val loadData = view.intentLoadData().flatMap { loadData(it) }

        subscribeViewModel(view, loadData)
    }

    //region USE CASES
    private fun getData(param: GetHistory.Param): Observable<ResultListViewModel> =
        getHistory.execute(param).toObservable()
            .zipWith(getParticipantSituationMap.execute(param.transactionList).toObservable(),
                BiFunction { historyLog, participantSituationMap ->
                    ResultListViewModel.createData(
                        param.messageSender,
                        historyLog,
                        participantSituationMap[param.messageSender]
                    )
                })

    private fun loadData(param: GetHistory.Param): Observable<ResultListViewModel> =
        getData(param)
            .startWith(ResultListViewModel.createLoading())
            .onErrorReturn { onError(it) }
    //endregion

    private fun onError(error: Throwable): ResultListViewModel =
        ResultListViewModel.createSnack(getErrorMessage(error))
}
