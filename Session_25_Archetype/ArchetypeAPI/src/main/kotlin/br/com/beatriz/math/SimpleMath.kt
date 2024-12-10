package br.com.beatriz.math

import org.springframework.web.bind.annotation.PathVariable
import kotlin.math.sqrt

class SimpleMath {

    fun sum(numberOne: Double, numberTwo: Double) = numberOne + numberTwo

    fun subtraction(numberOne: Double, numberTwo: Double) = numberOne - numberTwo

    fun multiplication(numberOne: Double, numberTwo: Double) = numberOne * numberTwo

    fun division(numberOne: Double, numberTwo: Double) = numberOne / numberTwo

    fun mean(numberOne: Double, numberTwo: Double) = (numberOne + numberTwo) / 2

    fun squareRoot(number: Double) = sqrt(number)
}