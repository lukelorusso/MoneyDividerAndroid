package com.lukelorusso.moneydivider.scenes.home

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lukelorusso.moneydivider.R
import com.lukelorusso.moneydivider.mapper.AddTransactionMapper
import com.lukelorusso.moneydivider.models.Constant
import com.lukelorusso.moneydivider.models.ParsingException
import com.lukelorusso.moneydivider.models.Transaction
import com.lukelorusso.moneydivider.scenes.result.ResultActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val ALL_PARTICIPANTS_PLACEHOLDER = "[!ALL_PARTICIPANTS_PLACEHOLDER]"
    }

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
                val mapper = AddTransactionMapper()
                val timestamp = System.currentTimeMillis()
                val (senderList, inputList) = parse(inputList())
                val transactionList = inputList.mapIndexed { i, inputLine ->
                    mapper.map(
                        timestamp,
                        senderList[i],
                        inputLine,
                        emptyList()
                    )
                }.filterNotNull()
                it.isEnabled = true
                if (transactionList.isNotEmpty()) startActivity(
                    ResultActivity.newIntent(
                        this,
                        Gson(),
                        ArrayList<Transaction>().apply { addAll(transactionList) }
                    )
                )
            }
        }
    }

    private fun inputList(): List<String> = mainTextInput.text.toString().split("\n")

    /**
     * Examples:
     * "18.00 LL Beer" -> LL pays 18 for all participants
     * "16,50 GS (LL)" -> GS pays 16.50 (we convert "," to ".") ONLY for LL
     * "3 CC (LL, CC) Dinner with LL" -> CC pays 3 for himself and LL (1.50 per head)
     * At the end of the parsing we know there are 3 total participants, so LL pays 6 per head
     */
    private fun parse(inputList: List<String>): Pair<List<String>, List<String>> {
        var allParticipants = mutableListOf<String>()
        val senderList = mutableListOf<String>()
        val parsedInputList = mutableListOf<String>()

        inputList.forEachIndexed { i, line ->
            var remaining = line.replace(Constant.RegExp.WHITE_SPACES, " ")

            // expecting the first thing to be the value
            var valueAsString = remaining.substring(0, remaining.indexOf(" "))
            remaining = remaining.replace("$valueAsString ", "")
            valueAsString = valueAsString.replace(',', '.')
            if (valueAsString.isBlank() || valueAsString.toDoubleOrNull() == null || remaining.isBlank())
                throw ParsingException(i)

            // expecting the second thing to be the payer
            val payer = remaining.substring(0, remaining.indexOf(" "))
            remaining = remaining.replace("$payer ", "")
            if (payer.isBlank()) throw ParsingException(i)
            allParticipants.add(payer)

            // checking participants
            var participants = ALL_PARTICIPANTS_PLACEHOLDER
            if (remaining.startsWith("(")) {
                if (!remaining.contains(")")) throw ParsingException(i)
                participants = remaining.substring(0, remaining.indexOf(")"))
                remaining = remaining.replace("$participants) ", "")
                participants = participants
                    .replace("(", "")
                    .replace(" ", "")
                allParticipants.addAll(participants.split(','))
            }

            // the remaining is the description
            senderList.add(payer)
            parsedInputList.add(
                "$valueAsString|$participants \"$remaining\""
            )

            allParticipants = allParticipants.distinct().toMutableList()
        }

        return Pair(
            senderList,
            parsedInputList.map { line ->
                line.replace(
                    ALL_PARTICIPANTS_PLACEHOLDER,
                    allParticipants.joinToString(",")
                )
            }
        )
    }

}
