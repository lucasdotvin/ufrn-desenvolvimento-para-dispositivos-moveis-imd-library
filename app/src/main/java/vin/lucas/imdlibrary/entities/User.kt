package vin.lucas.imdlibrary.entities

import java.util.Date

typealias UserKey = Int

data class User(
    val id: UserKey,
    var username: String,
    var cpf: String,
    var password: String,
    val createdAt: Date,
    var updatedAt: Date,
)
