package com.example.simulacaometodos.trab3.parsing

import com.google.gson.Gson

class Parser {

    lateinit var parsedData : ParsedSystem

    fun populateFromJson(json : String){
        parsedData = Gson().fromJson(json, ParsedSystem::class.java)
    }

    fun getScheduler() = parsedData.random.getScheduler()

    fun getQueueList() = parsedData.queues

    fun getNetwork() = parsedData.network
}


