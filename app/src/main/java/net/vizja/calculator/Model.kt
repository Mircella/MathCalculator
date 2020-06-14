package net.vizja.calculator

data class CalculatorInput(val calculatorElements: List<CalculatorElement>) {

    fun toCalculatorString() = calculatorElements.joinToString(separator = "") { it.value }
}

sealed class CalculatorElement(val value: String) {

    data class DoubleDigit(val digit: Double) : CalculatorElement(digit.toString())
    data class IntegerDigit(val digit: Int) : CalculatorElement(digit.toString())
    data class IncompleteDoubleDigit(val digit: String) : CalculatorElement(digit)
    data class Operation(val operationType: OperationType, var priority: Int) : CalculatorElement(" ${operationType.value} ")
}

sealed class CalculationResult(val value: String) {
    data class DoubleCalculationResult(val result: Double) : CalculationResult(result.toString())
    data class StringCalculationResult(val result: String) : CalculationResult(result)
    data class ErrorCalculationResult(val result: String) : CalculationResult(result)

    fun toCalculationString() = value
}

enum class OperationType(val value: String) {
    ADDITION("+"), MULTIPLICATION("*"), SUBTRACTION("-"), DIVISION("/"), POWER("^");

    fun hasHighPriority() = this in listOf(MULTIPLICATION, DIVISION, POWER)

    companion object {
        fun of(value: String) = values().find { it.value == value }
    }
}