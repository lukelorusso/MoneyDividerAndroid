package com.lukelorusso.moneydivider.scenes.result

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.gson.Gson
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.GetBalance
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.scenes.base.view.ABaseDataFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

abstract class ResultFragment : ABaseDataFragment(), ResultView {

    companion object {
        internal const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"
    }

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var presenter: ResultPresenter

    // Intents
    private val intentLoadData = PublishSubject.create<GetBalance.Param>()

    // Properties
    internal val transactionList by lazy {
        arguments?.getString(EXTRA_TRANSACTION_LIST)?.let { gson.fromJson<List<Transaction>>(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    //region intent
    override fun intentLoadData(): Observable<GetBalance.Param> = intentLoadData
    //endregion

    private fun initView() {
        Handler().post {
            transactionList?.also {
                intentLoadData.onNext(
                    GetBalance.Param(
                        it,
                        getString(R.string.result_owes)
                    )
                )
            }
        }
    }

}
