package net.vizja.calculator

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt


class MainViewModel(private val applicationContext: Context) : ViewModel() {

    val input: MutableLiveData<String> = mutableLiveData()
    val calculationResult: MutableLiveData<CalculationResult> = mutableLiveData()
    private val decimalCalculationResult: MutableLiveData<CalculationResult> = mutableLiveData() {
        if (it is CalculationResult.DoubleCalculationResult) {
            binaryCalculationResult.value =
                CalculationResult.StringCalculationResult(Integer.toBinaryString(it.result.roundToInt()))
            hexadecimalCalculationResult.value =
                CalculationResult.StringCalculationResult(Integer.toHexString(it.result.roundToInt()))
            octalCalculationResult.value =
                CalculationResult.StringCalculationResult(Integer.toOctalString(it.result.roundToInt()))
        }
    }
    private val binaryCalculationResult: MutableLiveData<CalculationResult> = mutableLiveData()
    private val octalCalculationResult: MutableLiveData<CalculationResult> = mutableLiveData()
    private val hexadecimalCalculationResult: MutableLiveData<CalculationResult> = mutableLiveData()
    private val oldCalculatorInput: MutableLiveData<CalculatorInput> = mutableLiveData()
    private val newCalculatorInput: MutableLiveData<CalculatorInput> = mutableLiveData()
    private var reset = false

    fun result() {
        val result = newCalculatorInput.value?.let {
            Calculator.calculate(it)
        }
        calculationResult.value = result
        decimalCalculationResult.value = result
        reset = true
    }

    fun binary() {
        binaryCalculationResult.value?.let {
            calculationResult.value = it
        }
    }

    fun octal() {
        octalCalculationResult.value?.let {
            calculationResult.value = it
        }
    }

    fun decimal() {
        decimalCalculationResult.value?.let {
            calculationResult.value = it
        }
    }

    fun hexadecimal() {
        hexadecimalCalculationResult.value?.let {
            calculationResult.value = it
        }
    }

    fun backSpace() {
        if (reset) {
            clear()
            reset = false
        }
        newCalculatorInput.value?.let {
            CalculatorInputFactory.removeLastSymbol(it)
        }?.let {
            setOldAndNewCalculatorInput(it)
            updateInput()
        }
    }

    fun clear() {
        oldCalculatorInput.value = null
        newCalculatorInput.value = null
        calculationResult.value = CalculationResult.StringCalculationResult("")
        updateInput()
    }

    fun power() {
        if (reset) {
            clear()
            reset = false
        }
        val calculatorInput = CalculatorInputFactory.calculatorInput(input.value, "^")
        updateCalculatorInput(calculatorInput)
    }

    fun symbol(view: View) {
        if (reset) {
            clear()
            reset = false
        }
        val button = view as AppCompatButton
        val buttonText = button.text.toString()
        val calculatorInput = CalculatorInputFactory.calculatorInput(input.value, buttonText)
        updateCalculatorInput(calculatorInput)
    }

    private fun updateCalculatorInput(calculatorInput: CalculatorInput) {
        if (CalculatorInputFactory.isValidCalculatorInput(calculatorInput)) {
            if (calculatorInput.calculatorElements.isNotEmpty()) {
                if (oldCalculatorInput.value == null
                    || (oldCalculatorInput.value != null
                            && calculatorInput.toCalculatorString() > oldCalculatorInput.value!!.toCalculatorString())
                ) {
                    setOldAndNewCalculatorInput(calculatorInput)
                } else {
                    newCalculatorInput.value = oldCalculatorInput.value
                }
            } else {
                newCalculatorInput.value = oldCalculatorInput.value
            }
        }
        updateInput()
    }

    private fun setOldAndNewCalculatorInput(calculatorInput: CalculatorInput) {
        newCalculatorInput.value = calculatorInput
        oldCalculatorInput.value = calculatorInput
    }

    private fun updateInput() {
        input.value = newCalculatorInput.value?.toCalculatorString()
    }
}