package com.lukelorusso.moneydivider.scenes.result

import com.lukelorusso.domain.usecases.GetBalance
import com.lukelorusso.domain.usecases.GetParticipantSituationMap
import com.lukelorusso.moneydivider.exception.ErrorMessageFactory
import com.lukelorusso.moneydivider.scenes.base.presenter.APresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ResultPresenter
@Inject constructor(
    private val getParticipantSituationMap: GetParticipantSituationMap,
    private val getBalance: GetBalance,
    private val router: ResultRouter,
    private val scheduler: Scheduler,
    errorMessageFactory: ErrorMessageFactory
) :
    APresenter<ResultView, ResultViewModel>(errorMessageFactory) {

    override fun attach(view: ResultView) {
        val loadData = view.intentLoadData().flatMap { loadData(it) }

        subscribeViewModel(view, loadData)
    }

    //region USE CASES
    private fun getData(param: GetBalance.Param): Observable<ResultViewModel> =
        getParticipantSituationMap.execute(param.transactionList).toObservable()
            .zipWith(getBalance.execute(param).toObservable(),
                BiFunction { participantSituationMap, balance ->
                    ResultViewModel.createData(participantSituationMap, balance)
                })

    private fun loadData(param: GetBalance.Param): Observable<ResultViewModel> =
        getData(param)
            .startWith(ResultViewModel.createLoading())
            .onErrorReturn { onError(it) }
    //endregion

    private fun onError(error: Throwable): ResultViewModel =
        ResultViewModel.createSnack(getErrorMessage(error))

}
