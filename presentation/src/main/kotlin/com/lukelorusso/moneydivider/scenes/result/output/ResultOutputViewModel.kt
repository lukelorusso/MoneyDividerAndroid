package com.lukelorusso.moneydivider.scenes.result.output

import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState

data class ResultOutputViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val participantSituationMap: Map<String, Double>? = null,
    val balance: List<String>? = null,
    val snackMessage: String? = null
) {
    companion object {
        fun createLoading() =
            ResultOutputViewModel(
                loadingState = LoadingState.LOADING,
                contentState = ContentState.CONTENT
            )

        fun createRetryLoading() =
            ResultOutputViewModel(
                loadingState = LoadingState.RETRY,
                contentState = ContentState.ERROR
            )

        fun createData(participantSituationMap: Map<String, Double>, balance: List<String>) =
            ResultOutputViewModel(
                contentState = ContentState.CONTENT,
                participantSituationMap = participantSituationMap,
                balance = balance
            )

        fun createSnack(snackMessage: String) =
            ResultOutputViewModel(contentState = ContentState.CONTENT, snackMessage = snackMessage)
    }

}
