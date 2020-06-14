package net.vizja.calculator

object CalculatorInputFactory {

    fun isValidCalculatorInput(calculatorInput: CalculatorInput): Boolean {
        val calculatorElements = calculatorInput.calculatorElements
        for ((index, value) in calculatorElements.withIndex()) {
            if (value is CalculatorElement.Operation) {
                if ((index != 0 && calculatorElements[index - 1] is CalculatorElement.Operation)
                    || notMinusTheFirst(value, index)
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun removeLastSymbol(calculatorInput: CalculatorInput): CalculatorInput {
        val calculatorElements = calculatorInput.calculatorElements
        val lastValue = calculatorElements.last().value
        val last = lastValue.substring(0, lastValue.length - 1)
        val lastCalculatorElement = toCalculatorElement(last)
        val newCalculatorElements = (calculatorElements.subList(0, calculatorElements.size - 1)
                + lastCalculatorElement).filterNotNull()
        return CalculatorInput(newCalculatorElements)
    }

    fun calculatorInput(calculatorInputText: String?, buttonText: String): CalculatorInput {
        val inputText = calculatorInputText?.let { "$it$buttonText" } ?: buttonText
        val inputTextSplitted = inputText.split(CALCULATOR_INPUT_DELIMITERS_PATTERN)
        val calculatorElements = toCalculatorElements(inputTextSplitted)
        return CalculatorInput(calculatorElements)
    }

    private fun toCalculatorElements(inputTextSplitted: List<String>): List<CalculatorElement> {
        return inputTextSplitted.filter { it != "" }.mapNotNull { toCalculatorElement(it) }
    }

    private fun toCalculatorElement(value: String): CalculatorElement? {
        return try {
            when {
                value.matches(INTEGER_DIGIT_PATTERN) -> {
                    CalculatorElement.IntegerDigit(value.toInt())
                }
                value.matches(INCOMPLETE_DOUBLE_DIGIT_PATTERN) -> {
                    CalculatorElement.IncompleteDoubleDigit(value)
                }
                value.matches(DOUBLE_DIGIT_PATTERN) -> {
                    CalculatorElement.DoubleDigit(value.toDouble())
                }
                else -> {
                    OperationType.of(value)?.let { CalculatorElement.Operation(it, 0) }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun notMinusTheFirst(value: CalculatorElement.Operation, index: Int) =
        value.operationType != OperationType.SUBTRACTION && index == 0


}