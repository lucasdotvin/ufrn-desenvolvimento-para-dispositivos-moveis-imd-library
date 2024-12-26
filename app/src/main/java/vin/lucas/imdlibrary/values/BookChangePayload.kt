package vin.lucas.imdlibrary.values

data class BookChangePayload(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val publicationYear: Int,
    val summary: String,
    val coverUrl: String,
)
