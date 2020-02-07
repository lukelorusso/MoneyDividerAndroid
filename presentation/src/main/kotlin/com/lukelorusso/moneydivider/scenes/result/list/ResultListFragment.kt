package com.lukelorusso.moneydivider.scenes.result.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.build
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState
import com.lukelorusso.moneydivider.scenes.result.ResultFragment
import com.lukelorusso.moneydivider.scenes.result.ResultViewModel
import kotlinx.android.synthetic.main.fragment_result_list.*

class ResultListFragment : ResultFragment() {

    companion object {
        val TAG: String = ResultListFragment::class.java.simpleName

        fun newInstance(
            gson: Gson,
            transactionList: List<Transaction>
        ): ResultListFragment = ResultListFragment().build {
            putString(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
        }
    }

    // View
    private val adapter by lazy {
        ResultListAdapter(
            getString(R.string.result_list_pointer_pattern),
            getString(R.string.result_give_suffix),
            getString(R.string.result_take_suffix)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_result_list, container, false)

    //region RENDER
    override fun render(viewModel: ResultViewModel) {
        activity?.runOnUiThread {
            TimberWrapper.d { "render: $viewModel" }

            showLoading(viewModel.loadingState == LoadingState.LOADING)
            showRetryLoading(viewModel.loadingState == LoadingState.RETRY)
            showContent(recyclerViewResult, viewModel.contentState == ContentState.CONTENT)
            showError(viewModel.contentState == ContentState.ERROR)

            renderData(viewModel.participantSituationMap, viewModel.balance)
            renderSnack(viewModel.snackMessage)
        }
    }
    //endregion

    //region PRIVATE
    private fun renderData(participantSituationMap: Map<String, Double>?, balance: List<String>?) {
        recyclerViewResult.adapter = adapter
        transactionList?.also { list ->
            val participantList = mutableListOf<String>()
            list.forEach { transaction ->
                participantList.add(transaction.sender)
                participantList.addAll(transaction.participantNameList)
            }
            adapter.transactionList = list
            adapter.data = participantList.distinct()
        }
    }
    //endregion

}
