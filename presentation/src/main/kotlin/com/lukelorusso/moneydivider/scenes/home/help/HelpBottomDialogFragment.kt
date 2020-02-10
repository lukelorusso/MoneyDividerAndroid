package com.lukelorusso.moneydivider.scenes.home.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lukelorusso.moneydivider.R


class HelpBottomDialogFragment : BottomSheetDialogFragment() {

    companion object {
        val TAG: String = HelpBottomDialogFragment::class.java.simpleName

        fun newInstance(): HelpBottomDialogFragment = HelpBottomDialogFragment()
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.dialog_help, container, false)

}
