package com.lukelorusso.moneydivider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_result.*
import java.math.BigDecimal
import java.math.RoundingMode


class ResultActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TOTAL_MAP = "extra_total_map"
        private const val EXTRA_LINES = "extra_lines"

        fun newIntent(context: Context, pairTotalsLines: Pair<HashMap<String, BigDecimal>, ArrayList<String>>): Intent =
            Intent(context, ResultActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_MAP, pairTotalsLines.first)
                putExtra(EXTRA_LINES, pairTotalsLines.second)
            }
    }

    // Properties
    private val totalMap by lazy {
        @Suppress("UNCHECKED_CAST")
        intent.getSerializableExtra(EXTRA_TOTAL_MAP) as HashMap<String, BigDecimal>? ?: hashMapOf()
    }
    private val lines by lazy { intent.getStringArrayListExtra(EXTRA_LINES) ?: arrayListOf() }

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
        var total = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING) // set 2 decimal places

        totalMap.values.forEach { value -> total = total.add(value) } // filling total

        val totalDivided = if (totalMap.size > 0) total.divide(
            BigDecimal(totalMap.size),
            BigDecimal.ROUND_CEILING
        ) else BigDecimal.ZERO // division of total by person number

        var result = getString(R.string.result_total, total)
        supportActionBar?.title = result

        if (totalMap.size > 0) {
            result += getString(R.string.result_total_amount_person, totalDivided)
            result += getString(R.string.result_details)
            totalMap.keys.forEach { person ->
                result += "\n$person = ${totalMap[person]}"
            }
            result += getString(R.string.result_repartition)
            totalMap.keys.forEach { person ->
                val individualRepartition = totalDivided.minus(totalMap[person] ?: BigDecimal.ZERO) // subtraction
                result += "\n$person = $individualRepartition"
                result += when (individualRepartition.signum()) { // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
                    1 -> getString(R.string.result_give_suffix)
                    -1 -> getString(R.string.result_take_suffix)
                    else -> ""
                }
            }
        }

        result += "\n"

        resultTextOutput.setText(result)
    }

}
