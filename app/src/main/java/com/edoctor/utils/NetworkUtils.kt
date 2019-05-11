package com.edoctor.utils

import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleSource
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun Context.isNetworkAvailable(): Boolean = connectivityManager.isNetworkAvailable()

fun ConnectivityManager.isNetworkAvailable(): Boolean =
    activeNetworkInfo?.isConnected ?: false

class NoConnectivityException : IOException("No connectivity")

fun Throwable.isNoNetworkError(): Boolean =
    this is NoConnectivityException
            || this is UnknownHostException
            || this is SSLException
            || (cause?.isNoNetworkError() ?: false)
