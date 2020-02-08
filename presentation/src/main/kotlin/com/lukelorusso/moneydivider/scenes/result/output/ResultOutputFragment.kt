package com.lukelorusso.moneydivider.scenes.result.output

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Constant
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.GetBalance
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.*
import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState
import com.lukelorusso.moneydivider.scenes.result.ResultFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_result_output.*
import javax.inject.Inject

class ResultOutputFragment : ResultFragment(), ResultOutputView {

    companion object {
        private const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"

        val TAG: String = ResultOutputFragment::class.java.simpleName

        fun newInstance(
            gson: Gson,
            transactionList: List<Transaction>
        ): ResultOutputFragment = ResultOutputFragment().build {
            putString(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
        }
    }

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var presenter: ResultOutputPresenter

    // Intents
    private val intentLoadData = PublishSubject.create<GetBalance.Param>()

    // Properties
    private val transactionList by lazy {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_result_output, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    //region intent
    override fun intentLoadData(): Observable<GetBalance.Param> = intentLoadData
    //endregion

    //region RENDER
    override fun render(viewModel: ResultOutputViewModel) {
        activity?.runOnUiThread {
            TimberWrapper.d { "render: $viewModel" }

            showLoading(viewModel.loadingState == LoadingState.LOADING)
            showRetryLoading(viewModel.loadingState == LoadingState.RETRY)
            showContent(content, viewModel.contentState == ContentState.CONTENT)
            showError(viewModel.contentState == ContentState.ERROR)

            renderData(viewModel.participantSituationMap, viewModel.balance)
            renderSnack(viewModel.snackMessage)
        }
    }
    //endregion

    //region PRIVATE
    private fun renderData(participantSituationMap: Map<String, Double>?, balance: List<String>?) {
        participantSituationMap?.also { situationMap ->
            var output = ""

            // Total expense per person
            output += "${getString(R.string.result_total_per_person)}\n"
            transactionList?.getTotalMap()?.forEach { (person, valueAsDouble) ->
                // formatting value
                val value = valueAsDouble.toIntlNumberString()
                output += "$person = $value\n"
            }
            output += "${Constant.Message.SEPARATOR}\n\n"

            // Situation per person
            output += "${getString(R.string.result_amount_per_person)}\n"
            situationMap.forEach { (person, valueAsDouble) ->
                // formatting value
                val value = valueAsDouble.toIntlNumberBigDecimal()
                output += "$person = $value (${when (value.signum()) { // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
                    1 -> getString(R.string.result_give_suffix)
                    -1 -> getString(R.string.result_take_suffix)
                    else -> ""
                }})\n"
            }
            output += "${Constant.Message.SEPARATOR}\n\n"

            // Repartition
            output += "${getString(R.string.result_repartition)}\n"
            balance?.forEach { line ->
                output += "$line\n"
            }
            output += "${Constant.Message.SEPARATOR}\n\n"
            
            resultTextOutput.text = output
        }
    }

    override fun initView() {
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
    //endregion

}
