package com.example.simulacaometodos.trab3.parsing

import com.example.simulacaometodos.DefaultRandom
import com.example.simulacaometodos.MyRandom
import com.example.simulacaometodos.TimeInterval
import com.example.simulacaometodos.trab3.Queue
import com.example.simulacaometodos.trab3.Scheduler


data class ParsedSystem(
    val network: List<ParsedNetwork>,
    val queues: List<ParsedQueue>,
    val random: ParsedRandom,
    val firstArrival: FirstArrival
)


data class ParsedQueue(
    val queueTag: String,
    val servers: Int,
    val capacity: Int,
    val maxArrival: Double,
    val maxService: Double,
    val minArrival: Double,
    val minService: Double,
) {

    fun toQueue(scheduler: Scheduler) = Queue(
        arrivalTime = if (maxArrival > 0 && minArrival > 0) TimeInterval(
            minArrival,
            maxArrival
        ) else null,
        processingTime = TimeInterval(minService, maxService),
        serverSize = servers,
        queueSize = capacity,
        scheduler = scheduler
    )
}

data class ParsedNetwork(
    val maxRangeProbability: Double,
    val minRangeProbability: Double,
    val sourceTag: String,
    val targetTag: String
)

data class ParsedRandom(
    val randomValues: List<Double>,
    val useRandom: Boolean
) {
    fun getScheduler() = if (useRandom)
        Scheduler(MyRandom(1.0,1.0, 99999.0))
    else
        Scheduler(randomValues.toMutableList())
}

data class FirstArrival(
    val initialTime: Double,
    val queueName: String
)
