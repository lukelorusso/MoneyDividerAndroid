package com.lukelorusso.moneydivider.scenes.home

import com.lukelorusso.domain.usecases.ParseTransactionList
import com.lukelorusso.moneydivider.exception.ErrorMessageFactory
import com.lukelorusso.moneydivider.scenes.base.presenter.APresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HomePresenter
@Inject constructor(
    private val parseTransactionList: ParseTransactionList,
    private val router: HomeRouter,
    private val scheduler: Scheduler,
    errorMessageFactory: ErrorMessageFactory
) :
    APresenter<HomeView, HomeViewModel>(errorMessageFactory) {

    override fun attach(view: HomeView) {
        val parseInput = view.intentParseInput().flatMap { parseTransactionList(it) }

        subscribeViewModel(view, parseInput)

        view.intentGotoResult()
            .subscribe { router.gotoResult(it) }
            .addTo(composite)
    }

    //region USE CASES
    private fun parseTransactionList(param: ParseTransactionList.Param): Observable<HomeViewModel> =
        parseTransactionList.execute(param).toObservable()
            .map { HomeViewModel.createTransactionList(it) }
            .startWith(HomeViewModel.createLoading())
            .onErrorReturn { onError(it) }
    //endregion

    private fun onError(error: Throwable): HomeViewModel =
        HomeViewModel.createSnack(getErrorMessage(error))

}
