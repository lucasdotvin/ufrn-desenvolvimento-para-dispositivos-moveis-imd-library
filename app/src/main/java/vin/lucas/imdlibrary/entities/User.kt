package vin.lucas.imdlibrary.entities

import java.util.Date

data class User(
    val id: Int,
    var username: String,
    var password: String,
    val createdAt: Date,
    var updatedAt: Date,
)
