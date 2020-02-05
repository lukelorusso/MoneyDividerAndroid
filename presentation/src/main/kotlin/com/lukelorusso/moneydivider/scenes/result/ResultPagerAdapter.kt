package com.lukelorusso.moneydivider.scenes.result

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.gson.Gson
import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.scenes.result.list.ResultListFragment
import com.lukelorusso.moneydivider.scenes.result.output.ResultOutputFragment
import java.lang.ref.WeakReference

/**
 * @author LukeLorusso on 07-01-2019.
 */
class ResultPagerAdapter(
    fm: FragmentManager,
    private val gson: Gson,
    private val transactionList: List<Transaction>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // It's important to keep WeakReference to Fragments.
    // Otherwise the risk is to lose the fragment's instance (empty context)
    private val pageMap = mutableMapOf<Int, WeakReference<Fragment>>()

    private val tabTitles = arrayOf(
        ResultOutputFragment.TAG,
        ResultListFragment.TAG
    )

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getItem(position: Int): Fragment {
        var f = pageMap[position]?.get()
        if (f == null) {
            f = when (position) {
                0 -> ResultOutputFragment.newInstance(
                    gson,
                    transactionList
                )
                1 -> ResultListFragment.newInstance(
                    gson,
                    transactionList
                )
                else -> Fragment()
            }
            pageMap[position] = WeakReference(f)
        }
        return f
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""//tabTitles[position]
    }

}
