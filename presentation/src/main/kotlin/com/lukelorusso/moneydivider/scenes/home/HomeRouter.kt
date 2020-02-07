package com.lukelorusso.moneydivider.scenes.home

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.scenes.result.ResultActivity
import javax.inject.Inject

class HomeRouter
@Inject internal constructor(private val activity: AppCompatActivity) {

    @Inject
    lateinit var gson: Gson

    fun gotoResult(transactionList: List<Transaction>) {
        activity.startActivity(
            ResultActivity.newIntent(
                activity,
                gson,
                transactionList
            )
        )
    }

}
