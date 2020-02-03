package com.lukelorusso.moneydivider.scenes.result

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.gson.Gson
import com.lukelorusso.moneydivider.models.Transaction
import com.lukelorusso.moneydivider.scenes.result.output.OutputFragment
import java.lang.ref.WeakReference

/**
 * @author LukeLorusso on 07-01-2019.
 */
class ResultPagerAdapter(
    fm: FragmentManager,
    private val gson: Gson,
    private val transactionList: List<Transaction>,
    private val totalMap: Map<String, Double>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // It's important to keep WeakReference to Fragments.
    // Otherwise the risk is to lose the fragment's instance (empty context)
    private var mOutputFragment: WeakReference<OutputFragment>? = null

    private val tabTitles = arrayOf(
        OutputFragment.TAG
    )

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val f = mOutputFragment?.get() ?: OutputFragment.newInstance(
                    gson,
                    transactionList,
                    totalMap
                )
                if (mOutputFragment == null || mOutputFragment?.get() == null)
                    mOutputFragment = WeakReference(f)
                f
            }
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""//tabTitles[position]
    }
}
