package com.lukelorusso.moneydivider.scenes.base.view

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_progress_full.*

abstract class ABaseDataFragment : ABaseFragment() {

    //region RENDER
    protected fun showFullLoading(visible: Boolean) {
        progressFull.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showLoading(visible: Boolean) {
        progress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showRefreshingLoading(swipeRefreshLayout: SwipeRefreshLayout, visible: Boolean) {
        swipeRefreshLayout.isRefreshing = visible
    }

    protected fun showRetryLoading(visible: Boolean) {
        btnErrorRetry.isClickable = !visible
        errorProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    protected open fun showContent(content: View, visible: Boolean) {
        content.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showError(visible: Boolean) {
        viewError.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun renderError(messageError: String?) {
        messageError?.also { message ->
            textErrorDescription.text = message
        }
    }

    protected fun renderSnack(messageError: String?) {
        messageError?.also { message ->
            activity?.also { activity ->
                Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    //endregion

}
