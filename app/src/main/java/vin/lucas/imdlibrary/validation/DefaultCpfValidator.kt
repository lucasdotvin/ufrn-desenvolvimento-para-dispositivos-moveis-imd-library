package vin.lucas.imdlibrary.validation

import vin.lucas.imdlibrary.contracts.validation.CpfValidator

class DefaultCpfValidator : CpfValidator {
    override fun validate(cpf: String): Boolean {
        val cpfDigits = cpf.filter { it.isDigit() }

        if (cpfDigits.length != 11) {
            return false
        }

        val cpfNumbers = cpfDigits.substring(0, 9).map { it.toString().toInt() }

        val firstReceivedVerifierDigit = cpfDigits[9].toString().toInt()
        val firstVerifierDigit = cpfDigits[9].toString().toInt()

        val firstCalculatedVerifierDigit = calculateVerifierDigit(cpfNumbers, 10)
        val secondCalculatedVerifierDigit = calculateVerifierDigit(
            cpfNumbers + firstCalculatedVerifierDigit,
            11,
        )

        return firstReceivedVerifierDigit == firstCalculatedVerifierDigit
                && firstVerifierDigit == secondCalculatedVerifierDigit
    }

    private fun calculateVerifierDigit(numbers: List<Int>, multiplier: Int): Int {
        val sum = numbers
            .mapIndexed { index, number -> number * (multiplier - index) }
            .sum()

        val remainder = sum % 11

        return if (remainder < 2) {
            0
        } else {
            11 - remainder
        }
    }
}
