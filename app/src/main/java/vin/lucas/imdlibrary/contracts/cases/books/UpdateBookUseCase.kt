package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload

interface UpdateBookUseCase {
    fun execute(book: Book, payload: BookChangePayload)
}
