package com.example.simulacaometodos

import com.example.simulacaometodos.trab3.RandomGenerator
import kotlin.random.Random

class DefaultRandom : RandomGenerator {

    override fun nextRandom(): Double = Random.nextDouble()
}
