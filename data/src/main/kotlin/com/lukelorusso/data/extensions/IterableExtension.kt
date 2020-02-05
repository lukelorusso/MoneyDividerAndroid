package com.lukelorusso.data.extensions

fun <T> Iterable<T>.containsDuplicates(): Boolean {
    return this.toList().size != this.toList().distinct().size
}
