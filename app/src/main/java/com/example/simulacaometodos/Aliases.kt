package com.example.simulacaometodos

typealias TimeInterval = Pair<Double, Double>
typealias ScheduledEvent = Pair<(Double) -> Unit, Double>

private fun Double.format(digits: Int) = "%.${digits}f".format(this).padStart(20, '0')

