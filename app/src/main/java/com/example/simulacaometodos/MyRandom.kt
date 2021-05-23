package com.example.simulacaometodos

import com.example.simulacaometodos.trab3.RandomGenerator

class MyRandom (
    private val a: Double,
    private val c: Double,
    private val m: Double,
    seed: Double = 1.0) : RandomGenerator {

    private var state = seed

    override fun nextRandom(): Double {
        state = ((a * state) + c) % m
        return state / m
    }
}
