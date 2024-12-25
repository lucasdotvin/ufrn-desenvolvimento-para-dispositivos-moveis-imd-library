package vin.lucas.imdlibrary.values

data class BookStoringPayload(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val publicationYear: Int,
    val coverUrl: String,
)
