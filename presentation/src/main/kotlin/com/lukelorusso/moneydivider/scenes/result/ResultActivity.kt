package com.lukelorusso.moneydivider.scenes.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.gson.Gson
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.extensions.getTotalMap
import com.lukelorusso.moneydivider.extensions.onScrollFinishedListener
import com.lukelorusso.moneydivider.extensions.toIntlNumberString
import com.lukelorusso.moneydivider.scenes.base.view.ABaseActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : ABaseActivity() {

    companion object {
        private const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"

        fun newIntent(
            context: Context,
            gson: Gson,
            transactionList: List<Transaction>
        ): Intent =
            Intent(context, ResultActivity::class.java).apply {
                putExtra(EXTRA_TRANSACTION_LIST, gson.toJson(transactionList))
            }
    }

    private val gson = Gson()

    // Properties
    private val transactionList by lazy {
        intent.getStringExtra(EXTRA_TRANSACTION_LIST).let { gson.fromJson<List<Transaction>>(it) }
    }
    private val total by lazy {
        var total = 0.0
        transactionList.getTotalMap().forEach { (_, valueAsDouble) ->
            total += valueAsDouble
        }
        total
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
        supportActionBar?.title = getString(
            R.string.result_total,
            total.toIntlNumberString()
        )

        viewpagerResult.adapter = ResultPagerAdapter(
            supportFragmentManager,
            gson,
            transactionList
        )
        viewpagerResult.onScrollFinishedListener { position ->
            supportActionBar?.title = when (position) {
                0 -> getString(
                    R.string.result_total,
                    total.toIntlNumberString()
                )
                1 -> getString(R.string.detail)
                else -> getString(R.string.result)
            }
        }
        tablayoutResult.setupWithViewPager(viewpagerResult)
    }

}
