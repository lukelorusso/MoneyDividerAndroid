package com.lukelorusso.moneydivider.extensions

import androidx.viewpager.widget.ViewPager

fun ViewPager.onPageScrolledListener(onScrollFinished: ((position: Int) -> Unit)? = null,
                                     onPageSelected: ((position: Int) -> Unit)? = null) {
    var lastPosition = 0
    clearOnPageChangeListeners()
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) { // if scroll action is finished
                onScrollFinished?.invoke(lastPosition)
            }
        }

        override fun onPageScrolled(position: Int, pOffset: Float, pOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            lastPosition = position
            onPageSelected?.invoke(position)
        }
    })
}

fun ViewPager.onScrollFinishedListener(onScrollFinished: ((position: Int) -> Unit)? = null) =
        this.onPageScrolledListener(onScrollFinished = onScrollFinished)
