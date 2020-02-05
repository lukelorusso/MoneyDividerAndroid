package com.lukelorusso.domain.model

data class Group(
    val name: String,
    val participantNameList: MutableList<String> = mutableListOf()
)
