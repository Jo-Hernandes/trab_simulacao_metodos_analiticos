package com.example.simulacaometodos

import android.content.Context
import com.example.simulacaometodos.trab1.main
import com.example.simulacaometodos.trab2.main2
import com.example.simulacaometodos.trab3.main3
import com.example.simulacaometodos.trab3.parsing.Parser
import org.junit.Assert
import org.junit.Test
import java.io.IOException


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun trab1_1() {
        main()
    }

    @Test
    fun trab1_2() {
        main2(10)
    }

    @Test
    fun trab1_3(){
        main3()
    }

    @Test
    fun testRandom() {
        val default = 4.0
        val modulo = 1000.0

        val randomGenerator = MyRandom(default, default, modulo, 29292123123123.0)
        val randomGeneratedList = List(10) { randomGenerator.nextRandom() }
        randomGeneratedList.forEach { print("valor $it \n") }
    }


}
