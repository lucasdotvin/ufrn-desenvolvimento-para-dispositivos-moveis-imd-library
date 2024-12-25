package vin.lucas.imdlibrary.contracts.services

import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload

interface BookService {
    fun store(payload: BookChangePayload): Book

    fun findByIsbn(isbn: String): Book

    fun findById(id: Long): Book

    fun update(book: Book)

    fun delete(book: Book)
}
