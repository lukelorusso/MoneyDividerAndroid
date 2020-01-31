package com.lukelorusso.moneydivider.models

data class BalanceRefund(
    val senderSubject: String,
    val receiverSubject: String,
    val value: Double
)
