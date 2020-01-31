package com.lukelorusso.moneydivider.scenes.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.mapper.BalanceMapper
import com.lukelorusso.moneydivider.models.Constant
import com.lukelorusso.moneydivider.models.Transaction
import kotlinx.android.synthetic.main.activity_result.*
import java.math.BigDecimal
import java.math.RoundingMode

class ResultActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"
        private const val EXTRA_TOTAL_MAP = "EXTRA_TOTAL_MAP"

        fun newIntent(
            context: Context,
            gson: Gson,
            transactionList: List<Transaction>,
            totalMap: Map<String, Double>
        ): Intent =
            Intent(context, ResultActivity::class.java).apply {
                putExtra(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
                putExtra(EXTRA_TOTAL_MAP, gson.toJson(totalMap))
            }
    }

    private val gson = Gson()

    // Properties
    private val transactionList by lazy {
        intent.getStringExtra(EXTRA_TRANSACTION_LIST).let { gson.fromJson<List<Transaction>>(it) }
    }
    private val totalMap by lazy {
        intent.getStringExtra(EXTRA_TOTAL_MAP).let { gson.fromJson<Map<String, Double>>(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toolbar back arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initView()
    }

    private fun initView() {
        Handler().post {
            var total = 0.0
            totalMap.forEach { (_, valueAsDouble) ->
                total += valueAsDouble
            }
            supportActionBar?.title = getString(
                R.string.result_total,
                BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN)
            )

            resultTextOutput.text = writeOutput()
        }
    }

    private fun writeOutput(): String {
        var output = ""

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

        return output
    }

    /*private fun elaborate(): String {
        var total = BigDecimal.ZERO

        totalMap.values.forEach { value -> total = total.add(value) } // filling total

        val totalDivided = if (totalMap.size > 0) total.divide(
            BigDecimal(totalMap.size),
            BigDecimal.ROUND_CEILING
        ) else BigDecimal.ZERO // division of total by person number

        val totalOutput = getString(R.string.result_total, total) // TODO
        supportActionBar?.title = totalOutput

        var totalDividedOutput = ""
        var repartitionOutput = ""
        var error = BigDecimal.ZERO

        if (totalMap.size > 0) {
            totalDividedOutput += getString(R.string.result_total_amount_person, totalDivided)
            totalDividedOutput += getString(R.string.result_details)

            repartitionOutput += getString(R.string.result_repartition)

            totalMap.keys.forEach { person ->
                totalDividedOutput += "\n$person = ${totalMap[person]}"

                // calculating individual repartition
                val individualRepartition = totalDivided
                    .minus(totalMap[person] ?: BigDecimal.ZERO)

                error = error.plus(individualRepartition)

                repartitionOutput += "\n$person = $individualRepartition"
                repartitionOutput += when (individualRepartition.signum()) { // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
                    1 -> getString(R.string.result_give_suffix)
                    -1 -> getString(R.string.result_take_suffix)
                    else -> ""
                }
            }
        }

        val marginErrorOutput = if (error.compareTo(BigDecimal.ZERO) != 0)
            getString(R.string.result_division_margin_error, error)
        else
            ""

        return totalOutput +
                totalDividedOutput +
                repartitionOutput +
                marginErrorOutput +
                "\n"
    }*/

}
