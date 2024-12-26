package vin.lucas.imdlibrary.entities

import java.util.Date

data class Book(
    val id: Long,
    var isbn: String,
    var title: String,
    var author: String,
    var publisher: String,
    var publicationYear: Int,
    var summary: String,
    var coverUrl: String,
    val createdAt: Date,
    var updatedAt: Date,
)
