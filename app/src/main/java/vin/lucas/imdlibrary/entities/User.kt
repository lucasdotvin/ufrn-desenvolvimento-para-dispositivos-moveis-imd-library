package vin.lucas.imdlibrary.entities

import java.util.Date

data class User(
    val id: Long,
    var username: String,
    var cpf: String,
    var password: String,
    val createdAt: Date,
    var updatedAt: Date,
)
