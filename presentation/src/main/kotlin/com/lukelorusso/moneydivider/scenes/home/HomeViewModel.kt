package com.lukelorusso.moneydivider.scenes.home

import com.lukelorusso.domain.model.Transaction
import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState

data class HomeViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val transactionList: List<Transaction>? = null,
    val snackMessage: String? = null
) {
    companion object {
        fun createLoading() =
            HomeViewModel(loadingState = LoadingState.LOADING, contentState = ContentState.CONTENT)

        fun createRetryLoading() =
            HomeViewModel(loadingState = LoadingState.RETRY, contentState = ContentState.ERROR)

        fun createTransactionList(transactionList: List<Transaction>) =
            HomeViewModel(contentState = ContentState.CONTENT, transactionList = transactionList)

        fun createSnack(snackMessage: String) =
            HomeViewModel(contentState = ContentState.CONTENT, snackMessage = snackMessage)
    }

}
