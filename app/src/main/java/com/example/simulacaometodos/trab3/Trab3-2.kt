package com.example.simulacaometodos.trab3

import com.example.simulacaometodos.trab1.format
import com.example.simulacaometodos.trab3.parsing.ParsedNetwork
import com.example.simulacaometodos.trab3.parsing.ParsedQueue
import com.example.simulacaometodos.trab3.parsing.ParsedSystem
import com.orhanobut.logger.Logger


fun main3_2(data: ParsedSystem) = with(data.random.getScheduler()) {

    queueMap = initializeQueues(data.queues, this)
    initializeNetwork(data.network)

    queueMap[data.firstArrival.queueName]?.let {
        this.initialEvent = SchedulerEvent(it::arrival, data.firstArrival.initialTime)
    }

    while (hasRandom()) {
        executeNextEvent()
    }

    queueMap.forEach { (name, queue) -> showResults(queue.timeAccumulator, name, currentTime) }
    Logger.i("total tempo: $currentTime   \n")

}

lateinit var queueMap: Map<String, Queue>

fun initializeQueues(
    parsedQueueList: List<ParsedQueue>,
    scheduler: Scheduler
): Map<String, Queue> =
    parsedQueueList.associate { it.queueTag to it.toQueue(scheduler) }

fun initializeNetwork(networkList: List<ParsedNetwork>) =
    networkList.forEach { network ->
        queueMap[network.sourceTag]?.addRoute(
            RouteNode(
                queue = queueMap.getValue(network.targetTag),
                probability = network.minRangeProbability..network.maxRangeProbability
            )
        )
    }

private fun showResults(accountedTimes: List<Double>, queueName: String, currentTime: Double) {
    Logger.i(
        "valoes para a $queueName \n \n"
                + "Posicao        |        Tempo                    |    Probabilidade   \n"
                + accountedTimes.filter { it > 0 }.mapIndexed { position, time ->
            "$position              |      ${time.format(4)}       |       ${
                ((time / currentTime) * 100).format(
                    2
                )
            }"
        }.joinToString("\n")
    )
}
