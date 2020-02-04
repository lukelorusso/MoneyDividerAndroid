package com.lukelorusso.moneydivider.scenes.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.lukelorusso.moneydivider.extensions.fromJson
import com.lukelorusso.moneydivider.models.Transaction

abstract class ResultFragment : Fragment() {

    companion object {
        internal const val EXTRA_TRANSACTION_LIST = "EXTRA_TRANSACTION_LIST"
    }

    private val gson = Gson()

    // Properties
    internal val transactionList by lazy {
        arguments?.getString(EXTRA_TRANSACTION_LIST)?.let { gson.fromJson<List<Transaction>>(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    internal abstract fun initView()

}
