package com.lukelorusso.moneydivider.scenes.home

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lukelorusso.moneydivider.R
import com.lukelorusso.data.mapper.AddAppTransactionListMapper
import com.lukelorusso.domain.model.ParsingException
import com.lukelorusso.moneydivider.scenes.result.ResultActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        initView()
    }

    private fun initView() {
        mainBtnSubmit.setOnClickListener {
            it.isEnabled = false
            Handler().post {
                val mapper =
                    AddAppTransactionListMapper()
                try {
                    val transactionList = mapper.map(
                        System.currentTimeMillis(),
                        inputList()
                    )

                    it.isEnabled = true
                    if (transactionList.isNotEmpty()) startActivity(
                        ResultActivity.newIntent(
                            this,
                            Gson(),
                            transactionList
                        )
                    )
                } catch (e: ParsingException) {
                    Toast.makeText(this, "Parsing exception", Toast.LENGTH_SHORT).show()
                    it.isEnabled = true
                }
            }
        }
    }

    private fun inputList(): List<String> = mainTextInput.text.toString().split("\n")

}
