package vin.lucas.imdlibrary.contracts.validation

interface CpfValidator {
    fun validate(cpf: String): Boolean
}
