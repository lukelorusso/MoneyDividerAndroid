package com.lukelorusso.moneydivider.models

data class Transaction(
    val timestamp: Long,
    val sender: String,
    val value: Double,
    val participantNameList: List<String>,
    val description: String,
    val unevenPlusMap: Map<String, Double>,
    val unevenStarMap: Map<String, Int>
)
