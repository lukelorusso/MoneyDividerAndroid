package com.lukelorusso.moneydivider

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()
    }

    private fun initView() {
        mainBtnSubmit.setOnClickListener {
            it.isEnabled = false
            Handler().post {
                val pair = elaborate(mainTextInput.text.toString())
                it.isEnabled = true
                startActivity(ResultActivity.newIntent(this, pair.first, pair.second))
            }
        }
    }

    private fun elaborate(text: String): Pair<HashMap<String, BigDecimal>, ArrayList<String>> {
        val transactionList = text.split("\n")
        val totalMap = hashMapOf<String, BigDecimal>()

        transactionList.forEach { line ->
            val items = line.split("\\s+".toRegex())

            if (items.size > 1) {
                var value: BigDecimal = items[0].replace(',', '.').toBigDecimalOrNull()
                    ?: BigDecimal.ZERO
                value.setScale(2, RoundingMode.CEILING) // set 2 decimal places and rounding mode

                val person = items[1]
                value = value.add(totalMap[person] ?: BigDecimal.ZERO)
                totalMap[person] = value
            }
        }
        return totalMap to ArrayList<String>().apply { addAll(transactionList) }
    }

}
