package com.adej.appik.util

interface RetrofitResultCallBack {
    fun onSuccess(value: String)
    fun onError(value: String)
}
