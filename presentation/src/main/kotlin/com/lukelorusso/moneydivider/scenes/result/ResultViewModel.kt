package com.lukelorusso.moneydivider.scenes.result

import com.lukelorusso.moneydivider.scenes.base.view.ContentState
import com.lukelorusso.moneydivider.scenes.base.view.LoadingState

data class ResultViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val participantSituationMap: Map<String, Double>? = null,
    val balance: List<String>? = null,
    val snackMessage: String? = null
) {
    companion object {
        fun createLoading() =
            ResultViewModel(loadingState = LoadingState.LOADING, contentState = ContentState.CONTENT)

        fun createRetryLoading() =
            ResultViewModel(loadingState = LoadingState.RETRY, contentState = ContentState.ERROR)

        fun createData(participantSituationMap: Map<String, Double>, balance: List<String>) =
            ResultViewModel(contentState = ContentState.CONTENT, participantSituationMap = participantSituationMap, balance = balance)

        fun createSnack(snackMessage: String) =
            ResultViewModel(contentState = ContentState.CONTENT, snackMessage = snackMessage)
    }

}
