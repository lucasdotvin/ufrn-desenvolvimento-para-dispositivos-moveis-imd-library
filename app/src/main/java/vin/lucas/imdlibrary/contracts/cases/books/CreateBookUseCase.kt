package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.values.BookChangePayload

interface CreateBookUseCase {
    fun execute(payload: BookChangePayload)
}
