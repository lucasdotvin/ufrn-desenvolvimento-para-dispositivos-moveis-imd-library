package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.entities.Book

interface FindBookUseCase {
    fun execute(id: Long): Book
}
