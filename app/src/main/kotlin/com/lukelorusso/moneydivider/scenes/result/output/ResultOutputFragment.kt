package com.lukelorusso.moneydivider.scenes.result.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.build
import com.lukelorusso.moneydivider.extensions.getTotalMap
import com.lukelorusso.moneydivider.extensions.toIntlNumberBigDecimal
import com.lukelorusso.moneydivider.extensions.toIntlNumberString
import com.lukelorusso.moneydivider.mapper.BalanceMapper
import com.lukelorusso.moneydivider.models.Constant
import com.lukelorusso.moneydivider.models.Transaction
import com.lukelorusso.moneydivider.scenes.result.ResultFragment
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

    override fun initView() {
        resultTextOutput.text = writeOutput()
    }

    private fun writeOutput(): String {
        var output = ""

        transactionList?.also { transactionList ->
            output += "${getString(R.string.result_total_per_person)}\n"
            transactionList.getTotalMap().forEach { (person, valueAsDouble) ->
                // formatting value
                val value = valueAsDouble.toIntlNumberString()
                output += "$person = $value\n"
            }
            output += "${Constant.Message.SEPARATOR}\n\n"

            val balanceMapper = BalanceMapper()

            output += "${getString(R.string.result_amount_per_person)}\n"
            balanceMapper.mapParticipantSituation(transactionList)
                .forEach { (person, valueAsDouble) ->
                    // formatting value
                    val value = valueAsDouble.toIntlNumberBigDecimal()
                    output += "$person = $value (${when (value.signum()) { // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
                        1 -> getString(R.string.result_give_suffix)
                        -1 -> getString(R.string.result_take_suffix)
                        else -> ""
                    }})\n"
                }
            output += "${Constant.Message.SEPARATOR}\n\n"

            output += "${getString(R.string.result_repartition)}\n"
            balanceMapper.mapBalance(transactionList)?.forEach { line ->
                output += "${line.replace(
                    Constant.Message.OWES,
                    getString(R.string.result_owes)
                )}\n"
            }
            output += "${Constant.Message.SEPARATOR}\n\n"
        }

        return output
    }

}
