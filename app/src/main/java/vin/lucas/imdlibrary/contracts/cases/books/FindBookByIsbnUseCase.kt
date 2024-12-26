package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.entities.Book

interface FindBookByIsbnUseCase {
    fun execute(isbn: String): Book
}
