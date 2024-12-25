package vin.lucas.imdlibrary.contracts.services

import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookStoringPayload

interface BookService {
    fun store(payload: BookStoringPayload): Book

    fun findByIsbn(isbn: String): Book

    fun findById(id: Long): Book

    fun update(book: Book)

    fun delete(book: Book)
}
