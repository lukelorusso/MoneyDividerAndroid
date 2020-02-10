package com.lukelorusso.moneydivider.scenes.result

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.scenes.base.view.ABaseDataFragment
import javax.inject.Inject

abstract class ResultFragment : ABaseDataFragment() {

    companion object {
        internal const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"
    }

    @Inject
    lateinit var gson: Gson

    // Properties
    internal val transactionList by lazy {
        arguments?.getString(EXTRA_TRANSACTION_LIST)?.let { gson.fromJson<List<Transaction>>(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    open fun initView() {}

}
