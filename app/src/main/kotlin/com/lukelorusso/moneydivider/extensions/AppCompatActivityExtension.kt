package com.lukelorusso.moneydivider.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lukelorusso.moneydivider.R

/**
 * Adds a [Fragment] to this activity's layout.
 * @param containerViewId The container view to where add the fragment.
 * @param fragment The fragment to be added.
 */
fun AppCompatActivity.addFragment(
    containerViewId: Int,
    fragment: Fragment,
    animationRight: Boolean? = null
) {
    supportFragmentManager.beginTransaction()
        .apply {
            animationRight?.also {
                if (animationRight) {
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }
            replace(containerViewId, fragment)
        }.commit()
}
