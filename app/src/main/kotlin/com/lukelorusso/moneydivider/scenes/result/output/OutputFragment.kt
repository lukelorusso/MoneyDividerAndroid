package com.lukelorusso.moneydivider.scenes.result.output

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.build
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.mapper.BalanceMapper
import com.lukelorusso.moneydivider.models.Constant
import com.lukelorusso.moneydivider.models.Transaction
import kotlinx.android.synthetic.main.fragment_output.*
import java.math.BigDecimal
import java.math.RoundingMode

class OutputFragment : Fragment() {

    companion object {
        private const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"
        private const val EXTRA_TOTAL_MAP = "EXTRA_TOTAL_MAP"

        val TAG: String = OutputFragment::class.java.simpleName

        fun newInstance(
            gson: Gson,
            transactionList: List<Transaction>,
            totalMap: Map<String, Double>
        ): OutputFragment = OutputFragment().build {
            putString(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
            putString(EXTRA_TOTAL_MAP, gson.toJson(totalMap))
        }
    }

    private val gson = Gson()

    // Properties
    private val transactionList by lazy {
        arguments?.getString(EXTRA_TRANSACTION_LIST)?.let { gson.fromJson<List<Transaction>>(it) }
    }
    private val totalMap by lazy {
        arguments?.getString(EXTRA_TOTAL_MAP)?.let { gson.fromJson<Map<String, Double>>(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_output, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        resultTextOutput.text = writeOutput()
    }

    private fun writeOutput(): String {
        var output = ""

        transactionList?.also { transactionList ->
            totalMap?.also { totalMap ->

                output += "${getString(R.string.result_total_per_person)}\n"
                totalMap.forEach { (person, valueAsDouble) ->
                    // formatting value
                    val value = BigDecimal(valueAsDouble).setScale(2, RoundingMode.HALF_EVEN)
                    output += "$person = $value\n"
                }
                output += "${Constant.Message.SEPARATOR}\n\n"

                val balanceMapper = BalanceMapper()

                output += "${getString(R.string.result_amount_per_person)}\n"
                balanceMapper.mapParticipantSituation(transactionList).forEach { (person, valueAsDouble) ->
                    // formatting value
                    val value = BigDecimal(valueAsDouble).setScale(2, RoundingMode.HALF_EVEN)
                    output += "$person = $value ${when (value.signum()) { // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
                        1 -> getString(R.string.result_give_suffix)
                        -1 -> getString(R.string.result_take_suffix)
                        else -> ""
                    }}\n"
                }
                output += "${Constant.Message.SEPARATOR}\n\n"

                output += "${getString(R.string.result_repartition)}\n"
                balanceMapper.mapBalance(transactionList)?.forEach { line ->
                    output += "${line.replace(Constant.Message.OWES, getString(R.string.result_owes))}\n"
                }
                output += "${Constant.Message.SEPARATOR}\n\n"

            }
        }

        return output
    }

}
