package net.vizja.calculator

val INTEGER_DIGIT_PATTERN = Regex("^\\d+$")
val DOUBLE_DIGIT_PATTERN = Regex("^\\d+.\\d+$")
val INCOMPLETE_DOUBLE_DIGIT_PATTERN = Regex("^\\d+.$")
val CALCULATOR_INPUT_DELIMITERS_PATTERN = Regex("(?<=[-+*/^\\s])|(?=[-+*/^\\s])")