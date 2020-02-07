// "Intl" stands for "International"
@file:Suppress("SpellCheckingInspection")

package com.lukelorusso.data.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.toIntlCurrencyString(nbDigit: Int = 2, abs: Boolean = false): String =
    toIntlCurrencyBigDecimal(nbDigit, abs).toString()

fun Double.toIntlCurrencyBigDecimal(nbDigit: Int = 2, abs: Boolean = false): BigDecimal =
    BigDecimal(this)
        .let { if (abs) it.abs() else it }
        .setScale(nbDigit, RoundingMode.HALF_EVEN)
