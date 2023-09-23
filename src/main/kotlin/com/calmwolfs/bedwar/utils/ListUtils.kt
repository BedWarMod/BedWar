package com.calmwolfs.bedwar.utils

import java.util.*

object ListUtils {
    fun <E> MutableList<List<E>>.addAsSingletonList(text: E) {
        add(Collections.singletonList(text))
    }
}