package vin.lucas.imdlibrary.contracts.repositories

interface HashRepository {
    fun hash(value: String): String

    fun verify(value: String, digest: String): Boolean
}
