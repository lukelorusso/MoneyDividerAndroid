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
import com.lukelorusso.moneydivider.extensions.onScrollFinishedListener
import com.lukelorusso.moneydivider.extensions.toIntlNumberString
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
    private var total = 0.0

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
                total.toIntlNumberString()
            )
        }

        viewpagerResult.adapter = ResultPagerAdapter(
            supportFragmentManager,
            gson,
            transactionList,
            totalMap
        )
        viewpagerResult.onScrollFinishedListener { position ->
            supportActionBar?.title = when (position) {
                0 -> getString(
                    R.string.result_total,
                    total.toIntlNumberString()
                )
                else -> getString(R.string.result)
            }
        }
        tablayoutResult.setupWithViewPager(viewpagerResult)
    }

}
