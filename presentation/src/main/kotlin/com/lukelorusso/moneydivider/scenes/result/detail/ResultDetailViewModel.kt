package com.lukelorusso.moneydivider.scenes.result.detail

import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState

data class ResultDetailViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val messageSender: String? = null,
    val historyLog: List<String>? = null,
    val situation: Double? = null,
    val snackMessage: String? = null
) {
    companion object {
        fun createLoading() =
            ResultDetailViewModel(
                loadingState = LoadingState.LOADING,
                contentState = ContentState.CONTENT
            )

        fun createRetryLoading() =
            ResultDetailViewModel(
                loadingState = LoadingState.RETRY,
                contentState = ContentState.ERROR
            )

        fun createData(messageSender: String, historyLog: List<String>, situation: Double?) =
            ResultDetailViewModel(
                contentState = ContentState.CONTENT,
                messageSender = messageSender,
                historyLog = historyLog,
                situation = situation
            )

        fun createSnack(snackMessage: String) =
            ResultDetailViewModel(contentState = ContentState.CONTENT, snackMessage = snackMessage)
    }

}
