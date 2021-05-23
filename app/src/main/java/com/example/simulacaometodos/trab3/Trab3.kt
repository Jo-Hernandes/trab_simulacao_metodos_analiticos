package com.example.simulacaometodos.trab3

import com.example.simulacaometodos.R
import com.example.simulacaometodos.TimeInterval
import com.example.simulacaometodos.trab1.format

val trab3Scheduler = Scheduler(
    mutableListOf(
        0.2176,
        0.0103,
        0.1109,
        0.3456,
        0.991,
        0.2323,
        0.9211,
        0.0322,
        0.1211,
        0.5131,
        0.7208,
        0.9172,
        0.9922,
        0.8324,
        0.5011,
        0.2931
    )
)
private const val FIRST_ARRIVAL = 1.0

val queue1 = Queue(
    arrivalTime = TimeInterval(1.0, 4.0),
    processingTime = TimeInterval(1.0, 1.5),
    serverSize = 1,
    queueSize = 9,
    scheduler = trab3Scheduler
)

val queue2 = Queue(
    processingTime = TimeInterval(5.0, 10.0),
    serverSize = 3,
    queueSize = 5,
    scheduler = trab3Scheduler
)

val queue3 = Queue(
    processingTime = TimeInterval(10.0, 20.0),
    serverSize = 2,
    queueSize = 8,
    scheduler = trab3Scheduler
)

fun main3() {
    trab3Scheduler.initialEvent = SchedulerEvent(queue1::arrival, FIRST_ARRIVAL)

    queue1.addRoute(RouteNode(queue3, 0.0..0.2))
    queue1.addRoute(RouteNode(queue2, 0.2..1.0))

    queue2.addRoute(RouteNode(queue1, 0.0..0.3))
    queue2.addRoute(RouteNode(queue3, 0.3..0.8))

    queue3.addRoute(RouteNode(queue2, 0.0..0.7))

    while (trab3Scheduler.hasRandom()) {
        trab3Scheduler.executeNextEvent()
    }

    showResults(queue1.timeAccumulator, "queue1", trab3Scheduler.currentTime)
    showResults(queue2.timeAccumulator, "queue2", trab3Scheduler.currentTime)
    showResults(queue3.timeAccumulator, "queue3", trab3Scheduler.currentTime)

    print("total tempo: ${trab3Scheduler.currentTime}   \n")

    R.raw.system
}

private fun showResults(accountedTimes: List<Double>, queueName: String, currentTime: Double) {
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
