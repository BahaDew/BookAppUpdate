package uz.bahadew.bookappupdate.utils

import timber.log.Timber

fun myLogger(message: String, tag: String = "BAHA") {
    Timber.tag(tag).d(message)
}