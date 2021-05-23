package com.example.simulacaometodos.trab3

import com.example.simulacaometodos.TimeInterval

class Queue(
    private val arrivalTime: TimeInterval? = null,
    private val processingTime: TimeInterval,
    private val serverSize: Int,
    private val queueSize: Int,
    private val scheduler: Scheduler
) : EventListener {

    init {
        scheduler.addObserver(this)
    }

    private var routes: MutableList<RouteNode> = mutableListOf()
    private var currentQueueSize = 0
    val timeAccumulator: MutableList<Double> = MutableList(queueSize + 1) { 0.0 }

    fun arrival() {
        if (currentQueueSize < queueSize) {
            currentQueueSize++
            if (currentQueueSize <= serverSize) {
                routeTo()
            }
        }
        arrivalTime?.let {
            scheduler.addEvent(this::arrival, arrivalTime)
        }
    }

    private fun routeTo() {
        if (routes.isNotEmpty()) {
            scheduler.provideRandom()?.let { random ->
                val nextRoute = routes.firstOrNull {
                    it.probability.contains(random)
                }
                nextRoute?.queue?.let { queue ->
                    scheduler.addEvent({ goToNext(queue) }, processingTime)
                } ?: run {
                    scheduler.addEvent(this::departure, processingTime)
                }
            }

        } else {
            scheduler.addEvent(this::departure, processingTime)
        }
    }

    private fun goToNext(nextQueue: Queue) {
        departure()
        if (nextQueue.currentQueueSize < nextQueue.queueSize) {
            nextQueue.currentQueueSize++
            if (nextQueue.currentQueueSize <= nextQueue.serverSize) {
                nextQueue.routeTo()
            }
        }
    }

    private fun departure() {
        currentQueueSize--
        if (currentQueueSize >= serverSize) {
            routeTo()
        }
    }

    fun addRoute(node: RouteNode) {
        routes.add(node)
    }

    override fun onNewEvent(time: Double) {
        timeAccumulator[currentQueueSize] += time
    }
}

