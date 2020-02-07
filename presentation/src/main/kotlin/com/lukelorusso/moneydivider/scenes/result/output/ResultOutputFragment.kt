package com.lukelorusso.moneydivider.scenes.result.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Constant
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.build
import com.lukelorusso.moneydivider.extensions.getTotalMap
import com.lukelorusso.moneydivider.extensions.toIntlNumberBigDecimal
import com.lukelorusso.moneydivider.extensions.toIntlNumberString
import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState
import com.lukelorusso.moneydivider.scenes.result.ResultFragment
import com.lukelorusso.moneydivider.scenes.result.ResultViewModel
import kotlinx.android.synthetic.main.fragment_result_output.*

class ResultOutputFragment : ResultFragment() {

    companion object {
        val TAG: String = ResultOutputFragment::class.java.simpleName

        fun newInstance(
            gson: Gson,
            transactionList: List<Transaction>
        ): ResultOutputFragment = ResultOutputFragment().build {
            putString(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_result_output, container, false)

    //region RENDER
    override fun render(viewModel: ResultViewModel) {
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
    //endregion

}
