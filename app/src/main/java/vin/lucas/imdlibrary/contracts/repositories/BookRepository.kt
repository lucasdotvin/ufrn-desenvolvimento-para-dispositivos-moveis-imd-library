package vin.lucas.imdlibrary.contracts.repositories

import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload

interface BookRepository {
    fun retrieveIdByIsbn(isbn: String): Long?

    fun findByIsbn(isbn: String): Book?

    fun findById(id: Long): Book?

    fun store(payload: BookChangePayload): Book

    fun update(book: Book): Boolean

    fun delete(book: Book): Boolean

    fun getAll(): List<Book>
}
