package com.lukelorusso.moneydivider.extensions

fun <T> Iterable<T>.containsDuplicates(): Boolean {
    return this.toList().size != this.toList().distinct().size
}
