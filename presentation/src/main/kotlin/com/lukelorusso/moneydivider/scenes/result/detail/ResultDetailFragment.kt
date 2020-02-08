package com.lukelorusso.moneydivider.scenes.result.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.domain.usecases.GetHistory
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.build
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.extensions.toIntlNumberBigDecimal
import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState
import com.lukelorusso.moneydivider.scenes.result.ResultFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_result_list.*
import javax.inject.Inject

class ResultDetailFragment : ResultFragment(), ResultDetailView {

    companion object {
        private const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"

        val TAG: String = ResultDetailFragment::class.java.simpleName

        fun newInstance(
            gson: Gson,
            transactionList: List<Transaction>
        ): ResultDetailFragment = ResultDetailFragment().build {
            putString(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
        }
    }

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var presenter: ResultDetailPresenter

    // Properties
    private val transactionList by lazy {
        arguments?.getString(EXTRA_TRANSACTION_LIST)?.let { gson.fromJson<List<Transaction>>(it) }
    }

    // View
    private val adapter by lazy { ResultDetailAdapter() }

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
        inflater.inflate(R.layout.fragment_result_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    //region intent
    override fun intentLoadData(): Observable<GetHistory.Param> = adapter.intentItemLoad.map {
        GetHistory.Param(
            it,
            transactionList ?: emptyList(),
            getString(R.string.result_give_suffix),
            getString(R.string.result_take_suffix)
        )
    }
    //endregion

    //region RENDER
    override fun render(viewModel: ResultDetailViewModel) {
        activity?.runOnUiThread {
            TimberWrapper.d { "render: $viewModel" }

            showLoading(viewModel.loadingState == LoadingState.LOADING)
            showRetryLoading(viewModel.loadingState == LoadingState.RETRY)
            showContent(recyclerViewResult, viewModel.contentState == ContentState.CONTENT)
            showError(viewModel.contentState == ContentState.ERROR)

            renderData(viewModel.messageSender, viewModel.historyLog, viewModel.situation)
            renderSnack(viewModel.snackMessage)
        }
    }
    //endregion

    //region PRIVATE
    private fun renderData(
        messageSender: String?,
        historyLog: List<String>?,
        situation: Double?
    ) {
        messageSender?.also { sender ->
            historyLog?.also { history ->
                situation?.also { value ->
                    val historyFormatted = history.joinToString("\n") { line ->
                        String.format(
                            getString(R.string.result_list_pointer_pattern),
                            line.substring(9, line.length)
                        )
                    }
                    adapter.historyMap[sender] = historyFormatted

                    val valueAsString = value.toIntlNumberBigDecimal().let {
                        "= $it (${when (it.signum()) { // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
                            1 -> getString(R.string.result_give_suffix)
                            -1 -> getString(R.string.result_take_suffix)
                            else -> ""
                        }})"
                    }
                    adapter.situationMap[sender] = valueAsString

                    val position = adapter.data.indexOf(sender)
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun initView() {
        recyclerViewResult.adapter = adapter
        transactionList?.also { list ->
            val participantList = mutableListOf<String>()
            list.forEach { transaction ->
                participantList.add(transaction.sender)
                participantList.addAll(transaction.participantNameList)
            }
            adapter.data = participantList.distinct().sorted()
        }
    }
    //endregion

}
