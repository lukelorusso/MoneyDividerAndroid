package com.lukelorusso.moneydivider.scenes.result.detail

import com.lukelorusso.domain.usecases.GetHistory
import com.lukelorusso.domain.usecases.GetParticipantSituationMap
import com.lukelorusso.moneydivider.exception.ErrorMessageFactory
import com.lukelorusso.moneydivider.scenes.base.presenter.APresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ResultDetailPresenter
@Inject constructor(
    private val getHistory: GetHistory,
    private val getParticipantSituationMap: GetParticipantSituationMap,
    private val router: ResultDetailRouter,
    private val scheduler: Scheduler,
    errorMessageFactory: ErrorMessageFactory
) :
    APresenter<ResultDetailView, ResultDetailViewModel>(errorMessageFactory) {

    override fun attach(view: ResultDetailView) {
        val loadData = view.intentLoadData().flatMap { loadData(it) }

        subscribeViewModel(view, loadData)
    }

    //region USE CASES
    private fun getData(param: GetHistory.Param): Observable<ResultDetailViewModel> =
        getHistory.execute(param).toObservable()
            .zipWith(getParticipantSituationMap.execute(param.transactionList).toObservable(),
                BiFunction { historyLog, participantSituationMap ->
                    ResultDetailViewModel.createData(
                        param.messageSender,
                        historyLog,
                        participantSituationMap[param.messageSender]
                    )
                })

    private fun loadData(param: GetHistory.Param): Observable<ResultDetailViewModel> =
        getData(param)
            .startWith(ResultDetailViewModel.createLoading())
            .onErrorReturn { onError(it) }
    //endregion

    private fun onError(error: Throwable): ResultDetailViewModel =
        ResultDetailViewModel.createSnack(getErrorMessage(error))
}
