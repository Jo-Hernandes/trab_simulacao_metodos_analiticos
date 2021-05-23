package com.example.simulacaometodos.trab3

import com.example.simulacaometodos.TimeInterval

private const val RANDOM_LIST_SIZE = 100000

class Scheduler {

    constructor(random: RandomGenerator) {
        randomValues = MutableList(RANDOM_LIST_SIZE) { random.nextRandom() }
    }

    constructor(list: MutableList<Double>){
        randomValues = list
    }

    var initialEvent: SchedulerEvent?
        get() {
            scheduler.sortBy { it.second }
            return scheduler.firstOrNull()
        }
        set(value) {
            value?.let { scheduler.add(it) }
        }

    var currentTime: Double = 0.toDouble()

    private var randomValues: MutableList<Double>


    private val scheduler: MutableList<SchedulerEvent> = mutableListOf()
    private val eventObservers: MutableList<EventListener> = mutableListOf()

    private fun convertTime(interval: TimeInterval, random: Double) =
        (interval.second - interval.first) * random + interval.first

    fun addEvent(event: () -> Unit, processingTime: TimeInterval) = provideRandom()?.let {
        scheduler.add(
            SchedulerEvent(
                event,
                currentTime + convertTime(processingTime, it)
            )
        )
    }

    fun addObserver(observer: EventListener) = eventObservers.add(observer)

    private fun notifyObservers(pastTime: Double) {
        val delta = (pastTime - currentTime)
        currentTime += delta
        eventObservers.forEach { it.onNewEvent(delta) }
    }

    private fun executeEvent(scheduledEvent: SchedulerEvent) {
        val (event, time) = scheduledEvent
        notifyObservers(time)
        event()
    }

    fun provideRandom(): Double? = randomValues.removeFirstOrNull()
    fun hasRandom() = randomValues.isNotEmpty()

    fun executeNextEvent() {
        scheduler.minByOrNull { it.second }?.let {
            scheduler.remove(it)
            executeEvent(it)
        }
    }
}

typealias SchedulerEvent = Pair<() -> Unit, Double>

interface RandomGenerator {
    fun nextRandom(): Double
}

interface EventListener {
    fun onNewEvent(time: Double)
}
