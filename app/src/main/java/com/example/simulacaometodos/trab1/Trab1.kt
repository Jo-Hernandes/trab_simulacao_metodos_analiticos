package com.example.simulacaometodos.trab1

import com.example.simulacaometodos.ScheduledEvent
import com.example.simulacaometodos.TimeInterval
import kotlin.random.Random

private val ARRIVALTIME: TimeInterval = TimeInterval(1.0, 2.0)
private val PROCESSINGTIME: TimeInterval = TimeInterval(3.0, 4.0)
private const val SERVERSIZE: Int = 2
private const val QUEUESIZE: Int = 4

private const val RANDOM_LIST_SIZE = 100000
private const val FIRST_ARRIVAL = 1

private val defaultRandomValues: MutableList<Double>?
        = mutableListOf(
        0.2, 0.7, 0.1, 0.9, 0.3, 0.4, 0.6, 0.4
    )

private val scheduler: MutableList<ScheduledEvent> = mutableListOf()

private val randomValues: MutableList<Double> =
    defaultRandomValues ?: MutableList(RANDOM_LIST_SIZE) { Random.nextDouble() }


private val timeAcumulator: MutableList<Double> = MutableList(QUEUESIZE + 1) { 0.0 }

private fun convertTime(interval: TimeInterval, random: Double) =
    (interval.second - interval.first) * random + interval.first

private fun accountTime(pastTime: Double) {
    val delta = (pastTime - currentTime)
    currentTime += delta
    timeAcumulator[currentQueueSize] += delta
}

private var currentQueueSize = 0

private var currentTime: Double = 0.toDouble()

private fun arrival(time: Double) {
    accountTime(time)
    if (currentQueueSize < QUEUESIZE) {
        currentQueueSize++
        if (currentQueueSize <= SERVERSIZE) {
            scheduler.add(
                ScheduledEvent(
                    ::departure,
                    currentTime + convertTime(PROCESSINGTIME, randomValues.removeFirst())
                )
            )
        }
    }
    scheduler.add(
        ScheduledEvent(
            ::arrival,
            currentTime + convertTime(ARRIVALTIME, randomValues.removeFirst())
        )
    )
}

private fun departure(time: Double) {
    accountTime(time)
    currentQueueSize--
    if (currentQueueSize >= SERVERSIZE) {
        scheduler.add(
            ScheduledEvent(
                ::departure,
                currentTime + convertTime(PROCESSINGTIME, randomValues.removeFirst())
            )
        )
    }
}

fun main() {
    scheduler.add(
        ScheduledEvent(
            ::arrival,
            FIRST_ARRIVAL.toDouble()
        )
    )


    while (randomValues.isNotEmpty()) {
        scheduler.minByOrNull { it.second }?.let {
            scheduler.remove(it)
            val (event, time) = it
            event(time)
            print("event :  " + it + "\n")


//
//            print ("evento:  $event   $time    queue : $currentQueueSize \n")
//            print ("current time: $currentTime \n ")
//            print ("tempo: " + timeAcumulator.joinToString( separator =  "  -  ")+ "\n")
//            print ("fila : " + scheduler.joinToString("  -  ") + "\n")
//            print("------------ \n")
        }
    }

    println("Posicao        |        Tempo                    |    Probabilidade   ")
    timeAcumulator.forEachIndexed { position, time ->
        println(
            "$position              |      ${time.format(5)}       |      " + (time / currentTime).format(
                5
            ) + "%"
        )
    }
    print("total tempo: $currentTime")
}

fun Double.format(digits: Int) = "%.${digits}f".format(this).padStart(20, ' ')
