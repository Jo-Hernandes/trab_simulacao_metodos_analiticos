package com.example.simulacaometodos.trab2

import com.example.simulacaometodos.ScheduledEvent
import com.example.simulacaometodos.TimeInterval
import com.example.simulacaometodos.trab1.format
import kotlin.random.Random


// fila 1
private val PROCESSINGTIME: TimeInterval = TimeInterval(2.0, 5.0)
private const val SERVERSIZE: Int = 2
private const val QUEUESIZE: Int = 3
private val timeAcumulator: MutableList<Double> = MutableList(QUEUESIZE + 1) { 0.0 }

//fila 2
private val NEXTPROCESSINGTIME: TimeInterval = TimeInterval(3.0, 5.0)
private const val NEXTSERVERSIZE: Int = 1
private const val NEXTQUEUESIZE: Int = 3
private val nextTimeAcumulator: MutableList<Double> = MutableList(NEXTQUEUESIZE + 1) { 0.0 }


// chegada
private const val RANDOM_LIST_SIZE = 100000
private const val FIRST_ARRIVAL = 2.5
private val ARRIVALTIME: TimeInterval = TimeInterval(2.0, 3.0)


private val defaultRandomValues: MutableList<Double>?
= null
//        = mutableListOf(
//    0.9921,
//    0.0004,
//    0.5534,
//    0.2761,
//    0.3398,
//    0.8963,
//    0.9023,
//    0.0132,
//    0.4569,
//    0.5121,
//    0.9208,
//    0.0171,
//    0.2299,
//    0.8545,
//    0.6001,
//    0.2921
//)


private var randomValues: MutableList<Double> =
    defaultRandomValues?.toMutableList() ?: MutableList(RANDOM_LIST_SIZE) { Random.nextDouble() }

private fun convertTime(interval: TimeInterval, random: Double) =
    (interval.second - interval.first) * random + interval.first

private fun accountTime(pastTime: Double) {
    val delta = (pastTime - currentTime)
    currentTime += delta
    timeAcumulator[currentQueueSize] += delta
    nextTimeAcumulator[currentNextQueueSize] += delta
}

private val scheduler: MutableList<ScheduledEvent> = mutableListOf()

private var currentQueueSize = 0
private var currentNextQueueSize = 0

private var currentTime: Double = 0.toDouble()

private fun arrival(time: Double) {
    accountTime(time)
    if (currentQueueSize < QUEUESIZE) {
        currentQueueSize++
        if (currentQueueSize <= SERVERSIZE) {
            scheduler.add(
                ScheduledEvent(
                    ::processNext,
                    currentTime + convertTime(PROCESSINGTIME, randomValues.removeFirst())
                )
            )
        }
    }
    randomValues.removeFirstOrNull()?.let {
        scheduler.add(
            ScheduledEvent(
                ::arrival,
                currentTime + convertTime(ARRIVALTIME, it)
            )
        )
    }
}

private fun processNext(time: Double) {
    accountTime(time)
    currentQueueSize--
    if (currentQueueSize >= SERVERSIZE) {
        scheduler.add(
            ScheduledEvent(
                ::processNext,
                currentTime + convertTime(PROCESSINGTIME, randomValues.removeFirst())
            )
        )
    }
    if (currentNextQueueSize < NEXTQUEUESIZE) {
        currentNextQueueSize++
        if (currentNextQueueSize <= NEXTSERVERSIZE) {
            scheduler.add(
                ScheduledEvent(
                    ::departure,
                    currentTime + convertTime(NEXTPROCESSINGTIME, randomValues.removeFirst())
                )
            )
        }
    }
}


private fun departure(time: Double) {
    accountTime(time)
    currentNextQueueSize--
    if (currentNextQueueSize >= NEXTSERVERSIZE) {
        scheduler.add(
            ScheduledEvent(
                ::departure,
                currentTime + convertTime(NEXTPROCESSINGTIME, randomValues.removeFirst())
            )
        )
    }
}

fun main2(executions: Int) {

    var summedAcumulator = timeAcumulator.toList()
    var nextSummedAcumulator = nextTimeAcumulator.toList()

    repeat(executions) {
        clearData()

        scheduler.add(
            ScheduledEvent(
                ::arrival,
                FIRST_ARRIVAL
            )
        )

        while (randomValues.isNotEmpty()) {
            scheduler.minByOrNull { it.second }?.let {
                scheduler.remove(it)
                val (event, time) = it
                event(time)
            }
        }
        summedAcumulator = sumAcumulatedValues(summedAcumulator, timeAcumulator)
        nextSummedAcumulator = sumAcumulatedValues(nextSummedAcumulator, nextTimeAcumulator)

    }

    showResults(summedAcumulator.map { it / executions }, "fila 1")
    showResults(nextSummedAcumulator.map { it / executions }, "fila 2")

}

private fun clearData() {
    currentTime = 0.0
    timeAcumulator.replaceAll { 0.0 }
    nextTimeAcumulator.replaceAll { 0.0 }
    randomValues =
        defaultRandomValues?.toMutableList()
            ?: MutableList(RANDOM_LIST_SIZE) { Random.nextDouble() }
    scheduler.clear()
    currentQueueSize = 0
    currentNextQueueSize = 0

}

private fun sumAcumulatedValues(summedList: List<Double>, acumulator: List<Double>) =
    summedList.mapIndexed { index, d -> d + acumulator[index] }

private fun showResults(accountedTimes: List<Double>, queueName: String) {
    print("valoes para a $queueName \n")
    println("Posicao        |        Tempo                    |    Probabilidade   ")
    accountedTimes.forEachIndexed { position, time ->
        println(
            "$position              |      ${time.format(5)}       |       ${
                (time / currentTime).format(
                    5
                )
            }"
        )
    }
    print("total probabilidade: " + accountedTimes.map { it / currentTime }.sum() + "\n")
}

