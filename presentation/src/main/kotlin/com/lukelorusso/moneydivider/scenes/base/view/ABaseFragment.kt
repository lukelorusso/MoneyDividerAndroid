package com.lukelorusso.moneydivider.scenes.base.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lukelorusso.moneydivider.AndroidApplication
import com.lukelorusso.moneydivider.di.components.ActivityComponent
import com.lukelorusso.moneydivider.di.components.ApplicationComponent

abstract class ABaseFragment : Fragment() {

    protected val appComponent: ApplicationComponent by lazy { (activity!!.application as AndroidApplication).appComponent }

    protected val activityComponent: ActivityComponent by lazy { (activity as ABaseActivity).activityComponent }

    override fun getContext(): Context = activity!!

    fun getAppCompatActivity(): AppCompatActivity? = activity as? AppCompatActivity

}
