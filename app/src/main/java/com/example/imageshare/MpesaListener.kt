package com.example.imageshare

interface MpesaListener {

    fun sendSuccessful(amount: String, phone: String, date: String, receipt: String){}
    fun sendFailed(reason: String){}
}
