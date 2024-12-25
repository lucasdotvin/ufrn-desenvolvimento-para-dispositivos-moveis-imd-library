package vin.lucas.imdlibrary.entities

import java.util.Date

data class Book(
    val id: Long,
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val publicationYear: Int,
    val coverUrl: String,
    val createdAt: Date,
    var updatedAt: Date,
)
