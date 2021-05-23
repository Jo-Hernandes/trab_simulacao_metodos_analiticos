package com.example.simulacaometodos

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.simulacaometodos.trab3.main3_2
import com.example.simulacaometodos.trab3.parsing.Parser
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun before(){
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(0) // (Optional) How many method line to show. Default 2
            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
            .tag("ANALISE SIMULACAO") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    @Test
    fun testParser() {
        val parser = Parser()
        val instrumentationContext = getInstrumentation().targetContext
        val json = getJsonDataFromAsset(instrumentationContext, "res/raw/system")

        json?.let {
            parser.populateFromJson(it)
            assertTrue(true)
        } ?: kotlin.run {
            fail("no file found")
        }
    }

    @Test
    fun trab3_finished(){
        val instrumentationContext = getInstrumentation().targetContext
        val json = getJsonDataFromAsset(instrumentationContext, "res/raw/system")
        val parser = Parser()

        json?.let {
            parser.populateFromJson(it)
            main3_2(parser.parsedData)
            assertTrue(true)
        } ?: kotlin.run {
            fail("no file found")
        }

    }

    private fun getJsonDataFromAsset(context: Context, resourceId : String): String? {
        val jsonString: String
        try {
            jsonString = context
                .classLoader
                .getResourceAsStream(resourceId)
                .bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }


}
