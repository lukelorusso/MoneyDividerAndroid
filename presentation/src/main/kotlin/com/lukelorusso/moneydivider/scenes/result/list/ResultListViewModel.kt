package com.lukelorusso.moneydivider.scenes.result.list

import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState

data class ResultListViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val messageSender: String? = null,
    val historyLog: List<String>? = null,
    val situation: Double? = null,
    val snackMessage: String? = null
) {
    companion object {
        fun createLoading() =
            ResultListViewModel(
                loadingState = LoadingState.LOADING,
                contentState = ContentState.CONTENT
            )

        fun createRetryLoading() =
            ResultListViewModel(
                loadingState = LoadingState.RETRY,
                contentState = ContentState.ERROR
            )

        fun createData(messageSender: String, historyLog: List<String>, situation: Double?) =
            ResultListViewModel(
                contentState = ContentState.CONTENT,
                messageSender = messageSender,
                historyLog = historyLog,
                situation = situation
            )

        fun createSnack(snackMessage: String) =
            ResultListViewModel(contentState = ContentState.CONTENT, snackMessage = snackMessage)
    }

}
