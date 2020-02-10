package com.lukelorusso.moneydivider.scenes.home

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.ParseTransactionList
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.onTextChanged
import com.lukelorusso.moneydivider.scenes.base.view.ABaseDataActivity
import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState
import com.lukelorusso.moneydivider.scenes.home.help.HelpBottomDialogFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : ABaseDataActivity(), HomeView {

    @Inject
    lateinit var presenter: HomePresenter

    // Intents
    private var intentGotoResult: PublishSubject<List<Transaction>> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        activityComponent.inject(this)
        setSupportActionBar(toolbar)
        initView()
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }

    //region intent
    override fun intentParseInput(): Observable<ParseTransactionList.Param> = mainSubmitBtn
        .clicks()
        .map {
            ParseTransactionList.Param(
                System.currentTimeMillis(),
                inputList()
            )
        }

    override fun intentGotoResult(): Observable<List<Transaction>> = intentGotoResult
    //endregion

    //region RENDER
    override fun render(viewModel: HomeViewModel) {
        runOnUiThread {
            TimberWrapper.d { "render: $viewModel" }

            showLoading(viewModel.loadingState == LoadingState.LOADING)
            showRetryLoading(viewModel.loadingState == LoadingState.RETRY)
            showContent(content, viewModel.contentState == ContentState.CONTENT)
            showError(viewModel.contentState == ContentState.ERROR)

            renderData(viewModel.transactionList)
            renderSnack(viewModel.snackMessage)
        }
    }

    override fun showLoading(visible: Boolean) {
        mainSubmitBtn.isEnabled = !visible
    }
    //endregion

    //region PRIVATE
    private fun inputList(): List<String> = mainTextInput.text.toString().split("\n")

    private fun initView() {
        mainHelpBtn.setOnClickListener {
            HelpBottomDialogFragment.newInstance()
                .show(supportFragmentManager, HelpBottomDialogFragment.TAG)
        }
        mainClearBtn.setOnClickListener {
            mainTextInput.text = null
        }
        mainTextInput.onTextChanged { text ->
            mainClearBtn.visibility = if (text.isEmpty()) View.GONE
            else View.VISIBLE
        }
    }

    private fun renderData(transactionList: List<Transaction>?) {
        transactionList?.also { data ->
            intentGotoResult.onNext(data)
        }
    }
    //endregion

}
