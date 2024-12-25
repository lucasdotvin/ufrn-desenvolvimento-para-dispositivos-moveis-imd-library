package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.values.BookStoringPayload

interface CreateBookUseCase {
    fun execute(payload: BookStoringPayload)
}
