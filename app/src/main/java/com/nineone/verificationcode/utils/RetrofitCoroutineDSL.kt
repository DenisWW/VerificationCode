package com.nineone.verificationcode.utils

class RetrofitCoroutineDSL<T> {
    var api: Api? = null

       var onSuccess: ((T) -> Unit)? = null



}