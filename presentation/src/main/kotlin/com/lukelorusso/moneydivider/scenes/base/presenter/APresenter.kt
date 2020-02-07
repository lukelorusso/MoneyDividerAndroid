package com.lukelorusso.moneydivider.scenes.base.presenter

import com.lukelorusso.moneydivider.exception.ErrorMessageFactory
import com.lukelorusso.moneydivider.scenes.base.view.LoadDataView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class APresenter<in View : LoadDataView<ViewModel>, ViewModel>(private val errorMessageFactory: ErrorMessageFactory) {
    protected val composite = CompositeDisposable()

    abstract fun attach(view: View)

    protected fun subscribeViewModel(view: View, vararg observables: Observable<ViewModel>) {
        composite.add(Observable.mergeArray(*observables).subscribe { view.render(it) })
    }

    protected fun getErrorMessage(error: Throwable): String = errorMessageFactory.getError(error)

    fun detach() {
        composite.clear()
    }

}
