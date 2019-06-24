package com.lukelorusso.moneydivider

import android.os.Bundle
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

    private fun initView() = mainBtnSubmit.setOnClickListener {
        val pairTotalsLines = elaborate(mainTextInput.text.toString())
        startActivity(ResultActivity.newIntent(this, pairTotalsLines))
    }

    private fun elaborate(text: String): Pair<HashMap<String, BigDecimal>, ArrayList<String>> {
        val lines = text.split("\n")
        val totalMap = hashMapOf<String, BigDecimal>()

        lines.forEach { line ->
            val items = line.split("\\s+".toRegex())

            if (items.size > 1) {
                var value: BigDecimal = items[0].replace(',', '.').toBigDecimalOrNull()
                    ?: BigDecimal.ZERO
                value.setScale(2, RoundingMode.CEILING)

                val person = items[1]
                value = value.add(totalMap[person] ?: BigDecimal.ZERO)
                totalMap[person] = value
            }
        }
        return totalMap to ArrayList<String>().apply { addAll(lines) }
    }

}
