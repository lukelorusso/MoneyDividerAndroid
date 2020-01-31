package com.lukelorusso.moneydivider.models

data class Group(
    val name: String,
    val participantNameList: MutableList<String> = mutableListOf()
)
