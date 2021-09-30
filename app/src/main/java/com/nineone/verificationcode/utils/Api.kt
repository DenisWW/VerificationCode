package com.nineone.verificationcode.utils

interface Api {

    suspend fun login(): String

}