package com.lukelorusso.data.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.toIntlNumberString(nbDigit: Int = 2, abs: Boolean = false): String =
    toIntlNumberBigDecimal(nbDigit, abs).toString()

fun Double.toIntlNumberBigDecimal(nbDigit: Int = 2, abs: Boolean = false): BigDecimal =
    BigDecimal(this)
        .let { if (abs) it.abs() else it }
        .setScale(nbDigit, RoundingMode.HALF_EVEN)
