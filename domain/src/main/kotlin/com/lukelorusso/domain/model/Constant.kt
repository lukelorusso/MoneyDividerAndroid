package com.lukelorusso.domain.model

object Constant {

    object Message {
        const val SEPARATOR = "---"
        const val YOU_OWE = "you pay back"
        const val YOU_GET = "you get back"
        const val OWES = "owes"
    }

    object Operator {
        const val PLUS = "+"
        const val STAR = "*"
    }

    object RegExp {
        private const val PARTICIPANT_NAME_SINGLE_CHAR_STRING = "[^()|!#$%^&*\\-_=+]"
        val PARTICIPANT_NAME_SINGLE_CHAR =
            PARTICIPANT_NAME_SINGLE_CHAR_STRING.toRegex()//"^[a-zA-Z]\$".toRegex()
        val PARTICIPANT_NAME = "$PARTICIPANT_NAME_SINGLE_CHAR_STRING+".toRegex()
        val VALUE = "\\d+(\\.\\d{1,2})?".toRegex()
        val GROUP_NAME = "^[A-Z]{3,12}\$".toRegex()
        val WHITE_SPACES = "\\s+".toRegex()
    }

    object AmbrogioStoredKey {
        const val TRANSACTION_LIST = "TRANSACTION_LIST"
        const val GROUP_LIST = "GROUP_LIST"
    }
}