package net.vizja.calculator

import net.vizja.calculator.CalculationResult.DoubleCalculationResult
import net.vizja.calculator.CalculationResult.ErrorCalculationResult
import net.vizja.calculator.CalculatorElement.*

object Calculator {

    fun calculate(calculatorInput: CalculatorInput): CalculationResult? {
        if (!CalculatorInputFactory.isValidCalculatorInput(calculatorInput)) {
            return ErrorCalculationResult("CalculatorInput is invalid")
        }
        val firstCalculatorElement = calculatorInput.calculatorElements.first()
        val validCalculatorElements =
            if (
                firstCalculatorElement is Operation
                && firstCalculatorElement.operationType == OperationType.SUBTRACTION
            ) {
                listOf(DoubleDigit(0.0)) + normalize(calculatorInput)
            } else {
                normalize(calculatorInput)
            }

        return calculate(validCalculatorElements)
    }

    private fun calculate(validCalculatorElements: List<CalculatorElement>): DoubleCalculationResult {
        var calculatorElementsWithPriority = assignPriority(validCalculatorElements)
        do {
            val result = executeCalculatorOperation(calculatorElementsWithPriority.second)
            calculatorElementsWithPriority = assignPriority(result)
        } while (calculatorElementsWithPriority.first != 0)
        require(calculatorElementsWithPriority.second.size == 1)
        val calculationResult = calculatorElementsWithPriority.second.first()
        require(calculationResult is DoubleDigit)
        return DoubleCalculationResult(calculationResult.digit)
    }

    private fun executeCalculatorOperation(
        elements: List<CalculatorElement>
    ): List<CalculatorElement> {
        for ((index, value) in elements.withIndex()) {
            if (
                value is Operation
                && value.priority == 0
                && (!(index == 0 || index == (elements.size - 1)))
            ) {
                val left = elements[index - 1]
                require(left is DoubleDigit) { "Left operand must be double digit" }
                val right = elements[index + 1]
                require(right is DoubleDigit) { "Right operand must be double digit" }
                val operation = CalculatorOperation(left, right, value)
                val result = operation.invoke()
                return insert(result, elements, index - 1)
            }
        }
        return elements
    }

    private fun insert(
        element: CalculatorElement,
        elements: List<CalculatorElement>,
        index: Int
    ): List<CalculatorElement> {
        val mutableElements = mutableListOf(*elements.toTypedArray())
        (0..2).forEach { _ ->
            mutableElements.removeAt(index)
        }
        mutableElements.add(index, element)
        return mutableElements.toList()
    }

    private fun assignPriority(validCalculatorElements: List<CalculatorElement>): Pair<Int, List<CalculatorElement>> {
        var priority = 0
        for (element in validCalculatorElements) {
            if (element is Operation && element.operationType.hasHighPriority()) {
                element.priority = priority
                priority++
            }
        }
        for (element in validCalculatorElements) {
            if (element is Operation && !element.operationType.hasHighPriority()) {
                element.priority = priority
                priority++
            }
        }
        return Pair(priority, validCalculatorElements)
    }

    private fun normalize(calculatorInput: CalculatorInput): List<CalculatorElement> {
        return calculatorInput.calculatorElements.map {
            when (it) {
                is DoubleDigit, is Operation -> it
                is IntegerDigit -> DoubleDigit(it.digit.toDouble())
                is IncompleteDoubleDigit -> DoubleDigit("${it.digit}0".toDouble())
            }
        }
    }
}