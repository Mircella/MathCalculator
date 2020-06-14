package net.vizja.calculator

import java.math.BigDecimal

data class CalculatorOperation(
    val leftOperand: CalculatorElement.DoubleDigit,
    val rightOperand: CalculatorElement.DoubleDigit,
    val operation: CalculatorElement.Operation
) {
    fun invoke(): CalculatorElement.DoubleDigit {
        val result = when (operation.operationType) {
            OperationType.ADDITION -> BigDecimal(leftOperand.digit).plus(BigDecimal(rightOperand.digit))
                .toDouble()
            OperationType.SUBTRACTION -> BigDecimal(leftOperand.digit).minus(BigDecimal(rightOperand.digit))
                .toDouble()
            OperationType.MULTIPLICATION -> BigDecimal(leftOperand.digit).multiply(
                BigDecimal(rightOperand.digit)
            ).toDouble()
            OperationType.DIVISION -> BigDecimal(leftOperand.digit).divide(BigDecimal(rightOperand.digit))
                .toDouble()
            OperationType.POWER -> BigDecimal(leftOperand.digit).pow(rightOperand.digit.toInt())
                .toDouble()
        }
        return CalculatorElement.DoubleDigit(result)
    }
}