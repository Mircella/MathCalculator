package net.vizja.calculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val str = "7.5-"

        val reg = Regex("(?<=[-])|(?=[-])")

        var list = str.split(reg)
        println(list)
    }
}