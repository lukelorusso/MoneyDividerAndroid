package com.lukelorusso.moneydivider.scenes.result.output

import com.lukelorusso.domain.usecases.GetBalance
import com.lukelorusso.domain.usecases.GetParticipantSituationMap
import com.lukelorusso.moneydivider.exception.ErrorMessageFactory
import com.lukelorusso.moneydivider.scenes.base.presenter.APresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ResultOutputPresenter
@Inject constructor(
    private val getParticipantSituationMap: GetParticipantSituationMap,
    private val getBalance: GetBalance,
    private val router: ResultOutputRouter,
    private val scheduler: Scheduler,
    errorMessageFactory: ErrorMessageFactory
) :
    APresenter<ResultOutputView, ResultOutputViewModel>(errorMessageFactory) {

    override fun attach(view: ResultOutputView) {
        val loadData = view.intentLoadData().flatMap { loadData(it) }

        subscribeViewModel(view, loadData)
    }

    //region USE CASES
    private fun getData(param: GetBalance.Param): Observable<ResultOutputViewModel> =
        getParticipantSituationMap.execute(param.transactionList).toObservable()
            .zipWith(getBalance.execute(param).toObservable(),
                BiFunction { participantSituationMap, balance ->
                    ResultOutputViewModel.createData(
                        participantSituationMap,
                        balance
                    )
                })

    private fun loadData(param: GetBalance.Param): Observable<ResultOutputViewModel> =
        getData(param)
            .startWith(ResultOutputViewModel.createLoading())
            .onErrorReturn { onError(it) }
    //endregion

    private fun onError(error: Throwable): ResultOutputViewModel =
        ResultOutputViewModel.createSnack(getErrorMessage(error))

}
