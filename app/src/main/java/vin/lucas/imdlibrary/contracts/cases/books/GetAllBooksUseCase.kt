package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.entities.Book

interface GetAllBooksUseCase {
    fun execute(): List<Book>
}
